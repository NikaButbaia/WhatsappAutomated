package listeners;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.testng.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotOnFailureListener implements ITestListener {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = resolveDriver(result);
        if (driver == null) return;

        byte[] png = null;
        String pageSource = null;

        try {
            if (driver instanceof TakesScreenshot) {
                png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            }
        } catch (WebDriverException ignored) {}

        try {
            pageSource = driver.getPageSource();
        } catch (WebDriverException ignored) {}

        Path testDir = buildArtifactDir(result);
        String stamp = TS.format(LocalDateTime.now());

        if (png != null && png.length > 0) {
            Path file = saveBytes(testDir.resolve("screenshot_" + stamp + ".png"), png);
            Allure.addAttachment("Failure screenshot " + shortName(result, stamp),
                    "image/png", new ByteArrayInputStream(png), "png");
            System.out.println("[Screenshot] saved -> " + file.toAbsolutePath());
        }

        if (pageSource != null && !pageSource.isBlank()) {
            Path file = saveBytes(testDir.resolve("page_" + stamp + ".html"), pageSource.getBytes());
            Allure.addAttachment("Page source " + shortName(result, stamp),
                    "text/html", new ByteArrayInputStream(pageSource.getBytes()), "html");
            System.out.println("[PageSource] saved -> " + file.toAbsolutePath());
        }
    }

    private Path buildArtifactDir(ITestResult result) {
        String base = System.getProperty("artifacts.dir", "target/artifacts");
        String suite = safe(result.getTestContext().getSuite().getName());
        String cls = safe(result.getTestClass().getRealClass().getSimpleName());
        String method = safe(result.getMethod().getMethodName());
        Path dir = Paths.get(base, suite, cls, method);
        try { Files.createDirectories(dir); } catch (IOException ignored) {}
        return dir;
    }

    private String shortName(ITestResult r, String stamp) {
        return r.getTestClass().getRealClass().getSimpleName()
                + "." + r.getMethod().getMethodName()
                + "@" + stamp;
    }

    private Path saveBytes(Path path, byte[] bytes) {
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save artifact: " + path, e);
        }
    }

    private String safe(String s) {
        return (s == null ? "unknown" : s.replaceAll("[^a-zA-Z0-9._-]", "_"));
    }

    private WebDriver resolveDriver(ITestResult result) {
        Object test = result.getInstance();
        if (test == null) return null;

        WebDriver d = findDriverInFields(test);
        if (d == null) d = findDriverViaGetter(test, "getDriver");
        if (d == null) d = findDriverViaGetter(test, "driver");

        return unwrap(d);
    }

    private WebDriver findDriverInFields(Object instance) {
        Class<?> c = instance.getClass();
        while (c != null && c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    Object val = f.get(instance);
                    if (val instanceof WebDriver) return (WebDriver) val;
                    if (val instanceof ThreadLocal) {
                        Object inner = ((ThreadLocal<?>) val).get();
                        if (inner instanceof WebDriver) return (WebDriver) inner;
                    }
                } catch (Throwable ignored) {}
            }
            c = c.getSuperclass();
        }
        return null;
    }

    private WebDriver findDriverViaGetter(Object instance, String methodName) {
        try {
            Method m = instance.getClass().getMethod(methodName);
            Object val = m.invoke(instance);
            return (val instanceof WebDriver) ? (WebDriver) val : null;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private WebDriver unwrap(WebDriver d) {
        try {
            while (d instanceof WrapsDriver) d = ((WrapsDriver) d).getWrappedDriver();
        } catch (Throwable ignored) {}
        return d;
    }

    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSuccess(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}
