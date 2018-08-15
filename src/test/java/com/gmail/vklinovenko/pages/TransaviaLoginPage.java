// https://www.transavia.com/en-EU/my-transavia/account/logon/
package com.gmail.vklinovenko.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransaviaLoginPage {
    private WebDriverWait wait;
    private final WebDriver driver;

    public TransaviaLoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 20);
    }

    @FindBy(id = "retrieveBookingByLastname_RecordLocator")
    private WebElement bookingNumberField;

    @FindBy(id = "retrieveBookingByLastname_LastName")
    private WebElement bookingLastNameField;

    @FindBy(id = "retrieveBookingByLastname_FlightDate-datepicker")
    private WebElement bookingDateField;

    @FindBy(xpath = "//button[text()='View booking']")
    private WebElement viewBookingButton;


    // Login page loaded
    public boolean loaded() {
        wait.until(ExpectedConditions.titleIs("Log in"));
        return true;
    }

    // Enter Booking number
    public void setBookingNumber(String bookingNumber) {
        wait.until(ExpectedConditions.elementToBeClickable(bookingNumberField)).click();
        bookingNumberField.clear();
        bookingNumberField.sendKeys(bookingNumber);
    }

    // Enter Last name
    public void setLastName(String lastName) {
        wait.until(ExpectedConditions.elementToBeClickable(bookingLastNameField)).click();
        bookingLastNameField.clear();
        bookingLastNameField.sendKeys(lastName);
    }

    // Enter Flight date
    public void setFlightDate(String flightDate) {
        wait.until(ExpectedConditions.elementToBeClickable(bookingDateField)).click();
        bookingDateField.clear();
        bookingDateField.sendKeys(flightDate);
    }

    // Click "View booking" button
    public void clickViewBookingButton() {
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", viewBookingButton);
    }
}
