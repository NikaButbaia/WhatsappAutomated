package org.nika.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;

public class TrelloPage extends BasePage {

    public TrelloPage(WebDriver driver) {
        super(driver);
    }

    private By toDoList = By.xpath("//li[@class='Iq1byT5o1KoAi6' and contains(., 'To Do')]//div[@class='ajc2ZCGn_g5KLY']");
    private By doToList = By.xpath("//li[@class='Iq1byT5o1KoAi6' and contains(., 'Do To')]//div[@class='ajc2ZCGn_g5KLY']");
    private By textArea = By.xpath("//textarea[@class='QnC5HZJq_5Juhe']");
    private By addButton = By.xpath("//button[contains(@class, 'ybVBgfOiuWZJtD')]");
    @FindBy(xpath = "//li[@class='Iq1byT5o1KoAi6' and contains(., 'To Do')]//div[@class='ajc2ZCGn_g5KLY']")
    WebElement toDofirst;
    @FindBy(xpath = "//li[@class='Iq1byT5o1KoAi6' and contains(., 'Do To')]//div[@class='ajc2ZCGn_g5KLY']")
    WebElement doTofirst;
//    @FindBy(xpath = "//textarea[@class='QnC5HZJq_5Juhe']")
//    WebElement textArea;





}

