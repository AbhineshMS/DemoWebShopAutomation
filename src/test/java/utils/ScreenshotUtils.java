package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    /** Saves a PNG into target/screenshots and returns its path (null if it could not be saved). */
    public static String capture(WebDriver driver, String testName) {
        if (driver == null) {
            return null;
        }
        try {
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Path dir = Paths.get(ConfigReader.get("screenshot.dir"));
            Files.createDirectories(dir);
            Path target = dir.resolve(testName + "_" + time + ".png");
            Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return target.toString();
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not take screenshot: " + e.getMessage());
            return null;
        }
    }
}
