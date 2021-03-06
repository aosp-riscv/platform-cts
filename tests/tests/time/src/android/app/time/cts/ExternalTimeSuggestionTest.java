/*
 * Copyright 2021 The Android Open Source Project
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

package android.app.time.cts;

import static android.app.time.cts.ParcelableTestSupport.assertRoundTripParcelable;
import static android.app.time.cts.ParcelableTestSupport.roundTripParcelable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.app.time.ExternalTimeSuggestion;

import org.junit.Test;

public class ExternalTimeSuggestionTest {

    private static final long ARBITRARY_REFERENCE_TIME = 1111L;
    private static final long ARBITRARY_UTC_TIME = 2222L;

    @Test
    public void testEquals() {
        ExternalTimeSuggestion one = new ExternalTimeSuggestion(
                ARBITRARY_REFERENCE_TIME, ARBITRARY_UTC_TIME);
        assertEquals(one, one);

        ExternalTimeSuggestion two = new ExternalTimeSuggestion(
                ARBITRARY_REFERENCE_TIME, ARBITRARY_UTC_TIME);
        assertEquals(one, two);
        assertEquals(two, one);

        ExternalTimeSuggestion three = new ExternalTimeSuggestion(
                ARBITRARY_REFERENCE_TIME + 1, ARBITRARY_UTC_TIME);
        assertNotEquals(one, three);
        assertNotEquals(three, one);

        // DebugInfo must not be considered in equals().
        one.addDebugInfo("Debug info 1");
        two.addDebugInfo("Debug info 2");
        assertEquals(one, two);
    }

    @Test
    public void testParcelable() {
        ExternalTimeSuggestion suggestion = new ExternalTimeSuggestion(
                ARBITRARY_REFERENCE_TIME, ARBITRARY_UTC_TIME);
        assertRoundTripParcelable(suggestion);

        // DebugInfo should also be stored (but is not checked by equals())
        suggestion.addDebugInfo("This is debug info");
        ExternalTimeSuggestion rtSuggestion = roundTripParcelable(suggestion);
        assertEquals(suggestion.getDebugInfo(), rtSuggestion.getDebugInfo());
    }
}
