import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemoContactBaseTest {
    private final Contact A_CONTACT_FROM_BASE = new Contact("Пахом", "Пахомыч", "+7931-7422816", Contact.Group.WORK);
    private final String EXAMPLE_NUMBER = A_CONTACT_FROM_BASE.getNumber();            // данные связаны с содержанием getExampleBase()
    private final String EXAMPLE_SHORT_NAME = A_CONTACT_FROM_BASE.getShortString();    // соответствуют первой записи из него

    private final String NUMBER_ABSENT_FROM_BASE = "+7-000";

    private ContactBase cb;

    @BeforeEach
    void setUp() {
        cb = ContactBase.getBaseExample();
    }

    @Test
    void resolves_name_ifPresent() {
        assertEquals(EXAMPLE_SHORT_NAME,
                cb.tryToGetNameFor(EXAMPLE_NUMBER));
    }

    @Test
    void bypasses_number_ifAbsent() {
        assertEquals(NUMBER_ABSENT_FROM_BASE,
                cb.tryToGetNameFor(NUMBER_ABSENT_FROM_BASE));
    }
    @Test
    void shows_ifContents() {
        assertTrue(cb.containsNumber(EXAMPLE_NUMBER));
    }

    @Test
    void shows_ifDoesNotContent() {
        assertFalse(cb.containsNumber(NUMBER_ABSENT_FROM_BASE));
    }

    @Test
    void contactInfo_gets_ByNumber() {
        assertEquals(A_CONTACT_FROM_BASE.toString(),
                cb.getContactInfoByNumber(EXAMPLE_NUMBER));
    }

    @Test
    void warning_ifNoEntity_toGetInfoOf() {
        assertEquals("Контакт с таким номером не найден",
                cb.getContactInfoByNumber(NUMBER_ABSENT_FROM_BASE));
    }
    @Test
    void contact_gets_byNumber() {
        assertEquals(A_CONTACT_FROM_BASE,
                cb.getContactByNumber(EXAMPLE_NUMBER));
    }

    @Test
    void contact_gets_byNameSurname() {
        assertEquals(A_CONTACT_FROM_BASE,
                cb.getContactByNameSurname(A_CONTACT_FROM_BASE.getName(), A_CONTACT_FROM_BASE.getSurname()));
    }
    @Test
    void contact_adds() {
        Contact newContact = new Contact("Другой", "Знакомый", "+7-000", Contact.Group.FRIENDS);
        cb.addContact(newContact);
        assertTrue(cb.containsNumber("+7-000"));
    }

    @Test
    void contact_removes_byNumber() {
        cb.removeContact(A_CONTACT_FROM_BASE.getNumber());
        assertFalse(cb.containsNumber(EXAMPLE_NUMBER));
    }
    @Test
    void contact_removes_byNS() {
        cb.removeContact(EXAMPLE_NUMBER);
        assertFalse(cb.containsNumber(EXAMPLE_NUMBER));
    }



}