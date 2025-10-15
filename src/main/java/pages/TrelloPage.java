package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class TrelloPage extends BasePage {

    public TrelloPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//textarea[@placeholder=\"Enter list name…\"]")
    WebElement newListNameInput;

    @FindBy(xpath = "//div[@data-testid=\"board-name-container\"]")
    WebElement boardHeader;

    @FindBy(xpath = "//ol[@id=\"board\"]")
    WebElement board;

    public TrelloPage waitForBoardToLoad(){
        utils.waitForElementVisible(boardHeader, 15);
        return this;
    }

    public TrelloPage loginIfNot(){
        List<WebElement> accVefList = driver.findElements(By.xpath("//button[@class='_NIfxkJaBExOvE ybVBgfOiuWZJtD O4iyCshk1noBhH _St8_YSRMkLv07']"));
        if (!accVefList.isEmpty()) {
            WebElement accVef = accVefList.get(0);
            accVef.click();
        }
        return this;
    }

    public TrelloPage addList(String listName) {
        utils.waitForElementVisible(board, 15);
        WebElement listComposerButton = driver.findElement(By.xpath("//button[@data-testid=\"list-composer-button\"]"));
        utils.javascriptClick(listComposerButton);
        newListNameInput.sendKeys(listName);
        WebElement newListSubmitButton = driver.findElement(By.xpath("//button[@data-testid=\"list-composer-add-list-button\"]"));
        utils.javascriptClick(newListSubmitButton);

        return this;
    }

    public boolean checkIfListExists(String listName) {
        List<WebElement> lists = driver.findElements(
                By.xpath("//h2[@data-testid=\"list-name\"]//span[text()=\"%s\"]".formatted(listName)));
        return !lists.isEmpty();
    }

    public TrelloPage addCard(String listName, String cardMessage){
        String xpath = "//h2[@data-testid=\"list-name\"]//span[text()=\"%s\"]".formatted(listName) +
                "//ancestor::div[@data-testid=\"list\"]//button[@data-testid=\"list-add-card-button\"]";
        utils.waitForElementClickable(xpath, 15);
        WebElement cardAddButton = driver.findElement(By.xpath(xpath));
        cardAddButton.click();
        WebElement addCardTextArea = driver.findElement(By.xpath("//textarea[@data-testid=\"list-card-composer-textarea\"]"));
        utils.waitForElementVisible(addCardTextArea, 10);
        addCardTextArea.sendKeys(cardMessage);
        WebElement addCardButton = driver.findElement(By.xpath("//button[@data-testid=\"list-card-composer-add-card-button\"]"));
        addCardButton.click();
        return this;
    }
}

