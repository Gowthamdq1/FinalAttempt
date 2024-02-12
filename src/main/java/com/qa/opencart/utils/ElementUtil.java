package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.factory.DriverFactory;

public class ElementUtil {

	private WebDriver driver;
	private JavaScriptUtil jsUtil;

	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		jsUtil = new JavaScriptUtil(driver);
	}

	public WebElement getElement(By locator) {
		WebElement element = driver.findElement(locator);
		if(Boolean.parseBoolean(DriverFactory.highlight)) {
			jsUtil.flash(element);
		}
		
		return element;
	}

	public WebElement getElement(By locator, int timeOut) {
		return waitForElementVisibile(locator, timeOut);
	}

	public void doSendKeys(By locator, String value) {
		WebElement element = getElement(locator);
		element.clear();
		element.sendKeys(value);
	}

	public void doClick(By locator) {
		getElement(locator).click();
	}

	public void doActionSendKeys(By locator, String value) {
		Actions act = new Actions(driver);
		act.sendKeys(driver.findElement(locator), value).build().perform();
	}

	public void doActionsClick(By locator) {
		Actions act = new Actions(driver);
		act.click(driver.findElement(locator)).build().perform();
	}

	public String doElementGetText(By locator) {
		return getElement(locator).getText();
	}

	public boolean doElementDisplayed(By locator) {
		return driver.findElement(locator).isDisplayed();
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public void getElementAttributes(By locator, String attrName) {
		List<WebElement> eleList = getElements(locator);
		for (WebElement e : eleList) {
			String attrVal = e.getAttribute(attrName);
			System.out.println(attrVal);
		}
	}

	public int getElementsCount(By locator) {
		int eleCount = getElements(locator).size();
		System.out.println("Total Elements ===> " + eleCount);
		return eleCount;
	}

	public List<String> getElementsTextList(By locator) {
		List<String> eleTextList = new ArrayList<String>();
		List<WebElement> eleList = getElements(locator);
		for (WebElement e : eleList) {
			String text = e.getText();
			eleTextList.add(text);
		}
		return eleTextList;
	}

	// ******************* Select class based Utilities ******************
	public void doSelectDropDownByIndex(By locator, int index) {
		Select select = new Select(getElement(locator));
		select.selectByIndex(index);
	}

	public void doSelectDropDownByValue(By locator, String Value) {
		Select select = new Select(getElement(locator));
		select.selectByValue(Value);
	}

	public void doSelectDropDownByVisibleText(By locator, String text) {
		Select select = new Select(getElement(locator));
		select.selectByVisibleText(text);
	}

	public List<WebElement> getDropDownOptionsList(By locator) {
		Select select = new Select(getElement(locator));
		return select.getOptions();
	}

	public List<String> getDropDownOptionsTextList(By locator) {
		List<WebElement> optionsList = getDropDownOptionsList(locator);

		List<String> optionsTextList = new ArrayList<String>();

		for (WebElement e : optionsList) {
			String text = e.getText();
			optionsTextList.add(text);
		}
		return optionsTextList;
	}

	public void selectDropDownValue(By locator, String expValue) {
		List<WebElement> optionsList = getDropDownOptionsList(locator);
		for (WebElement e : optionsList) {
			String text = e.getText();
			System.out.println(text);
			if (text.equals(expValue)) {
				e.click();
				break;
			}
		}
	}

	public int getTotalDropDownOptions(By locator) {
		int optionsCount = getDropDownOptionsList(locator).size();
		System.out.println("Total options count ==> " + optionsCount);
		return optionsCount;
	}

	public void doSearch(By suggListLocator, String suggName) {
		List<WebElement> suggList = getElements(suggListLocator);
		System.out.println(suggList.size());

		for (WebElement e : suggList) {
			String text = e.getText();
			System.out.println(text);
			if (text.contains(suggName)) {
				e.click();
				break;
			}
		}
	}
	// *******************************************WAITS****************

	/**
	 * An expectation for checking that an element is present on the DOM of a page.
	 * This does not necessarily mean that the element is visible.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public WebElement waitForElementPresence(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * and visible. Visibility means that the element is not only displayed but also
	 * has a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public WebElement waitForElementVisibile(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * An expectation for checking that all elements present on the web page that
	 * match the locator are visible. Visibility means that the elements are not
	 * only displayed but also have a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public List<WebElement> waitForElementsVisibility(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	/**
	 * An expectation for checking that there is at least one element present on a
	 * web page.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public List<WebElement> waitForElementsPresence(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	public Alert waitForAlertPresence(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	public String getAlertText(int timeOut) {
		return waitForAlertPresence(timeOut).getText();
	}

	public void acceptAlert(int timeOut) {
		waitForAlertPresence(timeOut).accept();
	}

	public void dismissAlert(int timeOut) {
		waitForAlertPresence(timeOut).dismiss();
	}

	public void alertSendKeys(int timeOut, String value) {
		waitForAlertPresence(timeOut).sendKeys(value);
	}

	public String waitForTitleContainsAndFetch(int timeOut, String titleFractionValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.titleContains(titleFractionValue));
		return driver.getTitle();
	}

	public String waitForTitleIsAndFetch(int timeOut, String titleValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.titleContains(titleValue));
		return driver.getTitle();
	}

	public String waitForURLContainsAndFetch(int timeOut, String UrlFractionValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.urlContains(UrlFractionValue));
		return driver.getCurrentUrl();
	}

	public String waitForURLIsAndFetch(int timeOut, String UrlValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.urlToBe(UrlValue));
		return driver.getCurrentUrl();
	}

	public boolean waitForURLContains(int timeOut, String UrlFractionValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.urlContains(UrlFractionValue));

	}

	public void waitForFrameAndSwitchToItByIDorName(int timeOut, String idOrName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("main"));
	}

	public void waitForFrameAndSwitchToItByIndex(int timeOut, int frameIndex) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}

	public void waitForFrameAndSwitchToItByFrameElement(int timeOut, WebElement frameElement) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}

	public void waitForFrameAndSwitchToItByFrameLocator(int timeOut, WebElement frameLocator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}

	/**
	 * An expectation for checking an element is visible and enabled such that you
	 * can click it.
	 * 
	 * @param timeOut
	 * @param Locator
	 */
	public void clickWhenReady(int timeOut, By Locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.elementToBeClickable(Locator)).click();
	}

	public WebElement waitForElementToBeClickable(int timeOut, By Locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.elementToBeClickable(Locator));
	}

	public void doClickWithActionsAndWait(int timeOut, By Locator) {
		WebElement ele = waitForElementToBeClickable(timeOut, Locator);
		Actions act = new Actions(driver);
		act.click(ele).build().perform();
	}
	public WebElement waitForElementPresenceWithFluentWait(int timeOut, int pollingTime, By locator) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(10))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.pollingEvery(Duration.ofSeconds(2))
				.withMessage("...Element is not found on the page....");
		
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	public void waitForAlertWithFluentWait(int timeOut, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(10))
				.ignoring(NoAlertPresentException.class)
				.pollingEvery(Duration.ofSeconds(2))
				.withMessage("...Alert is not found on the page....");
		
		wait.until(ExpectedConditions.alertIsPresent());
	}

}
