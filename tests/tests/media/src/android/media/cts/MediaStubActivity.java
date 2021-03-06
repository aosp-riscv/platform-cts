/*
 * Copyright (C) 2009 The Android Open Source Project
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

import android.media.cts.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MediaStubActivity extends Activity {
    private static final String TAG = "MediaStubActivity";
    private SurfaceHolder mHolder;
    private SurfaceHolder mHolder2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTurnScreenOn(true);
        setShowWhenLocked(true);

        setContentView(R.layout.mediaplayer);

        SurfaceView surfaceV = (SurfaceView)findViewById(R.id.surface);
        mHolder = surfaceV.getHolder();

        SurfaceView surfaceV2 = (SurfaceView)findViewById(R.id.surface2);
        mHolder2 = surfaceV2.getHolder();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }
    public SurfaceHolder getSurfaceHolder() {
        return mHolder;
    }

    public SurfaceHolder getSurfaceHolder2() {
        return mHolder2;
    }
}
