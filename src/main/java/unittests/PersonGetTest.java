package unittests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import responses.*;
import webservice.WebServicePerson;

public class PersonGetTest {

    @DataProvider
    public static Object[][] positiveGetProvider() {
        return new Object[][]{
                {"1", "surname", "name", "lastname", "1234-567890", "123456789012"},
                {null, "surname", "name", "lastname", "1234-567890", "123456789012"},
                {null, "surname", "name", "lastname", null, "123456789012"},
                {null, null, "name", "lastname", null, "123456789012"},
                {null, "surname", null, "lastname", null, "123456789012"},
                {null, "surname", "name", null, null, "123456789012"},
                {null, "surname", null, null, null, "123456789012"},
                {null, null, null, "lastname", null, "123456789012"},
                {null, null, "name", null, null, "123456789012"},
                {null, null, null, null, null, "123456789012"},

        };
    }

    @DataProvider
    public static Object[][] negativeGetProvider() {
        return new Object[][]{
                {"TEXT", null, null, null, null, "123456789012"},
                {null, null, null, null, "1234", "123456789012"},
                {null, null, null, null, "", "123456789012"},
                {null, null, null, null, null, "4353453534"},
                {null, null, null, null, null, ""},
                {null, null, null, null, null, null},
        };
    }

    @Test(dataProvider = "positiveGetProvider")
    public void getPersonTest(String id, String surname, String name, String lastname, String passport, String apiKey) {
        new WebServicePerson().getPersons(id, surname, name, lastname, passport, apiKey);
    }

    @Test(dataProvider = "negativeGetProvider", expectedExceptions = {
            Exception400.ClassCast.class,
            Exception401.EmptyApiKey.class,
            Exception401.NoApiKey.class,
            Exception401.InvalidApiKey.class,
            Exception400.InvalidPassportType.class
    })
    public void getPersonNegativeTest(String id, String surname, String name, String lastname, String passport, String apiKey) {
        new WebServicePerson().getPersons(id, surname, name, lastname, passport, apiKey);
    }

}
