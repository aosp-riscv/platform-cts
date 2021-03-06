/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.media.cts;

import static android.media.browse.MediaBrowser.MediaItem.FLAG_PLAYABLE;
import static android.media.cts.MediaBrowserServiceTestService.KEY_PARENT_MEDIA_ID;
import static android.media.cts.MediaBrowserServiceTestService.KEY_SERVICE_COMPONENT_NAME;
import static android.media.cts.MediaBrowserServiceTestService.TEST_SERIES_OF_NOTIFY_CHILDREN_CHANGED;
import static android.media.cts.MediaSessionTestService.KEY_EXPECTED_TOTAL_NUMBER_OF_ITEMS;
import static android.media.cts.MediaSessionTestService.STEP_CHECK;
import static android.media.cts.MediaSessionTestService.STEP_CLEAN_UP;
import static android.media.cts.MediaSessionTestService.STEP_SET_UP;
import static android.media.cts.Utils.compareRemoteUserInfo;

import android.content.ComponentName;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.media.browse.MediaBrowser.MediaItem;
import android.media.session.MediaSessionManager.RemoteUserInfo;
import android.os.Bundle;
import android.os.Process;
import android.service.media.MediaBrowserService;
import android.service.media.MediaBrowserService.BrowserRoot;
import android.test.InstrumentationTestCase;

import androidx.test.core.app.ApplicationProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Test {@link android.service.media.MediaBrowserService}.
 */
@NonMediaMainlineTest
public class MediaBrowserServiceTest extends InstrumentationTestCase {
    // The maximum time to wait for an operation.
    private static final long TIME_OUT_MS = 3000L;
    private static final long WAIT_TIME_FOR_NO_RESPONSE_MS = 500L;
    private static final ComponentName TEST_BROWSER_SERVICE = new ComponentName(
            "android.media.cts", "android.media.cts.StubMediaBrowserService");
    private final Object mWaitLock = new Object();

    private final MediaBrowser.ConnectionCallback mConnectionCallback =
            new MediaBrowser.ConnectionCallback() {
        @Override
        public void onConnected() {
            synchronized (mWaitLock) {
                mMediaBrowserService = StubMediaBrowserService.sInstance;
                mWaitLock.notify();
            }
        }
    };

    private final MediaBrowser.SubscriptionCallback mSubscriptionCallback =
            new MediaBrowser.SubscriptionCallback() {
            @Override
            public void onChildrenLoaded(String parentId, List<MediaItem> children) {
                synchronized (mWaitLock) {
                    mOnChildrenLoaded = true;
                    if (children != null) {
                        for (MediaItem item : children) {
                            assertRootHints(item);
                        }
                    }
                    mWaitLock.notify();
                }
            }

            @Override
            public void onChildrenLoaded(String parentId, List<MediaItem> children,
                    Bundle options) {
                synchronized (mWaitLock) {
                    mOnChildrenLoadedWithOptions = true;
                    if (children != null) {
                        for (MediaItem item : children) {
                            assertRootHints(item);
                        }
                    }
                    mWaitLock.notify();
                }
            }
        };

    private final MediaBrowser.ItemCallback mItemCallback = new MediaBrowser.ItemCallback() {
        @Override
        public void onItemLoaded(MediaItem item) {
            synchronized (mWaitLock) {
                mOnItemLoaded = true;
                assertRootHints(item);
                mWaitLock.notify();
            }
        }
    };

    private MediaBrowser mMediaBrowser;
    private RemoteUserInfo mBrowserInfo;
    private StubMediaBrowserService mMediaBrowserService;
    private boolean mOnChildrenLoaded;
    private boolean mOnChildrenLoadedWithOptions;
    private boolean mOnItemLoaded;
    private Bundle mRootHints;

