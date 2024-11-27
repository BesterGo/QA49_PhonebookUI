package com.phonebook.tests;
import com.phonebook.data.ContactData;
import com.phonebook.models.Contact;
import com.phonebook.models.User;
import com.phonebook.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddContactTests extends TestBase {

    @BeforeMethod
    public void precondition() {
        if (app.getUser().isLoginLinkPresent()) {
            app.getUser().clickOnSignOutButton();
        }
        app.getUser().clickOnLoginLink();
        app.getUser().fillRegisterLoginForm(new User()
                .setEmail(ContactData.EMAIL)
                .setPassword("..."));
        app.getUser().clickOnLoginButton();
    }

    @Test
    public void addContactPositiveTest() {
        app.getContact().fillContactForm(new Contact()
                .setName(ContactData.NAME)
                .setLastName(ContactData.LAST_NAME)
                .setPhone(ContactData.PHONE)
                .setEmail(ContactData.EMAIL)
                .setAddress(ContactData.ADDRESS)
                .setDescription(ContactData.DESC));
        app.getContact().clickOnSaveButton();
        Assert.assertTrue(app.getContact().isContactAdded(ContactData.NAME));
    }

    public void extracted() {
        app.getContact().clickOnAddLink();
    }

    @DataProvider
    public Iterator<Object[]> addNewContact() {
        List<Object[]> list=new ArrayList<>();
        list.add(new Object[]{"Olivero", "Kant", "12345678901", "kant@gm.com", "Berlinare", "goalkeeperino"});
        list.add(new Object[]{"Olivero", "Kant", "12345678912", "kant@gm.com", "Berlinare", "goalkeeperino"});
        list.add(new Object[]{"Olivero", "Kant", "1234567890123", "kant@gm.com", "Berlinare", "goalkeeperino"});
        return list.iterator();
    }

    @Test(dataProvider = "addNewContact")
    public void addContactPositiveFromDataProviderTest
            (String name, String lastName, String phone,
             String email, String address, String desc) {
        app.getContact().clickOnAddLink();
        app.getContact().fillContactForm(new Contact()
                .setName(name)
                .setLastName(lastName)
                .setPhone(phone)
                .setEmail(email)
                .setAddress(address)
                .setDescription(desc));
        app.getContact().clickOnSaveButton();
        Assert.assertTrue(app.getContact().isContactAdded(name));
    }


    @DataProvider
    public Iterator<Object[]> addNewContactWitchCsv() throws IOException {
        List<Object[]> list = new ArrayList<>();
        BufferedReader reader=new BufferedReader(new FileReader
                (new File("src/test/resources/contact.csv")));

        String line = reader.readLine();

        while (line != null) {
            String[] split=line.split(",");

            list.add(new Object[]{new Contact().setName(split[0])
                    .setLastName(split[1]).setPhone(split[2]).setEmail(split[3])
                    .setAddress(split[4]).setDescription(split[5])});
            line = reader.readLine();
        }
        return list.iterator();
    }

    @Test(dataProvider = "addNewContactWithCsv",dataProviderClass = DataProviders.class)
    public void addContactPositiveFromDataProviderWithCsvFileTest(Contact contact) {
        app.getContact().clickOnAddLink();
        app.getContact().fillContactForm(contact);
        app.getContact().clickOnSaveButton();
        Assert.assertTrue(app.getContact().isContactAdded(contact.getName()));
    }

    @AfterMethod
    public void postCondition() {
        app.getContact().deleteContact();
    }
}