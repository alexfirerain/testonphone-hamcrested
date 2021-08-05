import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static CallList callList;
    static ContactBase contactBase;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        callList = new CallList(true);
        contactBase = ContactBase.getBaseExample();
        runDemo();
    }

    private static void runDemo() {
        System.out.println("Телефонная книга с функцией пропущенных звонков!");
        boolean exit = false;
        while (!exit) {
            System.out.println("""

                    1 = добавить контакт
                    \t1+ = редактировать контакт
                    \t1- = удалить контакт
                    2 = имитация пропущенного звонка
                    3 = список пропущенных звонков
                    4 = опорожнение списка вызовов
                    0 = завершение программы""");
            switch (input.nextLine()) {
                case "0", "выход" -> exit = true;
                case "1", "добавить" -> System.out.println(addAContact() ? "Контакт добавлен" : "Добавление отменено");
                case "1+", "редактировать" -> System.out.println(editContact() ? "Контакт изменён" : "Изменение отменено");
                case "1-", "удалить" -> System.out.println(deleteContact() ? "Контакт удалён" : "Удаление отменено");
                case "2", "звонок" -> imitateANewMissedCall();
                case "3", "список" -> showMissedCallsList();
                case "4", "очистить" -> clear();
            }
        }
        System.out.println("Завершение работы с телефоном.");
    }


    // основной функционал:
    static void showMissedCallsList() {
        System.out.println("Пропущенные звонки:");
        if (callList.missedCallsCount() == 0) {
            System.out.println("<список пуст>");
        } else {
            Arrays.stream(callList.giveMissedCalls(contactBase))
                    .forEach(System.out::println);
        }
    }

    private static boolean addAContact() {
        System.out.println("Добавление контакта.");
        String[] fields = { "Имя", "Фамилия", "Телефон" };
        String[] values = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String parameter = getInput(fields[i]);
            if (parameter.equals("")) {
                System.out.println("Пустое поле, отмена добавления");
                return false;
            }
            values[i] = parameter;
        }
        if (contactBase.containsNumber(values[2])) {
            System.out.println("Контакт с таким номером уже присутствует. Заменить? (+ для подтверждения)");
            if (!input.nextLine().equals("+")) return false;
        }
        Contact.Group group = chooseGroup();
        contactBase.addContact(new Contact(values[0], values[1], values[2], group));
        return true;
    }
    private static boolean editContact() {
        boolean modified = false;
        Contact beingEdited = findContact("для редактирования");
        if (beingEdited == null) {
            System.out.println("Контакт не найден");
            return false;
        }
        System.out.println("Имя = " + beingEdited.getName() +
                "\nВведите новое имя (или пустую строку оставить прежнее)");
        String newName = input.nextLine();
        if (!newName.equals("")) {
            beingEdited.setName(newName);
            modified = true;
        }
        System.out.println("Фамилия = " + beingEdited.getSurname() +
                "\nВведите новую фамилию (или пустую строку оставить прежнюю)");
        String newSurname = input.nextLine();
        if (!newSurname.equals("")) {
            beingEdited.setSurname(newSurname);
            modified = true;
        }
        System.out.println("Телефон = " + beingEdited.getNumber() +
                "\nВведите новый номер (или пустую строку оставить прежний)");
        String newNumber = input.nextLine();
        if (!newNumber.equals("")) {
            Contact changed = new Contact(newNumber, beingEdited);
            contactBase.addContact(changed);
            contactBase.removeContact(beingEdited.getNumber());
            beingEdited = changed;
            modified = true;
        }
        System.out.println("Группа = " + beingEdited.getGroup());
        Contact.Group newGroup = chooseGroup();
        if (newGroup != beingEdited.getGroup()) {
            beingEdited.setGroup(newGroup);
            modified = true;
        }
        return modified;
    }

    private static void imitateANewMissedCall() {
        System.out.println("Введите номер звонка:");
        String number = input.nextLine();
        if (!number.equals(""))
            callList.generateAMissedCall(number);
    }

    private static boolean deleteContact() {
        Contact beingDeleted = findContact("для удаления");
        if (beingDeleted == null) {
            System.out.println("Контакт не найден");
            return false;
        }
        contactBase.removeContact(beingDeleted.getNumber());
        return true;
    }

    static void clear() {
        System.out.println("Очистить список звонков? (+ для подтверждения)");
        if (input.nextLine().equals("+")) {
            callList.clear();
            System.out.println("Список пропущенных звонков опустошён.");
        } else {
            System.out.println("Очистка отменена");
        }
    }

    // служебные функции:

    protected static String getInput(String field) {
        System.out.print(field + ": ");
        return input.nextLine();
    }
    protected static Contact.Group chooseGroup() {
        while (true) {
            System.out.println("Выберите группу контакта:\n1 = Работа\n2 = Друзья\n3 = Семья");
            switch (input.nextLine()) {
                case "1" : return Contact.Group.WORK;
                case "2" : return Contact.Group.FRIENDS;
                case "3" : return Contact.Group.FAMILY;
            }
        }
    }
    protected static Contact findContact(String purpose) {
        System.out.println("Выбрать контакт " + purpose + " по:\n" +
                "1 = имени и фамилии\n" +
                "2 = номеру телефона");
        while (true) {
            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Введите имя:");
                    String name = input.nextLine();
                    System.out.println("Введите фамилию:");
                    String surname = input.nextLine();
                    return contactBase.getContactByNameSurname(name, surname);
                }
                case "2" -> {
                    System.out.println("Введите номер:");
                    return contactBase.getContactByNumber(input.nextLine());
                }
            }
        }

    }

}
