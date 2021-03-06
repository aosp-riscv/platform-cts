/*
 * Copyright (C) 2017 The Android Open Source Project
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

package android.content.pm.cts;

import android.content.pm.PackageManager;
import android.platform.test.annotations.AppModeFull;
import android.test.AndroidTestCase;

@AppModeFull // TODO(Instant) Figure out which APIs should work.
public class PermissionFeatureTest extends AndroidTestCase {
    public void testPermissionRequiredFeatureDefined() {
        assertPermissionGranted("android.content.cts.REQUIRED_FEATURE_DEFINED");
    }

    public void testPermissionRequiredFeatureDefined_usingTags() {
        assertPermissionGranted("android.content.cts.REQUIRED_FEATURE_DEFINED_2");
    }

    public void testPermissionRequiredFeatureUndefined() {
        assertPermissionDenied("android.content.cts.REQUIRED_FEATURE_UNDEFINED");
    }

    public void testPermissionRequiredNotFeatureDefined() {
        assertPermissionDenied("android.content.cts.REQUIRED_NOT_FEATURE_DEFINED");
    }

    public void testPermissionRequiredNotFeatureUndefined() {
        assertPermissionGranted("android.content.cts.REQUIRED_NOT_FEATURE_UNDEFINED");
    }

    public void testPermissionRequiredNotFeatureUndefined_usingTags() {
        assertPermissionGranted("android.content.cts.REQUIRED_NOT_FEATURE_UNDEFINED_2");
    }

    public void testPermissionRequiredMultiDeny() {
        assertPermissionDenied("android.content.cts.REQUIRED_MULTI_DENY");
    }

    public void testPermissionRequiredMultiDeny_usingTags() {
        assertPermissionDenied("android.content.cts.REQUIRED_MULTI_DENY_2");
    }

    public void testPermissionRequiredMultiDeny_usingTagsAndAttributes() {
        assertPermissionDenied("android.content.cts.REQUIRED_MULTI_DENY_3");
    }

    public void testPermissionRequiredMultiGrant() {
        assertPermissionGranted("android.content.cts.REQUIRED_MULTI_GRANT");
    }

    public void testPermissionRequiredMultiGrant_usingTags() {
        assertPermissionGranted("android.content.cts.REQUIRED_MULTI_GRANT_2");
    }

    public void testPermissionRequiredMultiGrant_usingTagsAndAttributes() {
        assertPermissionGranted("android.content.cts.REQUIRED_MULTI_GRANT_3");
    }

    public void assertPermissionGranted(String permName) {
        final PackageManager pm = getContext().getPackageManager();
        assertEquals(PackageManager.PERMISSION_GRANTED,
                pm.checkPermission(permName, getContext().getPackageName()));
    }

    public void assertPermissionDenied(String permName) {
        final PackageManager pm = getContext().getPackageManager();
        assertEquals(PackageManager.PERMISSION_DENIED,
                pm.checkPermission(permName, getContext().getPackageName()));
    }
}
