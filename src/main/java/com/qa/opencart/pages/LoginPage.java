package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ElementUtil;

import io.qameta.allure.Step;

public class LoginPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	// 1. Private By locators

	private By emailId = By.id("input-email");
	private By password = By.id("input-password");
	private By loginBtn = By.xpath("//input[@value='Login']");
	private By forgotPwdLink = By.linkText("Forgotten Password");
	private By registerLink = By.linkText("Register");
	
	// 2. Page constructor
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(driver);
	}

	// 3. Page actions // methods
	
	@Step("..Getting the login page title...")
	public String getLoginPageTitle() {
		String title = eleUtil.waitForTitleIsAndFetch(AppConstants.DEFAULT_SHORT_TIME_OUT,
				AppConstants.LOGIN_PAGE_TITLE_VALUE);
		System.out.println("Login page title is : " + title);
		return title;
	}
	@Step("..Getting the login page URL...")
	public String getLoginPageURL() {
		String url = eleUtil.waitForURLContainsAndFetch(AppConstants.DEFAULT_SHORT_TIME_OUT,
				AppConstants.LOGIN_PAGE_URL_FRACTION_VALUE);
		System.out.println("Login page url : " + url);
		return url;
	}
	@Step("..Checking the forgot pwd link exist or not...")
	public boolean isForgotPwdLinkExist() {
		return eleUtil.waitForElementVisibile(forgotPwdLink, AppConstants.DEFAULT_MEDIUM_TIME_OUT).isDisplayed();
	}
	@Step("login with username : {0} and password: {1}")
	public AccountsPage doLogin(String un, String pwd) {
		eleUtil.waitForElementVisibile(emailId, AppConstants.DEFAULT_MEDIUM_TIME_OUT).sendKeys(un);
		eleUtil.doSendKeys(password, pwd);
		eleUtil.doClick(loginBtn);
		return new AccountsPage(driver);
	}
	@Step("Navigating to the registration page")
	public RegisterPage navigateToRegisterPage() {
		eleUtil.doClick(registerLink);
		return new RegisterPage(driver);
	}

}
