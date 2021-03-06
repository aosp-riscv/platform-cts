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
 * limitations under the License
 */

package android.server.wm.app;

import static android.app.WindowConfiguration.ACTIVITY_TYPE_ASSISTANT;
import static android.app.WindowConfiguration.WINDOWING_MODE_FULLSCREEN;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

public class TranslucentAssistantActivity extends AssistantActivity {

    /**
     * Launches a new instance of the TranslucentAssistantActivity directly into the assistant
     * stack.
     */
    static void launchActivityIntoAssistantStack(Activity caller, Bundle extras) {
        final Intent intent = new Intent(caller, TranslucentAssistantActivity.class);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        if (extras != null) {
            intent.putExtras(extras);
        }

        final ActivityOptions options = ActivityOptions.makeBasic();
        options.setLaunchActivityType(ACTIVITY_TYPE_ASSISTANT);
        options.setLaunchWindowingMode(WINDOWING_MODE_FULLSCREEN);
        caller.startActivity(intent, options.toBundle());
    }
}
