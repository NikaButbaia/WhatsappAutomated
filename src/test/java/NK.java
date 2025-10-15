import listeners.ScreenshotOnFailureListener;
import org.testng.annotations.*;
import pages.WhatsAppPage;
import pages.TrelloPage;
import utils.Utils;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Listeners(ScreenshotOnFailureListener.class)
public class NK {
    private WebDriver driver;
    private Utils utils;
    private WhatsAppPage whatsAppPage;
    private TrelloPage trelloPage;

    @BeforeSuite
    public void setUp() throws IOException {
        ChromeOptions options = new ChromeOptions();
        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        Path base = projectRoot.resolve("User Data");
        Files.createDirectories(base);
        Path dirA = base.resolve("A");
        Files.createDirectories(dirA);
        options.addArguments("--user-data-dir=" + dirA.toAbsolutePath());
        String profileName = "Default";
        options.addArguments("--profile-directory=" + profileName);
        options.addArguments("--no-first-run");
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--hide-scrollbars");
        options.addArguments("--mute-audio");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        driver = new ChromeDriver(options);
        utils = new Utils(driver);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> safeQuit(driver)));
    }

    @BeforeClass
    public void pageSetUp(){
        whatsAppPage = new WhatsAppPage(driver);
        trelloPage = new TrelloPage(driver);
    }

    @Test()
    public void test() throws InterruptedException, IOException {
        driver.get("https://web.whatsapp.com");
        HashMap<String, String> messagesTrello = new HashMap<>();
        for (Map.Entry<String, String> entry: Map.of("Giorgi","lalala","Sofo","lalala").entrySet()){
            String chatName = entry.getKey();
            String filterName = entry.getValue();
            whatsAppPage.waitForWPLoad()
                    .enterChat(chatName);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            HashMap<String, String> lastMessage = whatsAppPage.getLastMessages(filterName).getLast();
            if (whatsAppPage.isValidForTrello(lastMessage, chatName)){
                messagesTrello.put(entry.getKey(), lastMessage.keySet().stream().findFirst().orElse(null));
            }
        }
        if (messagesTrello.isEmpty()){
            return;
        }
        WebDriver newTab = driver.switchTo().newWindow(WindowType.TAB);
        newTab.get(""); //trello board url
        Thread.sleep(2000);
        trelloPage.loginIfNot();
        trelloPage.waitForBoardToLoad();
        for (Map.Entry<String, String> entry: messagesTrello.entrySet()){
            String name = entry.getKey();
            String message = entry.getValue();
            if (trelloPage.checkIfListExists(name)){
                trelloPage.addCard(name, message);
            } else {
                trelloPage.addList(name);
                Thread.sleep(3000);
                trelloPage.addCard(name, message);
            }
        }
        Thread.sleep(1000);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        safeQuit(driver);
        driver = null;
    }

    private static void safeQuit(WebDriver d) {
        if (d == null) return;
        try { d.manage().deleteAllCookies(); } catch (Throwable ignored) {}
        try { d.quit(); }
        catch (Throwable e1) {
            try { d.close(); } catch (Throwable ignored) {}
        }
    }
}
