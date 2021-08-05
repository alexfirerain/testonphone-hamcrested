import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BlankContactBaseTest {
    ContactBase cb;
    private final String EXAMPLE_NUMBER = "+7-000";
    private final String ANOTHER_NUMBER = "+7-001";
    private final Contact EXAMPLE_CONTACT = new Contact("Имя", "Фамилия", EXAMPLE_NUMBER, Contact.Group.WORK);
    private final String EXAMPLE_SHORT_NAME = EXAMPLE_CONTACT.getShortString();


    @BeforeEach
    void setUp() {
        cb = new ContactBase();
    }

    @Test
    void shows_thatDoesNotContent() {
        assertFalse(cb.containsNumber(EXAMPLE_NUMBER));
    }

    @Test
    void contact_adds() {                                   // фактически тестируются и addContact(), и containsNumber()!
        cb.addContact(EXAMPLE_CONTACT);
        assertTrue(cb.containsNumber(EXAMPLE_CONTACT.getNumber()));
    }

    @Test
    void bypasses_number() {
        assertEquals(EXAMPLE_NUMBER,
                cb.tryToGetNameFor(EXAMPLE_NUMBER));
    }

    @Test
    void returns_name_ifPresent() {
        cb.addContact(EXAMPLE_CONTACT);
        assertAll("returns_name_ifPresent",
                () -> assertEquals(EXAMPLE_SHORT_NAME,
                        cb.tryToGetNameFor(EXAMPLE_NUMBER)),
                () -> assertEquals(ANOTHER_NUMBER,
                        cb.tryToGetNameFor(ANOTHER_NUMBER))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "+7931-7422816", "+7921-5446819", "+7951-8821316", "+7931-4468766" })
     void demoBase_correctly_fills(String number) {           // просто попробовать параметризованный тест
        cb = ContactBase.getBaseExample();
        assertTrue(cb.containsNumber(number));
    }


}