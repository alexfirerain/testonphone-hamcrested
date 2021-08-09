import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class DemoCallListTest {
    private final static String EXAMPLE_NUMBER = "+7-000";
    final ContactBase CB = ContactBase.getBaseExample();


    CallList demoCallList;

    @BeforeEach
    void setUp() {
        demoCallList = new CallList(true);
    }

    @Test
    void rightly_filled_at_start() {
        String[] calls = demoCallList.giveMissedCalls(CB);
        assertThat(calls, arrayWithSize(CallList.CALLS_EXAMPLE.length));
    }

    @Test
    void right_demoNumbers_sequence() {
        String[] stringsOutput = demoCallList.giveMissedCalls(new ContactBase());
        int bound = CallList.CALLS_EXAMPLE.length;
        IntStream.range(0, bound)
                .forEach(i ->
                        assertThat(stringsOutput[i], endsWith(CallList.CALLS_EXAMPLE[i]))
                );
    }

    @Test
    void generating_adds_aCall() {
        int initialCount = demoCallList.missedCallsCount();
        demoCallList.generateAMissedCall(EXAMPLE_NUMBER);
        int expectedCount = initialCount + 1;
        assertThat(demoCallList.missedCallsCount(), equalTo(expectedCount));
    }

    @Test
    void generating_adds_aCall_atTail() {
        int initialCount = demoCallList.missedCallsCount();
        demoCallList.generateAMissedCall(EXAMPLE_NUMBER);
        assertThat(demoCallList.missedCallsCount(), equalTo(initialCount + 1));

        Date lastCallTime = demoCallList.missedCalls.lastKey();
        assertThat(demoCallList.missedCalls.get(lastCallTime), is(EXAMPLE_NUMBER));
    }

    @Test
    void really_clears() {
        assertThat(demoCallList.missedCallsCount(), not(equalTo(0)));
        demoCallList.clear();
        assertThat(demoCallList.missedCallsCount(), equalTo(0));
    }

    @Test
    void clearing_doesNotAffect_time() {
        long timeBefore = demoCallList.virtualInternalTime.getTime();
        demoCallList.clear();
        assertThat(demoCallList.virtualInternalTime.getTime(), equalTo(timeBefore));
    }

    @Test
    void time_correctly_promotes() {
        long timeBefore = demoCallList.virtualInternalTime.getTime();
        demoCallList.promoteVirtualTime(1000L);
        long timeAfter = demoCallList.virtualInternalTime.getTime();
        assertThat(timeAfter, equalTo(timeBefore + 1000L));
    }

    @Test
    void newDemoSequence_doubles_callList() {
        demoCallList.generateDemoMissedCallsSequence();
        assertThat(demoCallList.missedCallsCount(),
                equalTo(CallList.CALLS_EXAMPLE.length * 2));
    }

    @Test
    void notCrashes_if_noContactBase() {
        assertDoesNotThrow(() -> demoCallList.giveMissedCalls(null));
    }

}