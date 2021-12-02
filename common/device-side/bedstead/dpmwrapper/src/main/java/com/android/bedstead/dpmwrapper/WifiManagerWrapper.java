/*
 * Copyright (C) 2021 The Android Open Source Project
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
package com.android.bedstead.dpmwrapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.android.bedstead.dpmwrapper.TestAppSystemServiceFactory.ServiceManagerWrapper;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.HashMap;

final class WifiManagerWrapper extends ServiceManagerWrapper<WifiManager> {

    private static final String TAG = WifiManagerWrapper.class.getSimpleName();

    private static final HashMap<Context, WifiManager> sSpies = new HashMap<>();

    @Override
    WifiManager getWrapper(Context context, WifiManager manager, Answer<?> answer) {
        int userId = context.getUserId();
        WifiManager spy = sSpies.get(context);
        if (spy != null) {
            Log.d(TAG, "get(): returning cached spy for user " + userId);
            return spy;
        }

        spy = Mockito.spy(manager);
        String spyString = "WifiManagerWrapper#" + System.identityHashCode(spy);
        Log.d(TAG, "get(): created spy for user " + context.getUserId() + ": " + spyString);

        // TODO(b/176993670): ideally there should be a way to automatically mock all DPM methods,
        // but that's probably not doable, as there is no contract (such as an interface) to specify
        // which ones should be spied and which ones should not (in fact, if there was an interface,
        // we wouldn't need Mockito and could wrap the calls using java's DynamicProxy
        try {
            doReturn(spyString).when(spy).toString();

            // Used by WifiConfigCreator
            doAnswer(answer).when(spy).addNetwork(any());
            doAnswer(answer).when(spy).enableNetwork(anyInt(), anyBoolean());
            doAnswer(answer).when(spy).removeNetwork(anyInt());
            doAnswer(answer).when(spy).getConfiguredNetworks();
            doAnswer(answer).when(spy).updateNetwork(any());
            doAnswer(answer).when(spy).saveConfiguration();
            doAnswer(answer).when(spy).isWifiEnabled();
            doAnswer(answer).when(spy).setWifiEnabled(anyBoolean());

            // Used by WifiNetworkConfigurationWithoutFineLocationPermissionTest
            doAnswer(answer).when(spy).getCallerConfiguredNetworks();
        } catch (Exception e) {
            // Should never happen, but needs to be catch as some methods declare checked exceptions
            Log.wtf("Exception setting mocks", e);
        }

        sSpies.put(context, spy);
        Log.d(TAG, "get(): returning new spy for context " + context + " and user "
                + userId);

        return spy;
    }
}
