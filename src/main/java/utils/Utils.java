package utils;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utils klasa sadrzi helper funkcije koje se koriste na vise mesta u page klasama. 
 * Na ovaj nacin su delovi koje se ponavljaju izvuceni na jedno mesto.
 * Sve metode su staticke, tako da se mogu pozivati bez instanciranja same klase. 
 * 
 *
 */
public class Utils {

	public static void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception {
		// Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
		// Call getScreenshotAs method to create image file
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		// Move image file to new destination
		File DestFile = new File(fileWithPath);
		// Copy file at destination
		FileUtils.copyFile(SrcFile, DestFile);
	}

	public static boolean isPresent(WebDriver webdriver, By selector) {
		// try to find element by specified selector
		try {
			webdriver.findElement(selector);
		} catch (NoSuchElementException e) {
			// if element not exist return false
			return false;
		}
		return true;
	}

	public static WebElement waitToBeClickable(WebDriver driver, By selector, int waitInterval) {
		WebElement element = (new WebDriverWait(driver, Duration.ofSeconds(waitInterval)).until(ExpectedConditions.elementToBeClickable(selector)));
		return element;
	}

	public static WebElement waitForElementPresence(WebDriver driver, By selector, int waitInterval) {
		WebElement element = (new WebDriverWait(driver, Duration.ofSeconds(waitInterval))).until(ExpectedConditions.presenceOfElementLocated(selector));
		return element;
	}

	public static void waitForTitle(WebDriver driver, String title, int waitInterval){
		 (new WebDriverWait(driver, Duration.ofSeconds(waitInterval))).until(ExpectedConditions.titleIs(title));
	}

}
