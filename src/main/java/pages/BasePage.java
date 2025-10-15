package pages;

import utils.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;


public class BasePage {
    protected Utils utils;
    protected WebDriver driver;
    BasePage(WebDriver driver){
        PageFactory.initElements(driver,this);
        this.driver = driver;
        this.utils = new Utils(driver);
    }
}
