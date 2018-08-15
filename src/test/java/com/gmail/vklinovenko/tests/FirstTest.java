package com.gmail.vklinovenko.tests;

import com.gmail.vklinovenko.pages.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

public class FirstTest {

    private final String BASE_URL = "https://www.transavia.com/en-EU/home/";

    private FirefoxOptions options = new FirefoxOptions()
            .setProfile(new FirefoxProfile())
            .addPreference("browser.startup.homepage", "about:blank");

    private static WebDriver driver;

    // Page Objects
    private static TransaviaAdvancedSearchPage advancedSearchPage;
    private static TransaviaChooseAFarePage chooseAFarePage;
    private static TransaviaDestinationsPage destinationsPage;
    private static TransaviaHandLuggagePage handLuggagePage;
    private static TransaviaHomePage homePage;
    private static TransaviaFlightsSearchPage flightsSearchPage;
    private static TransaviaLoginPage loginPage;
    private static YoutubeVideoPage youtubeVideoPage;

    @BeforeClass
    public void beforeClass() {

        // Драйвер находится по пути, прописанном в $PATH, если нет - раскомментировать нужную строку и прописать путь:
        //System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver");                              // Linux
        //System.setProperty("webdriver.gecko.driver", "C://Program Files/geckodriver.exe");                 // Windows

        // Запуск драйвера и подключение Page Object
        driver = new FirefoxDriver(options);
        advancedSearchPage = new TransaviaAdvancedSearchPage(driver);
        chooseAFarePage = new TransaviaChooseAFarePage(driver);
        destinationsPage = new TransaviaDestinationsPage(driver);
        flightsSearchPage = new TransaviaFlightsSearchPage(driver);
        handLuggagePage = new TransaviaHandLuggagePage(driver);
        homePage = new TransaviaHomePage(driver);
        loginPage = new TransaviaLoginPage(driver);
        youtubeVideoPage = new YoutubeVideoPage(driver);

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        driver.get(BASE_URL);
        //driver.manage().deleteAllCookies();

        // Когда сайт рисует баннер на полэкрана с требованием разрешить куки - разрешаем.
        homePage.enableCookies();

        // Когда сайт требует ввода капчи, приостанавливаем тест и вводим её вручную.
        if (driver.getTitle().equals("Sorry to interrupt...")) homePage.enterCaptcha();
    }

    @BeforeMethod
    public void beforeMethod() {
        // Load homepage "https://www.transavia.com/en-EU/home" as a first step of each test
        driver.get(BASE_URL);
    }

    @AfterClass
    public void afterClass() {

        driver.manage().deleteAllCookies();
        driver.quit();
    }


    
    @Test(testName = "1. Заполнение поля \"Where do you want to go?\" для выбора одиночного перелета на одну персону " +
            "в одну сторону")

    /*
        1  Появляется окно выбора маршрута "Where do you want to go?"
        2  Появляется выпадающий список с названиями доступных мест для перелета в поле  "From"
        3  Появляется выпадающий список с названием доступных мест в поле  "To"
        4  Появляется поле с календарным списком дней недели, месяцами и годом
        5  Перевести checkbox "Return on"в состояние unchecked
        6  Появляется дополнительное поле для выбора количества пассажиров в поле "Who will be travelling?"
        7  Click button «Search»
        8  Найден хотя бы один рейс в период от 1 до 7 дней
    */

