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
package android.autofillservice.cts.dropdown;

import static android.autofillservice.cts.testcore.Helper.ID_PASSWORD;
import static android.autofillservice.cts.testcore.Helper.ID_PASSWORD_LABEL;
import static android.autofillservice.cts.testcore.Helper.ID_USERNAME;
import static android.autofillservice.cts.testcore.Helper.ID_USERNAME_LABEL;
import static android.autofillservice.cts.testcore.Helper.assertTextAndValue;
import static android.autofillservice.cts.testcore.Helper.assertTextFromResources;
import static android.autofillservice.cts.testcore.Helper.assertTextIsSanitized;
import static android.autofillservice.cts.testcore.Helper.assertTextOnly;
import static android.autofillservice.cts.testcore.Helper.findNodeByResourceId;
import static android.service.autofill.SaveInfo.SAVE_DATA_TYPE_PASSWORD;

import android.autofillservice.cts.R;
import android.autofillservice.cts.activities.PreFilledLoginActivity;
import android.autofillservice.cts.commontests.AutoFillServiceTestCase;
import android.autofillservice.cts.testcore.AutofillActivityTestRule;
import android.autofillservice.cts.testcore.CannedFillResponse;
import android.autofillservice.cts.testcore.InstrumentedAutoFillService.FillRequest;
import android.autofillservice.cts.testcore.InstrumentedAutoFillService.SaveRequest;
import android.platform.test.annotations.AppModeFull;

import org.junit.Test;

/**
 * Covers scenarios where the behavior is different because some fields were pre-filled.
 */
@AppModeFull(reason = "LoginActivityTest is enough")
public class PreFilledLoginActivityTest
        extends AutoFillServiceTestCase.AutoActivityLaunch<PreFilledLoginActivity> {

    private PreFilledLoginActivity mActivity;

    @Override
    protected AutofillActivityTestRule<PreFilledLoginActivity> getActivityRule() {
        return new AutofillActivityTestRule<PreFilledLoginActivity>(PreFilledLoginActivity.class) {
            @Override
            protected void afterActivityLaunched() {
                mActivity = getActivity();
            }
        };
    }

    @Test
    public void testSanitization() throws Exception {
        // Set service.
        enableService();

        // Set expectations.
        sReplier.addResponse(new CannedFillResponse.Builder()
                .setRequiredSavableIds(SAVE_DATA_TYPE_PASSWORD, ID_USERNAME, ID_PASSWORD)
                .build());

        // Change view contents.
        mActivity.onUsernameLabel((v) -> v.setText("DA USER"));
        mActivity.onPasswordLabel((v) -> v.setText(R.string.new_password_label));

        // Trigger auto-fill.
        mActivity.onUsername((v) -> v.requestFocus());

        // Assert sanitization on fill request:
        final FillRequest fillRequest = sReplier.getNextFillRequest();

        // ...dynamic text should be sanitized.
        assertTextIsSanitized(fillRequest.structure, ID_USERNAME_LABEL);

        // ...password label should be ok because it was set from other resource id
        assertTextFromResources(fillRequest.structure, ID_PASSWORD_LABEL, "DA PASSWORD", false,
                "new_password_label");

        // ...username and password should be ok because they were set in the SML
        assertTextAndValue(findNodeByResourceId(fillRequest.structure, ID_USERNAME),
                "secret_agent");
        assertTextAndValue(findNodeByResourceId(fillRequest.structure, ID_PASSWORD), "T0p S3cr3t");

        // Trigger save
        mActivity.onUsername((v) -> v.setText("malkovich"));
        mActivity.onPassword((v) -> v.setText("malkovich"));
        mActivity.tapLogin();

        // Assert the snack bar is shown and tap "Save".
        mUiBot.saveForAutofill(true, SAVE_DATA_TYPE_PASSWORD);
        final SaveRequest saveRequest = sReplier.getNextSaveRequest();

        // Assert sanitization on save: everything should be available!
        assertTextOnly(findNodeByResourceId(saveRequest.structure, ID_USERNAME_LABEL), "DA USER");
        assertTextFromResources(saveRequest.structure, ID_PASSWORD_LABEL, "DA PASSWORD", false,
                "new_password_label");
        assertTextAndValue(findNodeByResourceId(saveRequest.structure, ID_USERNAME), "malkovich");
        assertTextAndValue(findNodeByResourceId(saveRequest.structure, ID_PASSWORD), "malkovich");
    }
}
