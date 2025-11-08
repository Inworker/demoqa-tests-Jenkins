package tests.testJenkinsUI;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import tests.data.TestData;
import org.junit.jupiter.api.Test;
import tests.pages.TextBoxPage;
import tests.pages.components.FinalTableComponent;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static java.lang.String.format;

public class TextBoxWithFakerTests{

    TextBoxPage textBoxPage = new TextBoxPage();
    FinalTableComponent finalTableComponent = new FinalTableComponent();
    TestData testData = new TestData();

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1920x1080";
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.pageLoadStrategy = "eager";
        //Configuration.holdBrowserOpen = false;
        Configuration.timeout = 5000; // default 4000
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";
    }

    @Test
    void fillFormTest() {
        open("/text-box");
        $("#userName").setValue("Alex");
        $("#userEmail").setValue("alex@egorov.com");
        $("#currentAddress").setValue("Some street 1");
        $("#permanentAddress").setValue("Another street 1");
        $("#submit").click();

        $("#output #name").shouldHave(text("Alex"));
        $("#output #email").shouldHave(text("alex@egorov.com"));
        $("#output #currentAddress").shouldHave(text("Some street 1"));
        $("#output #permanentAddress").shouldHave(text("Another street 1"));
    }

    @Tag("demoqa")
    @Test
    void fillSecondFormTest() {
        step("Fill form", () ->{
        textBoxPage.openPage()
            .setFullName(testData.firstName, testData.lastName)
            .setEmail(testData.email)
            .setGender(testData.gender)
            .setPhone(testData.phone)
            .setDate(testData.year, testData.month, testData.day)
            .setSubject(testData.subjects)
            .setHobby(testData.hobbies)
            .uploadPicture(testData.pathImage)
            .setAddress(testData.address)
            .setStateAndCity(testData.state, testData.city)
            .submit();});
        step("Check final Table Component", () ->{

            finalTableComponent
                .checkTableData("Student Name", testData.firstName + " " + testData.lastName)
                .checkTableData("Student Email",testData.email)
                .checkTableData("Gender", testData.gender)
                .checkTableData("Mobile", testData.phone)
                .checkTableData("Date of Birth",testData.day + " " + testData.month + "," + testData.year)
                .checkTableData("Subjects", testData.subjects)
                .checkTableData("Hobbies", testData.hobbies)
                .checkTableData("Picture",testData.pathImage)
                .checkTableData("Address", testData.address)
                .checkTableData("State and City", testData.state + " " + testData.city);
    });}

    @Test
    void fillRequiredFields() {

        textBoxPage.openPage()
                .setFullName(testData.firstName, testData.lastName)
                .setGender(testData.gender)
                .setPhone(testData.phone)
                .setDate(testData.year, testData.month, testData.day)
                .setHobby(testData.hobbies)
                .submit();
        finalTableComponent.checkTitle()
                .checkTableData("Student Name", testData.firstName + " " + testData.lastName)
                .checkTableData("Mobile", testData.phone)
                .checkTableData("Date of Birth",testData.day + " " + testData.month + "," + testData.year);
    }
    @Test
    void fillNonRequiredFields() {

        textBoxPage.openPage()
                .setEmail(testData.email)
                .setDate(testData.year, testData.month, testData.day)
                .setSubject(testData.subjects)
                .setHobby(testData.hobbies)
                .uploadPicture(testData.pathImage)
                .setAddress(testData.address)
                .setStateAndCity(testData.state, testData.city)
                .submit();
        finalTableComponent.checkNotVisibleTable();
    }
}