    public void testOneWayTicket() {

        final String MAIN_HEADER = "Where do you want to go?";
        final String FROM_AIRPORT = "Reykjavik";
        final String TO_AIRPORT = "Paris";
        final String FLIGHT_DATE = "1 Sep 2018";
        //final String FAIL_FLIGHT_DATE = "8 Sep 2018"; // fail: no flights from 5th to 11th of September

        // 1 "Where do you want to go?" form appears
        Assert.assertTrue((homePage.getMainHeader().equals(MAIN_HEADER) && homePage.searchPanelAppears()));

        // 2 Available airports dropdown list appears in a field "From"
        Assert.assertTrue(homePage.fromFieldAppears());
        homePage.setFromField(FROM_AIRPORT);

        // 3 Available airports dropdown list appears in a field "To"
        Assert.assertTrue(homePage.toFieldAppears());
        homePage.setToField(TO_AIRPORT);

        // 4 Date field appears
        homePage.setOutDateField(FLIGHT_DATE);

        // 5 Uncheck "Return on" checkbox
        homePage.uncheckReturnOnCheckbox();

        // 6 Passengers popup panel appears after the click on "Who will be travelling?" field
        Assert.assertTrue(homePage.passengersFieldAppears());

        // 7 Click button "Search"
        homePage.clickSearchButton();

        // 8 At least one flight in a period from 1 to 7 days found
        Assert.assertTrue(flightsSearchPage.outboundFlightFound());
    }



    @Test(testName = "2. Проверка total суммы выбранных билетов. Двое взрослых, один ребенок (2-11 age)" +
            " и один младенец (0-2 age) из Амстердама в Париж в обе стороны. Выбрать билет с Hold Luggage 20kg.",
            priority = 1)

    /*
        1    Click field "From"
        1.1  Input "Amsterdam"
        1.2  Check  "Amsterdam" in dropdown list
        2    Click field "To"
        2.1  Input "Paris"
        2.2  Check "Paris" in dropdown list
        2.3.1 Check "Return on" checkbox if need
        2.3.2 Set depart and return dates
        3    Click field " Who will be travelling?"
        3.1  Click  "+" for "Adults" in dropdown list
        3.2  Click  "+" for "Children" in dropdown list
        3.3  click  "+" for "Babies" in dropdown list
        4    Click button "Search"
        5    Find title "Outbound flight"
        5.1  Click first button "select"
        6    Find title "Inbound flight"
        6.1  Click first button "select"
        7    Click button "next"
        8    Find title "Plus"
        8.1  Click button " Select"
        9    Get total price
    */

    public void testTotalSum() {

        final String FROM_AIRPORT = "Amsterdam";
        final String TO_AIRPORT = "Paris";

        final String DEPART_DATE = "1 Sep 2018";
        final String RETURN_DATE = "8 Sep 2018";

        // 1 click field "From", input "Amsterdam" and check "Amsterdam" in dropdown list
        homePage.setFromField(FROM_AIRPORT);

        // 2 click field "To", input "Paris" and check "Paris" in dropdown list
        homePage.setToField(TO_AIRPORT);

        // 2.3.1 Check "Return on" checkbox if need (Test1 unchecks it and cause Test2 crash in test suite)
        homePage.checkReturnOnCheckbox();

        // 2.3.2 Set depart and return dates (after p.2.1.1 dates are same,
        //       that may cause return flight be earlier than depart and lead to the test's crash)
        homePage.setOutDateField(DEPART_DATE);
        homePage.setInDateField(RETURN_DATE);

        // 3 click field " Who will be travelling?"
        homePage.clickPassengersField();

        // 3.1  click  "+" for "Adults" in dropdown list
        homePage.clickAdultsIncreaseButton();

        // 3.2  click  "+" for "Children" in dropdown list
        homePage.clickChildrenIncreaseButton();

        // 3.3  click  "+" for "Babies" in dropdown list
        homePage.clickBabiesIncreaseButton();

        // 4 click button "Search"
        int adults = homePage.getAdultsNum();         // Собираем данные о числе и типах пассажиров
        int children = homePage.getChildrenNum();     // для расчёта стоимости билетов
        int babies = homePage.getBabiesNum();
        homePage.clickSavePassengersButton();         // Список пассажиров может закрывать кнопку Search
        homePage.clickSearchButton();

        // 5 find title "Outbound flight"
        Assert.assertEquals(flightsSearchPage.getOutboundFlightTitle(),"Outbound flight");

        // 5.1 click first button "select"
        flightsSearchPage.clickFirstOutboundFlight();
        flightsSearchPage.submitOutboundFlight();

        // 6 find title "Inbound flight"
        Assert.assertEquals(flightsSearchPage.getInboundFlightTitle(),"Inbound flight");

        // 6.1 click first button "select"
        flightsSearchPage.clickFirstInboundFlight();
        flightsSearchPage.submitInboundFlight();

        // 7 click button "Next"
        flightsSearchPage.clickNextButton();

        // 8 find title "Plus"
        Assert.assertEquals(chooseAFarePage.getPlusTitle(), "Plus");

        // 8.1 click button "Select"
        chooseAFarePage.clickSelectPlusButton();

        // 9 get total price
        float total = adults * chooseAFarePage.getPersonPrice() + children * chooseAFarePage.getChildrenPrice()
                + babies * chooseAFarePage.getBabyPrice() + (adults + children) * chooseAFarePage.getExtraPriceB();
        Assert.assertEquals(chooseAFarePage.getTotalPrice(), total);
    }


