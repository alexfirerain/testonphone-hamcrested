import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ContactBase {
    private final HashMap<String, Contact> contacts;

    public ContactBase() {
        contacts = new HashMap<>();
    }

    public void addContact(Contact contact) {
        contacts.put(contact.getNumber(), contact);
    }

    public String tryToGetNameFor(String number) {
        return containsNumber(number) ?
                contacts.get(number).getShortString() :
                number;
    }

    public boolean containsNumber(String number) {
        return contacts.containsKey(number);
    }

    void removeContact(String number) {
        contacts.remove(number);
    }
    boolean removeContact(String name, String surname) {
        for (Map.Entry<String, Contact> contactEntry : contacts.entrySet()) {
            Contact contact = contactEntry.getValue();
            if (contact.getName().equals(name) && contact.getSurname().equals(surname)) {
                contacts.remove(contactEntry.getKey());
                return true;
            }
        }
        return false;
    }

    public String getContactInfoByNumber(String number) {
        if (!containsNumber(number))
            return "Контакт с таким номером не найден";

        return contacts.get(number).toString();
    }
    Contact getContactByNumber(String number) {
        return contacts.get(number);
    }

    Contact getContactByNameSurname(String name, String surname) {
        for (Map.Entry<String, Contact> contact : contacts.entrySet()) {
            Contact next = contact.getValue();
            if (next.getName().equals(name) && next.getSurname().equals(surname))
                return next;
        }
        return null;
    }

    // демо-пример списка контактов
    public static ContactBase getBaseExample() {
        ContactBase demoBase = new ContactBase();

        Stream.of(
                new Contact("Пахом", "Пахомыч", "+7931-7422816", Contact.Group.WORK),
                new Contact("Жека", "Склад", "+7921-5446819", Contact.Group.FRIENDS),
                new Contact("Давар", "Агроном", "+7951-8821316", Contact.Group.FAMILY),
                new Contact("Артём", "Скубедь", "+7931-4468766", Contact.Group.WORK)
        ).forEach(demoBase::addContact);

        return demoBase;
    }
}
