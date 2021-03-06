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
package android.content.pm.cts.shortcut.backup.launcher4;

import static com.android.server.pm.shortcutmanagertest.ShortcutManagerTestUtils.list;

import android.content.pm.cts.shortcut.device.common.ShortcutManagerDeviceTestBase;
import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ShortcutManagerPreBackupTest extends ShortcutManagerDeviceTestBase {
    static final String PUBLISHER4_PKG =
            "android.content.pm.cts.shortcut.backup.publisher4";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setAsDefaultLauncher();
    }

    public void testPreBackup() {
        // Pin all the shortcuts.
        getLauncherApps().pinShortcuts(PUBLISHER4_PKG, list("s1", "s2", "ms1", "ms2"),
                getUserHandle());
    }
}