    @Test(testName = "3. Зайти в кабинет и проверить ожидаемое и фактическое время приземления самолета",
            priority = 1)

    /*
        1  open transavia.com
        2  click Manage your booking (additional menu drops down)
        3  click View your booking (login page loads)
        4  Enter booking no. "MF8C9R"; last name "kukharau", flight date "9 June 2016" (booking page loads)
        5  Get arrival time and arrived time
     */

    // Данные для входа неверны. Выполнение п.5 без достоверных данных невозможно.

    public void testArrivedFactTime() {

        final String BOOKING_NUMBER = "MF8C9R";
        final String LAST_NAME = "kukharau";
        final String FLIGHT_DATE = "9 June 2016";

        // 1 open "https://www.transavia.com/en-EU/home" - in @BeforeMethod

        // 2 click Manage your booking (additional menu drops down)
        homePage.clickManageYourBookingButton();
        Assert.assertTrue(homePage.bookingSubNavAppears());

        // 3 click View your booking (login page loads)
        homePage.clickViewYourBookingButton();
        Assert.assertTrue(loginPage.loaded());

        // 4 Enter BOOKING_NUMBER, LAST_NAME, FLIGHT_DATE (booking page loads)
        loginPage.setBookingNumber(BOOKING_NUMBER);
        loginPage.setLastName(LAST_NAME);
        loginPage.setFlightDate(FLIGHT_DATE);
        loginPage.clickViewBookingButton();


        // 5 Get arrival time and arrived time
        // TODO Без реальных входных данных выполнение этого пункта невозможно! Данные из задания не подходят.
        Assert.fail();
    }



    @Test(testName = "4. Проверить равенство стоимости билета и стоимости оплаты по брони",
            priority = 1)

    /*
        1  repeat steps 1-4 from case 3
        2  click Booking details (new page loads)
        3  find Price breakdown page section
        4  Get Total sum
        5  Get Payment amount
        6  Compare 4 and 5
     */

    // Данные для входа неверны. Выполнение п.2 - п.6 без достоверных данных невозможно.

    public void testPaymentAmount() {

        final String BOOKING_NUMBER = "MF8C9R";
        final String LAST_NAME = "kukharau";
        final String FLIGHT_DATE = "9 June 2016";

        // 1 repeat steps 1-4 from case 3

        // 1.1 open "https://www.transavia.com/en-EU/home" - in @BeforeMethod

        // 1.2 click Manage your booking (additional menu drops down)
        homePage.clickManageYourBookingButton();
        Assert.assertTrue(homePage.bookingSubNavAppears());

        // 1.3 click View your booking (login page loads)
        homePage.clickViewYourBookingButton();
        Assert.assertTrue(loginPage.loaded());

        // 1.4 Enter BOOKING_NUMBER, LAST_NAME, FLIGHT_DATE (booking page loads)
        loginPage.setBookingNumber(BOOKING_NUMBER);
        loginPage.setLastName(LAST_NAME);
        loginPage.setFlightDate(FLIGHT_DATE);
        loginPage.clickViewBookingButton();

        // TODO Без реальных входных данных дальнейшее выполнение теста невозможно! Данные из задания не подходят.

        // 2 click Booking details (new page loads)

        // 3 find Price breakdown page section

        // 4 Get Total sum

        // 5 Get Payment amount

        // 6 Compare Total sum and Payment amount

        Assert.fail();
    }



