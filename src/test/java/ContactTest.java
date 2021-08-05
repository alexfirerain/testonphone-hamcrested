import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact("Имя", "Фамилия", "+7-000", Contact.Group.FRIENDS);
    }

    @Test
    void toString_test() {
        assertEquals("Имя Фамилия: +7-000 (Друзья)", contact.toString());
    }

    @Test
    void shortString_test() {
        assertEquals("Имя Фамилия", contact.getShortString());
    }

    @Test
    void recreation_by_newNumber() {
        Contact newContact = new Contact("+7-001", contact);
        assertEquals("Имя Фамилия: +7-001 (Друзья)", newContact.toString());
    }

}