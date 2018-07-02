package com.SeleniumFramework.test;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.xmlbeans.impl.store.Path;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.SeleniumFramework.commons.util.ConnectionHelper;
import com.SeleniumFramework.commons.util.LIFOStack;
import com.SeleniumFramework.commons.util.ResponseHelper;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import com.fasterxml.jackson.databind.JsonNode;
//import com.gargoylesoftware.htmlunit.WebWindow;
//import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.document.DocumentDescriptor;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JacksonHandle;
import com.saucelabs.saucerest.SauceREST;
//import com.thoughtworks.selenium.Selenium;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXlsDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class FunctionalLibrary extends ReportLibrary {

	protected DesiredCapabilities cap;
	public static String SAUCE_USERNAME;
	public static String SAUCE_ACCESS_KEY;
	private static String host;
	private static Integer port;
	private static String username;
	private static String password;
	private static String database;
	private String pageSource = ""; // Keep the page
	private int localCounter = 0;
	protected Cell currentDataCell;

	// DB Helpers
	private Connection conn;
	private HashMap<String, String> storeQueryResults = new LinkedHashMap<String, String>();

	// ADDED SOME FIELDS FOR API//
	public String APIurl = "";
	public List<String> APIheader = new ArrayList<String>();
	// public String APIheader = "";
	public String APImethod = "";
	public String APIpayload = "";
	public String APIactualResponse = "";

	final String fulfillmentCollectionName = "FulfillmentRequestTracking";
	final String DispatchCollectionName = "DispatchRecord";
	private DocumentDescriptor desc;
	private DatabaseClient client = getClient();
	private JacksonHandle jacksonHandle = new JacksonHandle();
	private JSONDocumentManager documentManager = client.newJSONDocumentManager();
	private DocumentMetadataHandle fulfillmentMetadata = new DocumentMetadataHandle()
	.withCollections(fulfillmentCollectionName);
	private DocumentMetadataHandle dispatchRecordMetadata = new DocumentMetadataHandle()
	.withCollections(DispatchCollectionName);

	public String PREVIOUS_TEST_CASE = "Before Test Execution";
	protected int LOG_VAR = 1;
	protected String testFlag;
	protected boolean LOOP_FLAG = false;

	protected HSSFWorkbook scriptWorkbook, mainScriptWorkbook;
	protected HSSFSheet readScriptSheet, readLoopSheet, readtestcasesheet, readactionsheet;
	
	protected ChromeDriverService chromeService;
	protected DesiredCapabilities capabilities;

	public String tcStartTime, uaMain;
	public String fieldName, fieldValue, inputSheet, testModulePath;
	public String screenName, fieldElementType, fieldName2, applicationName;
	protected int currTestRowPtr, startRow, endRow;
	protected String TC_ID = null;
	protected String TC_DESC = null;
	public int TEST_STEP_COUNT = 1, tempCounter = 1;
	public int testcaseCounter = 1;
	public int TCCounter = 1;
	public int failFlag;
	protected String fValue_tmp = "";
	public String BrwsrBsln;
	public POIFSFileSystem poifs3;
	public int synctime, Longsynctime;
	public HashMap<String, String> dataholder = new HashMap<String, String>(); // HashMap
	
	//By Shubhank
	protected LIFOStack<HSSFSheet> readScriptSheets = new LIFOStack<HSSFSheet>(5);
	protected LIFOStack<Integer> currTestRowPtrs = new LIFOStack<Integer>(5);
	
	// for
	// DataHold
	public Robot robo;
	public String ACTUALPARA = "";// Used in
	// Geteleproperty,Asserteleproperty
	// Keywords
	public String DVARIABLE = "";
	public WebDriverWait wait;
	public String BURL;
	public static String excelpath;
	public static FileInputStream fis;
	public static Workbook wb;
	public static Sheet sh;

	@Rule
	public TestName name = new TestName() {
		public String getMethodName() {
			return String.format("%s", super.getMethodName());
		}
	};

	/**
	 * Method keyword: This method has different keywords definition in nested
	 * if statement
	 * 
	 * @param fieldName
	 * @param objName:
	 *            contains object name on which action is to be performed
	 * @param feType:
	 *            contains field element type by which the object will be found
	 *            by selenium.
	 * @param fValue:
	 *            contains value to be put/verified into/with field.
	 * @param action:
	 *            contains the keyword with the help of which action is being
	 *            performed.
	 * @throws InterruptedException
	 * @throws IOException
	 */

	public enum KeywordActions {
		OpenURL, Input, Click, CheckByIndex, SetCheckBox, ClearAndType, Clear, WaitTime, // General
		// Keywords
		AddAPIurl, AddAPImethod, AddAPIheader, GetAPIResponse, CheckAPIResponse, // API
		// Keywords
		// added
		// by
		// Shubhank
		// DB functions
		runQuery, putValueFromQuery, verifyDBtextMatches, verifyDBtextSmallerThan, verifyDBtextGreaterThan,
		// Text comparison
		verifyTextMatches, checkElementCountEquals, verifyTextSmallerThan, verifyTextGreaterThan,
		// Navigation
		NavigateForward, NavigateTo, NavigateBack, switchToDefaultContent,
		// Upload
		UploadUsingRobot,

		VerifyElementExists, VerifyDynamicElementExists, VerifyElementByValue, VerifyElementProperty, VerifyURL,verifyTextNotContains, // Verification
		// Keywords
		verifyMatchesText, VerifyTextPresent, VerifyLink, VerifyMultiLinks, VerifyFalseEleExist, VerifyAlertText, VerifyPageSource,

		SelectDDByValTxt, SelectDDByInd, // Select operations

		HoldelementText, GenerateAndHoldSSN,SendelementValue, Geteleproperty, Asserteleproperty, // Data
		// Hold
		// and
		// Verification
		// Keywords

		ClosewindowByTitle, CloseBrowser, SwitchToWindow, VerifywindowTitle, // Window
		// and
		// Browser
		// Related
		// Keywords

		Actionclick, MouseHoverclick, MouseHoverJs, // MouseHover Keywords
		// (Action class)

		TypeRandomNbr, HandleAlert, KeyEvent, GetPreviousDate, RandomNameGenerator,

		ClosePDF, JscriptExecutor, VerifyTableRowCount, MouseHover, CloseReminder, JsClick, UncheckAllSelectbyIndex, OptionalClick, CloseAddEditAlerts, SwitchToframe, // Other
		// Keywords
		SwitchToWindowLatest, SwitchToFrameDefault,

		ValidateResponse, ValidateResponseExcel, AngJsClick, AngJsInput, AngJsVerifyElemExists, AngJsVerifyLink, AngJsVerifyTextPresent, AngJsSelectIndxValTxt, AngJSVerifyTextInput, MarklogicDBConnc, JsScroll,

		// HIX Specific keywords
		StoreHIXTokenFromGurrilla, Secques, verifyTextContains,SelectByValue,createOptumIDandTDstore,HandleForgotPasswordSecques,VerifyMaskedPasswordField,VerifyTextNotPresent,

		//XML Keywords
		WriteXMLFromDB,VerifyFromXML,VerifyTextNotPresentInDD,captureScreenshots,SaveToNotepad ,updateQuery, VerifyDropDownOptionsCount, InputWindowPopup,ClickWindowPopup,
		
		VerifyFileDownload, SelectByText, GetExcelColCount, GetExcelData;
		

	}

	public void keyword(String objName, String feType, String fValue, String action, String fieldName)
 {

		try {
			synctime = 1000;
			Longsynctime = 10000;
			KeywordActions Action = KeywordActions.valueOf(action);

			switch (Action) {
			case AddAPIurl:
				APIurl = fValue;
				break;
			case AddAPImethod:
				APImethod = fValue;
				break;
			case AddAPIheader:
				// APIheader = fValue;
				APIheader.add(fValue);
				break;
			case GetAPIResponse:
				getAPIresponse(fValue);
				break;

			case CheckAPIResponse:
				checkAPIresponse(fValue);
				break;

			case OpenURL:
				// For Sauce Only /////
				if (!(System.getenv("SELENIUM_HOST") == null || System.getenv("SELENIUM_HOST").toString().isEmpty())) {
					System.out.println("######### SELENIUM_HOST: " + System.getenv("SELENIUM_HOST"));
					url = url.replace(new URL(url).getHost(), (new URL(System.getenv("SELENIUM_HOST"))).getHost());
				}
				fValue_tmp = url;
				funcOpenUrl(feType, objName, fValue);
				break;
			case Click:
				if (!fValue.isEmpty())
					DVARIABLE = fValue;
				funcClick(feType, objName, fValue);
				System.out.println("Click Performed !!!");
				break;
			case UploadUsingRobot:
				funcPasteFromClipBoard(feType, objName, fValue);
				break;
			case Input:
				funcInput(feType, objName, fValue);
				// Thread.sleep(synctime);
				break;

			case ClearAndType:
				funClearthnType(feType, objName, fValue);
				break;

			case CheckByIndex:
				funcCheckByIndex(feType, objName, fValue);
				// Thread.sleep(synctime);
				break;

			case UncheckAllSelectbyIndex:
				funcUncheckAll(feType, objName, fValue);
				// Thread.sleep(synctime);
				break;

			case VerifyElementExists:
				funcVerify(feType, objName, fValue);
				// Thread.sleep(synctime);
				break;

			case VerifyDynamicElementExists:
				funcVerify(feType, objName, fValue);
				// Thread.sleep(synctime);
				break;

			case VerifywindowTitle:
				funcVerifyWin(feType, objName, fValue);
				break;

			case ClosewindowByTitle:
				funcCloseWin(feType, objName, fValue);
				break;

			case SetCheckBox:
				funcSetcheck(feType, objName, fValue);
				break;

			case VerifyElementByValue:
				funVerifyfieldValue(feType, objName, fValue);
				break;

			case VerifyURL:
				funcVerifyURL(fValue);
				break;

			case SelectDDByValTxt:
				funcSelectDataByVal(feType, objName, fValue);
				break;

			case SelectDDByInd:
				funcSelectDataByInd(feType, objName, fValue);
				break;	

			case VerifyTextPresent:
				funTextpresent(feType, objName, fValue);
				break;

			case VerifyAlertText:
				funAlertText(feType, objName, fValue);
				break;

			case HoldelementText:
				funHoldvalue(feType, objName, fValue);
				break;
			
			case GenerateAndHoldSSN:     
                funGenerateAndHoldSSN(feType, objName, fValue);     
                break; 
	
			case SendelementValue:
				funSendValue(feType, objName, fValue);
				break;

			case VerifyElementProperty: // Data Holder
				funElementProp(feType, objName, fValue);
				break;

			case VerifyLink:
				funLink(feType, objName, fValue);
				break;

			case VerifyMultiLinks:
				funMultiLinks(feType, objName, fValue);
				break;

			case TypeRandomNbr:
				funGenrtNumbr(feType, objName, fValue);
				break;

			case HandleAlert:
				funHandleAlert(feType, fValue);
				break;

			case CloseAddEditAlerts:
				funCloseAddEditAlerts(feType, objName, fValue);
				break;

			case Clear:
				funClear(feType, objName, fValue);
				// Thread.sleep(synctime);
				break;

			case VerifyFalseEleExist:
				funVerifyFals(feType, objName, fValue);
				// Thread.sleep(synctime);
				break;

			case KeyEvent:
				robo = new Robot();
				funKeyEvents(feType, objName, fValue);
				break;

			case Actionclick:
				if (!fValue.isEmpty())
					DVARIABLE = fValue;
				actionsClick(feType, objName, fValue);
				break;

			case WaitTime:
				waittime(fValue);
				break;
			case CloseBrowser:
				funBrowserclose();
				break;

			case Geteleproperty:
				funGetprop(feType, objName, fValue);
				break;

			case Asserteleproperty:
				funChkprop(feType, objName, fValue);
				break;

			case NavigateBack:
				driver.navigate().back();
				break;
			case NavigateForward:
				driver.navigate().forward();
				break;
			case NavigateTo:
			     funcNavigateTo(feType, objName, fValue);
			     break;

			case SwitchToWindow:
				funSwitchWin(feType, objName, fValue);
				break;

			case SwitchToframe:
				funSwitchframe(feType, objName, fValue);
				break;

			case SwitchToWindowLatest:
				funSwitchWinLatest(feType, objName, fValue);
				break;

			case switchToDefaultContent:
				funSwitchframeDef(feType, objName, fValue);
				break;

			case ClosePDF:
				funClosePDF();
				break;

			case MouseHoverclick:
				funmouseHoverClick(feType, objName, fValue);
				break;

			case MouseHover:
				funmouseHover(feType, objName, fValue);
				break;

			case MouseHoverJs:
				funMouseHoverJs(feType, objName, fValue);
				break;

			case VerifyTableRowCount:
				funTableRowCount(feType, objName, fValue);
				break;

			case JscriptExecutor:
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("" + fValue + "");
				break;

			case JsScroll:
				JavascriptExecutor js1 = (JavascriptExecutor) driver;
				js1.executeScript("scroll(0,250)", "");
				break;

			case JsClick:
				// if (!fValue.isEmpty())DVARIABLE = fValue;
				// JSfuncClick(feType, objName);
				funcClick(feType, objName, fValue);
				break;

			case VerifyPageSource:
				funverifyPageSource(feType, objName, fValue);
				// Thread.sleep(Longsynctime);
				break;

			case GetPreviousDate:
				fungetPreviousDate(feType, objName, fValue);
				// Thread.sleep(Longsynctime);

			case OptionalClick:
				if (!fValue.isEmpty())
					DVARIABLE = fValue;
				funOptionalClick(feType, objName, fValue);
				// Thread.sleep(Longsynctime);
				break;

			case ValidateResponse:
				funValidateResponse(inputSheet, feType, objName, fValue);
				// Thread.sleep(Longsynctime);
				break;

			case ValidateResponseExcel:
				funValidateResponseRxcel(inputSheet, fieldName, fValue);
				// Thread.sleep(Longsynctime);
				break;

			case AngJsClick:
				funcAngJSClick(feType, objName, fValue);
				// Thread.sleep(Longsynctime);
				break;

			case AngJsInput:
				funcAngJSInput(feType, objName, fValue);
				// Thread.sleep(Longsynctime);
				break;

			case AngJsVerifyElemExists:
				funcAngJSVerfiyElemExists(feType, objName);
				// Thread.sleep(Longsynctime);
				break;

			case AngJsSelectIndxValTxt:
				funcAngJSSelectData(feType, objName, fValue);
				// Thread.sleep(Longsynctime);
				break;

			case AngJsVerifyLink:
				funAngJsVerifyLink(feType, objName, fValue);
				// Thread.sleep(Longsynctime);
				break;

			case AngJsVerifyTextPresent:
				funAngJsVerifyTextpresent(feType, objName, fValue);
				break;
			case AngJSVerifyTextInput:
				funcAngVerifyTextInput(feType, objName, fValue);
				break;
			case MarklogicDBConnc:
				funMarklogicDBConnc();
				break;
			case verifyTextMatches:
				verifyTextMatches(feType, objName, fValue);
				break;
			case Secques:
				securityQuestions(feType, objName, fValue);
				break;
			case verifyTextSmallerThan:
				verifyTextSmallerThan(feType, objName, fValue);
				break;
			case verifyTextGreaterThan:
				verifyTextGreaterThan(feType, objName, fValue);
				break;
			case checkElementCountEquals:
				checkElementCountEquals(feType, objName, fValue);
				break;
			case runQuery:
				runQueryDatabase(fValue);
				break;

			case VerifyFromXML:
				funcVerifyFromXML(feType, objName, fValue);
				break;

			case WriteXMLFromDB:
				funcWriteXMLFromDB(feType, objName, fValue);
				break;

			case putValueFromQuery:
				funInputFromQuery(feType, objName, fValue);
				break;
			case verifyDBtextMatches:
				verifyDBtextMatches(feType, objName, fValue);
				break;
			case verifyDBtextGreaterThan:
				verifyDBtextGreaterThan(feType, objName, fValue);
				break;
			case verifyDBtextSmallerThan:
				verifyDBtextSmallerThan(feType, objName, fValue);
				break;
			case verifyTextContains:
				funcVerifyTextContains(feType, objName, fValue);
				break;
				
			case verifyTextNotContains:
				funcVerifyTextNotContains(feType, objName, fValue);
				break;	

			case VerifyTextNotPresent:     
				funTextNotpresent(feType, objName, fValue);  
				break;   

			case SelectByValue:
				funcSelectByValue(feType, objName, fValue);
				break;

			case createOptumIDandTDstore:
				funcCreateOptumIDandTDstore();
				break;
			case HandleForgotPasswordSecques:	
				funcHandleForgotPasswordSecques();
				break;
			
			case VerifyMaskedPasswordField:
				funcVerifyMaskedpasswordField(feType, objName, fValue);
				break;
				
			case VerifyTextNotPresentInDD:
				funcVerifyTextNotPresentInDD(feType, objName, fValue);
				break;
				
			case captureScreenshots:
				funcaptureScreenshots();
				break;
			
			case SaveToNotepad:
                funcSaveToNotepad(feType, objName, fValue);
                break;
                
			case VerifyDropDownOptionsCount:
				funcVerifyDropDownOptionsCount(feType, objName, fValue);
				break;
				
			case RandomNameGenerator:
				funcRandomNameGenerator(feType, objName ,fValue);
				break;

			case updateQuery:
				updateQueryDatabase(fValue);
				break;
			
            case InputWindowPopup:
            	funcInputInWindowPopup(feType, objName ,fValue);
            	break;
            
            case ClickWindowPopup:
            	funcClickInWindowPopup(feType, objName ,fValue);
            	break;	
            
            case VerifyFileDownload:
            	funcVerifyFileDownload(feType, objName, fValue);
            	break;

            case GetExcelColCount:
            	funGetColCount(fValue);
            	break;

            case GetExcelData:
            	funGetExcelData(fValue);
            	break;
            case SelectByText:
            	funcSelectByText(feType, objName, fValue);
            	break;


			}
		} catch (Exception e) {
			LOG_VAR = 0;

			//Old style, also update this to File type
			//			screenShoot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			//New style, use the type to Screenshot in ReportLib.java
			screenShoot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
			//		    String scrlinkkk = "ScreenShots" + File.separator + scrshot + ".png";
			//			scrshot = scrshtPath + File.separator + scrshot + ".png";


			System.out.println("############" + name.getMethodName() + " \n" + feType + " " + objName + " " + action
					+ " " + fieldName + " " + fValue + "\n" + "########## EXCEPTION OCCURED IN " + TC_ID
					+ "############\n" + "######THREAD: " + Thread.currentThread().getName() + "\n"
					+ "###### EXCEPTION: " + e.getMessage());
			e.printStackTrace();
			String newline = System.getProperty("line.separator");
			String Trace = "Exception thrown from Keyword method : " + newline + e.getMessage();
			sendLog(Trace, PREVIOUS_TEST_CASE, currTestRowPtrs,currTestRowPtr);
		}
	}

	private void funMarklogicDBConnc() {
		// TODO Auto-generated method stub

		getClient();
		JacksonHandle jacksonHandle = null;
		jacksonHandle = readFulfillmentRecordById("ISLTEST13");

		JsonNode node = jacksonHandle.get();
		System.out.println("Root Node" + node);

		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
		while (fieldsIterator.hasNext()) {
			Map.Entry<String, JsonNode> field = fieldsIterator.next();

			if (field.getKey() == "requestHistory") {
				JsonNode innerNode = field.getValue();
				System.out.println("Inner key for Request history" + innerNode);
				findArrNodeValue(innerNode);
			}
			if (field.getKey() == "fulfillmentRequest") {
				JsonNode innerNode = field.getValue();
				System.out.println("Inner key for Request header " + innerNode);
				findNodeValue(innerNode);
			}
		}
	}

	public DatabaseClient getClient() {
		try {
			client = DatabaseClientFactory.newClient("dbsrt0998.uhc.com", 8000, "FSL_SYS", "fslmlnpd@ms", "2Jvr34DH",
					Authentication.BASIC);
			// LOGGER.debug("MarkLogic connection is created");
			System.out.println();
		} catch (Exception e) {
			// LOGGER.error("Error occured during connection establishment."+e);
		}
		return client;
	}

	private void funValidateResponse(String sheet, String feType, String objName, String fValue)
			throws IOException, InterruptedException {

		String Outputparam = fValue;

		String[] Value = sheet.split("#");

		String Outputparam_Sheet = Value[0];
		String ResponseID = Value[1];

		String Outputfile = "WebServicesAutomation" + File.separator + "Output.xls";
		// fileOut = new FileOutputStream(Outputfile);
		// workbook = new HSSFWorkbook();
		// worksheet = workbook.getSheet("ServiceName");

		FileInputStream afis = new FileInputStream(Outputfile);
		POIFSFileSystem apoifs = new POIFSFileSystem(afis);
		HSSFWorkbook aworkbook = new HSSFWorkbook(apoifs);
		HSSFSheet areadsheet = aworkbook.getSheet(Outputparam_Sheet);
		String ExpectedValue = "";
		WebElement element;
		element = funcFindElement(feType, objName);

		String TagName = element.getTagName();

		if (TagName.equalsIgnoreCase("input") || TagName.equalsIgnoreCase("select")) {

			ExpectedValue = element.getAttribute("value");
		} else {
			ExpectedValue = element.getAttribute("innertext");
			if (ExpectedValue == null) {
				ExpectedValue = element.getText();
			}
		}
		int FIELD_NAME_CLMN_CNTR = 0;
		int FIELD_NAME_CLMN_NO = 0;
		int rowposition = 0;
		boolean Status = false;
		String getFieldNameColumnHeader;
		String OutputparamValue = " ";
		String Rowstatus = "true";

		while (rowposition < 500) {
			getFieldNameColumnHeader = getCellValue(areadsheet, rowposition, FIELD_NAME_CLMN_CNTR);

			if (getFieldNameColumnHeader.equalsIgnoreCase(ResponseID)) {
				Rowstatus = "true";

				while (FIELD_NAME_CLMN_CNTR < 500 && Rowstatus != "false") {
					getFieldNameColumnHeader = getCellValue(areadsheet, rowposition, FIELD_NAME_CLMN_CNTR);

					if (getFieldNameColumnHeader.equalsIgnoreCase(Outputparam)) {
						FIELD_NAME_CLMN_NO = FIELD_NAME_CLMN_CNTR;

						OutputparamValue = getCellValue(areadsheet, rowposition + 1, FIELD_NAME_CLMN_NO);

						if (OutputparamValue.contains(".")) {
							String ValueA = OutputparamValue.substring(0, 4);

							OutputparamValue = ValueA;
						} else if (Outputparam.contains("ProgramValue")) {
							if (OutputparamValue.equals("5")) {
								OutputparamValue = "Declined to Respond";
							} else if (OutputparamValue.equals("6")) {
								OutputparamValue = "Response Not required";
							} else if (OutputparamValue.equals("7")) {
								OutputparamValue = "Does not Apply";
							} else if (OutputparamValue.equals("8")) {
								OutputparamValue = "Unable to calculate score";
							} else if (OutputparamValue.equals("9")) {
								OutputparamValue = "N/A";
							}

						}
						Status = true;
						break;
					}

					else {
						FIELD_NAME_CLMN_CNTR = FIELD_NAME_CLMN_CNTR + 1;
					}

				}
			} else {
				Rowstatus = "false";
			}

			if (!Status) {
				rowposition = rowposition + 1;
				FIELD_NAME_CLMN_CNTR = 0;

				// break;
			} else {
				break;

			}
		}

		if (ExpectedValue.contains(OutputparamValue)) {
			System.out.println("Expected Parameter Value : " + ExpectedValue);
			System.out.println("TestInfo : " + OutputparamValue + " " + "Parameter Found and Matching ");

		} else {
			System.out.println("Expected Parameter Value : " + ExpectedValue);
			System.out.println("TestInfo : " + OutputparamValue + " " + "Parameter Not Found ");

		}

	}

	private void funValidateResponseRxcel(String Outsheet, String Testsheet, String fValue)
			throws IOException, InterruptedException {

		String Outputparam = fValue;

		String[] Value = Outsheet.split("#");

		String Outputparam_Sheet = Value[0];
		String ResponseID = Value[1];

		String Outputfile = "WebServicesAutomation" + File.separator + "Output.xls";

		String[] TestSheetValue = Testsheet.split("#");

		String TestRow = TestSheetValue[0];
		String TestColumn = TestSheetValue[1];

		String ExpectedValue = funGetDatafromExcel(TestRow, TestColumn);

		FileInputStream afis = new FileInputStream(Outputfile);
		POIFSFileSystem apoifs = new POIFSFileSystem(afis);
		HSSFWorkbook aworkbook = new HSSFWorkbook(apoifs);
		HSSFSheet areadsheet = aworkbook.getSheet(Outputparam_Sheet);

		int FIELD_NAME_CLMN_CNTR = 0;
		int FIELD_NAME_CLMN_NO = 0;
		int rowposition = 0;
		boolean Status = false;
		String getFieldNameColumnHeader;
		String OutputparamValue = " ";
		String Rowstatus = "true";

		while (rowposition < 500) {
			getFieldNameColumnHeader = getCellValue(areadsheet, rowposition, FIELD_NAME_CLMN_CNTR);

			if (getFieldNameColumnHeader.equalsIgnoreCase(ResponseID)) {
				Rowstatus = "true";

				while (FIELD_NAME_CLMN_CNTR < 500 && Rowstatus != "false") {
					getFieldNameColumnHeader = getCellValue(areadsheet, rowposition, FIELD_NAME_CLMN_CNTR);

					if (getFieldNameColumnHeader.equalsIgnoreCase(Outputparam)) {
						FIELD_NAME_CLMN_NO = FIELD_NAME_CLMN_CNTR;

						OutputparamValue = getCellValue(areadsheet, rowposition + 1, FIELD_NAME_CLMN_NO);
						Status = true;
						break;
					}

					else {
						FIELD_NAME_CLMN_CNTR = FIELD_NAME_CLMN_CNTR + 1;
					}

				}
			} else {
				Rowstatus = "false";

			}

			if (!Status) {
				rowposition = rowposition + 1;
				FIELD_NAME_CLMN_CNTR = 0;

				// break;
			} else {
				break;

			}
		}

		if (ExpectedValue.contains(OutputparamValue)) {
			System.out.println("Expected Parameter Value : " + ExpectedValue);
			System.out.println("TestInfo : " + OutputparamValue + " " + "Parameter Found and Matching ");

		} else {
			System.out.println("Expected Parameter Value : " + ExpectedValue);
			System.out.println("TestInfo : " + OutputparamValue + " " + "Parameter Not Found ");
			failFlag = 0;
			LOG_VAR = 0;
		}

	}


