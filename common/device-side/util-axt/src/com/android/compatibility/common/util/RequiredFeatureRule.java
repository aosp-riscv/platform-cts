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

package com.android.compatibility.common.util;

import static org.junit.Assume.assumeTrue;

import android.util.Log;

import androidx.test.InstrumentationRegistry;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Custom JUnit4 rule that does not run a test case if the device does not have a given feature.
 *
 * <p>The tests are skipped by throwing a {@link AssumptionViolatedException}.  CTS test runners
 * will report this as a {@code ASSUMPTION_FAILED}.
 */
public class RequiredFeatureRule implements TestRule {
    private static final String TAG = "RequiredFeatureRule";

    private final String mFeature;
    private final boolean mHasFeature;

    public RequiredFeatureRule(String feature) {
        mFeature = feature;
        mHasFeature = hasFeature(feature);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                if (!mHasFeature) {
                    Log.d(TAG, "skipping "
                            + description.getClassName() + "#" + description.getMethodName()
                            + " because device does not have feature '" + mFeature + "'");
                    assumeTrue("Device does not have feature '" + mFeature + "'",
                            mHasFeature);
                    return;
                }
                base.evaluate();
            }
        };
    }

    @Override
    public String toString() {
        return "RequiredFeatureRule[" + mFeature + "]";
    }

    public static boolean hasFeature(String feature) {
        return InstrumentationRegistry.getContext().getPackageManager().hasSystemFeature(feature);
    }
}
