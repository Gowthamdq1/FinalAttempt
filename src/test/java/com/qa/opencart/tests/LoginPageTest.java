package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("EPIC-100: design login for open cart app")
@Story("US-Login: 101: design login page features for open cart")
public class LoginPageTest extends BaseTest {

	@Severity(SeverityLevel.TRIVIAL)
	@Description("Checking the title of the page..Author : Naveen")
	@Test(priority = 1)
	public void loginPageTitleTest() {
		String actualTitle = loginPage.getLoginPageTitle();
		Assert.assertEquals(actualTitle, AppConstants.LOGIN_PAGE_TITLE_VALUE);
	}
	@Severity(SeverityLevel.NORMAL)
	@Description("Checking the URL of the page..Author : Naveen")
	@Test(priority = 2)
	public void loginPageURLTest() {
		String actualURL = loginPage.getLoginPageURL();
		Assert.assertTrue(actualURL.contains(AppConstants.LOGIN_PAGE_URL_FRACTION_VALUE));
	}
	@Severity(SeverityLevel.CRITICAL)
	@Description("Checking forgot pwd link exist..Author : Naveen")	
	@Test(priority = 3)
	public void forgotPwdLinkExistTest() {
		Assert.assertTrue(loginPage.isForgotPwdLinkExist());
	}
	@Severity(SeverityLevel.CRITICAL)
	@Description("Checking user is able to login to app with the correct username and password")
	@Test(priority = 4)
	public void loginTest() {
		accPage = loginPage.doLogin(prop.getProperty("username").trim(),prop.getProperty("password").trim());
		Assert.assertTrue(accPage.isLogoutLinkExist());
		
		
	}

}
