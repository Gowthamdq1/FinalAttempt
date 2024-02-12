package com.qa.opencart.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;

public class AccountsPageTest extends BaseTest{
	
	@BeforeClass
	public void accPageSetup() {
		accPage = loginPage.doLogin(prop.getProperty("username").trim(),prop.getProperty("password").trim());
	}
	@Test
	public void accPageTitleTest() {
		String actTitle = accPage.getAccPageTitle();
		Assert.assertEquals(actTitle, AppConstants.ACCOUNT_PAGE_TITLE_VALUE);
	}
	@Test
	public void accPageURLTest() {
		String actURL = accPage.getAccPageURL();
		Assert.assertTrue(actURL.contains(AppConstants.ACCOUNT_PAGE_URL_FRACTION_VALUE));
	}
	@Test
	public void isLogoutLinkExistTest() {
		Assert.assertTrue(accPage.isLogoutLinkExist());
	}
	@Test
	public void accPageHeadersCountTest() {
		List<String> actualAccPageHeadersList = accPage.getAccountsPageHeadersList();
		System.out.println("Acc page header list : "+actualAccPageHeadersList);
		Assert.assertEquals(actualAccPageHeadersList.size(), AppConstants.ACCOUNTS_PAGE_HEADER_COUNT);	
	}
	@Test
	public void accPageHeadersValueTest() {
		List<String> actualAccPageHeadersList = accPage.getAccountsPageHeadersList();
		System.out.println("Acc page header list : "+actualAccPageHeadersList);
		Assert.assertEquals(actualAccPageHeadersList, AppConstants.EXPECTED_ACCOUNT_PAGE_HEADER_LIST);	
	}
	
	@DataProvider
	public Object[][] getProductData(){
		return new Object[][] {
			{"Macbook"},
			{"iMac"},
			{"Apple"},
			{"Samsung"}
		};
	}
	
	@Test(dataProvider = "getProductData")
	public void searchProductCountTest(String searchKey) {
		searchPage = accPage.performSearch(searchKey);
		Assert.assertTrue(searchPage.getSearchProductsCount()>0);
	}
	
	@DataProvider
	public Object[][] getProductTestData(){
		return new Object[][] {
			{"Macbook","MacBook Pro"},
			{"Macbook","MacBook Air"},
		};
	}
	
	@Test(dataProvider = "getProductTestData")
	public void searchProductTest(String searchKey,String productName) {
		searchPage = accPage.performSearch(searchKey);
		
		if(searchPage.getSearchProductsCount()>0) {
			productInfoPage = searchPage.selectProduct(productName);
			String actProductHeader = productInfoPage.getProductHeaderValue();
			Assert.assertEquals(actProductHeader, productName);
		}
	}
	
	
	
	
	
	
	
	

}
