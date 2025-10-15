package org.nika.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Utils {
    private WebDriver driver;
    JavascriptExecutor js;

    public Utils(WebDriver driver) {
        this.driver = driver;
        js = (JavascriptExecutor) driver;
    }

    public void waitForUrlToContain(String fraction, int timeoutInSeconds) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        webDriverWait.until(ExpectedConditions.urlContains(fraction));
    }

    public void waitForElementVisible(WebElement element, int timeoutSeconds) {
        System.out.println("Waiting for element "+element);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        webDriverWait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementVisible(String xpath, int timeoutSeconds) {
        System.out.println("Waiting for element "+xpath);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void waitForElementInVisible(String xpath, int timeoutSeconds) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
    }

    public void waitForElementInVisible(WebElement element, int timeoutSeconds) {
        System.out.println("Waiting for element "+element);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        webDriverWait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void javascriptClick(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    public void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'end', inline: 'nearest'});", element);
    }

    public void waitForElementToBeInDom(String element, int timeoutSeconds) {
        System.out.println("Waiting for element "+element);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(element)));
    }
    public void numberOfElementsMoreThan(String xpath, int amount,int timeoutSeconds) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        webDriverWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath(xpath),amount));
    }
}