    @Override
    public void setUp() throws Exception {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mRootHints = new Bundle();
                mRootHints.putBoolean(MediaBrowserService.BrowserRoot.EXTRA_RECENT, true);
                mRootHints.putBoolean(MediaBrowserService.BrowserRoot.EXTRA_OFFLINE, true);
                mRootHints.putBoolean(MediaBrowserService.BrowserRoot.EXTRA_SUGGESTED, true);
                mMediaBrowser = new MediaBrowser(getInstrumentation().getTargetContext(),
                        TEST_BROWSER_SERVICE, mConnectionCallback, mRootHints);
                mBrowserInfo = new RemoteUserInfo(
                        getInstrumentation().getTargetContext().getPackageName(),
                        Process.myPid(),
                        Process.myUid());
            }
        });
        synchronized (mWaitLock) {
            mMediaBrowser.connect();
            mWaitLock.wait(TIME_OUT_MS);
        }
        assertNotNull(mMediaBrowserService);
    }

    @Override
    public void tearDown() {
        if (mMediaBrowser != null) {
            mMediaBrowser.disconnect();
            mMediaBrowser = null;
        }
    }

    public void testGetSessionToken() {
        assertEquals(StubMediaBrowserService.sSession.getSessionToken(),
                mMediaBrowserService.getSessionToken());
    }

    public void testNotifyChildrenChanged() throws Exception {
        synchronized (mWaitLock) {
            mMediaBrowser.subscribe(StubMediaBrowserService.MEDIA_ID_ROOT, mSubscriptionCallback);
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnChildrenLoaded);

            mOnChildrenLoaded = false;
            mMediaBrowserService.notifyChildrenChanged(StubMediaBrowserService.MEDIA_ID_ROOT);
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnChildrenLoaded);
        }
    }

    public void testNotifyChildrenChangedWithNullOptionsThrowsIAE() {
        try {
            mMediaBrowserService.notifyChildrenChanged(
                    StubMediaBrowserService.MEDIA_ID_ROOT, /*options=*/ null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testNotifyChildrenChangedWithPagination() throws Exception {
        synchronized (mWaitLock) {
            final int pageSize = 5;
            final int page = 2;
            Bundle options = new Bundle();
            options.putInt(MediaBrowser.EXTRA_PAGE_SIZE, pageSize);
            options.putInt(MediaBrowser.EXTRA_PAGE, page);

            mMediaBrowser.subscribe(StubMediaBrowserService.MEDIA_ID_ROOT, options,
                    mSubscriptionCallback);
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnChildrenLoadedWithOptions);

            mOnChildrenLoadedWithOptions = false;
            mMediaBrowserService.notifyChildrenChanged(StubMediaBrowserService.MEDIA_ID_ROOT);
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnChildrenLoadedWithOptions);

            // Notify that the items overlapping with the given options are changed.
            mOnChildrenLoadedWithOptions = false;
            final int newPageSize = 3;
            final int overlappingNewPage = pageSize * page / newPageSize;
            Bundle overlappingOptions = new Bundle();
            overlappingOptions.putInt(MediaBrowser.EXTRA_PAGE_SIZE, newPageSize);
            overlappingOptions.putInt(MediaBrowser.EXTRA_PAGE, overlappingNewPage);
            mMediaBrowserService.notifyChildrenChanged(
                    StubMediaBrowserService.MEDIA_ID_ROOT, overlappingOptions);
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnChildrenLoadedWithOptions);

            // Notify that the items non-overlapping with the given options are changed.
            mOnChildrenLoadedWithOptions = false;
            Bundle nonOverlappingOptions = new Bundle();
            nonOverlappingOptions.putInt(MediaBrowser.EXTRA_PAGE_SIZE, pageSize);
            nonOverlappingOptions.putInt(MediaBrowser.EXTRA_PAGE, page + 1);
            mMediaBrowserService.notifyChildrenChanged(
                    StubMediaBrowserService.MEDIA_ID_ROOT, nonOverlappingOptions);
            mWaitLock.wait(WAIT_TIME_FOR_NO_RESPONSE_MS);
            assertFalse(mOnChildrenLoadedWithOptions);
        }
    }

    public void testDelayedNotifyChildrenChanged() throws Exception {
        synchronized (mWaitLock) {
            mOnChildrenLoaded = false;
            mMediaBrowser.subscribe(StubMediaBrowserService.MEDIA_ID_CHILDREN_DELAYED,
                    mSubscriptionCallback);
            mWaitLock.wait(WAIT_TIME_FOR_NO_RESPONSE_MS);
            assertFalse(mOnChildrenLoaded);

            mMediaBrowserService.sendDelayedNotifyChildrenChanged();
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnChildrenLoaded);

            mOnChildrenLoaded = false;
            mMediaBrowserService.notifyChildrenChanged(
                    StubMediaBrowserService.MEDIA_ID_CHILDREN_DELAYED);
            mWaitLock.wait(WAIT_TIME_FOR_NO_RESPONSE_MS);
            assertFalse(mOnChildrenLoaded);

            mMediaBrowserService.sendDelayedNotifyChildrenChanged();
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnChildrenLoaded);
        }
    }

    public void testDelayedItem() throws Exception {
        synchronized (mWaitLock) {
            mOnItemLoaded = false;
            mMediaBrowser.getItem(StubMediaBrowserService.MEDIA_ID_CHILDREN_DELAYED,
                    mItemCallback);
            mWaitLock.wait(WAIT_TIME_FOR_NO_RESPONSE_MS);
            assertFalse(mOnItemLoaded);

            mMediaBrowserService.sendDelayedItemLoaded();
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnItemLoaded);
        }
    }

    public void testGetBrowserInfo() throws Exception {
        synchronized (mWaitLock) {
            // StubMediaBrowserService stores the browser info in its onGetRoot().
            assertTrue(compareRemoteUserInfo(mBrowserInfo, StubMediaBrowserService.sBrowserInfo));

            StubMediaBrowserService.clearBrowserInfo();
            mMediaBrowser.subscribe(StubMediaBrowserService.MEDIA_ID_ROOT, mSubscriptionCallback);
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnChildrenLoaded);
            assertTrue(compareRemoteUserInfo(mBrowserInfo, StubMediaBrowserService.sBrowserInfo));

            StubMediaBrowserService.clearBrowserInfo();
            mMediaBrowser.getItem(StubMediaBrowserService.MEDIA_ID_CHILDREN[0], mItemCallback);
            mWaitLock.wait(TIME_OUT_MS);
            assertTrue(mOnItemLoaded);
            assertTrue(compareRemoteUserInfo(mBrowserInfo, StubMediaBrowserService.sBrowserInfo));
        }
    }

    public void testBrowserRoot() {
        final String id = "test-id";
        final String key = "test-key";
        final String val = "test-val";
        final Bundle extras = new Bundle();
        extras.putString(key, val);

        MediaBrowserService.BrowserRoot browserRoot = new BrowserRoot(id, extras);
        assertEquals(id, browserRoot.getRootId());
        assertEquals(val, browserRoot.getExtras().getString(key));
    }

    /**
     * Check that a series of {@link MediaBrowserService#notifyChildrenChanged} does not break
     * {@link MediaBrowser} on the remote process due to binder buffer overflow.
     */
    public void testSeriesOfNotifyChildrenChanged() throws Exception {
        String parentMediaId = "testSeriesOfNotifyChildrenChanged";
        int numberOfCalls = 100;
        int childrenSize = 1_000;
        List<MediaItem> children = new ArrayList<>();
        for (int id = 0; id < childrenSize; id++) {
            MediaDescription description = new MediaDescription.Builder()
                    .setMediaId(Integer.toString(id)).build();
            children.add(new MediaItem(description, FLAG_PLAYABLE));
        }
        mMediaBrowserService.putChildrenToMap(parentMediaId, children);

        try (RemoteService.Invoker invoker = new RemoteService.Invoker(
                ApplicationProvider.getApplicationContext(),
                MediaBrowserServiceTestService.class,
                TEST_SERIES_OF_NOTIFY_CHILDREN_CHANGED)) {
            Bundle args = new Bundle();
            args.putParcelable(KEY_SERVICE_COMPONENT_NAME, TEST_BROWSER_SERVICE);
            args.putString(KEY_PARENT_MEDIA_ID, parentMediaId);
            args.putInt(KEY_EXPECTED_TOTAL_NUMBER_OF_ITEMS, numberOfCalls * childrenSize);
            invoker.run(STEP_SET_UP, args);
            for (int i = 0; i < numberOfCalls; i++) {
                mMediaBrowserService.notifyChildrenChanged(parentMediaId);
            }
            invoker.run(STEP_CHECK);
            invoker.run(STEP_CLEAN_UP);
        }

        mMediaBrowserService.removeChildrenFromMap(parentMediaId);
    }

    private void assertRootHints(MediaItem item) {
        Bundle rootHints = item.getDescription().getExtras();
        assertNotNull(rootHints);
        assertEquals(mRootHints.getBoolean(BrowserRoot.EXTRA_RECENT),
                rootHints.getBoolean(BrowserRoot.EXTRA_RECENT));
        assertEquals(mRootHints.getBoolean(BrowserRoot.EXTRA_OFFLINE),
                rootHints.getBoolean(BrowserRoot.EXTRA_OFFLINE));
        assertEquals(mRootHints.getBoolean(BrowserRoot.EXTRA_SUGGESTED),
                rootHints.getBoolean(BrowserRoot.EXTRA_SUGGESTED));
    }
}
