/*
 * Copyright (C) 2019 The Android Open Source Project
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

package android.location.cts.common;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LocationListenerCapture implements LocationListener, AutoCloseable {

    private final LocationManager mLocationManager;
    private final LinkedBlockingQueue<Location> mLocations;
    private final LinkedBlockingQueue<Integer> mFlushes;
    private final LinkedBlockingQueue<Boolean> mProviderChanges;

    public LocationListenerCapture(Context context) {
        mLocationManager = context.getSystemService(LocationManager.class);
        mLocations = new LinkedBlockingQueue<>();
        mFlushes = new LinkedBlockingQueue<>();
        mProviderChanges = new LinkedBlockingQueue<>();
    }

    public Location getNextLocation(long timeoutMs) throws InterruptedException {
        return mLocations.poll(timeoutMs, TimeUnit.MILLISECONDS);
    }

    public Integer getNextFlush(long timeoutMs) throws InterruptedException {
        return mFlushes.poll(timeoutMs, TimeUnit.MILLISECONDS);
    }

    public Boolean getNextProviderChange(long timeoutMs) throws InterruptedException {
        return mProviderChanges.poll(timeoutMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocations.add(location);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        mFlushes.add(requestCode);
    }

    @Override
    public void onProviderEnabled(String provider) {
        mProviderChanges.add(true);
    }

    @Override
    public void onProviderDisabled(String provider) {
        mProviderChanges.add(false);
    }

    @Override
    public void close() {
        mLocationManager.removeUpdates(this);
    }
}