import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        IntStream.range(0, bound).
                forEach(i -> assertThat(stringsOutput[i].endsWith(CallList.CALLS_EXAMPLE[i]), is(true)));
    }

    @Test
    void generating_adds_aCall() {
        int initialLoading = demoCallList.missedCallsCount();
        demoCallList.generateAMissedCall(EXAMPLE_NUMBER);
        assertEquals(initialLoading + 1,
                demoCallList.missedCallsCount());
    }

    @Test
    void generating_adds_aCall_atTail() {
        int initialLoading = demoCallList.missedCallsCount();
        demoCallList.generateAMissedCall(EXAMPLE_NUMBER);
        assertAll("generating_adds_aCall_atTail",
                () -> assertEquals(initialLoading + 1,
                        demoCallList.missedCallsCount()),
                () -> assertEquals(EXAMPLE_NUMBER,
                        demoCallList.missedCalls.get(demoCallList.missedCalls.lastKey()))
        );
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
        assertEquals(timeBefore, demoCallList.virtualInternalTime.getTime());
    }

    @Test
    void time_correctly_promotes() {
        long timeBefore = demoCallList.virtualInternalTime.getTime();
        demoCallList.promoteVirtualTime(1000L);
        long timeAfter = demoCallList.virtualInternalTime.getTime();
        assertEquals(timeBefore + 1000L, timeAfter);
    }

    @Test
    void newDemoSequence_doubles_callList() {
        demoCallList.generateDemoMissedCallsSequence();
        assertEquals(CallList.CALLS_EXAMPLE.length * 2,
                demoCallList.missedCallsCount());
    }

    @Test
    void notCrashes_if_noContactBase() {
        assertDoesNotThrow(() -> demoCallList.giveMissedCalls(null));
    }

}