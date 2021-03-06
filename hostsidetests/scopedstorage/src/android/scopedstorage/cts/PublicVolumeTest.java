/*
 * Copyright (C) 2020 The Android Open Source Project
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

package android.scopedstorage.cts;

import static com.google.common.truth.Truth.assertThat;

import android.scopedstorage.cts.lib.TestUtils;

import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Runs all of the tests from {@link ScopedStorageTest} on a public volume.
 */
@RunWith(AndroidJUnit4.class)
public class PublicVolumeTest extends ScopedStorageTest {
    @Override
    @Before
    public void setup() throws Exception {
        final String volumeName = TestUtils.getPublicVolumeName();
        assertThat(volumeName).isNotNull();
        TestUtils.setExternalStorageVolume(volumeName);
        super.setup();
    }

    @After
    public void resetExternalStorageVolume() {
        TestUtils.resetDefaultExternalStorageVolume();
    }
}
