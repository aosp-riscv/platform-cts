/*
 * Copyright (C) 2019 The Android Open Source Project
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
package com.android.cts.verifier.instantapps;

import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.android.cts.verifier.R;

/**
 * Test for manual verification of Instant Apps notification.
 *
 * The test verifies that an Instant App notification will be shown when
 * an Instant App is at foreground.
 */
public class NotificationTestActivity extends BaseTestActivity {

    private String gms_package_name = "com.google.android.gms";
    private String store_package_name = "com.android.vending";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setInfoResources(R.string.ia_notification, R.string.ia_notification_info, -1);
        TextView extraText = (TextView) findViewById(R.id.instruction_extra_text);
        if (deviceIsGms()) {
            extraText.setText(R.string.ia_notification_instruction_label);
        } else {
            extraText.setText(R.string.ia_notification_instruction_label_no_app_market_version);
        }
    }

    private boolean deviceIsGms() {
        return deviceHasGmsCore() && deviceHasPlayStore() && !deviceHasCnFeature();
    }

    private boolean deviceHasCnFeature() {
        boolean hasCnFeature = getApplicationContext().getPackageManager().hasSystemFeature("cn.google.services");
        return hasCnFeature;
    }

    private boolean deviceHasGmsCore() {
        try {
            return getPackageManager().getApplicationInfo(gms_package_name, PackageManager.MATCH_DIRECT_BOOT_AUTO) != null;
        } catch (PackageManager.NameNotFoundException ex) {
            return false;
        }
    }

    private boolean deviceHasPlayStore() {
        try {
            return getPackageManager().getApplicationInfo(store_package_name, PackageManager.MATCH_DIRECT_BOOT_AUTO) != null;
        } catch (PackageManager.NameNotFoundException ex) {
            return false;
        }
    }
}
