import org.nika.pages.WhatsAppPage;
import org.nika.pages.TrelloPage;
import org.nika.utils.Utils;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class NK {
    private WebDriver driver;
    private Utils utils;
    private WhatsAppPage whatsAppPage;

    @BeforeSuite
    public void setUp() throws IOException {
        ChromeOptions options = new ChromeOptions();
//        Path projectRoot = Paths.get(System.getProperty("user.dir"));
//        Path base = projectRoot.resolve("User Data");
//        Path dirA = base.resolve("A_" + System.currentTimeMillis());
//        Files.createDirectories(dirA);
//        options.addArguments("--user-data-dir=" + dirA.toAbsolutePath());
        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        Path base = projectRoot.resolve("User Data");
        Files.createDirectories(base);
        Path dirA = base.resolve("A");
        Files.createDirectories(dirA);
        options.addArguments("--user-data-dir=" + dirA.toAbsolutePath());
        String profileName = "Default";
        options.addArguments("--profile-directory=" + profileName);
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
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
    }

    @Test()
    public void test() throws InterruptedException, IOException {
        driver.get("https://web.whatsapp.com");
//        List<String> messagesTrello = new ArrayList<>();
        for (Map.Entry<String, String> entry: Map.of("patara luka","Shemovdivar").entrySet()){
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
            System.out.println(whatsAppPage.isValidForTrello(lastMessage, chatName));
        }
//        WebDriver newTab = driver.switchTo().newWindow(WindowType.TAB);
//        newTab.get("https://trello.com/b/KXmOslHa/nodaridagchrimalegamis5saatia");
//        WebDriverWait wait = new WebDriverWait(newTab, Duration.ofSeconds(10));
////        Thread.sleep(500000);
//        List<WebElement> accVefList = driver.findElements(By.xpath("//button[@class='_NIfxkJaBExOvE ybVBgfOiuWZJtD O4iyCshk1noBhH _St8_YSRMkLv07']"));
//        if (!accVefList.isEmpty()) {
//            WebElement accVef = accVefList.get(0);
//            accVef.click();
//            System.out.println("Button clicked.");
//        } else {
//            WebElement notes = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='Iq1byT5o1KoAi6' and contains(., 'To Do')]//div[@class='ajc2ZCGn_g5KLY']")));
//            ((JavascriptExecutor) newTab).executeScript("arguments[0].focus();", notes);
//            WebElement toDo = driver.findElement(By.xpath("//li[@class='Iq1byT5o1KoAi6' and contains(., 'To Do')]//div[@class='ajc2ZCGn_g5KLY']"));
//            toDo.click();
//            WebElement toDo1 = driver.findElement(By.xpath("//Textarea[@class='QnC5HZJq_5Juhe']"));
//            toDo1.sendKeys(ani);
//            Thread.sleep(500);
//            WebElement addbtn = driver.findElement(By.xpath("//Button[@class='ybVBgfOiuWZJtD orotyyeYQx_tso _St8_YSRMkLv07']"));
//            addbtn.click();
//            Thread.sleep(500);
//            WebElement doTo = driver.findElement(By.xpath("//li[@class='Iq1byT5o1KoAi6' and contains(., 'Do To')]//div[@class='ajc2ZCGn_g5KLY']"));
//            doTo.click();
//            WebElement doTo1 = driver.findElement(By.xpath("//Textarea[@class='QnC5HZJq_5Juhe']"));
//            doTo1.sendKeys(eleniko);
//            Thread.sleep(500);
//            WebElement addbtnz = driver.findElement(By.xpath("//Button[@class='ybVBgfOiuWZJtD orotyyeYQx_tso _St8_YSRMkLv07']"));
//            addbtnz.click();
//            Thread.sleep(500);
//        }
//        WebElement notes = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='Iq1byT5o1KoAi6' and contains(., 'To Do')]//div[@class='ajc2ZCGn_g5KLY']")));
//        ((JavascriptExecutor) newTab).executeScript("arguments[0].focus();", notes);
//        WebElement toDo = driver.findElement(By.xpath("//li[@class='Iq1byT5o1KoAi6' and contains(., 'To Do')]//div[@class='ajc2ZCGn_g5KLY']"));
//        toDo.click();
//        WebElement toDo1 = driver.findElement(By.xpath("//Textarea[@class='QnC5HZJq_5Juhe']"));
//        toDo1.sendKeys(ani);
//        Thread.sleep(500);
//        WebElement addbtn = driver.findElement(By.xpath("//Button[@class='ybVBgfOiuWZJtD orotyyeYQx_tso _St8_YSRMkLv07']"));
//        addbtn.click();
//        Thread.sleep(500);
//        WebElement doTo = driver.findElement(By.xpath("//li[@class='Iq1byT5o1KoAi6' and contains(., 'Do To')]//div[@class='ajc2ZCGn_g5KLY']"));
//        doTo.click();
//        WebElement doTo1 = driver.findElement(By.xpath("//Textarea[@class='QnC5HZJq_5Juhe']"));
//        doTo1.sendKeys(eleniko);
//        Thread.sleep(500);
//        WebElement addbtnz = driver.findElement(By.xpath("//Button[@class='ybVBgfOiuWZJtD orotyyeYQx_tso _St8_YSRMkLv07']"));
//        addbtnz.click();
//        Thread.sleep(500);
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
