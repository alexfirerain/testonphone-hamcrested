import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        assertThat(cb.tryToGetNameFor(EXAMPLE_NUMBER),
                equalTo(EXAMPLE_SHORT_NAME));
    }

    @Test
    void bypasses_number_ifAbsent() {
        assertThat(cb.tryToGetNameFor(NUMBER_ABSENT_FROM_BASE),
                equalTo(NUMBER_ABSENT_FROM_BASE));
    }
    @Test
    void shows_ifContents() {
        assertThat(cb.containsNumber(EXAMPLE_NUMBER), is(true));
    }

    @Test
    void shows_ifDoesNotContent() {
        assertThat(cb.containsNumber(NUMBER_ABSENT_FROM_BASE), is(false));
    }

    @Test
    void contactInfo_gets_ByNumber() {
        assertThat(cb.getContactInfoByNumber(EXAMPLE_NUMBER),
                equalTo(A_CONTACT_FROM_BASE.toString()));
    }

    @Test
    void warning_ifNoEntity_toGetInfoOf() {
        assertThat(cb.getContactInfoByNumber(NUMBER_ABSENT_FROM_BASE),
                is("Контакт с таким номером не найден"));
    }
    @Test
    void contact_gets_byNumber() {
        assertThat(cb.getContactByNumber(EXAMPLE_NUMBER), is(A_CONTACT_FROM_BASE));
    }

    @Test
    void contact_gets_byNameSurname() {
        String name = A_CONTACT_FROM_BASE.getName(),
                surname = A_CONTACT_FROM_BASE.getSurname();
        assertThat(cb.getContactByNameSurname(name, surname), is(A_CONTACT_FROM_BASE));
    }
    @Test
    void contact_adds() {
        Contact newContact = new Contact("Другой", "Знакомый", "+7-000", Contact.Group.FRIENDS);
        cb.addContact(newContact);
        assertThat(cb.contacts, hasValue(newContact));
        assertThat(cb.containsNumber("+7-000"), is(true));
    }

    @Test
    void contact_removes_byNumber() {
        cb.removeContact(A_CONTACT_FROM_BASE.getNumber());
        assertThat(cb.contacts, not(hasValue(A_CONTACT_FROM_BASE)));
    }
    @Test
    void contact_removes_byNS() {
        Contact toRemove = A_CONTACT_FROM_BASE;
        String name = toRemove.getName(),
                surname = toRemove.getSurname();
        cb.removeContact(name, surname);
        assertThat(cb.contacts, not(hasValue(toRemove)));
    }



}