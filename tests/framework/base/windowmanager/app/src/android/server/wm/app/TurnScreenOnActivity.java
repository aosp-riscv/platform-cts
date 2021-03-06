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
 * limitations under the License
 */

package android.server.wm.app;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.WindowManager;

public class TurnScreenOnActivity extends AbstractLifecycleLogActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final boolean useShowWhenLocked = getIntent().getBooleanExtra(
                Components.TurnScreenOnActivity.EXTRA_SHOW_WHEN_LOCKED, true /* defaultValue */);
        if (getIntent().getBooleanExtra(Components.TurnScreenOnActivity.EXTRA_USE_WINDOW_FLAGS,
                false /* defaultValue */)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            if (useShowWhenLocked) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            }
        } else {
            if (useShowWhenLocked) {
                setShowWhenLocked(true);
            }
            setTurnScreenOn(true);
        }

        final long sleepMs = getIntent().getLongExtra(
                Components.TurnScreenOnActivity.EXTRA_SLEEP_MS_IN_ON_CREATE, 0);
        if (sleepMs > 0) {
            SystemClock.sleep(sleepMs);
        }
    }
}
