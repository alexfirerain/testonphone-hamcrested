import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class BlankCallListTest {
    private final static String EXAMPLE_NUMBER = "+7-000";
    private final static String EXAMPLE_NUMBER_ALT = "+7-001";
    private final static ContactBase CB = ContactBase.getBaseExample();

    CallList callList;

    @BeforeEach
    void setUp() {
        callList = new CallList();
    }

    @Test
    void empty_at_start() {
        assertThat(callList.missedCallsCount(), equalTo(0));
    }

    @Test
    void empty_list_at_start() {
        assertThat(callList.giveMissedCalls(CB), is(emptyArray()));
    }

    @Test
    void promotion_changes_time() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.promoteVirtualTime(1000L);
        long timeAfter = callList.virtualInternalTime.getTime();
        assertThat(timeAfter, greaterThan(timeBefore));
    }

    @Test
    void time_correctly_promotes() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.promoteVirtualTime(1000L);
        long expectedTime = timeBefore + 1000L;
        assertThat(expectedTime, equalTo(callList.virtualInternalTime.getTime()));
    }

    @Test
    void takingCall_adds_aCall() {
        callList.takeAMissedCall(EXAMPLE_NUMBER);
        assertThat(callList.missedCallsCount(), equalTo(1));
    }

    @Test
    void takingCall_doesNot_changeTime() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.takeAMissedCall(EXAMPLE_NUMBER);
        assertThat(callList.missedCalls.lastKey().getTime(), equalTo(timeBefore));
    }

    @Test
    void generating_adds_aCall() {
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        assertThat(callList.missedCallsCount(), equalTo(1));
    }

    @Test
    void generatedCall_adds_atTail() {
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        Date lastCallDate = callList.missedCalls.lastKey();
        String lastCall = callList.missedCalls.get(lastCallDate);
        assertThat(lastCall, equalTo(EXAMPLE_NUMBER));
    }
    @Test
    void new_missedCall_gets_newDate() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        Date lastCallDate = callList.missedCalls.lastKey();
        assertThat(timeBefore, lessThan(lastCallDate.getTime()));
    }

    @Test
    void generatedCall_has_currentInternalDate() {
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        Date lastCallDate = callList.missedCalls.lastKey();
        assertThat(lastCallDate, equalTo(callList.virtualInternalTime));
    }
    @Test
    void newDate_not_later_than_MAX() {
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        long delta = callList.missedCalls.lastKey().getTime() - timeBefore;
        assertThat(delta,
                lessThanOrEqualTo(CallList.MAX_CALL_INTERVAL + 1));
    }

    @Test
    void two_timePeriods_differ() {                                         // random testing!
        long timeBefore = callList.virtualInternalTime.getTime();
        callList.generateAMissedCall(EXAMPLE_NUMBER);
        long delta1 = callList.missedCalls.lastKey().getTime() - timeBefore;
        callList.generateAMissedCall(EXAMPLE_NUMBER_ALT);
        long delta2 = callList.missedCalls.lastKey().getTime() - timeBefore - delta1;
        assertThat(delta1, not(equalTo(delta2)));
    }

    @Test
    void demo_fills_up_with_sample() {
        callList.generateDemoMissedCallsSequence();
        assertThat(callList.missedCallsCount(), equalTo(CallList.CALLS_EXAMPLE.length));
    }
}