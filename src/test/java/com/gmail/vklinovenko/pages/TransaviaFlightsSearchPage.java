// https://www.transavia.com/en-EU/book-a-flight/flights/search/
package com.gmail.vklinovenko.pages;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TransaviaFlightsSearchPage {
    private WebDriverWait wait;
    private final WebDriver driver;

    public TransaviaFlightsSearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 30);
    }

    @FindBy(xpath = "//span[@class='price-prefix']")
    private List<WebElement> availableFlights;

    @FindBy(xpath = "//section[@class='flight outbound']//h2[@class='h4']")
    private WebElement outFlightTitle;

    @FindBy(xpath = "//form[@id='flights']//h2[1]")
    private WebElement outFlightSection;

    @FindBy(xpath = "//section[@class='flight outbound']//div[@class='day day-with-availability'][1]")
    private WebElement firstOutFlightButton;

    @FindBy(xpath = "//section[@class='flight outbound']//button[@class='flight-result-button']")
    private WebElement selectOutButton;

    @FindBy(xpath = "//section[@class='flight outbound']//span[@class='status-selected']")
    private WebElement outFlightSelected;

    @FindBy(xpath = "//section[@class='flight inbound']//h2[@class='h4']")
    private WebElement inFlightTitle;

    @FindBy(xpath = "//form[@id='flights']//h2[2]")
    private WebElement inFlightSection;

    @FindBy(xpath = "//section[@class='flight inbound']//div[@class='day day-with-availability'][1]")
    private WebElement firstInFlightButton;

    @FindBy(xpath = "//section[@class='flight inbound']//button[@class='flight-result-button']")
    private WebElement selectInButton;

    @FindBy(xpath = "//section[@class='flight inbound']//span[@class='status-selected']")
    private WebElement inFlightSelected;

    @FindBy(name = "next_button")
    private WebElement nextButton;

    @FindBy(css = ".notification-error > p")
    private WebElement nonexistentRouteMessage;

    @FindBy(id = "openJawRouteSelection_DepartureStationOutbound-input")
    private WebElement fromOutField;

    @FindBy(xpath = "//form[@id='flights']//section/div[2]/div[1]/div[1]/div/div/div[1]//div[@class='autocomplete-results']")
    private WebElement fromOutAutocomplete;

    @FindBy(id = "openJawRouteSelection_ArrivalStationOutbound-input")
    private WebElement toOutField;

    @FindBy(xpath = "//form[@id='flights']//section/div[2]/div[1]/div[1]/div/div/div[2]//div[@class='autocomplete-results']")
    private WebElement toOutAutocomplete;

    @FindBy(id = "dateSelection_OutboundDate-datepicker")
    private WebElement dateOutField;

    @FindBy(id = "openJawRouteSelection_DepartureStationInbound-input")
    private WebElement fromInField;

    @FindBy(xpath = "//form[@id='flights']//section/div[2]/div[1]/div[2]/div/div/div[1]//div[@class='autocomplete-results']")
    private WebElement fromInAutocomplete;

    @FindBy(id = "openJawRouteSelection_ArrivalStationInbound-input")
    private WebElement toInField;

    @FindBy(xpath = "//form[@id='flights']//section/div[2]/div[1]/div[2]/div/div/div[2]//div[@class='autocomplete-results']")
    private WebElement toInAutocomplete;

    @FindBy(id = "dateSelection_InboundDate-datepicker")
    private WebElement dateInField;
    
    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(css = ".back")
    private WebElement totalPrice;

    // find "Outbound flight" title
    public String getOutboundFlightTitle() {
        wait.until(ExpectedConditions.visibilityOf(outFlightTitle));
        return outFlightTitle.getText();
    }

    // find "Outbound flight" page section
    public String getOutboundFlightSection() {
        wait.until(ExpectedConditions.visibilityOf(outFlightSection));
        return outFlightSection.getText();
    }

    // find "Inbound flight" title
    public String getInboundFlightTitle() {
        wait.until(ExpectedConditions.visibilityOf(inFlightTitle));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", inFlightTitle);
        return inFlightTitle.getText();
    }

    // find "Inbound flight" page section
    public String getInboundFlightSection() {
        wait.until(ExpectedConditions.visibilityOf(inFlightSection));
        return inFlightSection.getText();
    }

    // Outbound flight in the period of 7 days found
    public boolean outboundFlightFound() {
        if (availableFlights.size() > 0) return true;
        else return false;
    }

    // select first outbound flight
    public void clickFirstOutboundFlight() {
        try {Thread.sleep(1500);} catch (InterruptedException e){}
        wait.until(ExpectedConditions.visibilityOf(firstOutFlightButton));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", firstOutFlightButton);
        firstOutFlightButton.click();
    }

    // click button "select" for outbound flight
    public void submitOutboundFlight() {
        try {Thread.sleep(1000);} catch (InterruptedException e){};
        wait.until(ExpectedConditions.elementToBeClickable(selectOutButton)).click();
        wait.until(ExpectedConditions.visibilityOf(outFlightSelected));
    }

    // select first inbound flight
    public void clickFirstInboundFlight() {
        wait.until(ExpectedConditions.visibilityOf(firstInFlightButton));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", firstInFlightButton);
        firstInFlightButton.click();
    }

    // click button "select" for inbound flight
    public void submitInboundFlight() {
        try {Thread.sleep(1000);} catch (InterruptedException e){}
        wait.until(ExpectedConditions.elementToBeClickable(selectInButton)).click();
        wait.until(ExpectedConditions.visibilityOf(inFlightSelected));

    }

    // click button "Next"
    public void clickNextButton() {
        wait.until(ExpectedConditions.visibilityOf(inFlightSelected));
        wait.until(ExpectedConditions.visibilityOf(inFlightSelected));
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    // get "nonexistent route error" message text
    public String getNonexistentRouteMessage() {
        wait.until(ExpectedConditions.visibilityOf(nonexistentRouteMessage));
        return nonexistentRouteMessage.getText();
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

    // Input "From" for outbound route
    public void setFromOutField(String fromOut) {
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        wait.until(ExpectedConditions.elementToBeClickable(fromOutField)).click();
        fromOutField.clear();
        fromOutField.sendKeys(fromOut);
        checkInDropdownList(fromOutAutocomplete ,fromOut);
    }

    // Input "To" for outbound route
    public void setToOutField(String toOut) {
        wait.until(ExpectedConditions.elementToBeClickable(toOutField)).click();
        toOutField.clear();
        toOutField.sendKeys(toOut);
        checkInDropdownList(toOutAutocomplete ,toOut);
    }

    // Input date for outbound route
    public void setDateOutField(String dateOut) {
        wait.until(ExpectedConditions.elementToBeClickable(dateOutField)).click();
        dateOutField.clear();
        dateOutField.sendKeys(dateOut);
    }

    // Input "From" for inbound route
    public void setFromInField(String fromIn) {
        wait.until(ExpectedConditions.elementToBeClickable(fromInField)).click();
        fromInField.clear();
        fromInField.sendKeys(fromIn);
        checkInDropdownList(fromInAutocomplete ,fromIn);
    }

    // Input "To" for inbound route
    public void setToInField(String toIn) {
        wait.until(ExpectedConditions.elementToBeClickable(toInField)).click();
        toInField.clear();
        toInField.sendKeys(toIn);
        checkInDropdownList(toInAutocomplete ,toIn);
    }

    // Input date for inbound route
    public void setDateInField(String dateIn) {
        wait.until(ExpectedConditions.elementToBeClickable(dateInField)).click();
        dateInField.clear();
        dateInField.sendKeys(dateIn);
    }
    
    // Click "Search" button
    public void clickSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        // Evil magic! We cannot submit the form with one click. We need two!
    }

    // get total price
    public float getTotalPrice() {
        return Float.parseFloat(wait.until(ExpectedConditions.visibilityOf(totalPrice)).getText()
                .replaceAll("[^\\d\\.]", ""));
    }
}
