package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.aspectj.util.FileUtil;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.qa.opencart.exception.FrameworkException;

public class DriverFactory {

	public WebDriver driver;
	public Properties prop;
	public OptionsManager optionsManager;
	public static String highlight;

	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	// We cannot keep initDriver method as static and declaring it as static will
	// not allow
	// multiple threads to access it

	/**
	 * this method is initializing the driver on the basis of the given browser name
	 * 
	 * @param browserName
	 * @return this return the driver
	 * @throws MalformedURLException 
	 */
	public WebDriver initDriver(Properties prop){
		
		highlight = prop.getProperty("highlight").trim();
		
		optionsManager = new OptionsManager(prop);
		String browserName = prop.getProperty("browser").toLowerCase().trim();

		System.out.println("Browser name is : " + browserName);

		if (browserName.trim().equalsIgnoreCase("chrome")) {
			
			if(Boolean.parseBoolean(prop.getProperty("remote"))) {
				// Remote execution
				init_remoteDriver("chrome");
			}
			else {
				// Local execution
				tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			}
		} 
		// FIREFOX
		else if (browserName.equalsIgnoreCase("firefox")) {
			if(Boolean.parseBoolean(prop.getProperty("remote"))) {
				init_remoteDriver("firefox");
			}
			else {
			tlDriver.set(new FirefoxDriver(optionsManager.getfirefoxOptions()));
			}
		} 
		// EDGE
		else if (browserName.equalsIgnoreCase("edge")) {
			if(Boolean.parseBoolean(prop.getProperty("remote"))) {
				init_remoteDriver("edge");
			}
			else {
			tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
			}
		} else {
			System.out.println("Pls pass the right browser name..." + browserName);
			throw new FrameworkException("NO BROWSER FOUND EXCEPTION....");
		}

		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().get(prop.getProperty("url").trim());

		return getDriver();
	}
		/**
		 * This method is called internally to initialise the driver with RemoteWebDriver
		 */
	private void init_remoteDriver(String browser) {
		
		System.out.println("Running tests on grid server ::::"+browser);
		//run on remote
		try {
		switch (browser) {
		
		case "chrome":
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getChromeOptions()));
			break;
			
		case "firefox":
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getfirefoxOptions()));
			break;
			
		case "edge":
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getEdgeOptions()));
			break;

		default:
			System.out.println("Plz pass the right browser for remote execution..."+browser);
			throw new FrameworkException("NOREMOTEBROWSEREXCEPTION");
		}}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}}

	/*
	 * get the local thread copy of the driver synchronized here every thread will
	 * get their individual copy
	 */
	public synchronized static WebDriver getDriver() {
		return tlDriver.get();
	}

	/**
	 * This method is reading the properties from the .properties file
	 * 
	 * @return
	 */

	public Properties initProp() {

		// mvn clean install -Denv="stage"
		// -D is used for environment variables
		prop = new Properties();
		FileInputStream ip = null;
		String envName = System.getProperty("env");
		System.out.println("Running test cases on env : " + envName);
		try {
			if (envName == null) {
				System.out.println("No env is passed...Running tests on QA env");
				ip = new FileInputStream("./src/test/resources/config/qa.config.properties");
			} else {
				switch (envName.toLowerCase().trim()) {
				case "qa":
					ip = new FileInputStream("./src/test/resources/config/qa.config.properties");
					break;
				case "stage":
					ip = new FileInputStream("./src/test/resources/config/stage.config.properties");
					break;
				case "dev":
					ip = new FileInputStream("./src/test/resources/config/dev.config.properties");
					break;
				case "prod":
					ip = new FileInputStream("./src/test/resources/config/config.properties");
					break;
				default:
					System.out.println("Wrong env is passed...No need to run the test cases");
					throw new FrameworkException("WRONG ENV IS PASSED.....");
				// break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	// ./ mentioned it will go to the current project directory
	// file input stream Used to interact with the files

	/**
	 * take screenshot
	 */
	public static String getScreenshot() {
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/screenshot/" + System.currentTimeMillis() + ".png";
		// user.dir will give the current directory
		File destination = new File(path);
		try {
			FileUtil.copyFile(srcFile, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return path;

	}

}
