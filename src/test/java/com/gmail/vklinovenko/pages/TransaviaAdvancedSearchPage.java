// https://www.transavia.com/en-EU/book-a-flight/advanced-search/search/
package com.gmail.vklinovenko.pages;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TransaviaAdvancedSearchPage {
    private WebDriverWait wait;
    private final WebDriver driver;

    public TransaviaAdvancedSearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 20);
    }

    @FindBy(id = "countryStationSelection_Origin-input")
    private WebElement fromField;

    @FindBy(xpath = "//form[@id='alternativesearch']/div[2]/div[2]/div/div[1]//ol[@class='results']")
    private WebElement fromAutocomplete;

    @FindBy(id = "countryStationSelection_Destination-input")
    private WebElement toField;

    @FindBy(xpath = "//form[@id='alternativesearch']/div[2]/div[2]/div/div[2]//ol[@class='results']")
    private WebElement toAutocomplete;

    @FindBy(xpath = "//form[@id='alternativesearch']/div[3]/div[1]/div[2]")
    private WebElement budgetButton;

    @FindBy(id = "budgetSelection_EurosBudget")
    private WebElement budgetField;

    @FindBy(xpath = "//form[@id='alternativesearch']/div[4]/div[1]/div[2]")
    private WebElement takeoffButton;

    @FindBy(xpath = "//label[@for='data-type-month']")
    private WebElement specificMonthRadioButton;

    @FindBy(xpath = "//form[@id='alternativesearch']/div[4]/div[2]/div/div/div[2]/div[1]/div/div/div[1]/div[2]/div")
    private WebElement specificMonthSelect;

    @FindBy(xpath = "//form[@id='alternativesearch']/div[4]/div[2]/div/div/div[2]/div[1]/div/div/div[2]/div[2]/div")
    private WebElement weekDaySelect;

    @FindBy(xpath = "//form[@id='alternativesearch']/div[4]/div[2]/div/div/div[1]/div/div/div/div[2]/div/div")
    private WebElement flightTypeSelect;

    @FindBy(xpath = "//select[@id='data-flight-type']/option[@value='Single']")
    private WebElement singleTripOption;

    @FindBy(xpath = "//button[@class='button button-primary']")
    private WebElement searchButton;

    @FindBy(xpath = "//li//div[@class='panel']")
    private List<WebElement> destinationsList;

    // Login page loaded
    public boolean loaded() {
        try {Thread.sleep(1000);} catch (InterruptedException e){}
        wait.until(ExpectedConditions.titleIs("Inspiration for your next trip | Transavia"));
        return true;
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

    // Click field "From"
    public void clickFromField() {
        wait.until(ExpectedConditions.elementToBeClickable(fromField)).click();
    }

    // Input into field "From"
    public void setFromField(String from) {
        fromField.clear();
        fromField.sendKeys(from);
        checkInDropdownList(fromAutocomplete, from);
    }

    // Click field "To"
    public void clickToField() {
        wait.until(ExpectedConditions.elementToBeClickable(toField)).click();
    }

    // Input into field "To"
    public void setToField(String to) {
        toField.clear();
        toField.sendKeys(to);
        checkInDropdownList(toAutocomplete, to);
    }

    // Click button "What is your budget per person?"
    public void clickBudgetButton() {
        wait.until(ExpectedConditions.elementToBeClickable(budgetButton)).click();
    }

    // Click field "My budget"
    public void clickBudgetField() {
        wait.until(ExpectedConditions.elementToBeClickable(budgetField)).click();
    }

    // Input into field "My budget"
    public void setBudgetField(Integer budget) {
        budgetField.clear();
        budgetField.sendKeys(budget.toString());
    }

    // Click button "When will you be taking off?"
    public void clickTakeoffButton() {
        wait.until(ExpectedConditions.elementToBeClickable(takeoffButton)).click();
    }

    // Click "Specific month" button
    public void clickSpecificMonthRadioButton() {
        wait.until(ExpectedConditions.elementToBeClickable(specificMonthRadioButton)).click();
    }

    // Click "Round trip" dropdown menu, choose "single flight"
    public void selectSingleFlight() {
        wait.until(ExpectedConditions.elementToBeClickable(flightTypeSelect)).click();
        wait.until(ExpectedConditions.elementToBeClickable(singleTripOption)).click();
    }

    // Select specific month
    public void selectSpecificMonth(String month) {
        selectClicker(specificMonthSelect, month);
    }

    // Select day of the week
    public void selectWeekDay(String weekDay) {
        selectClicker(weekDaySelect, weekDay);
    }

    // Select item with text "text" in dropdown list "select"
    private void selectClicker(@NotNull WebElement select, String text) {
        wait.until(ExpectedConditions.elementToBeClickable(select)).click();
        List<WebElement> list = select.findElements(By.tagName("option"));
        for (WebElement item : list) {
            if (item.getText().contains(text)) {
                item.click();
                return;
            }
        }
        list.get(0).click();
    }

    // Click button "Search"
    public void clickSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    // Get any destinations
    public boolean flightsFound() {
        if (destinationsList.isEmpty()) return false;
        else return true;
    }

    public String getCheapestCity() {
        String city = destinationsList.get(0).findElements(By.tagName("h2")).get(0).getText();
        String country = destinationsList.get(0).findElements(By.tagName("h3")).get(0).getText();
        return city + ", " + country;
    }

    public float getCheapestPrice() {
        return Float.parseFloat(destinationsList.get(0).findElements(By.className("integer")).get(0).getText());
    }
}
