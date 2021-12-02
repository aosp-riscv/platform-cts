/*
 * Copyright (C) 2016 The Android Open Source Project
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

package android.trustedvoice.app;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardDismissCallback;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager.LayoutParams;

/**
 * This activity when in foreground sets the FLAG_DISMISS_KEYGUARD.
 * It then confirms that the keyguard was successfully dismissed
 * and logs a string to logcat on success.
 */
public class TrustedVoiceActivity extends Activity {

  private static final String TAG = TrustedVoiceActivity.class.getSimpleName();
  /**
   * The test string to log.
   */
  private static final String TEST_STRING = "TrustedVoiceTestString";

  private KeyguardManager mkeyguardManager;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    mkeyguardManager =
            (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

    // Unlock the keyguard.
    getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON
            | LayoutParams.FLAG_KEEP_SCREEN_ON);
    mkeyguardManager.requestDismissKeyguard(this, null);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      // Confirm that the keyguard was successfully unlocked.
      if (!mkeyguardManager.isKeyguardLocked()) {
        // Log the test string.
        Log.i(TAG, TEST_STRING);
      }
    }
  }
}

