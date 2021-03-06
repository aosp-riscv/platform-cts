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

package android.autofillservice.cts.testcore;

import static android.autofillservice.cts.testcore.Timeouts.FILL_TIMEOUT;

import static com.google.common.truth.Truth.assertWithMessage;

import android.widget.RadioGroup;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Custom {@link android.widget.RadioGroup.OnCheckedChangeListener} used to assert an
 * {@link RadioGroup} was auto-filled properly.
 */
public final class OneTimeRadioGroupListener implements RadioGroup.OnCheckedChangeListener {
    private final String name;
    private final CountDownLatch latch = new CountDownLatch(1);
    private final RadioGroup radioGroup;
    private final int expected;

    public OneTimeRadioGroupListener(String name, RadioGroup radioGroup,
            int expectedAutoFilledValue) {
        this.name = name;
        this.radioGroup = radioGroup;
        this.expected = expectedAutoFilledValue;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        latch.countDown();
    }

    public void assertAutoFilled() throws Exception {
        final boolean set = latch.await(FILL_TIMEOUT.ms(), TimeUnit.MILLISECONDS);
        assertWithMessage("Timeout (%s ms) on RadioGroup %s", FILL_TIMEOUT.ms(), name)
            .that(set).isTrue();
        final int actual = radioGroup.getCheckedRadioButtonId();
        assertWithMessage("Wrong auto-fill value on RadioGroup %s", name)
            .that(actual).isEqualTo(expected);
    }
}
