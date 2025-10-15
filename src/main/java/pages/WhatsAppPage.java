package pages;

import utils.ChatJsonStore;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhatsAppPage extends BasePage {

    public WhatsAppPage(WebDriver driver){
        super(driver);
    }

    @FindBy(xpath = "//button[@aria-label=\"Chats\"]")
    WebElement chatButton;

    @FindAll(@FindBy(xpath = "//div[contains(@class, \"message-in focusable-list-item\")]//span[@dir=\"ltr\"]"))
    List<WebElement> allCompanyMessages;

    public WhatsAppPage waitForWPLoad(){
        utils.waitForElementVisible(chatButton , 30);
        return this;
    }
    public WhatsAppPage enterChat(String chatName){
        utils.waitForElementToBeInDom("//span[text()=\"%s\"]".formatted(chatName), 30);
        WebElement element = driver.findElement(By.xpath("//span[text()=\"%s\"]".formatted(chatName)));
        utils.scrollIntoView(element);
        element.click();
        return this;
    }

    public List<HashMap<String, String>> getLastMessages(String filterName) throws InterruptedException {
        List<HashMap<String, String>> messageDateMapList = new ArrayList<>();

        Thread.sleep(5000);

        utils.numberOfElementsMoreThan(
                "//div[contains(@class, \"message-in focusable-list-item\")]//span[@dir=\"ltr\"]",
                0,
                10);
        for (WebElement webElement: allCompanyMessages){
            String messageText = webElement.getText().trim().toLowerCase();
            if (messageText.contains(filterName)){
                HashMap<String, String> messageDateMap = new HashMap<String, String>();
                String date = webElement.findElement(By.xpath(".//ancestor::div[@data-pre-plain-text]")).getAttribute("data-pre-plain-text");
                messageDateMap.put(webElement.getText().trim(), date.split("]")[0]+"]");
                messageDateMapList.add(messageDateMap);
            }
        }
        return messageDateMapList;
    }

    public boolean isValidForTrello(HashMap<String, String> messageDate, String chatName) throws IOException {
        String message = messageDate.keySet().stream().findFirst().orElse(null);
        String date = messageDate.values().stream().findFirst().orElse(null);
        Map<String, ChatJsonStore.ChatRecord> chatRecord = new ChatJsonStore.Store().view();

        if (!chatRecord.containsKey(chatName)){
            new ChatJsonStore.Store().putOrUpdate(chatName, message, date);
            return true;
        }
        String messageRecord = chatRecord.get(chatName).msg;
        String dateRecord = chatRecord.get(chatName).date;

        if (messageRecord.equals(message) && dateRecord.equals(date))
        {
            return false;
        }
        else {
            new ChatJsonStore.Store().putOrUpdate(chatName, message, date);
            return true;
        }


    }
}
