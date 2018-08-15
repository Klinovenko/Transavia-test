package com.gmail.vklinovenko.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YoutubeVideoPage {
    private WebDriverWait wait;
    private final WebDriver driver;

    public YoutubeVideoPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 20);
    }

    @FindBy(xpath = "//div[@id='container']/h1/yt-formatted-string")
    private WebElement videoName;

    @FindBy(xpath = "//yt-formatted-string[@id='owner-name']/a")
    private WebElement videoAuthor;

    // Get video name on the video page
    public String getVideoName() {
        return wait.until(ExpectedConditions.visibilityOf(videoName)).getText();
    }
    // Get video author on the video page
    public String getVideoAuthor() {
        return wait.until(ExpectedConditions.visibilityOf(videoAuthor)).getText();
    }
}