    @Test(testName = "5. Проверить валидность ссылки на видео с инструкцией Transavia по ручному багажу",
            priority = 2)

    /*
        1  open transavia.com
        2  click service (additional menu drops down)
        3  click hand luggage (page Hand Luggage loads)
        4  scroll down to the page section Video: hand lugage and Transavia
        5  Get video link
        6  Open video link
        7  Check video name and video author on the video page
     */

    public void testLuggageVideo() {

        final String VIDEO_URL = "https://youtu.be/fQMuhniqWAg";
        final String VIDEO_NAME = "Luggage";
        final String VIDEO_AUTHOR = "Transavia";

        // 1 open "https://www.transavia.com/en-EU/home" - in @BeforeMethod

        // 2 click "Service" (additional menu drops down)
        homePage.clickServiceButton();
        Assert.assertTrue(homePage.serviceSubNavAppears());

        // 3 click "Hand luggage" (page "Hand Luggage" loads)
        homePage.clickHandLuggageButton();
        Assert.assertTrue(handLuggagePage.loaded());

        // 4 scroll down to the page section "Video: hand luggage and Transavia"
        handLuggagePage.scrollToVideo();

        // 5 Get video link
        String videoLink = handLuggagePage.getVideoLink();
        Assert.assertEquals(videoLink, VIDEO_URL);

        // 6 Open video link
        driver.get(videoLink);

        // 7 Check video name and video author on the video page
        Assert.assertEquals(youtubeVideoPage.getVideoName(), VIDEO_NAME);
        Assert.assertEquals(youtubeVideoPage.getVideoAuthor(), VIDEO_AUTHOR);
    }


    @Test(testName = "6. Поиск билета в одну сторону с бюджетом < 200 euro (аэропорт вылета: Innsbruck, Austria)",
            priority = 2)

    /*
        1    open transavia.com
        2    click button "destination" (new page load)
        3    click button "find the perfect destination"(new page load)
        4    click field "From"
        4.1  input " Innsbruck, Austria" into field
        5    click button "What is your budget per person? "
        5.1  input "200" into field "My budget"
        6    click button "Search"
        7    get any destinations
     */

    //    !!! Рейсы из Инсбрука не находятся. Для теста используем аэропорт Барселоны

    public void testPerfectDestination() {

        final String FROM_AIRPORT = "Barcelona, Spain";
        final Integer MY_BUDGET = 200;

        // 1 open "https://www.transavia.com/en-EU/home" - in @BeforeMethod

        // 2 click button "Destination" (new page load)
        homePage.clickDestinationButton();
        Assert.assertTrue(destinationsPage.loaded());

        // 3 click button "Find the perfect destination"(new page load)
        destinationsPage.clickPerfectDestinationButton();
        Assert.assertTrue(advancedSearchPage.loaded());

        // 4 click field "From"
        advancedSearchPage.clickFromField();

        // 4.1 input "Innsbruck, Austria" into field
        advancedSearchPage.setFromField(FROM_AIRPORT);

        // 5 click button "What is your budget per person?"
        advancedSearchPage.clickBudgetButton();

        // 5.1 input "200" into field "My budget"
        advancedSearchPage.setBudgetField(MY_BUDGET);

        // 6 click button "Search"
        advancedSearchPage.clickSearchButton();

        // 7 get any destinations
        Assert.assertTrue(advancedSearchPage.flightsFound());
    }


