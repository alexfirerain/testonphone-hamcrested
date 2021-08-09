import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact("Имя", "Фамилия", "+7-000", Contact.Group.FRIENDS);
    }

    @Test
    public void toString_correctly_returns() {
        assertThat(contact, hasToString("Имя Фамилия: +7-000 (Друзья)"));
    }

    @Test
    void shortString_test() {
        String stringReturned = contact.getShortString();
        assertThat(stringReturned, is("Имя Фамилия"));
    }

    @Test
    void recreation_by_newNumber() {
        Contact newContact = new Contact("+7-001", contact);
        assertThat(newContact, hasToString("Имя Фамилия: +7-001 (Друзья)"));
    }

}