package com.SeleniumFramework.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.SeleniumFramework.sauce.util.SampleSauceTestBase;

public class DriverClassTest extends SampleSauceTestBase {
	public String envFile, screenShotRep, detailedRep;
	public String tcStatus, moduleName, cCellData, dCellData, eCellData, chromedriver;
	public String objectName, objName, xpathProperty, rvg;
	public int screenshotflag, screenshotCount, callactionFlag, reportFlag;
	public static String line2 = "";

	public DriverClassTest(String browser, String browerVersion, String Platform) {
		super(browser, browerVersion, Platform);
	}

	@Test
	public void executeTest() throws Exception {
		System.out.println("Executing Test" + " Thread: " + Thread.currentThread().getName());
		System.out.print("\t"+name.getMethodName());
		rvg = "";
		importData();
	}

	/**
	 * importData method gets the location/Path of Test Suite, Test Modules,
	 * Element Collection files
	 * 
	 * @param testUtility
	 *            testUtility contains the path of provider excelsheet where
	 *            paths of all above required files/folders are stored
	 */
	private void importData() {
		try {			
			// Checks for platform to start their respective services
			if (platform.equalsIgnoreCase("Chrome")) {
				executeForChrome();
			} else if (platform.equalsIgnoreCase("FireFox")) {
				executeForFireFox();
			} else if (platform.equalsIgnoreCase("IExplorer")) {
				executeForIEExplorer();
			} else if (platform.equalsIgnoreCase("All")) {
				executeForIEExplorer();
				Thread.sleep(3000);
				executeForFireFox();
				Thread.sleep(3000);
				executeForChrome();

			} else if (platform.equalsIgnoreCase("Sauce")) {// Use this if you
															// want to run on
															// sauceLab
				executeForSauce();
			} else {
				LOG_VAR = 0;
			}
			BufferedWriter out_result = new BufferedWriter(new FileWriter(reportDate, true));
			out_result.newLine();
			out_result.write("</table>");
			out_result.close();
			BufferedWriter out_detailedResult = new BufferedWriter(new FileWriter(reportLog, true));
			out_detailedResult.newLine();
			out_detailedResult.write("</table>");
			out_detailedResult.close();
		} catch (Exception e) {
			System.out.println(Thread.currentThread().getName()+": Exception from ImportData Function: " + e.getMessage());
			e.printStackTrace();	
		}
	}

