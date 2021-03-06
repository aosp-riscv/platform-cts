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

package com.android.cts.verifier.dialer;

import android.content.Intent;
import android.os.IBinder;
import android.telecom.InCallService;
import java.util.Observable;
import android.telecom.Call;

import com.android.cts.verifier.telecom.CtsIncomingCall;
import com.android.cts.verifier.telecom.CtsVerifierInCallUi;

public class DialerCallTestService extends InCallService {
  public static final String EXTRA_CALL_NAME = "incoming_call_name";

  private static DialerCallTestServiceObservable sObservable =
      new DialerCallTestServiceObservable();

  @Override
  public IBinder onBind(Intent intent) {
    return super.onBind(intent);
  }

  public static DialerCallTestServiceObservable getObservable() {
    return sObservable;
  }

  public static class DialerCallTestServiceObservable extends Observable {
    private boolean onIncoming;

    public void setOnIncoming(boolean value) {
      this.onIncoming = value;
      setChanged();
      notifyObservers(value);
    }

    public boolean getOnIncoming() {
      return this.onIncoming;
    }
  }

  @Override
  public void onCallAdded(Call call) {
    if (call.getState() == Call.STATE_RINGING) {
      getObservable().setOnIncoming(true);
      CtsIncomingCall.getInstance().setCall(call);
      Intent intent = new Intent(Intent.ACTION_MAIN);
      if (call.getDetails().getHandle() != null) {
        intent.putExtra(EXTRA_CALL_NAME, call.getDetails().getHandle().getSchemeSpecificPart());
      }
      intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setClass(this, CtsVerifierInCallUi.class);
      startActivity(intent);
    }
  }
}
