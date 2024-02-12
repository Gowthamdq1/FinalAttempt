package com.qa.opencart.tests;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ExcelUtil;

public class RegisterPageTest extends BaseTest {
	
	@BeforeClass
	public void regPageSetup() {
		registerPage = loginPage.navigateToRegisterPage();
	}
	public String getRandomEmail() {
		//Random random = new Random();
		//String email = "automation"+random.nextInt(1000)+"@gmail.com";
		String email = "automation"+System.currentTimeMillis()+"@gmail.com";
		return email;
		// This will generate numbers from 1 to 1000
	}
	@DataProvider
	public Object[][] getRegTestData() {
		Object regData[][] = ExcelUtil.getTestData(AppConstants.REGISTER_SHEET_NAME);
		return regData;
	}
	
	@Test(dataProvider = "getRegTestData")
	public void userRegTest(String firstName,String lastName,String telephone,
			String password,String subscribe) {
//		Assert.assertTrue(registerPage.registerUser(firstName,lastName,getRandomEmail(),telephone,
//				password,subscribe));
		
		registerPage.registerUser(firstName,lastName,getRandomEmail(),telephone,
				password,subscribe);
	
	}
	
	

}
