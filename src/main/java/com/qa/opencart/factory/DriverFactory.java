package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.aspectj.util.FileUtil;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
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
	 */
	public WebDriver initDriver(Properties prop) {
		
		highlight = prop.getProperty("highlight").trim();
		
		optionsManager = new OptionsManager(prop);
		String browserName = prop.getProperty("browser").toLowerCase().trim();

		System.out.println("Browser name is : " + browserName);

		if (browserName.trim().equalsIgnoreCase("chrome")) {
			//driver = new ChromeDriver(optionsManager.getChromeOptions());
			tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
		} else if (browserName.equalsIgnoreCase("firefox")) {
			//driver = new FirefoxDriver(optionsManager.getfirefoxOptions());
			tlDriver.set(new FirefoxDriver(optionsManager.getfirefoxOptions()));
		} else if (browserName.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		} else if (browserName.equalsIgnoreCase("edge")) {
			//driver = new EdgeDriver(optionsManager.getEdgeOptions());
			tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
		} else {
			System.out.println("Pls pass the right browser name..." + browserName);
			throw new FrameworkException("NO BROWSER FOUND EXCEPTION....");
		}

		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().get(prop.getProperty("url").trim());

		return getDriver();
	}
	/*
	 * get the local thread copy of the driver
	 * synchronized here every thread will get their individual copy
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
		
		//mvn clean install -Denv="stage"
		// -D is used for environment variables
		prop = new Properties();
		FileInputStream ip=null;
		String envName = System.getProperty("env");
		System.out.println("Running test cases on env : "+envName);
		try {	
		if(envName==null) {
			System.out.println("No env is passed...Running tests on QA env");
			ip = new FileInputStream("./src/test/resources/config/qa.config.properties");
		}
		else {
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
				//break;
			}
		}} 
		catch (FileNotFoundException e) {
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
		File srcFile = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir")+"/screenshot/"+System.currentTimeMillis()+".png";
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

