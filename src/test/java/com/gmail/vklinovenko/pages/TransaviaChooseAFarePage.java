// https://www.transavia.com/en-EU/book-a-flight/choose-a-fare/select/
package com.gmail.vklinovenko.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransaviaChooseAFarePage {
    private WebDriverWait wait;
    private final WebDriver driver;

    public TransaviaChooseAFarePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 20);
    }

    @FindBy(xpath = "//th[@data-product-class='B']/span[@class='name']")
    private WebElement plusColumnTitle;

    @FindBy(xpath = "//table/tfoot//td[3]//button[@class='button button--selection']")
    private WebElement selectPlusButton;

    @FindBy(css = ".back")
    private WebElement totalPrice;

    @FindBy(xpath = "//aside/div/div[3]/ul/li[3]/span[1]")
    private WebElement babyPriceOut;

    @FindBy(xpath = "//aside/div/div[6]/ul/li[3]/span[1]")
    private WebElement babyPriceIn;

    @FindBy(css = "th.th:nth-child(2) > span:nth-child(2)")
    private WebElement personPrice;

    @FindBy(css = "th.th:nth-child(3) > span:nth-child(2)")
    private WebElement extraPriceB;

    // find title "Plus"
    public String getPlusTitle() {
        wait.until(ExpectedConditions.visibilityOf(plusColumnTitle));
        return plusColumnTitle.getText();
    }

    // click button "Select"
    public void clickSelectPlusButton() {
        wait.until(ExpectedConditions.visibilityOf(selectPlusButton));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", selectPlusButton);
        selectPlusButton.click();
    }

    // get person price
    public float getPersonPrice() {
        return Float.parseFloat(wait.until(ExpectedConditions.visibilityOf(personPrice)).getText()
                .replaceAll("[^\\d\\.]", ""));
    }

    // get children price
    public float getChildrenPrice() {    // PersonPrice = ChildPrice , C'est la vie...
        return getPersonPrice();
    }

    // get float from InnerHTML
    private float getFloatInnerHTML(WebElement element) {
        String innerHTML =
                (String) ((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", element);
        return Float.parseFloat(innerHTML.replaceAll("[^\\d\\.]", ""));
    }

    // get baby price
    public float getBabyPrice() {
        return getFloatInnerHTML(babyPriceOut) + getFloatInnerHTML(babyPriceIn);
    }

    // get extra price
    public float getExtraPriceB() {
        return Float.parseFloat(wait.until(ExpectedConditions.visibilityOf(extraPriceB)).getText()
                .replaceAll("[^\\d\\.]", ""));
    }

    // get total price
    public float getTotalPrice() {
        return Float.parseFloat(wait.until(ExpectedConditions.visibilityOf(totalPrice)).getText()
                .replaceAll("[^\\d\\.]", ""));
    }

}
