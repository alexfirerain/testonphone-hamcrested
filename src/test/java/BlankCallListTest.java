import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class BlankCallListTest {
    private final static String EXAMPLE_NUMBER = "+7-000";
    private final static String EXAMPLE_NUMBER_ALT = "+7-001";

    CallList callList;

    @BeforeEach
    void setUp() {
        callList = new CallList();
    }

    @Test
    void empty_at_start() {
        assertEquals(0,
                callList.missedCallsCount());
    }

    @Test
    void empty_list_at_start() {
        assertThat(callList.giveMissedCalls(ContactBase.getBaseExample()), is(emptyArray()));
    }

    @Test
    void promotion_changes_time() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.promoteVirtualTime(1000L);
        long timeAfter = callList.virtualInternalTime.getTime();
        assertNotEquals(timeBefore, timeAfter);
    }

    @Test
    void time_correctly_promotes() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.promoteVirtualTime(1000L);
        long timeAfter = callList.virtualInternalTime.getTime();
        assertEquals(timeBefore + 1000L, timeAfter);
    }

    @Test
    void takingCall_adds_aCall() {
        callList.takeAMissedCall(EXAMPLE_NUMBER);
        assertEquals(1, callList.missedCallsCount());
    }

    @Test
    void takingCall_doesNot_changeTime() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.takeAMissedCall(EXAMPLE_NUMBER);
        assertEquals(timeBefore, callList.missedCalls.lastKey().getTime());
    }

    @Test
    void generating_adds_aCall() {
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        assertEquals(1, callList.missedCallsCount());
    }

    @Test
    void generatedCall_adds_atTail() {
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        assertEquals(EXAMPLE_NUMBER,
                callList.missedCalls.get(callList.missedCalls.lastKey()));
    }

    @Test
    void new_missedCall_gets_newDate() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        assertTrue(timeBefore < callList.missedCalls.lastKey().getTime());
    }
    @Test
    void newDate_not_later_than_MAX() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        long delta = callList.missedCalls.lastKey().getTime() - timeBefore;
        assertTrue(delta <= CallList.MAX_CALL_INTERVAL + 1);
    }

    @Test
    void two_timePeriods_differ() {                                         // random testing!
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        long delta1 = callList.missedCalls.lastKey().getTime() - timeBefore;
        callList.generateAMissedCall(EXAMPLE_NUMBER_ALT);
        long delta2 = callList.missedCalls.lastKey().getTime() - timeBefore - delta1;
        assertTrue(delta1 != delta2);
    }

    @Test
    void demo_fills_up_with_sample() {
        callList.generateDemoMissedCallsSequence();
        assertEquals(CallList.CALLS_EXAMPLE.length,
                callList.missedCallsCount());
    }
}