    @Test(testName = "7. Найти самый дешевый билет в одну сторону из Нидерландов во Францию в определенном месяце " +
            "текущего года (Сентябоь 2018 года), получить город прилета и стоимость билета",
            priority = 2)

    /*
        1  open transavia.com
        2  click Plan and book (additional menu drops down)
        3  click Advanced search (Advanced search page loads)
        4  type "Netherlands" into field From
        5  type "France" into field To
        6  click When will you be taking off
        7  click Round trip dropdown menu, choose single flight
        8  click Specific month button
        9  click Specific month dropdown menu, choose August 2017
        10 Click Day of the week dropdown menu, choose Any day of the week
        11 click Search
        12 get Price and City name from the first row of the table We found the following destinations for you
     */

    public void testLowCostTicket() {

        final String FROM_COUNTRY = "Netherlands";
        final String TO_COUNTRY = "France";
        final String FLIGHT_MONTH = "September 2018";
        final String WEEK_DAY = "Any";
        final String CHEAPEST_CITY = "Nice, France";
        final float  CHEAPEST_PRICE = 25;

        // 1 open "https://www.transavia.com/en-EU/home" - in @BeforeMethod

        // 2 click "Plan and book" (additional menu drops down)
        homePage.clickPlanandbookButton();
        Assert.assertTrue(homePage.planandbookSubNavAppears());

        // 3 click "Advanced search" (Advanced search page loads)
        homePage.clickAdvancedSearchButton();
        Assert.assertTrue(advancedSearchPage.loaded());

        // 4 type "Netherlands" into field "From"
        advancedSearchPage.clickFromField();
        advancedSearchPage.setFromField(FROM_COUNTRY);

        // 5 type "France" into field "To"
        advancedSearchPage.clickToField();
        advancedSearchPage.setToField(TO_COUNTRY);

        // 6 click "When will you be taking off"
        advancedSearchPage.clickTakeoffButton();

        // 7 click "Round trip" dropdown menu, choose "Single flight"
        advancedSearchPage.selectSingleFlight();

        // 8 click "Specific month" radio button
        advancedSearchPage.clickSpecificMonthRadioButton();

        // 9 click "Specific month" dropdown menu, choose "September 2018"
        advancedSearchPage.selectSpecificMonth(FLIGHT_MONTH);

        // 10 Click "Day of the week" dropdown menu, choose "Any day of the week"
        advancedSearchPage.selectWeekDay(WEEK_DAY);

        // 11 click "Search"
        advancedSearchPage.clickSearchButton();

        // 12 get "Price" and "City name" from the first row of the table "We found the following destinations for you"
        Assert.assertTrue(advancedSearchPage.flightsFound());
        Assert.assertEquals(advancedSearchPage.getCheapestCity(), CHEAPEST_CITY);
        Assert.assertEquals(advancedSearchPage.getCheapestPrice(), CHEAPEST_PRICE);   // Free flights are not available!
    }


    @Test(testName = "8. Find flights from Dubai to Agadir,Morocco",
            description = "Verify that error message \"Unfortunately we do not fly from... ...try again.\" is shown",
            priority = 1)

    /*
        1    open transavia.com
        2    click field "From"
        2.1  input "Dubai" into field
        3    click field "To"
        3.1  input "Agadir, Morocco" into field
        4    click button "Search"
        5    get error message
     */