	private void executeForChrome() throws Exception {
			platform = "Chrome";
			tmpPlatform = platform;
		if (environment.equalsIgnoreCase("Desktop_Web")) {
			String ss;
			ss = "lib" + File.separator + "chromedriver.exe";
			System.out.println("SS: " + ss);
			System.setProperty("webdriver.chrome.driver", ss);
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, ss);
//			ChromeDriverService service = ChromeDriverService.createDefaultService();
//			ChromeOptions options = new ChromeOptions();
//			options.addArguments("test-type");
//			options.addArguments("--start-maximized");
//			options.addArguments("--disable-extensions");
//			driver = new ChromeDriver(service, options);

//			selenium = new com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium(driver, "http://www.google.com");
			Thread.sleep(3000);
//			System.out.println("Platform for URL: " + selenium.getEval("navigator.userAgent"));
//			Thread.sleep(5000);
//			System.out.println("TesInfo: Platform for URL: " + selenium.getEval("navigator.userAgent"));
			String rv;// = selenium.getEval("navigator.userAgent");
			try {
//				if (rvg.equalsIgnoreCase("")) {
//					rvg = "FF" + rv.split("Firefox/")[1];
//				} else {
//					rvg = rvg + "_FF" + rv.split("Firefox/")[1];
//				}
				rv = capabilities.getCapability("browserName")+"_"+capabilities.getCapability("version");
						//"FF_" + rv.split("Firefox/")[1];

			} catch (Exception e) {
				rv = "Chrome";
				rvg = "Chrome";
			}
			tmpBrowserVer = rv;
			PREVIOUS_TEST_CASE = "Before Test Execution";
			//driver.quit();
			testSuite(moduleName, rv);

			LOG_VAR = 1;
			TEST_STEP_COUNT = 1;
			failedStep = " ";
			//service.stop();
			
			try{driver.quit();}catch(Exception e){};
		}

	}	

	private void executeForFireFox() throws Exception {
		platform = "Firefox";
		tmpPlatform = platform;
		String ss;
		ss = "lib" + File.separator + "geckodriver.exe";
		System.out.println("SS: " + ss);
		System.setProperty("webdriver.gecko.driver", ss);
		 DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//		 capabilities.setCapability("marionette", false);
		if (environment.equalsIgnoreCase("Desktop_Web")) {
//			driver = new FirefoxDriver(capabilities);
			PREVIOUS_TEST_CASE = "Before Test Execution";
			String rv;
			try {
				rv = capabilities.getCapability("browserName")+"_"+capabilities.getCapability("version");
			} catch (Exception e) {
				rv = "FF";
				rvg = "FF";
			}
//			oldTab = driver.getWindowHandle();
//			selenium.close();

			tmpBrowserVer = rv;
			testSuite(moduleName, rv); // Calls
										// testSuite
										// method
										// with
										// new
										// user
										// agent
										// each
										// time
			PREVIOUS_TEST_CASE = "Before Test Execution";
			LOG_VAR = 1;
			TEST_STEP_COUNT = 1;
			failedStep = " ";
			testcaseCounter = 0;
			
			try{driver.quit();}catch(Exception e){};
		}

	}

	private void executeForIEExplorer() throws Exception {
		platform = "Explorer";
		tmpPlatform = platform;
		String ss = new File("lib" + File.separator + "IEDriverServer.exe").getCanonicalPath();
		System.setProperty("webdriver.ie.driver", ss);
		DesiredCapabilities capab = DesiredCapabilities.internetExplorer();
		capab.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capab.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
//		driver = new InternetExplorerDriver(capab);
		String rv;
		try {
			rv = capab.getCapability("browserName")+"_"+capabilities.getCapability("version");
//					driver.getCapabilities().getBrowserName()+"_"+driver.getCapabilities().getVersion();
		} catch (Exception e) {
			rv = "IE";
			rvg = "IE";
		}
		tmpBrowserVer = rv;
		PREVIOUS_TEST_CASE = "Before Test Execution";
//		driver.quit();
		testSuite(moduleName, rv); // Calls
									// testSuite
									// method
									// with
									// new
									// user
									// agent
									// each
									// time

		PREVIOUS_TEST_CASE = "Before Test Execution";

		LOG_VAR = 1;
		TEST_STEP_COUNT = 1;
		failedStep = " ";
		testcaseCounter = 0;
		
		try{driver.quit();}catch(Exception e){};
	}

	/**
	 * @throws Exception
	 */
	private void executeForSauce() throws Exception {
		executeSauceOneByOne(cap);
	}

	/**
	 * @param desiredCapabilities
	 * @throws Exception
	 * 
	 */
	private void executeSauceOneByOne(DesiredCapabilities desiredCapabilities) throws Exception {
			String rv = desiredCapabilities.getBrowserName();
			rvg = desiredCapabilities.getVersion();//+"_"+desiredCapabilities.getPlatform();
		tmpBrowserVer = rv;
		PREVIOUS_TEST_CASE = "Before Test Execution";
		testSuite(moduleName, rv); // Calls testSuite method with new user agent
									// each time
		PREVIOUS_TEST_CASE = "Before Test Execution";
		LOG_VAR = 1;
		TEST_STEP_COUNT = 1;
		failedStep = " ";
		testcaseCounter = 0;;
		try{driver.quit();}catch(Exception e){};
		System.out.println("Ending Thread: " + Thread.currentThread().getName());
	}
}