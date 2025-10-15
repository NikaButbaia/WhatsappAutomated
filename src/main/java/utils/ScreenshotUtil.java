package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.*;

public final class ScreenshotUtil {
    private ScreenshotUtil() {}

    public static Path capture(WebDriver driver, String name) {
        try {
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path dir = Paths.get("target", "screenshots");
            Files.createDirectories(dir);
            String safeName = name.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path file = dir.resolve(safeName + ".png");
            Files.write(file, png, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save screenshot", e);
        }
    }
}