private void updateQueryDatabase(String fvalue) throws Exception 
		{
			try {

				Class.forName(db_driver);
				conn = DriverManager.getConnection(db_url, db_username, db_password);
				PreparedStatement statement = conn.prepareStatement(fvalue);
				statement.executeUpdate();

			} catch (Exception e) {
				//throw new Exception("Exception for SQL Statement: " + fvalue + "\n" + e.toString());
				e.printStackTrace();

			}
		}

	public String funGetDatafromExcel(String TestDatasheet_Name, String TestDataheader)
			throws IOException, InterruptedException {

		// String Outputparam = fValue;

		String module = strModuleName;
		String TestDatafile_path = "SeleniumFramework" + File.separator + "Test_Modules" + File.separator + "" + module
				+ ".xls";

		System.out.println("Complete Path" + TestDatafile_path);

		FileInputStream afis = new FileInputStream(TestDatafile_path);
		POIFSFileSystem apoifs = new POIFSFileSystem(afis);
		HSSFWorkbook aworkbook = new HSSFWorkbook(apoifs);
		HSSFSheet areadsheet = aworkbook.getSheet("Test_ALPHA");
		String ExpectedValue = "";

		int FIELD_NAME_CLMN_CNTR = 0;
		int FIELD_NAME_CLMN_NO = 0;
		int rowposition = 0;
		boolean Status = false;
		String getFieldNameColumnHeader;
		String OutputparamValue = " ";
		String Rowstatus = "true";

		while (rowposition < 500) {
			getFieldNameColumnHeader = getCellValue(areadsheet, rowposition, FIELD_NAME_CLMN_CNTR);

			if (getFieldNameColumnHeader.equalsIgnoreCase(TestDatasheet_Name)) {
				Rowstatus = "true";

				while (FIELD_NAME_CLMN_CNTR < 500 && Rowstatus != "false") {
					getFieldNameColumnHeader = getCellValue(areadsheet, rowposition, FIELD_NAME_CLMN_CNTR);

					if (getFieldNameColumnHeader.equalsIgnoreCase(TestDataheader)) {
						FIELD_NAME_CLMN_NO = FIELD_NAME_CLMN_CNTR;

						OutputparamValue = getCellValue(areadsheet, rowposition + 1, FIELD_NAME_CLMN_NO);
						Status = true;
						break;
					}

					else {
						FIELD_NAME_CLMN_CNTR = FIELD_NAME_CLMN_CNTR + 1;
					}

				}
			} else {
				Rowstatus = "false";

			}

			if (!Status) {
				rowposition = rowposition + 1;
				FIELD_NAME_CLMN_CNTR = 0;

			} else {
				break;

			}

		}
		return OutputparamValue;

	}

	private void funcOpenUrl(String feType, String objName, String fValue)
			throws InterruptedException, FileNotFoundException, IOException, JSONException {
		// TESTING NEW CODE
		if (platform.contains("Explorer")) {
			String ss = new File("lib" + File.separator + "IEDriverServer.exe").getCanonicalPath();
			System.setProperty("webdriver.ie.driver", ss);
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
			capabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);

			driver = new InternetExplorerDriver(capabilities);
		} else if (platform.equalsIgnoreCase("Firefox")) {
			String ss;
			ss = "lib" + File.separator + "geckodriver.exe";
			// System.out.println("SS: " + ss);
			System.setProperty("webdriver.gecko.driver", ss);
			// FirefoxProfile profile = new FirefoxProfile();
			// driver = new FirefoxDriver(profile);
			driver = new FirefoxDriver();
		} else if (platform.equalsIgnoreCase("Chrome")) {
			String ss;
			ss = "lib" + File.separator + "chromedriver.exe";
			System.out.println("SS: " + ss);
			System.setProperty("webdriver.chrome.driver", ss);
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, ss);
	/*		ChromeDriverService service = ChromeDriverService.createDefaultService();
	        ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			options.addArguments("--start-maximized");
			options.addArguments("--disable-extensions");*/
			driver = new ChromeDriver();
			//driver = new ChromeDriver();
			
		}
		if (platform.equalsIgnoreCase("sauce")) {
			try {

				//				For using optum shared sauce tunnel
				cap.setCapability("parent-tunnel", "sauce_admin");
				cap.setCapability("tunnelIdentifier", "OptumSharedTunnel-Stg");

				//				This sets the timeout if no input
				cap.setCapability("maxDuration", 3600);
				//		        capabilities.setCapability("commandTimeout", 300);
				//		        capabilities.setCapability("idleTimeout", 300);

				//		        Way to increase performance
				cap.setCapability("recordVideo", true);
				cap.setCapability("videoUploadOnPass", true);
				cap.setCapability("recordScreenshots", true);
				cap.setCapability("recordLogs", true);
				cap.setCapability("Job Visibility", "public");

				r = new SauceREST(SAUCE_USERNAME, SAUCE_ACCESS_KEY);
				String tunnels = r.getTunnels();
				System.out.println("TUNNEL INFO: " + tunnels.toString());
				String jsonResponse = r.getConcurrency();
				JSONObject job = new JSONObject(jsonResponse);
				// job.getJSONObject("concurrency").getJSONObject("nkuma111").getJSONObject(SAUCE_USER).get("overall");
				int waitTime = 1;
				while (Integer.parseInt(job.getJSONObject("concurrency").getJSONObject(SAUCE_USERNAME)
						.getJSONObject("remaining").get("overall").toString()) == 0 && waitTime <= 60) {
					System.out.println(name.getMethodName() + "***********WAITING FOR SESSION TO BE AVAILABLE ");
					Thread.sleep(2000);
					waitTime++;
				}
			} catch (Exception e) {
				System.out.println("");
			}

			if (System.getenv("JOB_NAME") == null)
				cap.setCapability("build", applicationName + "_Automation");
			else
				cap.setCapability("build", System.getenv("JOB_NAME") + "__" + System.getenv("BUILD_NUMBER"));
			cap.setCapability("name", strModuleName + "_" + testName);
			driver = new RemoteWebDriver(
					new URL("http://" + SAUCE_USERNAME + ":" + SAUCE_ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub"), cap);
			sauceJobId = ((RemoteWebDriver) driver).getSessionId().toString();
			System.out.println(sauceSession.replace("SESSION", sauceJobId));
			sauceSessionLink = "https://saucelabs.com/beta/tests/"
					+ sauceJobId;
		}

		// END OF TESTING NEW CODE
		driver.get(fValue_tmp);
		driver.manage().window().maximize();
		//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 120);

		// MODIFIED FOR MAHIX
	/*	if (MAHIX_UserEmailId.isEmpty()) {
			MAHIX_UserId = "erin" + Math.random();
			MAHIX_UserPassword = "Test123#";
			MAHIX_UserEmailId = MAHIX_UserId + "@sharklasers.com";
		}
*/
		// END OF MODIFICATION FOR MAHIX

		failFlag = 1;
	}

	private void funCloseAddEditAlerts(String feType, String objName, String fValue) {

		try {
			List<WebElement> Close = driver.findElements(By.xpath(objName));
			System.out.println("TestInfo:Alert Count" + Close.size());
			for (WebElement CloseButton : Close) {
				try {
					CloseButton.click();
				} catch (Exception e) {
					System.out.println("TestInfo : Looping to close the Alert");
				}
			}

		} catch (Exception e) {
			System.out.println("TestError : Alert not Shown on the Page");
		}

	}

	private void funOptionalClick(String feType, String objName, String fValue) {
		try{
		WebElement element;
		Thread.sleep(8000);
		try {
			element = funcFindElement(feType, objName);

		} catch (Exception e) {
			element = null;
		}

		if (element != null) {
			System.out.println("TestInfo : Peforming Click Operation");
			funcFindElement(feType, objName).click();
		} else {
			System.out.println("TestInfo : Element not Shown on the Page");

		}
		}catch(Exception e){
			
		}

	}

	private void funverifyPageSource(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = driver.findElement(By.id("PWGadgetBIfr"));
		driver.switchTo().frame(element);
		List<WebElement> Buttons = driver.findElements(By.xpath("//img[contains(text(),'Select')]"));
		System.out.println("Size" + Buttons.size());
		for (WebElement Name : Buttons) {

			System.out.println(Name.getText());
		}
		String SourceCode = driver.getPageSource();
		System.out.println(SourceCode);
		if (SourceCode.contains(fValue)) {

			System.out.println("TestInfo: String Present in Source code");
			failFlag = 1;
		} else {

			System.out.println("TestError: String Present in Source code");
			failFlag = 0;
		}

	}

	private void fungetPreviousDate(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -18);
		java.util.Date result = cal.getTime();
		System.out.println(dateFormat.format(result));
		String DateValue = dateFormat.format(result);
		element.sendKeys(DateValue);
	}

	private void JSfuncClick(String fetype, String objName) throws Exception {
		// WebElement element;
		// if(objName.equals("//input[@value='No']")){
		// System.out.println("");
		// }
		// element = funcFindElement(fetype, objName);
		// JavascriptExecutor executor = (JavascriptExecutor) driver;
		// executor.executeScript("arguments[0].click();", element);
		//// Thread.sleep(Longsynctime);
		List<WebElement> es = funcFindElements(fetype, objName);
		System.out.println("Found " + es.size() + " elements");
		if (es.size() < 1)
			throw new Exception("No Element found with " + fetype + " = " + objName);
		for (WebElement ele : funcFindElements(fetype, objName)) {
			if (ele.isEnabled() && ele.isDisplayed()) {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", ele);
				System.out.println("Clicked the visible enabled out from the list");
				break;
			}
		}
		;

	}

	private void funcAngJSClick(String fetype, String objName, String fValue) throws InterruptedException, IOException {
		WebElement element;
		element = funcFindElement(fetype, objName);
		// boolean pageFlags[] = checkIfAngularJsPageLoad();
		if (checkIfAngularJsPageLoad()) {
			element.click();
		} else {
			throw new NoSuchElementException("Element- " + objName + " is not loaded still to perform click operation");
		}
	}

	private void funAngJsVerifyLink(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		if (checkIfAngularJsPageLoad()) {
			if (element.isDisplayed()) {
				System.out.println("TestInfo: Link is Displayed on Screen");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				System.out.println("TestError: Link is Not Displayed on Screen");
				failFlag = 0;
			}
		} else {
			throw new NoSuchElementException(
					"Element- " + objName + " is not loaded still to peform verifylink operation");
		}
	}

	private void funAngJsVerifyTextpresent(String feType, String objName, String fValue) throws InterruptedException {
		String validator_gbl;
		@SuppressWarnings("deprecation")
		boolean Flag = driver.getPageSource().contains(fValue);// selenium.isTextPresent(fValue);
		validate = funcFindElement(feType, objName);
		if (checkIfAngularJsPageLoad()) {
			if (Flag) {
				System.out.println("TestInfo : Text present on Screen");
				validator_gbl = validate.getText();
				System.out.println(validator_gbl);
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				System.out.println("TestError : Text not present on Screen");
				validator_gbl = validate.getText();
				failFlag = 0;
				// LOG_VAR= 0;
				System.out.println(validator_gbl);
			}
		} else {
			throw new NoSuchElementException(
					"Element- " + objName + " is not loaded still to peform verifyTextPresent operation");
		}

	}

	private void funcAngJSInput(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		if (checkIfAngularJsPageLoad()) {
			element.clear();
			element.sendKeys(fValue);
		} else {
			throw new NoSuchElementException("Element- " + objName + " is not loaded still to perform input operation");
		}

	}

	private void funcAngVerifyTextInput(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		if (checkIfAngularJsPageLoad()) {
			String temp = element.getAttribute("value");

			if (temp.equals(fValue)) {
				System.out.println("Correct Text is displayed in textbox");
				System.out.println(temp);
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				System.out.println("TestError : Text not present in textbox");

				failFlag = 0;
				LOG_VAR = 0;
				System.out.println(temp);
			}
		} else {
			throw new NoSuchElementException("Element- " + objName + " is not loaded still to perform input operation");
		}

	}

	private boolean funcAngJSVerfiyElemExists(String fetype, String objName) throws IOException, InterruptedException {
		WebElement element;
		element = funcFindElement(fetype, objName);
		if (checkIfAngularJsPageLoad()) {
			if (!(element.equals(null)) || (element.isEnabled() && element.isDisplayed())) {
				System.out.println("TestInfo : Element Exists on Screen");
				failFlag = 1;
				LOG_VAR = 1;
				return true;
			} else {
				failFlag = 0;
				LOG_VAR = 0;
				System.out.println("TestError : Element not Exists on Screen");
				return false;
			}
		} else {
			throw new NoSuchElementException(
					"Element- " + objName + " is not loaded still to perform verifyelementexists operation");
		}

	}

	/* Method to select based on Value,Index and Text */
	private void funcAngJSSelectData(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		Select select = new Select(element);
		String[] Value = fValue.split("#");
		if (checkIfAngularJsPageLoad()) {
			if (Value[0].equalsIgnoreCase("Index")) {
				int Num = Integer.parseInt(Value[1]);
				select.selectByIndex(Num);
			} else if (Value[0].equalsIgnoreCase("Value")) {
				select.selectByValue(Value[1]);
			} else {
				element.sendKeys(fValue);
				if(!select.getFirstSelectedOption().getText().equals(fValue))select.selectByVisibleText(fValue);
				System.out.println("Selected "+select.getFirstSelectedOption().getText());
			}
		} else {
			throw new NoSuchElementException(
					"Element- " + objName + " is not loaded still to perform selectdata operation");
		}
	}

	public boolean checkIfAngularJsPageLoad() throws InterruptedException {
		// long sleepTime = 10000;
		int noOfIterations = 10;
		int iterationCount = 0;
		// boolean Pageloadflag[];
		boolean PageLoad = false;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		while (iterationCount < noOfIterations) {
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				System.out.println("Page Is loaded.");
				System.out.println("iteration count is -" + iterationCount);
				// return true;
				// Pageloadflag[0 ]= true;
				PageLoad = true;
				break;
			} else { // Pageloadflag[0 ]= false;
				Thread.sleep(1000);
				iterationCount++;
			}
		}
		// below code if we get hook(element), which gets enabled/disabled post
		// internal service request status for individual components refresh
		/*
		 * if(hook){ Pageloadflag[1]= true; }else{ Pageloadflag[1]= false; }
		 * return Pageloadflag;
		 */
		return PageLoad;

	}

	private void funTableRowCount(String feType, String objName, String fValue) throws InterruptedException {
		List<WebElement> element = driver.findElements(By.xpath(objName));
		int ACount = element.size();
		System.out.println("Actual Count :" + ACount);
		int ECount = Integer.parseInt(fValue);
		if (ACount == ECount) {
			System.out.println("TestInfo:" + ACount + " " + "Rows Present on Page");
			failFlag = 1;
		} else {
			failFlag = 0;
			System.out.println("TestError:" + ECount + " " + "Rows Present on Page");
		}

	}

	private void funClosePDF() throws InterruptedException, IOException {
		/*
		 * if(BrowserName.equalsIgnoreCase("Internet Explorer")) {
		 * Thread.sleep(synctime); Runtime.getRuntime().exec(
		 * "C:\\CBT_Selenium_Framework\\lib\\HelperFiles\\HandleAPDF.exe");
		 * }else if (BrowserName.equalsIgnoreCase("Firefox")||BrowserName.
		 * equalsIgnoreCase("Chrome")) { for(String win
		 * :driver.getWindowHandles()) {
		 * System.out.println(driver.switchTo().window(win).getTitle()); String
		 * title = driver.switchTo().window(win).getTitle(); Thread.sleep(5000);
		 * if (title.contains(".pdf")) { driver.close(); for(String win1
		 * :driver.getWindowHandles()) { Thread.sleep(5000);
		 * System.out.println(driver.switchTo().window(win1).getTitle());
		 * driver.switchTo().window(win1); Thread.sleep(5000); } } } }
		 */

	}

	private void funBrowserclose() throws InterruptedException, FileNotFoundException, IOException, JSONException {
		driver.close();
		System.out.println("***********************Driver Ended for : " + Thread.currentThread().getName() + ": "
				+ name.getMethodName());
		if (platform.contains("Explorer")) {
			Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
		}
		if (platform.equalsIgnoreCase("sauce")) {
			driver.quit();
		}
	}

	// private void funBrowserOpenURL() throws InterruptedException,
	// FileNotFoundException, IOException, JSONException {
	//
	// }

	private void funSwitchWin(String feType, String objName, String fValue) throws InterruptedException {
		// driver.switchTo().defaultContent();
		Thread.sleep(1000);
		try {
			for (String Handle : driver.getWindowHandles())// 2
			{
				System.out.println(driver.switchTo().window(Handle).getTitle());
				String Title = driver.switchTo().window(Handle).getTitle();
				if (Title.equals(fValue)) {
					driver.switchTo().window(Handle);
					System.out.println("TestInfo : Successfully Switched to Window!");
					break;

				}
			}

		} catch (Exception e) {
			// TODO: handle exception

			/*
			 * List<WebElement> ele = driver.findElements(By.xpath("abc"));
			 * ele.size()
			 */
		}
	}

	// funSwitchframe

	private void funSwitchframe(String feType, String objName, String fValue) throws Exception {
		try {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(fValue));
		} catch (Exception e) {
			throw new Exception("Frame: " + fValue + " not available to select. Is it nested?");
		}
	}

	private void funSwitchWinLatest(String feType, String objName, String fValue) throws InterruptedException {
		try {
			//////////////////////////// Shubhank
			driver.switchTo()
			.window(new ArrayList<String>(driver.getWindowHandles()).get(driver.getWindowHandles().size() - 1));
			///////////////////////// Shubhank code end

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error: " + e.toString());

			/*
			 * List<WebElement> ele = driver.findElements(By.xpath("abc"));
			 * ele.size()
			 */
		}
	}

	private void funSwitchframeDef(String feType, String objName, String fValue) throws InterruptedException {

		try {
			driver.switchTo().defaultContent();

		} catch (Exception e) {
		}
	}

	/**
	 * @param feType
	 *            = xpath/id/name/link/linkText/CSS
	 * @param objName
	 *            = String
	 * @param fValue
	 *            = Attribure of element
	 * @description: This will store attribute innertext/Value or text to the
	 *               global variable
	 * @throws InterruptedException
	 */
	private void funGetprop(String feType, String objName, String fValue) throws InterruptedException {
		String Storevalue = "";
		String Attribute[] = fValue.split("#");
		Attribute[0] = Attribute[0].toLowerCase();
		Storevalue = getEleProp(feType, objName, Attribute[0]);

		dataholder.put(Attribute[1], Storevalue);
		System.out.println("TestInfo: Expected Value " + Storevalue);

	}

	private void funChkprop(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Attribute = fValue.trim();
		String ExpectedPropValue = "";

		try {

			if (Attribute != null) {

				if (element.getTagName().equalsIgnoreCase("input")) {
					ExpectedPropValue = element.getAttribute("value").trim();

				} else {
					ExpectedPropValue = element.getAttribute("innertext");
					if (ExpectedPropValue == null) {
						ExpectedPropValue = element.getText().trim();
						System.out.println("TestInfo: Expected Value " + ExpectedPropValue);
					}
					// String ExpectedPropValue = element.getText().trim();
					System.out.println("TestInfo: Expected Value " + ExpectedPropValue);
				}

				if (!(inputSheet.isEmpty())) {

					if (ExpectedPropValue.equalsIgnoreCase(fValue)) {
						System.out.println("TestInfo: Property Values are Matching ");
						failFlag = 1;

					} else

					{
						System.out.println("TestError: Property Values are Not Matching ");
						System.out.println("Expected Value" + ExpectedPropValue);
						System.out.println("Actual Value" + fValue);
						failFlag = 0;
						LOG_VAR = 0;
					}

				}

			} else {
				String ActualPropValue = dataholder.get(ACTUALPARA);
				if (ExpectedPropValue.equalsIgnoreCase(ActualPropValue)) {
					System.out.println("TestInfo: Property Values are Matching ");
					failFlag = 1;

				} else

				{
					System.out.println("TestError: Property Values are Not Matching ");
					System.out.println("Expected Value" + ExpectedPropValue);
					System.out.println("Actual Value" + ActualPropValue);
					failFlag = 0;
					LOG_VAR = 0;
				}

			}

		} catch (Exception e) {
			failFlag = 0;
			System.out.println("TestError: Error in Property Match !!");
			LOG_VAR = 0;
		}

	}

	private void funKeyEvents(String feType, String objName, String fValue) throws InterruptedException {
		if (fValue.equalsIgnoreCase("CTRL+END")) {
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_END);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_CONTROL);
			robo.keyRelease(KeyEvent.VK_END);
		} else if (fValue.equalsIgnoreCase("CTRL+HOME")) {
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_HOME);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_CONTROL);
			robo.keyRelease(KeyEvent.VK_HOME);
		} else if (fValue.equalsIgnoreCase("ENTER")) {
			robo.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_ENTER);
		}

		else if (fValue.equalsIgnoreCase("TAB")) {
			robo.keyPress(KeyEvent.VK_TAB);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_TAB);

		}

		else if (fValue.equalsIgnoreCase("CTRLDELETE")) {
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_A);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_CONTROL);
			robo.keyRelease(KeyEvent.VK_A);
			robo.keyPress(KeyEvent.VK_DELETE);
			robo.keyRelease(KeyEvent.VK_DELETE);

		}

		else if (fValue.equalsIgnoreCase("ARROWDOWN")) {
			robo.keyPress(KeyEvent.VK_KP_DOWN);
			robo.keyRelease(KeyEvent.VK_KP_DOWN);
			Thread.sleep(synctime);
		}
	}

	public void actionsClick(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		wait.until(ExpectedConditions.visibilityOf(element));// Explicit Wait
		// for Element
		Actions builder = new Actions(driver);
		builder.moveToElement(element).build().perform();
		element.click();
	}

	public void funmouseHoverClick(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		Locatable hoverItem = (Locatable) element;
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		mouse.mouseMove(hoverItem.getCoordinates());
		element.click();

	}

	public void funmouseHover(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		Locatable hoverItem = (Locatable) element;
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		mouse.mouseMove(hoverItem.getCoordinates());
		// element.click();

	}

	private void funMouseHoverJs(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		String script = "if(document.createEvent) " + "{ var evObj = document.createEvent('MouseEvents'); "
				+ "evObj.initEvent('mouseover', true, false); " + "arguments[0].dispatchEvent(evObj); } "
				+ "else if(document.createEventObject) " + "{ arguments[0].fireEvent('onmouseover'); " + "}";

		String script1 = "var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
				+ "arguments[0].dispatchEvent(evObj);";

		jsExecutor.executeScript(script, element);

		element.click();

	}

	private void funVerifyFals(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		try {
			element = driver.findElement(By.xpath(objName));

		} catch (Exception e) {
			element = null;
		}

		if (element != null) {
			System.out.println("TestError : Element Exists on Screen");
			failFlag = 0;
		} else {
			System.out.println("TestInfo : Element not Exists on Screen");
			failFlag = 1;
		}

	}

	private void funClear(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		element.clear();
	}

	private void funHandleAlert(String feType, String fValue) throws InterruptedException {
		if (fValue.equalsIgnoreCase("OK")) {
			driver.switchTo().alert().accept();
			Thread.sleep(synctime);
		} else {
			driver.switchTo().alert().dismiss();

		}
	}

	private void funAlertText(String feType, String objName, String fValue)

	{
		String AAlert_Text = fValue;
		String EAlert_Text = driver.switchTo().alert().getText();
		System.out.println("AlertInfo: Alert Text is " + " " + EAlert_Text);

		if (AAlert_Text.equals(EAlert_Text)) {
			System.out.println("TestInfo:Alert Text is Matching!");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			System.out.println("TestError:Alert Text Not Matching!");
			failFlag = 0;
			// LOG_VAR= 0;
		}
	}

	private void funGenrtNumbr(String feType, String objName, String fValue) throws InterruptedException, AWTException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String[] Number = fValue.split(",");
		int min = Integer.parseInt(Number[0]);
		int max = Integer.parseInt(Number[1]);
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		System.out.println(randomNum);
		String value = Integer.toString(randomNum);
		System.out.println(value);

		Robot robo = new Robot();
		robo.keyPress(KeyEvent.VK_CONTROL);
		robo.keyPress(KeyEvent.VK_END);
		Thread.sleep(synctime);
		element.sendKeys(value);

		robo.keyRelease(KeyEvent.VK_CONTROL);
		robo.keyRelease(KeyEvent.VK_END);
	}

	private void funMultiLinks(String feType, String objName, String fValue) {
		List<WebElement> Links = driver.findElements(By.linkText(objName));
		for (WebElement ele : Links) {
			System.out.println(ele.getText());
		}
		int Act_LinksCount = Links.size();
		int Exp_LinksCount = Integer.parseInt(fValue);
		System.out.println("Actual Links:" + Act_LinksCount + "Expected Links: " + Exp_LinksCount);
		if (Act_LinksCount == Exp_LinksCount) {
			System.out.println("TestInfo: Links count is Matching.");
			failFlag = 1;
		} else {
			System.out.println("TestError: Links Count Not Matching!!");
			failFlag = 0;
		}

	}

	private void funLink(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		if (element.isDisplayed()) {
			System.out.println("TestInfo: Link is Displayed on Screen");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			System.out.println("TestError: Link is Not Displayed on Screen");
			failFlag = 0;
		}
	}

	private void funElementProp(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String ActualVal = dataholder.get(fValue);
		String ExpectedVal = element.getAttribute("value");
		if (ActualVal.equals(ExpectedVal)) {
			System.out.println("TestInfo : Field Value is Matching !");
			failFlag = 1;
			LOG_VAR = 1;

		} else {
			failFlag = 0;
			System.out.println("TestError : Field Value Not Matching !");
		}

	}

	private void funSendValue(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String SendValue = dataholder.get(fValue);
		element.sendKeys(SendValue);
		System.out.println("TestInfo : Send Value is: " + " " + SendValue);
	}

      private void funHoldvalue(String feType, String objName, String fValue) throws InterruptedException {
                WebElement element;
                int xy=1;
                element = funcFindElement(feType, objName);
                String Holdvalue = getText(element);
                System.out.println("TestInfo : Hold Value is: " + " " + Holdvalue);
                dataholder.put(fValue, Holdvalue); // Store in HashTable in Key/Value //
                
                try{
                        while( xy < 12 && Holdvalue=="" ) {
                        	Thread.sleep(500);
                            xy++;
                            Holdvalue = getText(element);
                         }
	                        currentDataCell.setCellValue(Holdvalue);
	                        fieldValue = Holdvalue;
                        } catch(NullPointerException e){ 
                        System.err.println("if you need to write, please mention 'Random' in that cell"); 
                        }
                
        }
      
      private void funGenerateAndHoldSSN(String feType, String objName, String fValue) throws InterruptedException {     
               String ssn = "2" + (new Random().nextInt(9999999) + 10000000);     
    	       System.out.println("TestInfo : SSN NO to be used is : " + " " + ssn);     
    	       dataholder.put(fValue, ssn); // Store in HashTable in Key/Value //   
    	       Thread.sleep(2000);
    	       currentDataCell.setCellValue(ssn);     
    	            fieldValue = ssn;     
          }  

	private void verifyMatchesText(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Holdvalue = getText(element);
		System.out.println("TestInfo : Hold Value is: " + " " + Holdvalue);
		// dataholder.put(fValue, Holdvalue); // Store in HashTable in Key/Value
		// format.
		if (dataholder.containsKey(fValue)) {
			if (Holdvalue.equalsIgnoreCase(dataholder.get(fValue))) {
				System.out.println("Element text matches with UI");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				failFlag = 0;
				// LOG_VAR=0;
				System.out.println("TestError :Element text doesn't matches with UI");

			}
		} else {
			if (Holdvalue.equalsIgnoreCase(fValue)) {
				System.out.println("Element text matches with UI");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				failFlag = 0;
				// LOG_VAR=0;
				System.out.println("TestError :Element text doesn't matches with UI");

			}
		}
	}

	private void securityQuestions(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String secques = getText(element);
		System.out.println("TestInfo : Hold Value is: " + " " + secques);

		if (secques.equalsIgnoreCase("What was your first phone number?")) {
			driver.findElementById("challengeQuestionList[0].userAnswer").sendKeys("1234");
		}

		if (secques.equalsIgnoreCase("What is your best friend's name?")) {
			driver.findElementById("challengeQuestionList[0].userAnswer").sendKeys("optum");
		}
		if (secques.equalsIgnoreCase("What is your favorite color?")) {
			driver.findElementById("challengeQuestionList[0].userAnswer").sendKeys("blue");

		}
	}

	private void funcVerifyTextContains(String feType, String objName, String fValue) throws Exception {
		WebElement element;

		element = funcFindElement(feType, objName);
		String TexttoVerify = getText(element);
		for(int t=1;t<=10;t++)
		{	
		if (((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete"))
		{ 	TexttoVerify = getText(element); 
		break;	}
		else { Thread.sleep(3000);	}
		}
		System.out.println(fValue+"fValue");
		System.out.println(TexttoVerify+"TexttoVerify");

		if(TexttoVerify.contains(fValue))
		{
			System.out.println("Value is present on the screen");
			failFlag = 1;
			LOG_VAR = 1;

		}
		else
		{
			System.out.println("Value is not present on the screen");	
			failFlag = 0;
			throw new Exception("Value is not present on the screen");
		}

	}
	
	
	private void funcVerifyTextNotContains(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;

		element = funcFindElement(feType, objName);
		String TexttoVerify = getText(element);
		System.out.println(fValue+"fValue");
		System.out.println(TexttoVerify+"TexttoVerify");

		if(TexttoVerify.contains(fValue))
		{
			System.out.println("Value is present on the screen");
			failFlag = 0;
		}
		else
		{
			System.out.println("Value is not present on the screen");	
			failFlag = 1;
			LOG_VAR = 1;
		}

	}
	
	
	private void funcVerifyDropDownOptionsCount (String feType, String objName, String fValue) throws InterruptedException {
		int Actualddcount;
		WebElement element;

		element = funcFindElement(feType, objName);
		Select dd= new Select(element);
		Actualddcount=dd.getOptions().size();

		if(Actualddcount == Integer.parseInt(fValue))
		{
			System.out.println("Count Matched");
			failFlag = 0;
		}
		else
		{
			System.out.println("Count not Matched");	
			failFlag = 1;
			LOG_VAR = 1;
		}

	}
	private void funTextpresent(String feType, String objName, String fValue) throws InterruptedException {
		String validator_gbl;
		boolean Flag = driver.getPageSource().contains(fValue);// selenium.isTextPresent(fValue);
		validate = funcFindElement(feType, objName);
		if (Flag) {
			System.out.println("TestInfo : Text present on Screen");
			validator_gbl = validate.getText();
			System.out.println(validator_gbl);
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			System.out.println("TestError : Text not present on Screen");
			validator_gbl = validate.getText();
			failFlag = 0;
			// LOG_VAR= 0;
			System.out.println(validator_gbl);
		}

	}

	private void funTextNotpresent(String feType, String objName, String fValue) throws InterruptedException {
		String validator_gbl;
		boolean Flag = driver.getPageSource().contains(fValue);// selenium.isTextPresent(fValue);
		if (Flag) {
			System.out.println("TestError : Text not present on Screen");
			failFlag = 0;
			// LOG_VAR= 0;
			
		} else {
			System.out.println("TestInfo : Text present on Screen");
			failFlag = 1;
			LOG_VAR = 1;
		}

	}


	/** Function to click the check box based on Index **/
	private void funcCheckByIndex(String feType, String objName, String fValue) throws InterruptedException {
		List<WebElement> eles = driver.findElements(By.xpath(objName));
		int Index = Integer.parseInt(fValue);
		int counter = 0;
		for (WebElement ele : eles) {
			if (counter == Index) {
				ele.click();
				break;
			}
			counter++;
		}
	}

	/** Function to click the check box based on Index **/
	private void funcUncheckAll(String feType, String objName, String fValue) throws InterruptedException {
		List<WebElement> eles = driver.findElements(By.xpath(objName));
		for (WebElement ele : eles) {
			ele.click();
		}
		int Index = Integer.parseInt(fValue);
		int counter = 0;
		for (WebElement ele : eles) {
			if (counter == Index) {
				ele.click();
				break;
			}
			counter++;
		}
	}

	private void funcVerifyWin(String feType, String objName, String fValue) {
		for (String windowHandle : driver.getWindowHandles()) {
			String Actual_Title = driver.switchTo().window(windowHandle).getTitle();
			if (Actual_Title.equalsIgnoreCase(fValue)) {
				System.out.println("TestInfo : Window Exists.");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				failFlag = 0;
				// LOG_VAR=0;
				System.out.println("TestError : Window not Exists!");

			}
		}
	}

	/** Function to Close Window By Title **/

	private void funcCloseWin(String feType, String objName, String fValue) {
		Boolean Winflag = false;
		for (String windowHandle : driver.getWindowHandles()) {
			String Actual_Title = driver.switchTo().window(windowHandle).getTitle();
			System.out.println(Actual_Title);
			if (Actual_Title.equalsIgnoreCase(fValue)) {
				driver.switchTo().window(windowHandle).close();
				System.out.println("TestInfo : Window was Closed.");
				Winflag = true;
				failFlag = 1;
				LOG_VAR = 1;
				break;
			}
		}
		if (!Winflag) {
			failFlag = 0;
			System.out.println("TestError: Unable to Close Window or not Exists");
		}
	}

	private void funcSetcheck(String feType, String objName, String fValue) throws Exception {
		WebElement element;
		element = funcFindElement(feType, objName);
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(element));
		//		pageSource = driver.getPageSource();
		highLight(element);
		if (fValue.equalsIgnoreCase("check")) {
			if (!(element.isSelected()))element.sendKeys(Keys.SPACE);
			//element.click();
		} else if (fValue.equalsIgnoreCase("uncheck")) {
			if (element.isSelected())element.sendKeys(Keys.SPACE);
			//element.click();
		}
	}

	private void funVerifyfieldValue(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Actual_Value = element.getAttribute("value");
		if (Actual_Value.equals(fValue)) {
			System.out.println("TestInfo: Filed value is Matching");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			failFlag = 0;
			System.out.println("TestError: Filed value  not Matching!");

		}

	}

	private void funClearthnType(String feType, String objName, String fValue) throws Exception {
		WebElement element;
		element = funcFindElement(feType, objName);
		element.clear();
		if(dataholder.containsKey(fValue)) fValue = dataholder.get(fValue).toString();
		element.sendKeys(fValue);
	}

	private void funcVerifyURL(String fValue) {
		String URL = "";
		URL = driver.getCurrentUrl();
		if (fValue.equals(URL)) {
			System.out.println("TestInfo:URL is Matching");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			System.out.println("TestError:URL is not Matching");
			// LOG_VAR= 0;
			failFlag = 0;
			// testFlag="n";
		}
	}

	/* Method to select based on Value,Index and Text */
       private void funcSelectDataByVal(String feType, String objName, String fValue) throws Exception {
                try {
        		WebElement element = funcFindElement(feType, objName);
                highLight(element);
                funcAngJSSelectData(feType, objName, fValue);
		        } catch (StaleElementReferenceException e) {
		        	funcSelectDataByVal(feType, objName, fValue);
		        }	
        }

	private void funcSelectDataByInd(String feType, String objName, String fValue) throws Exception {
		WebElement element = funcFindElement(feType, objName);
		highLight(element);
		//		 pageSource = driver.getPageSource();
		// try {
		Select select = new Select(element);
		int Num = Integer.parseInt(fValue);
		select.selectByIndex(Num);
	}

	private void funcInput(String fetype, String objName, String fValue) throws Exception {
		WebElement element;
		element = (new WebDriverWait(driver, 40)).until(ExpectedConditions.visibilityOf(funcFindElement(fetype, objName)));
		highLight(element);
		if (dataholder.containsKey(fValue)) {
			fValue = dataholder.get(fValue);
		}
		element.sendKeys(fValue);
	}

	private void highLight(WebElement element) throws Exception {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", element);
		} catch (Exception e) {
			if (e.toString().contains("arguments[0] is null")) {
				throw new Exception("No Such Element");
			}
		}
	}

	private void funcClick(String fetype, String objName, String fValue) throws Exception {
            try {
                   for (int i=0; i<15; i++){ 
                            try {
                             Thread.sleep(2000);
                             }catch (InterruptedException e) {} 
                            //To check page ready state.
                            if (((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete")){ 
                             break; 
                            }   
                           }
                   WebElement ele = (new WebDriverWait(driver, 30))
                                .until(ExpectedConditions.elementToBeClickable(funcFindElement(fetype, objName)));
                   String type = ele.getAttribute("type");
                   if (type != null && (type.equalsIgnoreCase("checkbox") || type.equalsIgnoreCase("radio") || objName.contains("Import UDA")))
                         ele.sendKeys(Keys.SPACE);
                   else {
                         JavascriptExecutor executor = (JavascriptExecutor) driver;
                         executor.executeScript("arguments[0].click();", (new WebDriverWait(driver, 30))
                                       .until(ExpectedConditions.elementToBeClickable(funcFindElement(fetype, objName))));
                   }

            } catch (StaleElementReferenceException e) {
                   funcClick(fetype, objName, fValue);
            }
     }


	//	private WebElement getVisibleEnabledElement(String fetype, String objName, String fValue) throws Exception {
	//		WebElement element = null;
	//		if (!fValue.isEmpty()) {
	//			DVARIABLE = fValue;
	//		}
	//		// element = funcFindElement(fetype, objName);
	//		List<WebElement> es = funcFindElements(fetype, objName);
	//		if (es == null) {
	//			System.err.println("No Element found with " + fetype + " = " + objName);
	//			throw new Exception("No Element found with " + fetype + " = " + objName);
	//		} else {
	//			System.out.println("Found " + es.size() + " elements");
	//			// System.out.println("Element Found");
	//			for (WebElement ele : es) {
	//				System.out.println("Element Enabled: " + ele.isEnabled());
	//				System.out.println("Element Displayed: " + ele.isDisplayed());
	//				if (ele.isEnabled() && ele.isDisplayed()) {
	//					element = ele;
	//					System.out.println("Visible and Displayed element picked");
	//					break;
	//				}
	//			}
	//		}
	//		return element;
	//	}

	/**
	 * @param pageSource
	 * @return true if page source changed
	 */
	private boolean isPageChanged(final String pageSource) {
		final String msg = "Waiting for page souce change";
		System.out.println(msg);
		boolean flag = false;
		try {
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					// TODO Auto-generated method stub
					if (pageSource.equals(driver.getPageSource())) {
						System.out.print(".");
						return false;
					} else {
						return true;
					}
				}
			});
			flag = true;

			int i = 1;
			while(((JavascriptExecutor)driver).executeScript("return (document.readyState == 'complete')").equals(false)){
				Thread.sleep(200);
				i = i+1;
				if(i>50)break;
			}
			//			System.out.println("/******************* END of Page source changed****************/");
			while (!(driver.findElement(By.tagName("div")).isDisplayed()) && localCounter < 5) {
				System.out.println("STILL A BLANK PAGE...!!!");
				localCounter = localCounter + 1;
				isPageChanged(driver.getPageSource());
			}
			localCounter = 0;
		} catch (Exception e) {
			localCounter = 0;
			flag = false;
		}
		return flag;
	}

	private boolean funcVerify(String fetype, String objName) throws IOException, InterruptedException {
		WebElement element;
		element = funcFindElement(fetype, objName);
		if (!(element.equals(null)) || (element.isEnabled() && element.isDisplayed())) {
			System.out.println("TestInfo : Element Exists on Screen");
			failFlag = 1;
			LOG_VAR = 1;
			return true;
		} else {
			failFlag = 0;
			LOG_VAR = 0;
			System.out.println("TestError : Element not Exists on Screen");
			return false;
		}
	}

	 private boolean funcVerify(String fetype, String objName, String fValue) throws Exception {
                    for (int i=0; i<100; i++){ 
                        try {
                         Thread.sleep(700);
                         }catch (InterruptedException e) {} 
                        //To check page ready state.
                        if (((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete"))
                        { 
                         break; 
                        }   
                       }
                        WebElement element;
                        // if (!fValue.isEmpty()) {
                        // objName = objName.replace("VARIABLE", fValue);
                        // }

                        element = 
                                        //                                        getVisibleEnabledElement(fetype, objName, fValue);
                                        funcFindElement(fetype, objName);
                        if (!(element.equals(null)) || (element.isEnabled() && element.isDisplayed())) {
                                System.out.println("TestInfo : Element Exists on Screen");
                                failFlag = 1;
                                LOG_VAR = 1;
                                return true;
                        } else {
                                failFlag = 0;
                                LOG_VAR = 0;
                                System.out.println("TestError : Element not Exists on Screen");
                                return false;
                        }
        }


	 /**
	     * To access URL from user, if not found try to append existing domain with value provided 
	      * @param feType
	     * @param objName
	     * @param fValue
	     * @throws Exception
	     */
	     private void funcNavigateTo(String feType, String objName, String fValue) throws Exception {
	           
	                try{
	                      driver.navigate().to(fValue);
	                }catch(Exception e){
	                      System.out.println("Invalid URL [" +fValue + "] Trying to append existing domain with value provided");
	                      
	                      URI url = new URI(driver.getCurrentUrl());
	                      String domain = url.getHost();
	                      if(fValue.charAt(0)=='/'){
	                           driver.navigate().to("https://"+domain+fValue);
	                      }else{
	                           throw new Exception("InValid URL [https://"+domain+fValue + "]");
	                      }
	                }
	     }

	 public static String getValueFromAppConfig(String fvalue) {
		try {

			// FileInputStream file = new FileInputStream(new
			// File(DriverClass.APPCONFIG));
			FileInputStream file = new FileInputStream(new File(""));
			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						Boolean Value = cell.getBooleanCellValue();
						if (Value.equals(fvalue)) {
							cell = cellIterator.next();
							fvalue = cell.getStringCellValue();
						}
						break;

					case Cell.CELL_TYPE_STRING:
						String SValue = cell.getStringCellValue();
						if (SValue.contains(fvalue)) {
							cell = cellIterator.next();
							fvalue = cell.getStringCellValue();
							break;
						}
						break;
					}
				}

			}
			file.close();
			FileOutputStream out = new FileOutputStream(new File(
					"SeleniumFramework" + File.separator + "Test_Templates" + File.separator + "OPTUMIDDATA.xls"));
			workbook.write(out);
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fvalue;
	}

	public static String getSettingsFromExxConfig(String strKey) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(
				"SeleniumFramework" + File.separator + "Test_Templates" + File.separator + "AppConfig.properties"));
		String strData = prop.getProperty(strKey);
		strData = strData.trim();
		return strData;
	}

	public static String getSettingsFromTemplate(String strKey) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("SeleniumFramework" + File.separator + "Test_Templates" + File.separator
				+ "Registration_Data.properties"));
		String strData = prop.getProperty(strKey);
		strData = strData.trim();
		return strData;
	}

	private WebElement returnIfElementPresent(By by) {
		WebElement ele = null;
		try {
			ele = driver.findElement(by);
		} catch (Exception e) {
			System.out.println("");// Do nothing
		} finally {
			return ele;
		}

	}

	protected WebElement funcFindElement(String elmToIdentify, String obj) throws InterruptedException {

		if (elmToIdentify.equals("") && obj.equals("")) {
			System.out.println("No such Object is found: " + fieldName + " on screen: " + screenName);
			LOG_VAR = 0;
			// testFlag="n";
			String Trace = "No such Object is found: " + fieldName + " on screen: " + screenName;
			sendLog(Trace, PREVIOUS_TEST_CASE, currTestRowPtrs,currTestRowPtr);
		} else {
			By by = null;

			if (elmToIdentify.equalsIgnoreCase("id")) {
				by = By.id(obj);
			} else if (elmToIdentify.equalsIgnoreCase("LinkText")) {
				by = By.linkText(obj);
			} else if (elmToIdentify.equalsIgnoreCase("Class")) {
				by = By.className(obj);
			} else if (elmToIdentify.equalsIgnoreCase("Classname")) {
				by = By.className(obj);	
			} else if (elmToIdentify.equalsIgnoreCase("CSS")) {
				by = By.cssSelector(obj);
				by = By.id(obj);
			} else if (elmToIdentify.equalsIgnoreCase("Name")) {
				by = By.name(obj);
			} else if (elmToIdentify.equalsIgnoreCase("PartialLinkText")) {
				by = By.partialLinkText(obj);
			} else if (elmToIdentify.equalsIgnoreCase("TagName")) {
				by = By.tagName(obj);
			} else if (elmToIdentify.equalsIgnoreCase("xpath")) {
				by = By.xpath(obj);
			} else if (elmToIdentify.equalsIgnoreCase("dynamicXpath")) {
				obj = obj.replace("VARIABLE", DVARIABLE);
				by = By.xpath(obj);
			}
			WebElement ele = null;
			try {
				ele = (new WebDriverWait(driver, 45))
						.until((ExpectedConditions.presenceOfElementLocated(by)));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
			} catch(TimeoutException e){
				if(pageLoading() || jqueryRunning())
					ele = findElementAgain(ele, by);
			} catch(StaleElementReferenceException e){
				Thread.sleep(3000);
				funcFindElement(elmToIdentify, obj);
			}
			return ele;
		}
		throw new NoSuchElementException("Timeout waiting for Object: " + obj + ", whose FieldName is: " + fieldName
				+ " on Screen: " + screenName);
	}

	protected List<WebElement> funcFindElements(String elmToIdentify, String obj) {
		// waitForJSandJQueryToLoad();
		if (elmToIdentify.equals("") && obj.equals("")) {
			System.out.println("No such Object is found: " + fieldName + " on screen: " + screenName);
			LOG_VAR = 0;
			// testFlag="n";
			String Trace = "No such Object is found: " + fieldName + " on screen: " + screenName;
			sendLog(Trace, PREVIOUS_TEST_CASE, currTestRowPtrs,currTestRowPtr);
		} else {
			By by = null;

			if (elmToIdentify.equalsIgnoreCase("id")) {
				by = By.id(obj);
			} else if (elmToIdentify.equalsIgnoreCase("LinkText")) {
				by = By.linkText(obj);
			} else if (elmToIdentify.equalsIgnoreCase("Class")) {
				by = By.className(obj);
			} else if (elmToIdentify.equalsIgnoreCase("Classname")) {
				by = By.className(obj);	
			} else if (elmToIdentify.equalsIgnoreCase("CSS")) {
				by = By.cssSelector(obj);
				by = By.id(obj);
			} else if (elmToIdentify.equalsIgnoreCase("Name")) {
				by = By.name(obj);
			} else if (elmToIdentify.equalsIgnoreCase("PartialLinkText")) {
				by = By.partialLinkText(obj);
			} else if (elmToIdentify.equalsIgnoreCase("TagName")) {
				by = By.tagName(obj);
			} else if (elmToIdentify.equalsIgnoreCase("xpath")) {
				by = By.xpath(obj);
			} else if (elmToIdentify.equalsIgnoreCase("dynamicXpath")) {
				obj = obj.replace("VARIABLE", DVARIABLE);
				by = By.xpath(obj);
			}
			List<WebElement> eles = null;
			try {
				if (
						// driver.findElements(by)
						((new WebDriverWait(driver, 45)).until(ExpectedConditions.presenceOfElementLocated(by))) != null)
					System.out.println("Element present and finding more...");
				eles = driver.findElements(by);
				if (eles == null)
					throw new Exception("No Element Found");
			} catch (Exception e) {
				boolean foundInFrame = false;
				System.out.println("Finding element in default and frames...");
				driver.switchTo().defaultContent();
				if (driver.findElements(by).size() > 0) {
					eles = driver.findElements(by);
					foundInFrame = true;
				}
				List<WebElement> frames = driver.findElements(By.tagName("iframe"));
				for (WebElement webElement : frames) {
					if (foundInFrame)
						break;
					driver.switchTo().defaultContent();
					driver.switchTo().frame(webElement);
					if (driver.findElements(by).size() > 0) {
						eles = driver.findElements(by);
						foundInFrame = true;
						break;
					}
					List<WebElement> frameslevel2 = driver.findElements(By.tagName("iframe"));
					for (WebElement webElement2 : frameslevel2) {
						driver.switchTo().defaultContent();
						driver.switchTo().frame(webElement);
						driver.switchTo().frame(webElement2);
						if (driver.findElements(by).size() > 0) {
							eles = driver.findElements(by);
							foundInFrame = true;
							break;
						}
						List<WebElement> frameslevel3 = driver.findElements(By.tagName("iframe"));
						for (WebElement webElement3 : frameslevel3) {
							driver.switchTo().defaultContent();
							driver.switchTo().frame(webElement);
							driver.switchTo().frame(webElement2);
							driver.switchTo().frame(webElement3);
							if (driver.findElements(by).size() > 0) {
								eles = driver.findElements(by);
								foundInFrame = true;
								break;
							}
						}
						if (foundInFrame)
							break;
					}
					if (foundInFrame)
						break;
				}
			}
			return eles;
		}

		throw new NoSuchElementException("Timeout waiting for Object: " + obj + ", whose FieldName is: " + fieldName
				+ " on Screen: " + screenName);
	}
	
	private String funGetExcelData(String fvalue) throws InvalidFormatException, IOException {

		FileInputStream fis = new FileInputStream(fvalue);
		Workbook wb = WorkbookFactory.create(fis);
		String sheetName = ((Path)fis).getFileName().toString();
		Sheet sh = wb.getSheet(sheetName);
		Row row = sh.getRow(0);
		Cell c = row.getCell(0);
		c.getCellTypeEnum();
		String data = c.getStringCellValue();
		data = data.toString();
		return data;
		}

	private void funGetColCount(String excelPath) throws InvalidFormatException, Exception {
		excelpath = excelPath;
		fis = new FileInputStream(excelpath);
		new WorkbookFactory();
		wb = WorkbookFactory.create(fis);
		sh = wb.getSheet("Item_UDA.xlsx");
		int getCol = sh.getRow(0).getLastCellNum();
		}


	// Verify if a file is downloaded
		public boolean funcVerifyFileDownload(String feType, String objName, String fvalue) throws Exception {
		// objName = download path
		// fvalue = File Name
		WebElement elem = funcFindElement(feType, objName);
		(new WebDriverWait(driver, 15)).until((ExpectedConditions.elementToBeClickable(elem)));
		Thread.sleep(1000);
		elem.click();
		Thread.sleep(1000);
		Path p = Paths.get(fvalue);
		String fileName = p.getFileName().toString();
		int index = fvalue.lastIndexOf("\\");
		String filePath = fvalue.substring(0, index);
		File dir = new File(filePath);
		File[] dirContents = dir.listFiles();

		for (int i = 0; i < dirContents.length; i++) {
		if (dirContents[i].getName().equals(fileName)) {
		// File has been found, it can now be deleted:
		// dirContents[i].delete();
		return true;
		}
		}
		return false;
		}


	public void funcSelectByText(String feType, String objName, String fvalue) throws InterruptedException {
		WebElement elem = funcFindElement(feType, objName);
		(new WebDriverWait(driver, 15)).until((ExpectedConditions.elementToBeClickable(elem)));
		Thread.sleep(1000);
		Boolean found = false;
		List<String> actualElementValues = new ArrayList<String>();
		List<String> listValue = Arrays.asList(fvalue.split(","));
		List<WebElement> listElement = funcFindElements(feType, objName);
		for (String value : listValue) {
		found = false;
		for (WebElement element : listElement) {
		String actualElementValue = element.getAttribute("text");
		actualElementValues.add(actualElementValue);
		if (actualElementValue.equalsIgnoreCase(value)) {
		element.click();
		found = true;
		break;
		}
		}
		}

		}
	
	public static String getSettingsFromOpenEnroll(String strKey) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(
				"SeleniumFramework" + File.separator + "Test_Templates" + File.separator + "OpenEnroll.properties"));
		String strData = prop.getProperty(strKey);
		strData = strData.trim();
		return strData;
	}

	/**
	 * Method getNumberofIterations : this method gets the number of iterations
	 * for what times looped test steps to be iterated.
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getNumberofIterations() throws IOException {// TODO: what is the
		// input to this
		// method, is
		// readScriptSheet
		String dataSheetName;
		int LOOPITERATION = 0;
		int TEMP_SR = startRow;
		int TEMP_ER = endRow;
		int loopRowCounter;
		int dataListPointer;
		String counterString, delimiter;
		HSSFSheet newSheet;

		while (TEMP_SR <= TEMP_ER) {
			dataSheetName = getCellValue(readScriptSheet, TEMP_SR, 5);
			if (!(dataSheetName.isEmpty())) {
				System.out.println("datasheetName: " + dataSheetName);
				loopRowCounter = 1;
				dataListPointer = 0;
				counterString = null;
				delimiter = null;
				// getting the number of iterations
				boolean inputFlag = true;
				// Opens the datasheet
				// newSheet=scriptWorkbook.getSheet(dataSheetName);
				newSheet = scriptWorkbook.getSheet("Test_ALPHA");
				delimiter = getCellValue(newSheet, dataListPointer, 0);
				while (!delimiter.equalsIgnoreCase("End")) {
					if (delimiter.equalsIgnoreCase(dataSheetName)) {
						break;
					} else {
						dataListPointer = dataListPointer + 1;
						delimiter = getCellValue(newSheet, dataListPointer, 0);
					}

				}
				// In while loop below, it checks for the number of data to be
				// taken.If flag is 'y' the increases the counter.
				dataListPointer = dataListPointer + 1;
				while (inputFlag) {
					counterString = getCellValue(newSheet, dataListPointer, 0);// newsheet.getRow(LoopRowCounter).getCell(0).getStringCellValue().trim();
					System.out.println("counterString: " + counterString);
					try {
						if (!counterString.equalsIgnoreCase("END_LIST")) {
							loopRowCounter = loopRowCounter + 1;
							dataListPointer++;
						} else {
							inputFlag = false;
							System.out.println("Input Data Count ends");
						}
					} catch (Exception e) {
						System.out.println(e.getLocalizedMessage());
						e.printStackTrace();
					}
				}

				if (LOOPITERATION < loopRowCounter) {
					LOOPITERATION = loopRowCounter - 1;
				}
			}
			TEMP_SR = TEMP_SR + 1;
		}
		System.out.println("LOOPITERATION : " + LOOPITERATION);
		return LOOPITERATION; // returns the number of data.
	}

	public void executeTest(String testName) throws Exception {
		int TESTROWCOUNTER = 1;
		boolean TestStepFlag = true;
		String testStepName, testExecutionFlag, loopString;
		String startRowString, endRowString;
		String testStepsToLoop;
		while (TestStepFlag) {
			testStepName = getCellValue(readScriptSheet, TESTROWCOUNTER, 2);
			testExecutionFlag = getCellValue(readScriptSheet, TESTROWCOUNTER, 0);// readtestcasesheet.getRow(TestRowCounter).getCell(1).getStringCellValue().trim();

			if (testExecutionFlag.equalsIgnoreCase("End")) {
				TestStepFlag = false;
			} // else if(testName.equals(CURRENTTESTCASE))
			else if (testName.equals(testStepName)) {
				currTestRowPtr = TESTROWCOUNTER;
				PREVIOUS_TEST_CASE = testName;
				TC_ID = getCellValue(readtestcasesheet, TCCounter, 1);
				TC_DESC = getCellValue(readtestcasesheet, TCCounter, 6);
				totalTCount = totalTCount + 1;
				// }
				loopString = getCellValue(readtestcasesheet, TCCounter, 3);// readtestcasesheet.getRow(TestRowCounter).getCell(2).getStringCellValue().trim();
				// here it gets start row and end row for parameterization
				if (loopString.equalsIgnoreCase("Loop")) {
					startRowString = getCellValue(readtestcasesheet, TCCounter, 4);// readtestcasesheet.getRow(TestRowCounter).getCell(3).getStringCellValue().toString().trim();
					endRowString = getCellValue(readtestcasesheet, TCCounter, 5);// readtestcasesheet.getRow(TestRowCounter).getCell(4).getStringCellValue().toString().trim();
					if ((startRowString.isEmpty()) && (endRowString.isEmpty())) {
						int TEMPVAR = currTestRowPtr;
						startRow = currTestRowPtr;
						boolean TEMPFLAG = true;
						while (TEMPFLAG) {
							testStepsToLoop = getCellValue(readScriptSheet, TEMPVAR, 2);// readscriptsheet.getRow(tempvar).getCell(1).getStringCellValue().trim();
							// System.out.println("TestStepsToLoop: " +
							// TestStepsToLoop);
							if (testStepsToLoop.equals(PREVIOUS_TEST_CASE)) {
								TEMPVAR = TEMPVAR + 1;
							} else {
								TEMPFLAG = false;
							}
						}
						endRow = TEMPVAR - 1;
						System.out.println("Start Row: " + startRow);
						System.out.println("End Row: " + endRow);
					} else {
						startRow = Integer.parseInt(startRowString) - 1;
						endRow = Integer.parseInt(endRowString) - 1;
					}
					LOOP_FLAG = true;
				} else {
					LOOP_FLAG = false;
				}
				break;// TODO:why this break-lenina
			}
			TESTROWCOUNTER = TESTROWCOUNTER + 1;
		}
		if (testFlag.equalsIgnoreCase("y")) {
			tcStartTime = getStartTime();
		}
	}

	public int[] getTotalStepsAndStepPointer(String testName) throws IOException { // TODO:
		// good
		// to
		// move
		// to
		// diff
		// class
		int[] retObj = { 0, 0, 0, 0, 1 };
		int TESTROWCOUNTER = 1, TEMPVAR = 1;
		boolean TestStepFlag = true;
		boolean TEMPFLAG = true;
		String testStepName, testExecutionFlag, loopString;
		String startRowString, endRowString;
		String testStepsToLoop;
		while (TestStepFlag) {
			testStepName = getCellValue(readScriptSheet, TESTROWCOUNTER, 2);
			testExecutionFlag = getCellValue(readScriptSheet, TESTROWCOUNTER, 0);// readtestcasesheet.getRow(TestRowCounter).getCell(1).getStringCellValue().trim();
			if (testExecutionFlag.equalsIgnoreCase("End")) {
				retObj[4] = 0;
				TestStepFlag = false;
			} // else if(testName.equals(CURRENTTESTCASE))
			else if (testName.equals(testStepName)) {
				currTestRowPtr = TESTROWCOUNTER;
				PREVIOUS_TEST_CASE = testName;
				TC_ID = getCellValue(readtestcasesheet, TCCounter, 1);
				TC_DESC = getCellValue(readtestcasesheet, TCCounter, 6);
				totalTCount = totalTCount + 1;
				loopString = getCellValue(readtestcasesheet, TCCounter, 3);
				if (loopString.equalsIgnoreCase("Loop") || loopString.equalsIgnoreCase("yes")
						|| loopString.equalsIgnoreCase("y")) {
					startRowString = getCellValue(readtestcasesheet, TCCounter, 4);// readtestcasesheet.getRow(TestRowCounter).getCell(3).getStringCellValue().toString().trim();
					endRowString = getCellValue(readtestcasesheet, TCCounter, 5);// readtestcasesheet.getRow(TestRowCounter).getCell(4).getStringCellValue().toString().trim();
					if ((startRowString.isEmpty()) && (endRowString.isEmpty())) {
						int TEMPLOOPVAR = currTestRowPtr;
						startRow = currTestRowPtr;
						boolean TEMPLOOPFLAG = true;
						while (TEMPLOOPFLAG) {
							testStepsToLoop = getCellValue(readScriptSheet, TEMPLOOPVAR, 2);// readscriptsheet.getRow(tempvar).getCell(1).getStringCellValue().trim();

							if (testStepsToLoop.equals(testName)) {
								TEMPLOOPVAR = TEMPLOOPVAR + 1;
							} else {
								TEMPLOOPFLAG = false;
							}
						}
						endRow = TEMPLOOPVAR - 1;
						System.out.println("Start Row: " + startRow);
						System.out.println("End Row: " + endRow);
					} else {
						startRow = Integer.parseInt(startRowString) - 1;
						endRow = Integer.parseInt(endRowString) - 1;
					}
					LOOP_FLAG = true;
				} else {
					LOOP_FLAG = false;
					int tempLoop = currTestRowPtr;
					while (TEMPFLAG) {
						testStepsToLoop = getCellValue(readScriptSheet, tempLoop, 2);// readscriptsheet.getRow(tempvar).getCell(1).getStringCellValue().trim();
						// System.out.println("TestStepsToLoop: " +
						// TestStepsToLoop);
						if (testStepsToLoop.equals(testName)) {
							TEMPVAR = TEMPVAR + 1;
						} else {
							TEMPFLAG = false;
						}
						tempLoop = tempLoop + 1;
					}
				}
				break;
			}
			TESTROWCOUNTER = TESTROWCOUNTER + 1;
		}
		retObj[0] = TEMPVAR - 1;
		retObj[1] = currTestRowPtr;
		retObj[2] = startRow;
		retObj[3] = endRow;
		System.out.println("Start Row: " + startRow);
		System.out.println("End Row: " + endRow);
		return retObj;
	}

	private static JRXlsDataSource getDataSource1() throws JRException {
		JRXlsDataSource ds;
		try {
			String[] columnNames = new String[] { "TCID", "TESTCASENAME", "RESULT", "BROWSER", "TESTSTAUS", "TESTCOUNT",
					"TPASS", "TFAIL", "TESTURL" };
			int[] columnIndexes = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
			System.out.println(System.getProperty("user.dir"));
			String url = "SeleniumFramework/Test_Excel/Tester.xls";
			ds = new JRXlsDataSource(JRLoader.getLocationInputStream(url
					// "C:\\CM_CBT_Automation\\SeleniumWebAutomationFramework\\Selenium_Framework\\SeleniumFramework\\Test_Excel\\Tester.xls"
					// "SeleniumFramework\\Test_Excel\\Tester.xls"
					));

			ds.setColumnNames(columnNames, columnIndexes);
			ds.setUseFirstRowAsHeader(true);

			// uncomment the below line to see how sheet selection works
		} catch (IOException e) {
			throw new JRException(e);
		}

		return ds;
	}

	public static void exportReportToXHtmlFile(JasperPrint jasperPrint, String outputFile)
			throws JRException, IOException, InterruptedException {
		JasperExportManager.exportReportToHtmlFile(jasperPrint, outputFile);
		Runtime rTime = Runtime.getRuntime();
		System.out.println(System.getProperty("user.dir"));
		String url = System.getProperty("user.dir") + File.separator + "SeleniumFramework" + File.separator
				+ "Test_Jasper_Report" + File.separator + "Report.html";
		String browser = "C:" + File.separator + "Program Files" + File.separator + "Internet Explorer" + File.separator
				+ "iexplore.exe ";
		// Process pc = rTime.exec(browser + url);
		// pc.waitFor();
	}

	public static void JasperReportExecut() throws JRException, IOException, InterruptedException {
		/*String reportFile = "SeleniumFramework" + File.separator + "Jasper_Data" + File.separator + "Jasper.jrxml";
		JRXlsDataSource ds1 = getDataSource1();
		JasperPrint jasperPrint;
		JasperDesign jasperDesign = JRXmlLoader.load(reportFile);
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		jasperPrint = JasperFillManager.fillReport(jasperReport, null, ds1);
		createFolder("SeleniumFramework" + File.separator + "Test_Jasper_Report");
		exportReportToXHtmlFile(jasperPrint,
				"SeleniumFramework" + File.separator + "Test_Jasper_Report" + File.separator + "Report.html");*/
	}

	public JacksonHandle readFulfillmentRecordById(String id) {
		String recordId = File.separator + fulfillmentCollectionName + File.separator + id + ".json";
		Boolean isExist = isExistFulfillmentRecord(id);
		if (isExist) {
			jacksonHandle = documentManager.read(recordId, fulfillmentMetadata, jacksonHandle);
		}
		return jacksonHandle;
	}

	public JacksonHandle readDispatchRecordById(String id) {
		String recordId = File.separator + DispatchCollectionName + File.separator + id + ".json";
		Boolean isExist = isExistDispatchRecord(id);
		if (isExist) {
			jacksonHandle = documentManager.read(recordId, dispatchRecordMetadata, jacksonHandle);
		}
		return jacksonHandle;
	}

	public boolean isExistFulfillmentRecord(String id) {
		String recordId = File.separator + fulfillmentCollectionName + File.separator + id + ".json";
		desc = documentManager.exists(recordId);
		if (desc != null)
			return true;
		else
			return false;
	}

	public boolean isExistDispatchRecord(String id) {
		String recordId = File.separator + DispatchCollectionName + File.separator + id + ".json";
		desc = documentManager.exists(recordId);
		if (desc != null)
			return true;
		else
			return false;
	}

	/*
	 * public static void main(String[] args) { JacksonHandle jacksonHandle=
	 * null; FulfillmentDAOImpl daoImpl = new FulfillmentDAOImpl();
	 * //jacksonHandle = daoImpl.readFulfillmentRecordById("test332");
	 * jacksonHandle = daoImpl.readFulfillmentRecordById("ISLTEST13");
	 * 
	 * JsonNode node = jacksonHandle.get(); System.out.println("Root Node" +
	 * node); //System.out.println(jacksonHandle.get().get("requestHeader"));
	 * 
	 * 
	 * // objMap.
	 * 
	 * Iterator<Map.Entry<String,JsonNode>> fieldsIterator = node.fields();
	 * while(fieldsIterator.hasNext()) { Map.Entry<String,JsonNode> field =
	 * fieldsIterator.next(); //System.out.println("field Key :" +
	 * field.getKey()); //System.out.println("field Value :" +
	 * field.getValue());
	 * 
	 * if (field.getKey() == "requestHistory") {
	 * 
	 * JsonNode innerNode = field.getValue(); System.out.println(
	 * "Inner key for Request history" + innerNode);
	 * findArrNodeValue(innerNode);
	 * 
	 * }
	 * 
	 * if (field.getKey() == "fulfillmentRequest") {
	 * 
	 * JsonNode innerNode = field.getValue();
	 * 
	 * System.out.println("Inner key for Request header " + innerNode);
	 * findNodeValue(innerNode);
	 * 
	 * }
	 * 
	 * } // System.out.println(node.fields());
	 * 
	 * //System.out.println(jacksonHandle.get().get("fulfillmentRequest").get(
	 * "requestHeader"));
	 * 
	 * 
	 * 
	 * }
	 */
	public void findNodeValue(JsonNode innerNode) {
		// TODO Auto-generated method stub

		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = innerNode.fields();
		while (fieldsIterator.hasNext()) {
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			// System.out.println("field Key :" + field.getKey());
			// System.out.println("field Value :" + field.getValue());

			if (field.getKey().equals("requestHeader")) {

				JsonNode childNode = field.getValue();
				System.out.println("Inner key for requestheader is " + childNode);
				Iterator<Map.Entry<String, JsonNode>> childFieldsIterator = childNode.fields();
				while (childFieldsIterator.hasNext()) {
					Map.Entry<String, JsonNode> childField = childFieldsIterator.next();
					System.out.println("childField field Key :" + childField.getKey());
					System.out.println(" childField field Value :" + childField.getValue());

				}

				// findNodeValue(innerNode);

			}
			// jsonNode.fields();
			// System.out.println("json Node :" + jsonNode);
		}

	}

	public void findArrNodeValue(JsonNode innerNode) {
		// TODO Auto-generated method stub

		for (JsonNode jsonNode : innerNode) {

			Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> field = fieldsIterator.next();
				// System.out.println("field Key :" + field.getKey());
				// System.out.println("field Value :" + field.getValue());

				if (field.getKey().equals("eventType") && field.getValue().textValue().equals("Generated")) {

					JsonNode childNode = field.getValue();
					System.out.println("Inner key for event Type" + childNode);
					// findNodeValue(innerNode);

				}
				// jsonNode.fields();
				// System.out.println("json Node :" + jsonNode);
			}

		}

	}

	public static String readFile(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	public void getAPIresponse(String fValue) throws JSONException, Exception {
		String jPath = "";
		APIactualResponse = "";
		for (String str : fValue.split("/")) {
			jPath = jPath + "" + File.separator + "" + str;
		}
		String payloadPath = "SeleniumFramework" + File.separator + "API" + jPath;

		HttpURLConnection httpURLConnection = null;
		JSONObject requestJsonObject = new JSONObject(readFile(payloadPath));
		HashMap<String, String> headerParameters = new HashMap<String, String>();

		// headerParameters.put(APIheader.split(":")[0],
		// APIheader.split(":")[1]);
		for (String apiHeader : APIheader) {
			headerParameters.put(apiHeader.split(":")[0], apiHeader.split(":")[1]);
		}
		if (APImethod.equalsIgnoreCase("Post")) {
			httpURLConnection = (HttpURLConnection) ConnectionHelper.createPostConnection(APIurl, headerParameters);
		} else if (APImethod.equalsIgnoreCase("Get")) {
			httpURLConnection = (HttpURLConnection) ConnectionHelper.createGetConnection(APIurl, headerParameters);
		}
		APIactualResponse = ResponseHelper.postResponseObject(httpURLConnection, requestJsonObject);
		int responseCode = httpURLConnection.getResponseCode();
		System.out.println("#################### RESPONSE CODE: " + responseCode);
		if (APIactualResponse.isEmpty() || responseCode != 200) {
			System.out.println("Got no response for the API");
			throw new Exception("Got no response for the API");
		}
		// else {
		// System.out.println("################## GOT RESPONSE: "
		// +APIactualResponse);
		// }
		httpURLConnection.disconnect();
		APIheader.clear();
		headerParameters.clear();
	}

	public void checkAPIresponse(String fValue) throws Exception {
		String jsonPath = "";
		for (String str : fValue.split("/")) {
			jsonPath = jsonPath + "" + File.separator + "" + str;
		}
		String ExpectedJsonPath = "SeleniumFramework" + File.separator + "API" + jsonPath;
		if (APIactualResponse.isEmpty()) {
			System.out.println("No Response to compare. Seem the rest call failed");
			throw new Exception("No Response to compare. Seem the rest call failed");
		}
		JSONObject expectedJson = new JSONObject(readFile(ExpectedJsonPath));
		JSONObject actualJson = new JSONObject(APIactualResponse);

		System.out.println("#################### Actual Response ################");
		System.out.println(actualJson.toString());
		System.out.println("#################### Expected Response #################");
		System.out.println(expectedJson.toString());

		if (!expectedJson.toString().equals(actualJson.toString())) {
			System.out.println("Responses not matched");
			throw new Exception("API response didn't matched to expected");
		}
	}

	private void runQueryDatabase(String fvalue) throws Exception {
		{
			try {

				Class.forName(db_driver);
				conn = DriverManager.getConnection(db_url, db_username, db_password);
				PreparedStatement statement = conn.prepareStatement(fvalue);
				statement.setMaxRows(1);
				ResultSet rs = statement.executeQuery(fvalue);
				ResultSetMetaData rsMetaData = rs.getMetaData();
				System.out.println("result set columns" + rsMetaData.getColumnCount());
				int numberOfColumns = rsMetaData.getColumnCount();
				while (rs.next()) {
					for (int i = 1; i <= numberOfColumns; i++) {
						storeQueryResults.put(rsMetaData.getColumnName(i), rs.getString(rsMetaData.getColumnName(i)));
					}
					System.out.println("storeQueryResults" + storeQueryResults);

				}
			} catch (Exception e) {
				throw new Exception("Exception for SQL Statement: " + fvalue + "\n" + e.toString());
			}
			/*
			 * try { conn.close(); }
			 * 
			 * catch (Exception e) { e.printStackTrace(); }
			 */
		}
	}

	private void writeXMLDB(String Query,String ColName) throws Exception {
		{
			try {

				runQueryDatabase(Query);
				File fileName = new File("./SeleniumFramework/XML/"+testName+".xml");
				FileOutputStream is = new FileOutputStream(fileName);
				OutputStreamWriter osw = new OutputStreamWriter(is);    
				Writer w = new BufferedWriter(osw);
				w.write(storeQueryResults.get(ColName.toUpperCase()));
				w.close();

			} catch (Exception e) {
				e.printStackTrace();
				LOG_VAR=0;

			}
		}
	}


	private void funcVerifyFromXML(String feType, String objName, String fvalue) throws InterruptedException, Exception 
    {
          try
          {

                String parts[]=fvalue.split("#");
                String parentTag=parts[0].trim();
                String childTag=parts[1];
                String value="";
                String XMLValue="";
                String XMLFlag="0";

                if(fvalue.contains("!"))
                {
                      String parts1[]=childTag.split("!");
                      childTag = parts1[0];
                      value=parts1[1];
                }
                else if (fvalue.contains("&"))
                {
                      String parts1[]=childTag.split("&");
                      childTag = parts1[0];
                      value=parts1[1];
                }
                else
                {
                      value=parts[2];
                }


                DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new File("./SeleniumFramework/XML/"+testName+".xml"));
                //Document doc = builder.parse(new File("C:\\Users\\rgupta64\\Desktop\\InputEnrlXML.xml"));

                NodeList itemList = doc.getElementsByTagName(parentTag);
                Node itemNode;
                Element itemElt;
                if(itemList.getLength()>0)
                {
                      for(int k=0; k < itemList.getLength(); k++)
                      {
                            itemNode = itemList.item(k);

                            if(itemNode.getNodeType() == Node.ELEMENT_NODE) 
                            {
                                  itemElt = (Element) itemNode;

                                  if(fvalue.contains("!"))
                                  {
                                        XMLValue=itemElt.getAttribute(childTag);
                                        System.out.println(childTag+" : "+XMLValue);
                                        
                                        if(value.equalsIgnoreCase(XMLValue))
                                        {
                                              System.out.println("value is matching");
                                              XMLFlag="1";

                                        }
                                        else
                                        {
                                              continue;
                                        }
                                        
                                  }
                                  else if(fvalue.contains("&"))
                                  {
                                        String str=itemElt.getFirstChild().getNodeName();

                                        if(itemElt.getFirstChild().getNodeName().equalsIgnoreCase(childTag))
                                        {
                                              XMLValue=itemElt.getFirstChild().getTextContent();
                                              System.out.println(childTag+ " : "+ XMLValue);
                                        }
                                        else
                                        {
                                              continue;
                                        }

                                  }
                                  else
                                  {
                                        XMLValue=itemElt.getElementsByTagName(childTag).item(0).getTextContent();
                                        System.out.println(childTag+" : "+XMLValue);
                                        
                                        if(value.equalsIgnoreCase(XMLValue))
                                        {
                                              System.out.println("value is matching");
                                              XMLFlag="1";

                                        }
                                        else
                                        {
                                              continue;
                                        }

                                  }

                            }
                      }
                      if(fvalue.contains("&"))
                      {
                            if(value.equalsIgnoreCase(XMLValue))
                            {
                                  System.out.println("Value are matching");
                                  failFlag=1;
                            }
                            else
                            {
                                  System.out.println("Values are not matching");
                                  failFlag=0;
                                  LOG_VAR=0;
                                  throw new Exception("Values are not matching");
                            }
                      }

                      else if((XMLFlag.equalsIgnoreCase("0")))
                      {
                            throw new Exception("Value is not present anywhere");
                      }

                }
                else
                {
                      throw new Exception("Parent tag not present");
                }
          }
          catch (ArrayIndexOutOfBoundsException e)
          {
                System.out.println("Value is not checked");
          }

          catch (Exception e)
          {
                e.printStackTrace();
                LOG_VAR = 0;
          }
    }


	private void funcWriteXMLFromDB(String feType, String objName, String fvalue) {

		try {

			String[] parts = fvalue.split("#");
			String QueryName = parts[0]; 
			String QColumnName = parts[1];
			writeXMLDB(QueryName,QColumnName);

		} 
		catch (Exception e) {
			System.out.println("funInputEmployeeIdHrhd--functionlib" + e.getMessage());
		}
	}

	private void funInputFromQuery(String feType, String objName, String fvalue) {
		// TODO Auto-generated method stub
		try {
			String[] parts = fvalue.split("#");
			String QueryName = parts[0]; // 004
			String QColumnName = parts[1];
			runQueryDatabase(QueryName);
			funcInput(feType, objName, storeQueryResults.get(QColumnName));

		} catch (Exception e) {
			System.out.println("funInputEmployeeIdHrhd--functionlib" + e.getMessage());
		}
	}


	private String getText(WebElement e) {
		return e.getTagName().equalsIgnoreCase("input") ? e.getAttribute("value")
				: (e.getTagName().equalsIgnoreCase("select") ? (new Select(e)).getFirstSelectedOption().getText()
						: e.getText());
	}

	private void verifyDBtextMatches(String feType, String objName, String fvalue) throws Exception {
		try {
			// db values
			String[] parts = fvalue.split("#");
			String QueryName = parts[0]; // 004
			String QColumnName = parts[1];
			runQueryDatabase(QueryName);
			String expectedValue = storeQueryResults.get(QColumnName);
			System.out.println("Expected value is" + expectedValue);
			WebElement element;
			element = funcFindElement(feType, objName);
			String webValue = getText(element);
			if (webValue.equals(storeQueryResults.get(expectedValue))) {
				System.out.println("Database text matches with the  Expected Value");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				System.out.println("Database text matche doesn't with the  Expected Value");
				failFlag = 0;
				LOG_VAR = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void verifyDBtextSmallerThan(String feType, String objName, String fvalue) throws Exception {
		try {
			// db values
			String[] parts = fvalue.split("#");
			String QueryName = parts[0]; // 004
			String QColumnName = parts[1];
			runQueryDatabase(QueryName);
			fvalue = storeQueryResults.get(QColumnName);
			fvalue = fvalue.replaceAll("[^\\w\\s\\.]", "");
			float expectedValue = Float.parseFloat(fvalue);
			System.out.println("Expected value is" + expectedValue);
			// WebElement Text
			WebElement element;
			element = funcFindElement(feType, objName);
			String webValue = getText(element);
			webValue = webValue.replaceAll("[^\\w\\s\\.]", "");
			float ActualValue = Float.parseFloat(webValue);
			System.out.println("Webelement value is" + ActualValue);
			int retval = Float.compare(ActualValue, expectedValue);
			if (retval > 0) {
				System.out.println("WebEelement value is greater than the  Expected Value");
				failFlag = 0;
				LOG_VAR = 0;
			} else if (retval < 0) {
				System.out.println("WebEelement value is smaller than the  Expected Value");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				System.out.println("WebEelement and Expected values are Equal");
				failFlag = 0;
				LOG_VAR = 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void verifyDBtextGreaterThan(String feType, String objName, String fvalue) throws Exception {
		try {
			// db values
			String[] parts = fvalue.split("#");
			String QueryName = parts[0]; // 004
			String QColumnName = parts[1];
			runQueryDatabase(QueryName);
			fvalue = storeQueryResults.get(QColumnName);
			fvalue = fvalue.replaceAll("[^\\w\\s\\.]", "");
			float expectedValue = Float.parseFloat(fvalue);
			System.out.println("Expected value is" + expectedValue);
			// WebElement Text
			WebElement element;
			element = funcFindElement(feType, objName);
			String webValue = getText(element);
			webValue = webValue.replaceAll("[^\\w\\s\\.]", "");
			float ActualValue = Float.parseFloat(webValue);
			System.out.println("Webelement value is" + ActualValue);
			int retval = Float.compare(ActualValue, expectedValue);
			if (retval > 0) {
				System.out.println("WebEelement value is greater than the  Expected Value");
				failFlag = 1;
				LOG_VAR = 1;
			} else if (retval < 0) {
				System.out.println("WebEelement value is smaller than the  Expected Value");
				failFlag = 0;
				LOG_VAR = 0;
			} else {
				System.out.println("WebEelement and Expected values are Equal");
				failFlag = 0;
				LOG_VAR = 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void verifyTextSmallerThan(String feType, String objName, String fvalue) throws Exception {
		try {
			// db values
			fvalue = fvalue.replaceAll("[^\\w\\s\\.]", "");
			float expectedValue = Float.parseFloat(fvalue);
			System.out.println("Expected value is" + expectedValue);
			// WebElement Text
			WebElement element;
			element = funcFindElement(feType, objName);
			String webValue = getText(element);
			webValue = webValue.replaceAll("[^\\w\\s\\.]", "");
			float ActualValue = Float.parseFloat(webValue);
			System.out.println("Webelement value is" + ActualValue);
			int retval = Float.compare(ActualValue, expectedValue);
			if (retval > 0) {
				System.out.println("WebEelement value is greater than the  Expected Value");
				failFlag = 0;
				LOG_VAR = 0;
			} else if (retval < 0) {
				System.out.println("WebEelement value is smaller than the  Expected Value");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				System.out.println("WebEelement and Expected values are Equal");
				failFlag = 0;
				LOG_VAR = 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void verifyTextGreaterThan(String feType, String objName, String fvalue) throws Exception {
		try {
			// db values
			fvalue = fvalue.replaceAll("[^\\w\\s\\.]", "");
			float expectedValue = Float.parseFloat(fvalue);
			System.out.println("Expected value is" + expectedValue);
			// WebElement Text
			WebElement element;
			element = funcFindElement(feType, objName);
			String webValue = getText(element);
			webValue = webValue.replaceAll("[^\\w\\s\\.]", "");
			float ActualValue = Float.parseFloat(webValue);
			System.out.println("Webelement value is" + ActualValue);
			int retval = Float.compare(ActualValue, expectedValue);

			if (retval > 0) {
				System.out.println("WebEelement value is greater than the  Expected Value");
				failFlag = 1;
				LOG_VAR = 1;
			} else if (retval < 0) {
				System.out.println("WebEelement value is smaller than the  Expected Value");
				failFlag = 0;
				LOG_VAR = 0;
			} else {
				System.out.println("WebEelement and Expected values are Equal");
				failFlag = 1;
				LOG_VAR = 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void verifyTextMatches(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Holdvalue = getText(element);
		for(int t=1;t<=10;t++)
		{	
		if (((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete"))
		{ 	Holdvalue = getText(element); 
		break;	}
		else { Thread.sleep(3000);	}
		}
		
		if (Holdvalue.equalsIgnoreCase(fValue)) {
			System.out.println("Expected Element text matches with UI");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			failFlag = 0;
			LOG_VAR = 0;
			System.out.println("TestError :Expected Element text doesn't matches with UI");

		}
	}
	
	private void funcVerifyTextNotPresentInDD(String feType, String objName, String fValue) throws InterruptedException 
       {
            WebElement element;
			String val;
			int l=0;
             element = funcFindElement(feType, objName);
             Select select = new Select(element);
             List<WebElement> e = select.getOptions();
             int itemCount = e.size();
             while( l < itemCount)
             {
                    val=e.get(l).getText();
                System.out.println(e.get(l).getText());
                 if(val.equalsIgnoreCase(fValue))
                 {
                    System.out.println("value is present");
                    failFlag = 0;
                           LOG_VAR = 0;
                 }
                 else
                 {
                    System.out.println("value is not present");
                    failFlag = 1;
                    LOG_VAR = 1;
                 }
                 l++;
             }
             
       }

		private void funcaptureScreenshots() 
		    {
				 screenShoot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
				 LOG_VAR=1;
			}
		
		private void funcSaveToNotepad(String feType, String objName, String fvalue) {
            try
            {
                  File fileName = new File("SeleniumFramework" + File.separator + "Notepad" + File.separator +testName+".txt");
                  FileOutputStream is = new FileOutputStream(fileName);
                  OutputStreamWriter osw = new OutputStreamWriter(is);    
                  Writer w = new BufferedWriter(osw);
                  w.write(fvalue);
                  w.close();
            }
            catch (Exception e) 
            {
                  System.out.println("Error Message" + e.getMessage());
            }
      }

	private void checkElementCountEquals(String fetype, String objname, String fValue) throws InterruptedException {
		WebElement htmltable;
		htmltable = funcFindElement(fetype, objname);
		List<WebElement> rows = htmltable.findElements(By.tagName("tr"));
		int count = rows.size();

		if (count > 0) {
			System.out.println("TestInfo : All paycheck Element link Exists on Screen");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			failFlag = 0;
			LOG_VAR = 0;
			System.out.println("TestError :paycheck Element not Exists on Screen");
		}

		// TODO Auto-generated method stub

	}

	private void funcPasteFromClipBoard(String feType, String objName, String fValue) throws Exception {
		String myString = (new File("SeleniumFramework" + File.separator + "Upload" + File.separator + fValue)
		.getAbsolutePath());
		System.out.println("File to upload path: " + myString);
		StringSelection stringSelection = new StringSelection(myString);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, stringSelection);
		actionsClick(feType, objName, "");
		// JSfuncClick(feType, objName);
		// funKeyEvents(feType, objName, "CTRL+V");
		Thread.sleep(3000);
		(new Robot()).keyPress(KeyEvent.VK_CONTROL);
		(new Robot()).keyPress(KeyEvent.VK_V);
		Thread.sleep(3000);
		(new Robot()).keyRelease(KeyEvent.VK_CONTROL);
		(new Robot()).keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		(new Robot()).keyPress(KeyEvent.VK_TAB);
		(new Robot()).keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(1000);
		(new Robot()).keyPress(KeyEvent.VK_TAB);
		(new Robot()).keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(1000);
		(new Robot()).keyPress(KeyEvent.VK_ENTER);
		(new Robot()).keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(2000);
	}

	private String getEleProp(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Storevalue = "";
		if (fValue.equalsIgnoreCase("bg-color")) {
			Storevalue = element.getCssValue("background-color");
		} else if (fValue.equalsIgnoreCase("color")) {
			Storevalue = element.getCssValue("color");
		} else if (fValue.equalsIgnoreCase("width")) {
			Storevalue = String.valueOf(element.getSize().getWidth());
		} else if (fValue.equalsIgnoreCase("height")) {
			Storevalue = String.valueOf(element.getSize().getHeight());
		} else {
			Storevalue = element.getAttribute(fValue);
		}
		return Storevalue;

	}

	//	public void waitForJSandJQueryToLoad() throws InterruptedException {
	//		// boolean state = false;
	//		final String pageS = pageSource;
	//		String tempText = "Waiting Jquery to complete(.) Then for PageLoad (-)";
	//
	//		ExpectedCondition<Boolean> pageNajaxLoad = new ExpectedCondition<Boolean>() {
	//			@Override
	//			public Boolean apply(WebDriver driver) {
	//				return !(pageLoading() && jqueryRunning());
	//			}
	//		};
	////		Thread.sleep(4000);
	//
	//		// if(!pageLoading())System.out.println("Page Ready State = Complete");
	//		// if(!jqueryRunning())System.out.println("Jquery State = Completed");
	//
	//		boolean somethingHappening = (jqueryRunning() && pageLoading())?true:false;
	//		if (somethingHappening) {
	//			System.out.println("Waiting for Change...");
	//			wait.until(pageNajaxLoad);
	//			System.out.println("Changes done on page");
	////			System.err.println("New page Source: " + driver.getPageSource());
	//			// state = pageLoading() && jqueryRunning();
	//		} else {
	//			System.out.println("No Change happen on page");
	//			// state = false;
	//		}
	//		// return state;
	//	}

	public boolean jqueryRunning() {
		try {
			if(((JavascriptExecutor) driver).executeScript("return jQuery.active == 0").equals(true))
				System.out.println("No Jquery");
			else System.out.println("Jquery loading...");
			return false;
		} catch (Exception e) {
			System.out.println("Jquery running");
			return true;
		}
	}

	public boolean pageLoading() {
		try {
			if(((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete"))
				System.out.println("No page loading");
			else System.out.println("Page still loading");
			return false;
		} catch (Exception e) {
			System.out.println("Page loading...");
			return true;
		}
	}

	public String getAbsoluteXPath(WebElement element) {
		// return (String) ((JavascriptExecutor) driver)
		// .executeScript("function absoluteXPath(element) {" + "var comp, comps
		// = [];" + "var parent = null;"
		// + "var xpath = '';" + "var getPos = function(element) {" + "var
		// position = 1, curNode;"
		// + "if (element.nodeType == Node.ATTRIBUTE_NODE) {" + "return null;" +
		// "}"
		// + "for (curNode = element.previousSibling; curNode; curNode =
		// curNode.previousSibling){"
		// + "if (curNode.nodeName == element.nodeName) {" + "++position;" + "}"
		// + "}" + "return position;"
		// + "};" +
		//
		// "if (element instanceof Document) {" + "return '/';" + "}" +
		//
		// "for (; element && !(element instanceof Document); element =
		// element.nodeType ==Node.ATTRIBUTE_NODE ? element.ownerElement :
		// element.parentNode) {"
		// + "comp = comps[comps.length] = {};" + "switch (element.nodeType) {"
		// + "case Node.TEXT_NODE:"
		// + "comp.name = 'text()';" + "break;" + "case Node.ATTRIBUTE_NODE:"
		// + "comp.name = '@' + element.nodeName;" + "break;" + "case
		// Node.PROCESSING_INSTRUCTION_NODE:"
		// + "comp.name = 'processing-instruction()';" + "break;" + "case
		// Node.COMMENT_NODE:"
		// + "comp.name = 'comment()';" + "break;" + "case Node.ELEMENT_NODE:"
		// + "comp.name = element.nodeName;" + "break;" + "}" + "comp.position =
		// getPos(element);" + "}" +
		//
		// "for (var i = comps.length - 1; i >= 0; i--) {" + "comp = comps[i];"
		// + "xpath += '/' + comp.name.toLowerCase();" + "if (comp.position !==
		// null) {"
		// + "xpath += '[' + comp.position + ']';" + "}" + "}" +
		//
		// "return xpath;" +
		//
		// "} return absoluteXPath(arguments[0]);", element);
		String jscript = "function getPathTo(node) {" + "  var stack = [];" + "  while(node.parentNode !== null) {"
				+ "    stack.unshift(node.tagName);" + "    node = node.parentNode;" + "  }"
				+ "  return stack.join('/');" + "}" + "return getPathTo(arguments[0]);";
		return (String) driver.executeScript(jscript, element);
	}

	private void waittime(String fValue) throws InterruptedException {
		System.out.println("wait time entered");
		int i = Integer.parseInt(fValue);
		i = (i * 1000);
		try {
			//			Thread.sleep(2000);
			//			driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
			try {
				By by1 = By.id(
						"busyIndicatorHeading-PCuyY");
				driver.switchTo().defaultContent();
				if (!returnIfElementPresent(by1).equals(null)){
					System.out.println("Loading detected...");
					(new WebDriverWait(driver, 30)).until(ExpectedConditions.invisibilityOfElementLocated(by1));
				}
				else {
					Thread.sleep(i);
				}
			} catch (Exception e) {
				Thread.sleep(i);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// waitForJSandJQueryToLoad();

	}

	public void funcSelectByValue(String feType, String objName, String fvalue) throws InterruptedException {
		WebElement elem=funcFindElement(feType, objName);
		(new WebDriverWait(driver, 15))
		.until((ExpectedConditions.elementToBeClickable(elem)));
		Thread.sleep(1000);
		Boolean found = false;
		List<String> actualElementValues = new ArrayList<String>();
		List<String> listValue = Arrays.asList(fvalue.split(","));
		List<WebElement> listElement = funcFindElements(feType, objName);
		for (String value : listValue) {
			found = false;
			for (WebElement element : listElement) {
				String actualElementValue = element.getAttribute("value");
				actualElementValues.add(actualElementValue);
				if (actualElementValue.equalsIgnoreCase(value)) {
					element.click();
					found = true;
					break;
				}
			}
		}

	}
	
	public void funcVerifyListValues(String feType, String objName, String fvalue) throws InterruptedException {
		Boolean found = false;
		List<String> actualElementValues = new ArrayList<String>();
		List<String> ExpValue = Arrays.asList(fvalue.split(","));
		List<WebElement> ActValue = funcFindElements(feType, objName);
		for (String value : ExpValue) {
			found = false;
			for (WebElement element : ActValue) {
				String actualElementValue = element.getAttribute("value");
				if (actualElementValue.equalsIgnoreCase(value)) {
					found = true;
					break;
				}
			}
		}

	}
	
	public void funcVerifyMaskedpasswordField(String feType, String objName, String fvalue) throws InterruptedException {
		WebElement elem=funcFindElement(feType, objName);
		(new WebDriverWait(driver, 15))
		.until((ExpectedConditions.elementToBeClickable(elem)));
		 Thread.sleep(1000);
		 System.out.println(elem.getAttribute("type"));
		 String PasswordType=elem.getAttribute("type");
		 if (PasswordType.equalsIgnoreCase("password")) {
				System.out.println("Type is Password so it is masked");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				failFlag = 0;
				LOG_VAR = 0;
				System.out.println("TestError :Type is not Password so it is not masked");

			}
		 
	}
	
	public void funcHandleForgotPasswordSecques() throws InterruptedException {
		String answer1="",answer2="";
		WebElement elem=funcFindElement("id", "labelQuestion1");
		(new WebDriverWait(driver, 15))
		.until((ExpectedConditions.elementToBeClickable(elem)));
		 Thread.sleep(1000);
		 String question1 = elem.getText();
         if (question1.toLowerCase().contains("favorite color"))
        	 answer1 = "green";
         if (question1.toLowerCase().contains("best friend"))
        	 answer1 = "optum";
         if (question1.toLowerCase().contains("phone number"))
        	 answer1 = "1234";
         returnIfElementPresent(By.id("securityQuestion1_input")).sendKeys(answer1);
         Thread.sleep(2000);
 		WebElement elem1=funcFindElement("id", "labelSecurityQuestion2_input");
 		(new WebDriverWait(driver, 15))
 		.until((ExpectedConditions.elementToBeClickable(elem1)));
 		 Thread.sleep(1000);
 		 String question2 = elem1.getText();
          if (question2.toLowerCase().contains("favorite color"))
         	 answer2 = "green";
          if (question2.toLowerCase().contains("best friend"))
         	 answer2 = "optum";
          if (question2.toLowerCase().contains("phone number"))
         	 answer2 = "1234";
          returnIfElementPresent(By.id("securityQuestion2_input")).sendKeys(answer2);
	}

	public void funcCreateOptumIDandTDstore() {
		MAHIX_UserId = "erin" + Math.random();
		MAHIX_UserId = MAHIX_UserId.replace(".", "");
		// This will write to the particular cell of TestdataSheet TEST_Alpha in
		// Testscript sheet
		currentDataCell.setCellValue(MAHIX_UserId);
		// This will make your value reflect in Report
		fieldValue = MAHIX_UserId;
		System.out.println("New fieldValue: " + MAHIX_UserId);
	}
	
	
	public void funcInputInWindowPopup(String feType, String objName, String fValue)  throws InterruptedException {
		Pattern p=new Pattern(objName);
		Screen s=new Screen();
		try {
			s.type(p, fValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void funcClickInWindowPopup(String feType, String objName, String fValue)  throws InterruptedException {
		Pattern p=new Pattern(objName);
		Screen s=new Screen();
		try {
			s.click(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void funcRandomNameGenerator(String feType, String objName, String fValue) throws InterruptedException {
		String STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		// String name;
		WebElement element = funcFindElement(feType, objName);

		StringBuilder builder = new StringBuilder();

		int c = 2;
		if ((fValue.length()) > 8) {
			fValue = fValue.substring(0, 7);
			while (c-- != 0) {

				int character = (int) (Math.random() * STRING.length());
				//System.out.println(character);
				builder.append(STRING.charAt(character));
			}

			fValue = fValue + builder.toString();

		}

		else {

			while (c-- != 0) {

				int character = (int) (Math.random() * STRING.length());
				//System.out.println(character);
				builder.append(STRING.charAt(character));
			}

			fValue = fValue + builder.toString();

		}
		try {

			currentDataCell.setCellValue(fValue);
			fieldValue = fValue;

			if (dataholder.containsKey(fValue)) {
				fValue = dataholder.get(fValue);
				System.out.println(fValue);
			}
			element.sendKeys(fValue);

		} catch (NullPointerException e) {
			System.err.println("No Value found");
		}

	}

	public WebElement findElementAgain(WebElement ele, By by) throws StaleElementReferenceException{
		String frame1Name, frame2Name, frame3Name;
		//			System.err.println(e.toString());
		//			driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
		boolean foundInFrame = false;
		driver.switchTo().defaultContent();
		(new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(by));

		ele = returnIfElementPresent(by);
		if (!(ele == null || ele.equals(null))) {
			foundInFrame = true;
		} else System.out.println("Finding element in frames...");
		if (returnIfElementPresent(By.tagName("iframe")) != null && !foundInFrame) {
			// if (returnIfElementPresent(By.xpath("//iframe["+i+"]"))
			// != null && !foundInFrame) {
			List<WebElement> frames = driver.findElements(By.tagName("iframe"));
			for (WebElement webElement : frames) {
				// if (foundInFrame)
				// break;
				driver.switchTo().defaultContent();
				frame1Name = "DefaultContent -> Frame: " + webElement.getAttribute("id");
				driver.switchTo().frame(webElement);
				ele = returnIfElementPresent(by);
				if (!(ele == null || ele.equals(null))) {
					System.out.println(frame1Name);
					foundInFrame = true;
					break;
				}
				if (returnIfElementPresent(By.tagName("iframe")) != null && !foundInFrame) {
					List<WebElement> frameslevel2 = driver.findElements(By.tagName("iframe"));
					for (WebElement webElement2 : frameslevel2) {
						// System.out.println("second frame");
						driver.switchTo().defaultContent();
						driver.switchTo().frame(webElement);
						frame2Name = webElement2.getAttribute("id");
						frame2Name = frame1Name + " --> " + frame2Name;
						driver.switchTo().frame(webElement2);
						ele = returnIfElementPresent(by);
						if (!(ele == null || ele.equals(null))) {
							System.out.print(frame2Name);
							foundInFrame = true;
							break;
						}
						if (returnIfElementPresent(By.tagName("iframe")) != null && !foundInFrame) {
							List<WebElement> frameslevel3 = driver.findElements(By.tagName("iframe"));
							for (WebElement webElement3 : frameslevel3) {
								driver.switchTo().defaultContent();
								driver.switchTo().frame(webElement);
								driver.switchTo().frame(webElement2);
								frame3Name = webElement3.getAttribute("id");
								frame3Name = frame2Name + " --> " + frame3Name;
								driver.switchTo().frame(webElement3);
								ele = returnIfElementPresent(by);
								if (!(ele == null || ele.equals(null))) {
									System.out.print(frame3Name);
									foundInFrame = true;
									break;
								}
							}
						}
						if (foundInFrame)
							break;
					}
				}
				if (foundInFrame)
					break;
			}
		}
		return ele;
	}
}

