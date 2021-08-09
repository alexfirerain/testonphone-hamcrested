import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        assertThat(cb.contacts, not(hasValue(EXAMPLE_NUMBER)));
//        assertThat(cb.containsNumber(EXAMPLE_NUMBER), is(false));
    }

    @Test
    void contact_adds() {
        cb.addContact(EXAMPLE_CONTACT);
        assertThat(cb.contacts, hasValue(EXAMPLE_CONTACT));
    }

    @Test
    void bypasses_number() {
        assertThat(cb.tryToGetNameFor(EXAMPLE_NUMBER),
                equalTo(EXAMPLE_NUMBER));
    }

    @Test
    void returns_name_only_ifPresent() {
        cb.addContact(EXAMPLE_CONTACT);
        assertThat(cb.tryToGetNameFor(EXAMPLE_NUMBER), equalTo(EXAMPLE_SHORT_NAME));
        assertThat(cb.tryToGetNameFor(ANOTHER_NUMBER), equalTo(ANOTHER_NUMBER));
    }

    @ParameterizedTest
    @ValueSource(strings = { "+7931-7422816", "+7921-5446819", "+7951-8821316", "+7931-4468766" })
     void demoBase_correctly_fills(String number) {           // просто попробовать параметризованный тест
        cb = ContactBase.getBaseExample();
        assertThat(cb.contacts, hasKey(number));
    }


}