/*
 * Copyright (C) 2015 The Android Open Source Project
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

import android.test.AndroidTestCase;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.Charset;

/**
 * Verify that the boot.art isn't mapped out of the system partition.
 */
public class ARTBootASLRTest extends AndroidTestCase {
    public void testARTASLR() throws Exception {
        FileInputStream ins = new FileInputStream("/proc/self/maps");
        InputStreamReader reader = new InputStreamReader(ins, Charset.defaultCharset());
        BufferedReader bufReader = new BufferedReader(reader);
        String line;
        boolean foundBootART = false;
        while ((line = bufReader.readLine()) != null) {
            // Check that we have a data or extracted boot image.
            // There should still be a system boot image mapping that contains only the image
            // bitmap.
            if (line.matches("(.*) \\[anon:dalvik(.*)boot\\.art\\]?")) {
                foundBootART = true;
            } else if (line.matches("(.*) /data/dalvik-cache/(.*)boot\\.art")) {
                foundBootART = true;
            }
        }
        if (!foundBootART) {
            fail("expected to find boot.art");
        }
        bufReader.close();
        reader.close();
        ins.close();
    }
}