    public void testNonexistentRoute() {

        final String FROM_AIRPORT = "Dubai";
        final String TO_AIRPORT = "Agadir, Morocco";
        final String NONEXISTENT_ROUTE_ERR_MSG = "Unfortunately we do not fly from Dubai, United Arab Emirates " +
                "to Agadir, Morocco. However, we do fly from Dubai, United Arab Emirates to other destinations. " +
                "Please change your destination and try again.";

        // 1 Open "https://www.transavia.com/en-EU/home" - in @BeforeMethod

        // 2 Click field "From" and input "Dubai" into field
        homePage.setFromField(FROM_AIRPORT);

        // 3 Click field "To" and input " Agadir, Morocco" into field
        homePage.setToField(TO_AIRPORT);

        // 4 Click button "Search"
        homePage.clickSearchButton();

        // 5 Get error message
        Assert.assertEquals(flightsSearchPage.getNonexistentRouteMessage(), NONEXISTENT_ROUTE_ERR_MSG);
    }


    @Test(testName = "9. Verify complicated route cost: Bologna-Eindhoven (1st date) " +
            "and Amsterdam-Casablanca (2nd date)",
            priority = 1)

    /*
        1    open transavia.com
        2    click Add multiple destinations
        3    Find Outbound flight page section
        3.1  type "Bologna, Italy" into field "From"
        3.2  type "Eindhoven, Netherlands" into field "To"
        3.3  type 1st date  into field Date (e,g, 22 Aug 2018)
        4    Find Inboud flight page section
        4.1  type "Amsterdam (Schiphol), Netherlands" into field "From"
        4.2  type "Casablanca, Morocco" into field "To"
        4.3  type 2nd date into field Date (e,g, 28 Aug 2018)
        5    click Search
        6    click select button next to 15:40 outbound flight
        7    click select button next to 10:20 inbound flight
        8    get Total amount
     */

    public void testMultipleDestinations() {

        final String FROM_OUT_AIRPORT = "Bologna, Italy";
        final String TO_OUT_AIRPORT = "Eindhoven, Netherlands";
        final String DATE_OUT = "25 Aug 2018";
        final String FROM_IN_AIRPORT = "Amsterdam (Schiphol), Netherlands";
        final String TO_IN_AIRPORT = "Casablanca, Morocco";
        final String DATE_IN = "1 Sep 2018";

        // 1 Open "https://www.transavia.com/en-EU/home" - in @BeforeMethod

        // 2 Click "Add multiple destinations"
        homePage.clickAddMultipleDestinations();

        // 3 Find Outbound flight page section
        Assert.assertEquals(flightsSearchPage.getOutboundFlightSection()," Outbound flight");

        // 3.1 Type "Bologna, Italy" into field "From"
        flightsSearchPage.setFromOutField(FROM_OUT_AIRPORT);

        // 3.2 Type "Eindhoven, Netherlands" into field "To"
        flightsSearchPage.setToOutField(TO_OUT_AIRPORT);

        // 3.3 Type 1st date  into field Date (e,g, 2 May 2017)
        flightsSearchPage.setDateOutField(DATE_OUT);

        // 4 Find Inboud flight page section
        Assert.assertEquals(flightsSearchPage.getInboundFlightSection()," Inbound flight");

        // 4.1 Type "Amsterdam (Schiphol), Netherlands" into field "From"
        flightsSearchPage.setFromInField(FROM_IN_AIRPORT);

        // 4.2 Type "Casablanca, Morocco" into field "To"
        flightsSearchPage.setToInField(TO_IN_AIRPORT);

        // 4.3 Type 2nd date into field Date (e,g, 8 May 2017)
        flightsSearchPage.setDateInField(DATE_IN);

        // 5 Click Search
        flightsSearchPage.clickSearchButton();

        // 6 Click select button next to 15:40 outbound flight
        //   !!!!   Selecting first flight in list TODO сделать поиск по времени
        flightsSearchPage.clickFirstOutboundFlight();
        flightsSearchPage.submitOutboundFlight();

        // 7 Click select button next to 10:20 inbound flight
        //   !!!!   Selecting first flight in list TODO сделать поиск по времени
        flightsSearchPage.clickFirstInboundFlight();
        flightsSearchPage.submitInboundFlight();

        // 8 Get Total amount
        Assert.assertTrue(flightsSearchPage.getTotalPrice() > 0);
    }
}
