// https://www.transavia.com/en-EU/home/
package com.gmail.vklinovenko.pages;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;

public class TransaviaHomePage {
    private WebDriverWait wait;
    private final WebDriver driver;

    public TransaviaHomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 20);
    }

    @FindBy(xpath = "//button[text()='Accept all cookies']")
    private WebElement acceptCookiesButton;

    @FindBy(id = "distilCaptchaForm")
    private WebElement captchaForm;

    @FindBy(xpath = "//li[@class='primary-navigation_item'][2]/a")
    private WebElement planandbookButton;

    @FindBy(id = "horizontal-sub-navigation-planandbook")
    private WebElement planandbookSubNav;

    @FindBy(linkText = "Advanced search")
    private WebElement advancedSearchButton;

    @FindBy(xpath = "//li[@class='primary-navigation_item'][3]/a")
    private WebElement manageYourBookingButton;

    @FindBy(id = "horizontal-sub-navigation-manageyourbooking")
    private WebElement bookingSubNav;

    @FindBy(linkText = "View your booking")
    private WebElement viewYourBookingButton;

    @FindBy(xpath = "//li[@class='primary-navigation_item'][4]/a")
    private WebElement serviceButton;

    @FindBy(id = "horizontal-sub-navigation-service")
    private WebElement serviceSubNav;

    @FindBy(linkText = "Handluggage")
    private WebElement handLuggageButton;

    @FindBy(linkText = "View all of our destinations")
    private WebElement destinationButton;

    @FindBy(xpath = "//h1[@class='h3']")
    private WebElement mainHeader;

    @FindBy(xpath = "//form[@id='desktop']")
    private WebElement searchPanel;

    @FindBy(id = "routeSelection_DepartureStation-input")
    private WebElement fromField;

    @FindBy(xpath = "//form[@id='desktop']/section/div[2]/div[1]/div[1]//ol")
    private WebElement fromAutocomplete;

    @FindBy(id = "routeSelection_ArrivalStation-input")
    private WebElement toField;

    @FindBy(xpath = "//form[@id='desktop']/section/div[2]/div[1]/div[2]//ol")
    private WebElement toAutocomplete;

    @FindBy(id = "dateSelection_OutboundDate-datepicker")
    private WebElement outDateField;

    @FindBy(xpath = "//span[@class='datepicker-trigger icon-font icon-calendar'][1]")
    private WebElement outDatepickerTrigger;

    @FindBy(xpath = "/html/body/div[6]/div[2]/div/div/div/div/span[1]")
    private WebElement outDatepickerMonth;

    @FindBy(xpath = "/html/body/div[6]/div[2]/div/div/div/div/span[2]")
    private WebElement outDatepickerYear;

    @FindBy(xpath = "/html/body/div[6]/div[2]/div/div/table/thead/tr")
    private WebElement outDatepickerCalendar;

    @FindBy(id = "dateSelection_IsReturnFlight-datepicker")
    private WebElement inDateField;

    @FindBy(id = "dateSelection_IsReturnFlight")
    private WebElement returnOnCheckbox;

    @FindBy(id = "booking-passengers-input")
    private WebElement passengersField;

    @FindBy(className = "passengers")
    private WebElement passengersPanel;

    @FindBy(xpath = "//div[@class='selectfield adults']//button[2]")
    private WebElement adultsIncreaseButton;

    @FindBy(xpath = "//div[@class='selectfield children']//button[2]")
    private WebElement childrenIncreaseButton;

    @FindBy(xpath = "//div[@class='selectfield babies']//button[2]")
    private WebElement babiesIncreaseButton;

    @FindBy(xpath = "//div[@class='selectfield adults']//input")
    private WebElement adultsNumField;

    @FindBy(xpath = "//div[@class='selectfield children']//input")
    private WebElement childrenNumField;

    @FindBy(xpath = "//div[@class='selectfield babies']//input")
    private WebElement babiesNumField;

    @FindBy(xpath = "//div[@id='buttonContainer']//button")
    private WebElement savePassengersButton;

    @FindBy(xpath = "//form[@id='desktop']//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(linkText = "Add multiple destinations")
    private WebElement multipleDestinationsLink;

    // Enable cookies when site asks it with a half-screen banner
    public void enableCookies() {
        wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesButton)).click();
    }

    // Making a pause to enter captcha manually.
    public void enterCaptcha() {
        wait.until(ExpectedConditions.visibilityOf(captchaForm));
        JOptionPane pane = new JOptionPane("Enter captcha manually\nThen click OK",
                JOptionPane.WARNING_MESSAGE);
        JDialog dialog = pane.createDialog("Robot hunter is here!");
        dialog.setLocation(captchaForm.getLocation().getX(), captchaForm.getLocation().getY());
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    // Get main header text
    public String getMainHeader() {
        return wait.until(ExpectedConditions.visibilityOf(mainHeader)).getText();
    }

    // Find search panel
    public boolean searchPanelAppears() {
        return searchPanel.isDisplayed();
    }

    // Check in autocomplete dropdown list
    private void checkInDropdownList(@NotNull WebElement autocompleteDropdownList, String text) {
        List<WebElement> autocompleteResults = autocompleteDropdownList.findElements(By.className("item"));
        for (WebElement element : autocompleteResults) {
            if (element.getText().contains(text)){
                element.click();
                return;
            }
        }
        autocompleteResults.get(0).click();
    }

    // Dropdown list "From" appears
    public boolean fromFieldAppears() {
        return wait.until(ExpectedConditions.elementToBeClickable(fromField)).isEnabled();
    }

    // Input into field "From"
    public void setFromField(String from) {
        wait.until(ExpectedConditions.elementToBeClickable(fromField));
        fromField.click();
        fromField.clear();
        fromField.sendKeys(from);
        checkInDropdownList(fromAutocomplete, from);
    }

    // Dropdown list "To" appears
    public boolean toFieldAppears() {
        return toField.isEnabled();
    }

    // Input into field "To"
    public void setToField(String to) {
        wait.until(ExpectedConditions.elementToBeClickable(toField));
        toField.click();
        toField.clear();
        toField.sendKeys(to);
        checkInDropdownList(toAutocomplete, to);
    }

    // Outbound flight date field appears
    public boolean outDateFieldAppears() {
        return outDateField.isEnabled();
    }

    // Input into field "Outbound flight date"
    public void setOutDateField(String date) {
        wait.until(ExpectedConditions.elementToBeClickable(outDateField));
        outDateField.click();
        outDateField.clear();
        outDateField.sendKeys(date);
    }

    // Input into field "Outbound flight date"
    public void setInDateField(String date) {
        wait.until(ExpectedConditions.elementToBeClickable(inDateField));
        inDateField.click();
        inDateField.clear();
        inDateField.sendKeys(date);
    }

    // Uncheck "Return on" checkbox
    public void uncheckReturnOnCheckbox() {
        if (!inDateField.getAttribute("placeholder").equals("Single flight"))
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", returnOnCheckbox);
    }

    // Check "Return on" checkbox
    public void checkReturnOnCheckbox() {
        if (inDateField.getAttribute("placeholder").equals("Single flight"))
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", returnOnCheckbox);
    }

    // Field "Who will be traveling?" appears
    public boolean passengersFieldAppears() {
        clickPassengersField();
        if(wait.until(ExpectedConditions.visibilityOf(passengersPanel)).isDisplayed()) {
            clickSavePassengersButton();
            return true;
        }
        else return false;
    }

    // Click field "Who will be traveling?"
    public void clickPassengersField() {
        wait.until(ExpectedConditions.elementToBeClickable(passengersField)).click();
    }

    // click "+" button for adults in passengers dropdown list
    public void clickAdultsIncreaseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(adultsIncreaseButton)).click();
    }

    // click "+" button for children in passengers dropdown list
    public void clickChildrenIncreaseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(childrenIncreaseButton)).click();
    }

    // click "+" button for babies in passengers dropdown list
    public void clickBabiesIncreaseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(babiesIncreaseButton)).click();
    }

     // Get number of Adults
    public int getAdultsNum() {
        return Integer.parseInt(adultsNumField.getAttribute("value"));
    }

    // Get number of Children
    public int getChildrenNum() {
        return Integer.parseInt(childrenNumField.getAttribute("value"));
    }

    // Get number of Babies
    public int getBabiesNum() {
        return Integer.parseInt(babiesNumField.getAttribute("value"));
    }

    // click Save button in passengers dropdown list
    public void clickSavePassengersButton() {
        wait.until(ExpectedConditions.visibilityOf(savePassengersButton));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", savePassengersButton);
    }

    // click button "Search"
    public void clickSearchButton() {
        searchButton.click();
    }

    // click "Add multiple destinations"
    public void clickAddMultipleDestinations() {
        wait.until(ExpectedConditions.elementToBeClickable(multipleDestinationsLink)).click();
    }

    // click "Plan and book"
    public void clickPlanandbookButton() {
        try {Thread.sleep(1000);} catch (InterruptedException e){}
        wait.until(ExpectedConditions.elementToBeClickable(planandbookButton)).click();
    }

    // Horizontal planandbook sub-navigation panel appears
    public boolean planandbookSubNavAppears() {
        return wait.until(ExpectedConditions.visibilityOf(planandbookSubNav)).isDisplayed();
    }

    // click "Advanced search"
    public void clickAdvancedSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(advancedSearchButton)).click();
    }

    // click "Manage your booking"
    public void clickManageYourBookingButton() {
        try {Thread.sleep(1000);} catch (InterruptedException e){}
        wait.until(ExpectedConditions.elementToBeClickable(manageYourBookingButton)).click();
    }

    // Horizontal booking sub-navigation panel appears
    public boolean bookingSubNavAppears() {
        return wait.until(ExpectedConditions.visibilityOf(bookingSubNav)).isDisplayed();
    }

    // click "View your booking"
    public void clickViewYourBookingButton() {
        wait.until(ExpectedConditions.elementToBeClickable(viewYourBookingButton)).click();
    }

    // click "Service"
    public void clickServiceButton() {
        try {Thread.sleep(1000);} catch (InterruptedException e){}
        wait.until(ExpectedConditions.elementToBeClickable(serviceButton)).click();
    }

    // Horizontal service sub-navigation panel appears
    public boolean serviceSubNavAppears() {
        return wait.until(ExpectedConditions.visibilityOf(serviceSubNav)).isDisplayed();
    }

    // click "Hand luggage"
    public void clickHandLuggageButton() {
        wait.until(ExpectedConditions.elementToBeClickable(handLuggageButton)).click();
    }

    // click button "Destination"
    public void clickDestinationButton() {
        wait.until(ExpectedConditions.elementToBeClickable(destinationButton)).click();
    }
}