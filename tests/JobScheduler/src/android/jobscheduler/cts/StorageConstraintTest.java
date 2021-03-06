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

package android.jobscheduler.cts;


import android.annotation.TargetApi;
import android.app.job.JobInfo;

/**
 * Schedules jobs with the {@link android.app.job.JobScheduler} that have storage constraints.
 */
@TargetApi(26)
public class StorageConstraintTest extends BaseJobSchedulerTest {
    private static final String TAG = "StorageConstraintTest";

    /** Unique identifier for the job scheduled by this suite of tests. */
    public static final int STORAGE_JOB_ID = StorageConstraintTest.class.hashCode();

    private JobInfo.Builder mBuilder;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mBuilder = new JobInfo.Builder(STORAGE_JOB_ID, kJobServiceComponent);
    }

    @Override
    public void tearDown() throws Exception {
        mJobScheduler.cancel(STORAGE_JOB_ID);
        super.tearDown();
    }

    String getJobState() throws Exception {
        return getJobState(STORAGE_JOB_ID);
    }

    void assertJobReady() throws Exception {
        assertJobReady(STORAGE_JOB_ID);
    }

    void assertJobWaiting() throws Exception {
        assertJobWaiting(STORAGE_JOB_ID);
    }

    void assertJobNotReady() throws Exception {
        assertJobNotReady(STORAGE_JOB_ID);
    }

    // --------------------------------------------------------------------------------------------
    // Positives - schedule jobs under conditions that require them to pass.
    // --------------------------------------------------------------------------------------------

    /**
     * Schedule a job that requires the device storage is not low, when it is actually not low.
     */
    public void testNotLowConstraintExecutes() throws Exception {
        setStorageStateLow(false);

        kTestEnvironment.setExpectedExecutions(1);
        kTestEnvironment.setExpectedWaitForRun();
        mJobScheduler.schedule(mBuilder.setRequiresStorageNotLow(true).build());
        assertJobReady();
        kTestEnvironment.readyToRun();

        assertTrue("Job with storage not low constraint did not fire when storage not low.",
                kTestEnvironment.awaitExecution());
    }

    // --------------------------------------------------------------------------------------------
    // Negatives - schedule jobs under conditions that require that they fail.
    // --------------------------------------------------------------------------------------------

    /**
     * Schedule a job that requires the device storage is not low, when it actually is low.
     */
    public void testNotLowConstraintFails() throws Exception {
        setStorageStateLow(true);

        kTestEnvironment.setExpectedExecutions(0);
        kTestEnvironment.setExpectedWaitForRun();
        mJobScheduler.schedule(mBuilder.setRequiresStorageNotLow(true).build());
        assertJobWaiting();
        assertJobNotReady();
        kTestEnvironment.readyToRun();

        assertFalse("Job with storage now low constraint fired while low.",
                kTestEnvironment.awaitExecution(250));

        // And for good measure, ensure the job runs once storage is okay.
        kTestEnvironment.setExpectedExecutions(1);
        kTestEnvironment.setExpectedWaitForRun();
        setStorageStateLow(false);
        assertJobReady();
        kTestEnvironment.readyToRun();
        assertTrue("Job with storage not low constraint did not fire when storage not low.",
                kTestEnvironment.awaitExecution());
    }

    /**
     * Test that a job that requires the device storage is not low is stopped when it becomes low.
     */
    public void testJobStoppedWhenStorageLow() throws Exception {
        setStorageStateLow(false);

        kTestEnvironment.setExpectedExecutions(1);
        kTestEnvironment.setContinueAfterStart();
        kTestEnvironment.setExpectedWaitForRun();
        kTestEnvironment.setExpectedStopped();
        mJobScheduler.schedule(mBuilder.setRequiresStorageNotLow(true).build());
        assertJobReady();
        kTestEnvironment.readyToRun();

        assertTrue("Job with storage not low constraint did not fire when storage not low.",
                kTestEnvironment.awaitExecution());

        setStorageStateLow(true);
        assertTrue("Job with storage not low constraint was not stopped when storage became low.",
                kTestEnvironment.awaitStopped());
    }
}
