/**
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

package android.security.cts;

import android.platform.test.annotations.AsbSecurityTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.android.tradefed.testtype.DeviceJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(DeviceJUnit4ClassRunner.class)
public class Poc17_12 extends SecurityTestCase {

    /**
     * b/38045794
     */
    @Test
    @AsbSecurityTest(cveBugId = 38045794)
    public void testPocCVE_2017_6262() throws Exception {
        if(containsDriver(getDevice(),"/dev/dri/renderD128")) {
            AdbUtils.runPocNoOutput("CVE-2017-6262", getDevice(), TIMEOUT_NONDETERMINISTIC);
        }
    }
}
