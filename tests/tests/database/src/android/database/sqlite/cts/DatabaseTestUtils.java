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

package android.database.sqlite.cts;

import android.content.Context;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import androidx.test.InstrumentationRegistry;

/**
 * Common utility methods for testing
 */
class DatabaseTestUtils {

    private static final String TAG = "SQLiteOpenHelperTest";

    static boolean waitForConnectionToClose(int maxAttempts, int pollIntervalMs)
            throws Exception {
        for (int i = 0; i < maxAttempts; i++) {
            String output = getDbInfoOutput();
            Log.d(TAG, "waitForConnectionToClose #" + i + ": " + output);
            if (!output.contains("Connection #0:")) {
                return true;
            }
            Thread.sleep(pollIntervalMs);
        }
        return false;
    }

    static String getDbInfoOutput() throws Exception {
        Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
        return executeShellCommand("dumpsys dbinfo " + ctx.getPackageName());
    }

    static String executeShellCommand(String cmd) throws Exception {
        return UiDevice.getInstance(
                InstrumentationRegistry.getInstrumentation()).executeShellCommand(cmd);
    }
}
