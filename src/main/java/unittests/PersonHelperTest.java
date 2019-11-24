package unittests;

import helpers.PersonHelper;
import org.testng.annotations.Test;

public class PersonHelperTest extends BaseTest {
    private PersonHelper unit = new PersonHelper();

    @Test
    public void getPersons() {
        System.out.println(unit.getPersons().size());

        System.out.println(unit.getPersons(9).size());

        System.out.println(unit.getPersons(10).size());
        System.out.println(unit.getPersons(0).size());
        System.out.println(unit.getPersons(-5).size());

        System.out.println(unit.getPersons("1234-567890").size());

        System.out.println(unit.getPersons("Иванов", null, null).size());
        System.out.println(unit.getPersons(null, "Валерий", null).size());
        System.out.println(unit.getPersons(null, null, "Михайлович").size());

        System.out.println(unit.getPersons("Иванов", "Петр", null).size());
        System.out.println(unit.getPersons(null, "Валерий", "Вениаминович").size());
        System.out.println(unit.getPersons("Иванов", null, "Михайлович").size());

        System.out.println(unit.getPersons("Глазунова", "Екатерина", "Васильевна").size());

    }
}
