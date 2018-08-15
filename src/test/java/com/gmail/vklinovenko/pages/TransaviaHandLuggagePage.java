// https://www.transavia.com/en-EU/faq/hand-luggage/
package com.gmail.vklinovenko.pages;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransaviaHandLuggagePage {
    private WebDriverWait wait;
    private final WebDriver driver;

    public TransaviaHandLuggagePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 20);
    }

    @FindBy(xpath = "//h3[text()='Video: hand luggage and Transavia']")
    private WebElement videoPageSectionHeader;

    @FindBy(xpath = "/html/body/div[9]/div/div/div/div[2]/div/iframe")
    private WebElement videoFrame;

    // Login page loaded
    public boolean loaded() {
        wait.until(ExpectedConditions.titleIs("Information about hand luggage | Transavia"));
        return true;
    }

    // Scroll down to the page section "Video: hand luggage and Transavia"
    public void scrollToVideo() {
        wait.until(ExpectedConditions.visibilityOf(videoPageSectionHeader));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", videoPageSectionHeader);
    }

    // Get video link
    public String getVideoLink() {
        return parseURL(videoFrame.getAttribute("src"));
    }

    // Parse url from "https://www.youtube.com/embed/fQMuhniqWAg" to "https://youtu.be/fQMuhniqWAg" format
    private String parseURL(@NotNull String url) {
        String parsedURL = url.replaceFirst("www.", "");
        parsedURL = parsedURL.replaceFirst("youtube.com/embed", "youtu.be");
        return parsedURL;
    }
}
