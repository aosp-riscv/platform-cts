/*
 * Copyright (C) 2018 The Android Open Source Project
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

package android.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Parcel;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class RemoteActionTest {

    @Test
    public void testParcel() {
        Icon icon = Icon.createWithContentUri("content://test");
        String title = "title";
        String description = "description";
        PendingIntent action = PendingIntent.getBroadcast(
                InstrumentationRegistry.getTargetContext(), 0, new Intent("TESTACTION"),
                PendingIntent.FLAG_IMMUTABLE);
        RemoteAction reference = new RemoteAction(icon, title, description, action);
        reference.setEnabled(false);
        reference.setShouldShowIcon(false);

        final Parcel parcel = Parcel.obtain();
        reference.writeToParcel(parcel, reference.describeContents());
        parcel.setDataPosition(0);
        RemoteAction result = RemoteAction.CREATOR.createFromParcel(parcel);

        assertEquals(icon.getUri(), result.getIcon().getUri());
        assertEquals(title, result.getTitle());
        assertEquals(description, result.getContentDescription());
        assertEquals(action.getCreatorPackage(), result.getActionIntent().getCreatorPackage());
        assertFalse(result.isEnabled());
        assertFalse(result.shouldShowIcon());
    }

    @Test
    public void testClone() {
        Icon icon = Icon.createWithContentUri("content://test");
        String title = "title";
        String description = "description";
        PendingIntent action = PendingIntent.getBroadcast(
                InstrumentationRegistry.getTargetContext(), 0, new Intent("TESTACTION"),
                PendingIntent.FLAG_IMMUTABLE);
        RemoteAction reference = new RemoteAction(icon, title, description, action);
        reference.setEnabled(false);
        reference.setShouldShowIcon(false);

        RemoteAction result = reference.clone();

        assertEquals(icon.getUri(), result.getIcon().getUri());
        assertEquals(title, result.getTitle());
        assertEquals(description, result.getContentDescription());
        assertEquals(action.getCreatorPackage(), result.getActionIntent().getCreatorPackage());
        assertFalse(result.isEnabled());
        assertFalse(result.shouldShowIcon());
    }
}
