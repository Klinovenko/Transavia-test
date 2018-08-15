// https://www.transavia.com/en-EU/destinations/
package com.gmail.vklinovenko.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransaviaDestinationsPage {
    private WebDriverWait wait;
    private final WebDriver driver;

    public TransaviaDestinationsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 20);
    }

    @FindBy(css = "a.button")
    private WebElement perfectDestinationButton;

    // Login page loaded
    public boolean loaded() {
        wait.until(ExpectedConditions.titleIs("Destinations"));
        return true;
    }

    // click button "Find the perfect destination"
    public void clickPerfectDestinationButton() {
        wait.until(ExpectedConditions.elementToBeClickable(perfectDestinationButton)).click();
    }
}
