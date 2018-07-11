package com.SeleniumFramework.test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.SeleniumFramework.commons.util.ExcelFileUtil;
import com.SeleniumFramework.commons.util.LIFOStack;
import com.saucelabs.saucerest.SauceREST;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class ReportLibrary extends ExcelFileUtil {

	public String dt1, reportDate, reportLog, exceptionLog, screenShot;
	public static WebElement validate;
	public static int failedTCount = 0, passedTCount = 0, sumFail, totalTCount = 0;;
	public static String zipdate, validate1, QCExcelPath, failedStep, vurl = "Not Assigned";
	public String scrshtPath, scrshot, strModuleName, testName, applicationName;
	// public Selenium selenium;
	public RemoteWebDriver driver;
	// private ExcelFileUtil excelFileUtil = getInstance();
	public static String tmpPlatform;
	public static String oldTab;
	public long TCtimeCalculator, ModuletimeCalulator;
	
//	File screenShoot;
	public String sauceJobId;
	public SauceREST r;
	Screenshot screenShoot;

	public String sauceSession = "";
	public String sauceSessionLink = "";

	public static int rnum = 1;
	public int cnum = 0;
	public static FileOutputStream fileOut;
	public static HSSFWorkbook workbook;
	public static HSSFSheet worksheet;
	public String url;
	public static String reportzip;

	public static void createLog() {
		try {
			FileWriter fstream = new FileWriter("out.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * @param modulePath
	 * @throws Exception
	 */
	public void setUp(String modulePath) throws Exception {
		String strTestCasehtml = " <" + "style" + ">"
				+ "table.tableizer-table {border: 1px solid #CCC; font-family: Arial, Helvetica, sans-serif; font-size: 14px;} .tableizer-table td {padding: 4px; margin: 3px; border: 1px solid #ccc"
				+ ";}" + ".tableizer-table th " + "{" + "background-color: #620B38; color: #FFF; font-weight: bold"
				+ ";" + "}" + "<" + "/style>" + "<table class" + "=" + "\"" + "tableizer-table" + "\"" + ">"
				+ "<tr class" + "=" + "\"" + "tableizer-firstrow" + "\""
				+ "><th>TestSuitName</th><th>TestCaseID</th><th>TestCaseName</th><th>TestCaseDescription</th><th>StartTime</th><th>EndTime</th><th>Status</th><th>FailedTestStep</th>";
		String strTestStephtml = "<style>"
				+ "table.tableizer-table {border: 1px solid #CCC; font-family: Arial, Helvetica, sans-serif; font-size: 14px;} .tableizer-table td {padding: 4px; margin: 3px; border: 1px solid #ccc"
				+ ";}" + ".tableizer-table th " + "{" + "background-color: #620B38; color: #FFF; font-weight: bold"
				+ ";" + "}" + "<" + "/style>" + "<table class" + "=" + "\"" + "tableizer-table" + "\"" + ">"
				+ "<tr class" + "=" + "\"" + "tableizer-firstrow" + "\""
				+ "><th>TestSuitName</th><th>TestCaseID</th><th>TestCaseName</th><th>TestStepID</th><th>ScreenName</th><th>FieldName</th><th>Value</th><th>ExpectedResult</th><th>ActualResult</th><th>ExecutionStatus</th><th>ScreenShot</th>";
		String strExcpLog = "Exception Log : ";

		Date now = new Date();
		dt1 = DateFormat.getDateTimeInstance().format(now).toString();
		reportDate = dt1;
		reportDate = reportDate.replaceAll(":", "_");
		String qcxl = reportDate;
		reportLog = reportDate;
		reportDate = "Result_" + reportDate;
		reportLog = "DetailedResult_" + reportDate;
		exceptionLog = "ExcepLog_" + reportDate;
		reportDate = modulePath + File.separator + reportDate + ".html";
		reportLog = modulePath + File.separator + reportLog + ".html";
		exceptionLog = modulePath + File.separator + exceptionLog + ".txt";

		File f_result = new File(reportDate);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(reportDate, true));
			out.write(strTestCasehtml);
			System.out.println("Report Date: " + reportDate);

			out.close();
		} catch (IOException e) {
		}

		if (!f_result.exists())

		{
			f_result.createNewFile();

		}
		f_result = null;

		File f_log = new File(reportLog);
		try {
			BufferedWriter out_detailedResult = new BufferedWriter(new FileWriter(reportLog, true));
			out_detailedResult.write(strTestStephtml);
			System.out.println("Report Date: " + reportDate);

			out_detailedResult.close();
		} catch (IOException e) {
		}
		// File f_log_old=new File(reportLog);
		if (!f_log.exists()) {
			f_log.createNewFile();

		}
		f_log = null;

		File f_excepLog = new File(exceptionLog);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(f_excepLog, true));
			out.write(strExcpLog);
			System.out.println("Report Date: " + reportDate);

			out.close();
		} catch (IOException e) {
		}

		if (!f_excepLog.exists())

		{
			f_excepLog.createNewFile();
			// f_result.renameTo(new File(reportDate));
			// f_result.delete();

		}
		f_excepLog = null;

	}

	public static void createUpdateExcel(String moduleName, String uc) throws IOException, InvalidFormatException {
		// QCExcelPath =
		// "..\\Selenium_Framework\\SeleniumFramework\\QCUpdateSheet";
		QCExcelPath = "SeleniumFramework" + File.separator + "SeleniumFramework" + File.separator + "QCUpdateSheet";
		QCExcelPath = new File(QCExcelPath).getCanonicalPath();
		QCExcelPath = QCExcelPath + File.separator + moduleName + "_" + uc + ".xls";
		// Creating New excelsheet for each module to upload selected module's
		// results to QC
		HSSFWorkbook workbook = new HSSFWorkbook();
		// HSSFSheet firstSheet =
		String tempsheetName = moduleName + "_" + uc;
		tempsheetName = tempsheetName.replaceAll(" ", "");
		workbook.createSheet(tempsheetName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(QCExcelPath));
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fos.close();

		InputStream inp = new FileInputStream(QCExcelPath);
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheetAt(0);
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		//style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLACK.index);
		style.setFont(font);
		Cell cell;
		Row row;
		for (int i = 0; i < 1500; i++) {
			sheet.createRow(i);
			row = sheet.getRow(i);
			for (int j = 0; j < 255; j++) {
				row.createCell(j);
			}
		}

		row = sheet.getRow(0);
		cell = row.getCell(0);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue("Test_Name");
		cell.setCellStyle(style);
		cell = row.getCell(1);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue("Status");
		cell.setCellStyle(style);
		cell = row.getCell(2);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue("StepNumber");
		cell.setCellStyle(style);
		cell = row.getCell(3);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue("Updated_In_QC");
		cell.setCellStyle(style);
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(QCExcelPath);
		wb.write(fileOut);
		fileOut.close();

	}

	public void sendLog(String Trace, String testCaseName, LIFOStack<Integer> currTestRowPtrs,
			int currTestRowPtr ) {
			
			// By Shubhank
			String stepId;
			String elementsInStack = currTestRowPtrs.getElementsWithoutPopOut();
			if(elementsInStack==""){
				stepId=currTestRowPtr+"";
			}else{
				stepId = elementsInStack+ "_" + currTestRowPtr;
			}
			
		try {
			// Create file
			FileWriter fstream = new FileWriter(exceptionLog, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.newLine();
			out.write("================================================================================");
			out.newLine();
			out.write("TestCase: " + testCaseName);
			out.newLine();
			out.write("TestStep: " + stepId);
			out.newLine();
			out.write(Trace);
			out.newLine();
			out.write("================================================================================");
			out.newLine();

			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	public void f_sendTestCaseResult(String TCID, String strModulename, String strTestcasename, String strTestcasedesc,
			String StartTime, int status, String failedStep) throws Exception {
		try {
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			Date now = new Date();
			String strEndTime = df.format(now).toString();
			
			TCtimeCalculator = (System.currentTimeMillis() - TCtimeCalculator) / 1000;
		    String format = String.format("%%0%dd", 2);
		    String seconds = String.format(format, TCtimeCalculator % 60);
		    String minutes = String.format(format, (TCtimeCalculator % 3600) / 60);
		    String hours = String.format(format, TCtimeCalculator / 3600);
		    String time =  hours + ":" + minutes + ":" + seconds;
			
			BufferedWriter out_result = new BufferedWriter(new FileWriter(reportDate, true));
			out_result.newLine();
			String passTxt = "<td>PASSED</td>", failTxt = "<td>FAILED</td>";
			if (!(sauceSession.isEmpty() || sauceSession.equals(""))) {
				failTxt = "<td><a href=\"" + sauceSessionLink + "\">FAILED</a>" + "</td>";
				passTxt = "<td><a href=\"" + sauceSessionLink + "\">PASSED</a>" + "</td>";
			}
			if (status == 0) {
				out_result.write("<tr style=" + "\"" + "color:Red" + "\"" + ">" + "<td>" + strModulename + "</td>"
						+ "<td>" + TCID + "</td><td>" + strTestcasename + "</td><td>" + strTestcasedesc + "</td>"
//						+ "<td>" + StartTime + "</td>" + "<td>" + strEndTime + "</td>" + failTxt + "<td>" + failedStep
						+ "<td colspan = '2'> Duration: "+ time+ " </td>" + failTxt + "<td>" + failedStep
						+ "</td></tr>");
				//r.jobFailed(sauceJobId);
				failedTCount = failedTCount + 1;
			} else {
				out_result.write("<tr style=" + "\"" + "color:Green" + "\"" + ">" + "<td>" + strModulename + "</td>"
						+ "<td>" + TCID + "</td><td>" + strTestcasename + "</td>" + "<td>" + strTestcasedesc + "</td>"
						//+ "<td>" + StartTime + "</td>" + "<td>" + strEndTime + "</td>" + passTxt + "</tr>");
						+ "<td colspan = '2'> Duration: "+ time+ " </td>" + passTxt + "</tr>");
				//r.jobPassed(sauceJobId);
				passedTCount = passedTCount + 1;
			}

			out_result.close();
		} catch (IOException e) {
		}
	}

	public void f_sendTestStepResult(String TCID, String strModulename, String strTestcasename, LIFOStack<Integer> currTestRowPtrs,
                    int currTestRowPtr, String strScreen, String strActionName, String strFieldname, String strValue, int intStatus)
                    throws Exception{
             try {
                    
                    String stepId;
                    String elementsInStack = currTestRowPtrs.getElementsWithoutPopOut();
                    if(elementsInStack==""){
                           stepId=currTestRowPtr+"";
                    }else{
                           stepId = elementsInStack+ "_" + currTestRowPtr;
                    }

                    BufferedWriter out_detailedResult = new BufferedWriter(new FileWriter(reportLog, true));
                    out_detailedResult.newLine();
                    String strExpected, strActualResult;
                    String scrLink;
                    String scrpath = dt1;
                    strExpected = Func_ExpectedResult(strActionName, strFieldname, strValue);
                    System.out.println(strExpected);
                    strActualResult = Func_ActualResult(strActionName, strFieldname, strValue, intStatus);
                    WebDriver augmentedDriver;
                    String passTxt = "<td>PASSED</td>", failTxt = "<td>FAILED</td>";
                    if (!(sauceSession.isEmpty() || sauceSession.equals(""))) {
                           failTxt = "<td><a href=\"" + sauceSessionLink + "\">FAILED</a>" + "</td>";
                           passTxt = "<td><a href=\"" + sauceSessionLink + "\">PASSED</a>" + "</td>";
                    }
                    if (intStatus == 0) {

                           /*
                           * if(platform.equalsIgnoreCase("Chrome")) { augmentedDriver =
                           * new Augmenter().augment(driver); scrFile = ((TakesScreenshot)
                           * augmentedDriver).getScreenshotAs(OutputType.FILE); } else {
                           */
                           // screenShoot =
                           // ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                           // }

                           scrpath = scrpath.replaceAll(":", "_");
                           int m = (int) (10 + 9999 * Math.random());
                           System.out.println("TestError: Failed Manual TestStep :" + failedStep);
                           String TCName = strTestcasename.replaceAll(" ", "_");
                           TCName = TCName.replaceAll("[^\\p{L}\\p{Nd}\\^_]", "");
                           int indx = TCName.length();
                           if (indx > 30) {
                                 TCName = TCName.substring(1, 30);
                           }
                           scrshot = "[1]" + TCName + "_" + "Step_" + failedStep + "_" + m;
                           scrshot = scrshot.replaceAll(" ", "");

                           String scrlinkkk = "ScreenShots" + File.separator + scrshot + ".png";
                           scrshot = scrshtPath + File.separator + scrshot + ".png";

                           try{
                                 ImageIO.write(screenShoot.getImage(),"PNG",new File(scrshot));
                           } catch(Exception e){
                                 System.err.println("No screenshot captured");
                           }
                           scrLink = "<a href=";

                           scrLink = scrLink + scrlinkkk;
                           scrLink = scrLink + ">ScreenShot</a>";
                           out_detailedResult.write("<tr style=" + "\"" + "color:Red" + "\"" + ">" + "<td>" + strModulename
                                        + "</td>" + "<td>" + TCID + "</td><td>" + strTestcasename + "</td><td>" + stepId
                                        + "</td>" + "<td>" + strScreen + "</td>" + "<td>" + strFieldname + "</td>" + "<td>" + strValue
                                        + "</td>" + "<td>" + strExpected + "</td>" + "<td>" + strActualResult + "</td>" + failTxt
                                        + "<td>" + scrLink + "</td>" + "</td></tr>");


                    } 

                    else   {

                           if(strActionName.equalsIgnoreCase("captureScreenshots"))
                           {
                                 String timetxt =  new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss").format(new Date());

                                // String scrshot=scrshtPath+File.separator+timetxt+".png";
                                 //String scrlinkkk = "ScreenShots" + File.separator + scrshot ;
                                 

                                 String scrlinkkk = "ScreenShots" + File.separator + timetxt + ".png";
                                 scrshot = scrshtPath + File.separator + timetxt + ".png";
                                 
                                 File CapturedScreenshots = new File(scrshot);
                                 try{
                                        ImageIO.write(screenShoot.getImage(),"PNG",CapturedScreenshots);
                                        // FileUtils.copyFile(CapturedScreenshots, new File("./SeleniumFramework/"+File.separator+"./Shubhank/"+zipdate+".png"));
                                        System.out.println("image captured");

                                 }

                                 catch(Exception e)
                                 {
                                        System.err.println("No screenshot captured");

                                 }



                                 scrLink = "<a href=";
                                 // scrLink = scrLink+ "file:///"+scrshot;
                                 scrLink = scrLink + scrlinkkk;
                                 scrLink = scrLink + ">ScreenShot</a>";

                                 out_detailedResult.write("<tr style=" + "\"" + "color:Green" + "\"" + ">" + "<td>" + strModulename
                                               + "</td>" + "<td>" + TCID + "</td><td>" + strTestcasename + "</td><td>" + stepId
                                               + "</td>" + "<td>" + strScreen + "</td>" + "<td>" + strFieldname + "</td>" + "<td>" + strValue
                                               + "</td>" + "<td>" + strExpected + "</td>" + "<td>" + strActualResult + "</td>" + passTxt
                                               + "<td>" + scrLink + "</td>" + "</td></tr>");
                           }


                           out_detailedResult.write("<tr style=" + "\"" + "color:Green" + "\"" + ">" + "<td>" + strModulename
                                        + "</td>" + "<td>" + TCID + "</td><td>" + strTestcasename + "</td><td>" + stepId
                                        + "</td>" + "<td>" + strScreen + "</td>" + "<td>" + strFieldname + "</td>" + "<td>" + strValue
                                        + "</td>" + "<td>" + strExpected + "</td>" + "<td>" + strActualResult + "</td>" + passTxt
                                        + "</tr>");
                    }
                    out_detailedResult.close();




             }
             catch (IOException e) {
                    e.printStackTrace();
             }
       }

	
	//by Shubhank
	public void f_sendFunctionalResult(String strModulename, String TCID, String strTestcasename,
			String strScreen, String moduleNameWithStatus)
			throws Exception {
		try {
			BufferedWriter out_detailedResult = new BufferedWriter(new FileWriter(reportLog, true));
			out_detailedResult.newLine();

			out_detailedResult.write("<tr style=" + "\"" + "color:Blue" + "\"" + ">" + "<td>" + strModulename
					+ "</td>" + "<td>" + TCID + "</td><td>" + strTestcasename + "</td><td>" + ""
					+ "</td>" + "<td>" + strScreen + "</td>" + "<td>" + moduleNameWithStatus + "</td>" + "<td>" + ""
					+ "</td>" + "<td>" + "" + "</td>" + "<td>" + "" + "</td>" 
					+ "<td>" + "" + "</td>" + "</td></tr>");

			out_detailedResult.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private static BufferedImage resize(BufferedImage image, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}

	public String Func_ExpectedResult(String strActionName, String strFieldname, String strValue) throws Exception {
		// System.out.println(strActionName);
		// System.out.println(strActionName.equalsIgnoreCase("CLICK"));
		int VarSwtch;
		VarSwtch = 0;
		String strExpected = null;
		if (strActionName.equalsIgnoreCase("CLICK")) {
			VarSwtch = 1;
		}
		if (strActionName.equalsIgnoreCase("OPENURL")) {
			VarSwtch = 2;
		}
		if (strActionName.equalsIgnoreCase("VALIDATE")) {
			VarSwtch = 3;
		}
		if (strActionName.equalsIgnoreCase("VERIFY")) {
			VarSwtch = 4;
		}
		if (strActionName.equalsIgnoreCase("VerifyURL")) {
			VarSwtch = 5;
		}
		if (strActionName.equalsIgnoreCase("INPUT")) {
			VarSwtch = 6;
		}
		if (strActionName.equalsIgnoreCase("SelectIndxValTxt")) {
			VarSwtch = 7;
		}
		if (strActionName.equalsIgnoreCase("SetCheckBox")) {
			VarSwtch = 8;
		}
		if (strActionName.equalsIgnoreCase("CheckNotExist")) {
			VarSwtch = 9;
		}
		if (strActionName.equalsIgnoreCase("CallFunction")) {
			VarSwtch = 10;
		}
		if (strActionName.equalsIgnoreCase("VerifyFalseEleExist")) {
			VarSwtch = 11;
		}
		if (strActionName.equalsIgnoreCase("KeyEvent")) {
			VarSwtch = 12;
		}
		if (strActionName.equalsIgnoreCase("Validate_Element_Color")) {
			VarSwtch = 13;
		}
		if (strActionName.equalsIgnoreCase("Validate_Font_Size")) {
			VarSwtch = 14;
		}
		if (strActionName.equalsIgnoreCase("Validate_Element_Size")) {
			VarSwtch = 15;
		}
		if (strActionName.equalsIgnoreCase("Validate_Font_Type")) {
			VarSwtch = 16;
		}
		if (strActionName.equalsIgnoreCase("Validate_Background_Color")) {
			VarSwtch = 17;
		}
		if (strActionName.equalsIgnoreCase("Wait")) {
			VarSwtch = 18;
		}
		if (strActionName.equalsIgnoreCase("Validate_Title")) {
			VarSwtch = 19;// CheckByIndex
		}

		if (strActionName.equalsIgnoreCase("CheckByIndex")) {
			VarSwtch = 20;
		}

		if (strActionName.equalsIgnoreCase("SetCheckBox")) {
			VarSwtch = 21;
		}

		if (strActionName.equalsIgnoreCase("ClearAndType")) {
			VarSwtch = 22;
		}

		if (strActionName.equalsIgnoreCase("Clear")) {
			VarSwtch = 23;
		}

		if (strActionName.equalsIgnoreCase("VerifyElementExists")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyElementByValue")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyElementProperty")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyElementProperty")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyTextPresent")) {
			VarSwtch = 4;
		}

		if (strActionName.equalsIgnoreCase("VerifyLink")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyMultiLinks")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyAlertText")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyPageSource")) {
			VarSwtch = 24;
		}

		if (strActionName.equalsIgnoreCase("ClosewindowByTitle")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("SwitchToWindow")) {
			VarSwtch = 25;
		}

		if (strActionName.equalsIgnoreCase("VerifywindowTitle")) {
			VarSwtch = 26;
		}

		if (strActionName.equalsIgnoreCase("Actionclick")) {
			VarSwtch = 1;
		}

		if (strActionName.equalsIgnoreCase("MouseHoverclick")) {
			VarSwtch = 1;
		}

		if (strActionName.equalsIgnoreCase("OptionalClick")) {
			VarSwtch = 1;
		}

		if (strActionName.equalsIgnoreCase("JsClick")) {
			VarSwtch = 1;
		}
		
		if (strActionName.equalsIgnoreCase("WriteXMLFromDB")) {
			VarSwtch = 27;
		}
		
		if (strActionName.equalsIgnoreCase("VerifyFromXML")) {
			VarSwtch = 28;
		}
		
		if (strActionName.equalsIgnoreCase("HoldelementText")) {
			VarSwtch = 29;
		}	

		if (strActionName.equalsIgnoreCase("verifyTextContains")) {
			VarSwtch = 12;
		}                

		if (strActionName.equalsIgnoreCase("VerifyListValues")) {
			VarSwtch = 12;

		}	
		if (strActionName.equalsIgnoreCase("verifyFromDatabase")) {
			VarSwtch = 30;

		}	
		
		

		switch (VarSwtch) {
		case 1:
			strExpected = "click on " + strFieldname;
			break;
		case 2:
			strExpected = "Browser Should be Navigated to " + url;
			break;
		case 3:
			strExpected = "Field  " + strFieldname + " should be validated with value:" + strValue;
			break;
		case 4:
			strExpected = "Field  " + strFieldname + " is visible and enabled on the screen";
			break;
		case 5:
			strExpected = "Current URL value should be : " + strValue;
			break;
		case 6:
			strExpected = "The Text " + strValue + " should be passed into field " + strFieldname;
			break;
		case 7:
			strExpected = "Item " + strValue + " should be selected into dropdown " + strFieldname;
			break;
		case 8:
			strExpected = "Checkbox " + strFieldname + " should be " + strValue + "ed";
			break;
		case 9:
			strExpected = strFieldname + " should not exist on current screen";
			break;
		case 10:
			strExpected = strFieldname + " should be called";
			break;
		case 11:
			strExpected = strValue + " should not be present at Page";
			break;
		case 12:
			strExpected = strValue + " should be pressed at element : " + strFieldname;
			break;
		case 13:
			strExpected = "Color of Element : " + strFieldname + " should be : " + strValue;
			break;
		case 14:
			strExpected = "Font size of " + strFieldname + " should be : " + strValue;
			break;
		case 15:
			strExpected = "Size(Height x Width) of " + strFieldname + " should be : " + strValue;
			break;
		case 16:
			strExpected = "Font type of text : " + strFieldname + " should be : " + strValue;
			break;
		case 17:
			strExpected = "Background color of " + strFieldname + " should be : " + strValue;
			break;
		case 18:
			strExpected = "A wait of " + Float.parseFloat(strValue) / 1000 + " seconds should be applied";
			break;
		case 19:
			strExpected = "Page title should be validated with the Text: " + strValue;
			break;

		case 20:
			strExpected = "Checkbox by Index " + strFieldname + " should be " + strValue + "ed";
			break;
		case 21:
			strExpected = "Set Checkbox " + strFieldname + " should be " + strValue + "ed";
			break;
		case 22:
			strExpected = "Clear and Enter field " + strFieldname + " should be " + strValue + "ed";
			break;
		case 23:
			strExpected = "Clear" + strFieldname + " should be " + strValue + "ed";
			break;
		case 24:
			strExpected = "Page Source" + strFieldname + " should be " + strValue + "ed";
			break;
		case 25:
			strExpected = "Switch to Window " + strFieldname + " should be " + strValue + "ed";
			break;
		case 26:
			strExpected = "Verify Window " + strFieldname + " should be " + strValue + "ed";
			break;
		case 27:
			strExpected = "XML is fetched from Database and stored in project directory";
			break;
			
		case 28:
			strExpected = "Values from Stored XML are compared";
			break;
		
		case 29:
			strExpected = "HoldValue for" + strFieldname + " should be " + strValue;
			break;
		
		case 30:
			strExpected = "Database expected value should be " + strValue;
			break;
			

		}// }catch(Exception e){

		// }
		return strExpected;

	}

	public String Func_ActualResult(String strActionName, String strFieldname, String strValue, int intStatus)
			throws Exception {
		int VarSwtch;
		VarSwtch = 0;
		String strActual = null;
		if (strActionName.equalsIgnoreCase("CLICK")) {
			VarSwtch = 1;
		}
		if (strActionName.equalsIgnoreCase("OPENURL")) {
			VarSwtch = 2;
		}
		if (strActionName.equalsIgnoreCase("VALIDATE")) {
			VarSwtch = 3;
		}
		if (strActionName.equalsIgnoreCase("VERIFY")) {
			VarSwtch = 4;
		}
		if (strActionName.equalsIgnoreCase("VerifyURL")) {
			VarSwtch = 5;
		}
		if (strActionName.equalsIgnoreCase("INPUT")) {
			VarSwtch = 6;
		}
		if (strActionName.equalsIgnoreCase("SelectIndxValTxt")) {
			VarSwtch = 7;
		}
		if (strActionName.toUpperCase().equalsIgnoreCase("SetCheckBox")) {
			VarSwtch = 8;
		}
		if (strActionName.equalsIgnoreCase("CheckNotExist")) {
			VarSwtch = 9;
		}
		if (strActionName.equalsIgnoreCase("CallFunction")) {
			VarSwtch = 10;
		}
		if (strActionName.equalsIgnoreCase("VerifyFalseEleExist")) {
			VarSwtch = 11;
		}
		if (strActionName.equalsIgnoreCase("KeyEvent")) {
			VarSwtch = 12;
		}
		if (strActionName.equalsIgnoreCase("Validate_Element_Color")) {
			VarSwtch = 13;
		}
		if (strActionName.equalsIgnoreCase("Validate_Font_Size")) {
			VarSwtch = 14;
		}
		if (strActionName.equalsIgnoreCase("Validate_Element_Size")) {
			VarSwtch = 15;
		}
		if (strActionName.equalsIgnoreCase("Validate_Font_Type")) {
			VarSwtch = 16;
		}
		if (strActionName.equalsIgnoreCase("Validate_Background_Color")) {
			VarSwtch = 17;
		}
		if (strActionName.equalsIgnoreCase("Wait")) {
			VarSwtch = 18;
		}
		if (strActionName.equalsIgnoreCase("Validate_Title")) {
			VarSwtch = 19;
		}
		if (strActionName.equalsIgnoreCase("CheckByIndex")) {
			VarSwtch = 20;
		}

		if (strActionName.equalsIgnoreCase("SetCheckBox")) {
			VarSwtch = 21;
		}

		if (strActionName.equalsIgnoreCase("ClearAndType")) {
			VarSwtch = 22;
		}

		if (strActionName.equalsIgnoreCase("Clear")) {
			VarSwtch = 23;
		}

		if (strActionName.equalsIgnoreCase("VerifyElementExists")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyElementByValue")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyElementProperty")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyElementProperty")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyTextPresent")) {
			VarSwtch = 4;
		}

		if (strActionName.equalsIgnoreCase("VerifyLink")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyMultiLinks")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyAlertText")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("VerifyPageSource")) {
			VarSwtch = 24;
		}

		if (strActionName.equalsIgnoreCase("ClosewindowByTitle")) {
			VarSwtch = 3;
		}

		if (strActionName.equalsIgnoreCase("SwitchToWindow")) {
			VarSwtch = 25;
		}

		if (strActionName.equalsIgnoreCase("VerifywindowTitle")) {
			VarSwtch = 26;
		}

		if (strActionName.equalsIgnoreCase("Actionclick")) {
			VarSwtch = 1;
		}

		if (strActionName.equalsIgnoreCase("MouseHoverclick")) {
			VarSwtch = 1;
		}

		if (strActionName.equalsIgnoreCase("OptionalClick")) {
			VarSwtch = 1;
		}

		if (strActionName.equalsIgnoreCase("JsClick")) {
			VarSwtch = 1;
		}
		

		if (strActionName.equalsIgnoreCase("WriteXMLFromDB")) {
			VarSwtch = 27;
		}
		
		if (strActionName.equalsIgnoreCase("VerifyFromXML")) {
			VarSwtch = 28;
		}
		
		if (strActionName.equalsIgnoreCase("HoldelementText")) {
			VarSwtch = 29;
		}
		
		if (strActionName.equalsIgnoreCase("verifyTextContains")) {
			VarSwtch = 12;
		}
		if(strActionName.equalsIgnoreCase("VerifyListValues")) {

			VarSwtch = 12;	
		}
		if (strActionName.equalsIgnoreCase("verifyFromDatabase")) {
			VarSwtch = 30;
		}

		String tgName = "";
		String val = "";
		if (VarSwtch == 3) {

			try {
				tgName = validate.getTagName();
			} catch (Exception e) {
				tgName = "";
			}
		}
		if (VarSwtch == 19) {

			try {
				val = driver.getTitle();
			} catch (Exception e) {
				val = "Page Not found";
			}
		}

		if (intStatus == 1) {
			switch (VarSwtch) {
			case 1:
				strActual = "Succesfully click on " + strFieldname;
				break;
			case 2:
				strActual = "Successfully  Navigated to " + strFieldname;
				break;
			case 3:
				strActual = "Successfully Field  " + strFieldname + " is validated with value: "
						+ (tgName.equals("") ? " Element Exists"
								: ((validate.getTagName().trim().equalsIgnoreCase("input"))
										? validate.getAttribute("value") : validate.getText()));
				break;
			case 4:
				strActual = "Field  " + strFieldname + " is visible and enabled on the screen";
				break;
			case 5:
				strActual = "Current URL value is  :" + vurl;
				break;
			case 6:
				strActual = "Successfully Text " + strValue + " is entered into field " + strFieldname;
				break;
			case 7:
				strActual = "Item " + strValue + " is selected into dropdown " + strFieldname;
				break;
			case 8:
				strActual = "Checkbox " + strFieldname + " is " + strValue + "ed";
				break;
			case 9:
				strActual = strFieldname + " doesn't exist on current screen";
				break;
			case 10:
				strActual = strFieldname + " is called";
				break;
			case 11:
				strActual = strValue + " is not present on page";
				break;
			case 12:
				strActual = "Key: " + strValue + " is pressed at element : " + strFieldname;
				break;
			case 13:
				strActual = "Color of Element : " + strFieldname + " is : " + validate1;
				break;
			case 14:
				strActual = "Font size of " + strFieldname + " is : " + validate1;
				break;
			case 15:
				strActual = "Size(Height x Width) of " + strFieldname + " is : " + validate1;
				break;
			case 16:
				strActual = "Font type of text : " + strFieldname + " is : " + validate1;
				break;
			case 17:
				strActual = "Background color of " + strFieldname + " is : " + validate1;
				break;
			case 18:
				strActual = "A wait of " + Float.parseFloat(strValue) / 1000 + " seconds is applied";
				break;
			case 19:
				strActual = "Page title is equal to : " + strValue;
				break;

			case 20:
				strActual = "Checkbox by Index " + strFieldname + " is" + strValue + "ed";
				break;
			case 21:
				strActual = "Set Checkbox " + strFieldname + " should be " + strValue + "ed";
				break;
			case 22:
				strActual = "Clear and Enter field " + strFieldname + " should be " + strValue + "ed";
				break;
			case 23:
				strActual = "Clear" + strFieldname + " should be " + strValue + "ed";
				break;
			case 24:
				strActual = "Page Source" + strFieldname + " should be " + strValue + "ed";
				break;
			case 25:
				strActual = "Switch to Window " + strFieldname + " should be " + strValue + "ed";
				break;
			case 26:
				strActual = "Verify Window " + strFieldname + " should be " + strValue + "ed";
				break;
			case 27:
				strActual = "XML is successfully saved";
				break;
			case 28:
				strActual = "Value is successfully verified in XML";
				break;
			case 29:
				strActual = "Value for field " + strFieldname + " is " + strValue;
				break;
			case 30:	
				strActual = "Values are matched with database field " + strValue;
				break;			
				

			}
		} else {
			switch (VarSwtch) {
			case 1:
				strActual = "Failed to click on " + strFieldname;
				break;
			case 2:
				strActual = "Failed to  Navigate to " + strFieldname;
				break;
			case 3:
				strActual = "Failed to validate  Field  " + strFieldname + " with "
						+ (tgName.equals("") ? " Element does not Exist"
								: ((validate.getTagName().trim().equalsIgnoreCase("input"))
										? validate.getAttribute("value") : validate.getText()));
				break;
			case 4:
				strActual = "Field  " + strFieldname + " is not visible and enabled on the screen";
				break;
			case 5:
				strActual = "Current URL value is  : " + vurl;
				break;
			case 6:
				strActual = "Failed to enter Text " + strValue + "  into field " + strFieldname;
				break;
			case 7:
				strActual = "Item " + strValue + " is not selected into dropdown " + strFieldname;
				break;
			case 8:
				strActual = "Checkbox " + strFieldname + " is not " + strValue + "ed";
				break;
			case 9:
				strActual = strFieldname + " exists on current screen while it should not exist";
				break;
			case 10:
				strActual = "Error occurred while calling " + strFieldname + " method";
				break;
			case 11:
				strActual = strValue + " is present on page while it should not be present at given page";
				break;
			case 12:
				strActual = "Key: " + strValue + " is NOT pressed at element : " + strFieldname;
				break;
			case 13:
				strActual = "Actual Color of Element : " + strFieldname + " is : " + validate1;
				break;
			case 14:
				strActual = "Actual Font size of " + strFieldname + " is : " + validate1;
				break;
			case 15:
				strActual = "Actual Size(Height x Width) of " + strFieldname + " is : " + validate1;
				break;
			case 16:
				strActual = "Actual Font type of text : " + strFieldname + " is : " + validate1;
				break;
			case 17:
				strActual = "Actual Background color of " + strFieldname + " is : " + validate1;
				break;
			case 18:
				strActual = "A wait of " + Float.parseFloat(strValue) / 1000 + " seconds is not applied";
				break;
			case 19:
				strActual = "Page title is equal to : " + val + "while it should be : " + strValue;
				break;

			case 20:
				strActual = "Failed to click on Checkbox by Index " + strFieldname + " is" + strValue + "ed";
				break;
			case 21:
				strActual = "Failed to Set Checkbox " + strFieldname + " should be " + strValue + "ed";
				break;
			case 22:
				strActual = "Failed to Clear and Enter field " + strFieldname + " should be " + strValue + "ed";
				break;
			case 23:
				strActual = "Failed to Clear" + strFieldname + " should be " + strValue + "ed";
				break;
			case 24:
				strActual = "Failed to get Page Source" + strFieldname + " should be " + strValue + "ed";
				break;
			case 25:
				strActual = "Failed to Switch to Window " + strFieldname + " should be " + strValue + "ed";
				break;
			case 26:
				strActual = "Failed to Verify Window " + strFieldname + " should be " + strValue + "ed";
				break;

			case 27:
				strActual = "Failed to save XML";
				break;

			case 28:
				strActual = "Failed to Verify value in XML";
				break;

			case 29:
				strActual = "Failed to hold value for" + strFieldname ;
				break;	
			case 30:	
				strActual = "Values are not matched with database field " + strValue;
				break;		
			}
		}

		return strActual;

	}

	/**
	 * Updates Test Reports to QC if required.
	 */
	public void updateTestReportaToQC() {

		try {
			// String qcvbPath =
			// "C:\\Selenium_Framework\\SeleniumFramework\\Test_Data\\UpdateQC.vbs";
			String qcvbPath = "SeleniumFramework" + File.separator + "Test_Data" + File.separator + "UpdateQC.vbs";
			String aa = "cmd /c start" + " " + qcvbPath;
			System.out.println(aa);
			Runtime.getRuntime().exec(aa);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method zips the final test reports and sends the mail if required.
	 * 
	 * @param seleniumHandler
	 * @throws Exception
	 */
	public void zipReportsAndSendMail(FunctionalLibrary seleniumHandler) throws Exception {
		String reportzip;
		Date now = new Date();
		zipdate = DateFormat.getDateTimeInstance().format(now).toString();
		zipdate = zipdate.replaceAll(":", "_");
		// File zipfolder = new File("c:\\SeleniumFramework_Report");
		File zipfolder = new File("SeleniumFramework_Report");
		if (!zipfolder.exists()) {
			zipfolder.mkdir();
		}
		// reportzip="c:\\SeleniumFramework_Report\\Report_" + zipdate + ".zip";
		reportzip = "SeleniumFramework_Report" + File.separator + "Report_" + zipdate + ".zip";
		zipDir(reportzip, htmlRep, zipdate);
		System.out.println("Total Testcases Executed: " + seleniumHandler.totalTCount);
		System.out.println("Failed Test Cases: " + failedTCount);
		if (sendMail.equalsIgnoreCase("YES")) {
			if (mailinglist.isEmpty() || mailinglist.equals(" ")) {
				System.out.println("NO email ID is given to send report");
			} else {
				if (mailsubject.isEmpty() || mailsubject.equals(" ")) {
					// System.out.println("Sending email without subject....");
					mailsubject = "Test Execution Report";
				}
				File file = new File(reportzip);
				file = file.getAbsoluteFile();
				reportzip = file.getCanonicalPath();
				file = new File(outlookvbspath);
				file = file.getAbsoluteFile();
				outlookvbspath = file.getCanonicalPath();
				System.out.println(reportzip);
				System.out.println(outlookvbspath);
				String[] arrobj = outlookvbspath.split("[\\\\]");
				String[] arrobj2 = reportzip.split("\\\\");
				int kt = arrobj.length;
				int i = 0;
				for (i = 0; i < kt; i++) {
					System.out.println(arrobj[i]);
					if (arrobj[i].contains(" ")) {
						arrobj[i] = "\"" + arrobj[i] + "\"";
					}
				}

				kt = arrobj2.length;
				for (i = 0; i < kt; i++) {
					System.out.println(arrobj2[i]);
					if (arrobj2[i].contains(" ")) {
						arrobj2[i] = "\"" + arrobj2[i] + "\"";
					}
				}
				String vbspath = StringUtils.join(arrobj, "\\");
				String zippath = StringUtils.join(arrobj2, "\\");
				System.out.println(vbspath);
				System.out.println(zippath);
				// reportzip = file.getPath();
				// System.out.println(file);
				String args = zippath + " " + seleniumHandler.totalTCount + " " + passedTCount + " " + failedTCount
						+ " " + platform + " " + "\"" + mailsubject + "\"" + " " + "\"" + mailinglist + "\"";
				System.out.println(args);
				String aa = "cmd /c start" + " " + vbspath + " " + args;
				System.out.println(aa);
				Runtime.getRuntime().exec(aa);
				Thread.sleep(3000);
			}
			File deldr = new File("c:\\Test_Reports_" + zipdate);
			deleteDir(deldr);
		}
	}

	public void f_sendTestStepResultIteration(String strTestcasename) throws Exception {

		try {

			BufferedWriter out_detailedResultIteration = new BufferedWriter(new FileWriter(reportLog, true));
			out_detailedResultIteration.newLine();
			String nn = "<tr><th>" + strTestcasename
					+ "</th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>";

			out_detailedResultIteration.write(nn);
			// out_detailedResult.write("<tr
			// style="+"\""+"color:Blue"+"\""+"><td
			// style="+"\""+"width:100%"+"\">"+strTestcasename+"</td></tr>");
			out_detailedResultIteration.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void f_sendTestSuiteResultUA(String temp) {
		try {

			BufferedWriter out_result = new BufferedWriter(new FileWriter(reportDate, true));
			out_result.newLine();

			// out_detailedResult.write("<tr
			// style="+"\""+"color:Blue"+"\""+"><td
			// style="+"\""+"color:Blue"+"\""+">"+strTestcasename+"</td></tr>");
			out_result.write("<tr style=" + "\"" + "color:Red" + "\"" + "><td style=" + "\"" + "width:100%" + "\">"
					+ temp + "</td></tr>");
			out_result.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public static void funExcelResult(String TCID, String strTestcasename, String strModulename, int status,
			String BrowserName, int TFAIL, int TPASS, String URL, String AlmTestCasePath, String AlmSuitePath) {
		int COUNT = 0;
		String Tstatus = "";
		HSSFRow row = worksheet.createRow(rnum);
		if (rnum == 1) {

			HSSFRow row1 = worksheet.createRow(0);
			HSSFCell cellA1 = row1.createCell(0);
			cellA1.setCellValue("TCID");

			HSSFCell cellA2 = row1.createCell(1);
			cellA2.setCellValue("TESTCASENAME");

			HSSFCell cellA3 = row1.createCell(2);
			cellA3.setCellValue("RESULT");

			HSSFCell cellA4 = row1.createCell(3);
			cellA4.setCellValue("BROWSER");

			HSSFCell cellA5 = row1.createCell(4);
			cellA5.setCellValue("TESTSTATUS");

			HSSFCell cellA6 = row1.createCell(5);
			cellA6.setCellValue("TESTCOUNT");

			HSSFCell cellA7 = row1.createCell(6);
			cellA7.setCellValue("TPASS");

			HSSFCell cellA8 = row1.createCell(7);
			cellA8.setCellValue("TFAIL");

			HSSFCell cellA9 = row1.createCell(8);
			cellA9.setCellValue("TESTURL");

			HSSFCell cellA10 = row1.createCell(9);
			cellA10.setCellValue("ALM_TC_PATH");

			HSSFCell cellA11 = row1.createCell(10);
			cellA11.setCellValue("ALM_SUITE_PATH");

		}

		if (status == 0) {

			Tstatus = "FAILED";
			COUNT = failedTCount;

		} else {

			Tstatus = "PASSED";
			// passedTCount = passedTCount + 1;
			sumFail = 1;
			COUNT = passedTCount;
		}

		HSSFCell cellA1 = row.createCell(0);
		cellA1.setCellValue(TCID);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
		//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellA1.setCellStyle(cellStyle);

		HSSFCell cellA2 = row.createCell(1);
		cellA2.setCellValue(strTestcasename);
		cellA2.setCellStyle(cellStyle);

		HSSFCell cellA3 = row.createCell(2);
		cellA3.setCellValue(Tstatus);
		cellA3.setCellStyle(cellStyle);

		HSSFCell cellA4 = row.createCell(3);
		cellA4.setCellValue(BrowserName);
		cellA4.setCellStyle(cellStyle);

		HSSFCell cellA5 = row.createCell(4);
		cellA5.setCellValue(Tstatus);
		cellA5.setCellStyle(cellStyle);

		HSSFCell cellA6 = row.createCell(5);
		cellA6.setCellValue(COUNT);
		cellA6.setCellStyle(cellStyle);

		rnum = rnum + 1;

		HSSFRow row1 = worksheet.getRow(1);
		HSSFCell cellA7 = row1.createCell(6);
		cellA7.setCellValue(TPASS);
		cellA7.setCellStyle(cellStyle);

		HSSFRow row2 = worksheet.getRow(1);
		HSSFCell cellA8 = row2.createCell(7);
		cellA8.setCellValue(TFAIL);
		cellA8.setCellStyle(cellStyle);

		HSSFRow row3 = worksheet.getRow(1);
		HSSFCell cellA9 = row3.createCell(8);
		cellA9.setCellValue(URL);
		cellA9.setCellStyle(cellStyle);
	}
	
	/**
	 * Method getStartTime: this method is called when a new test cases is about
	 * to be executed. It returns String in form of hh:mm:ss
	 * 
	 * @return hh:mm:ss
	 */
	public String getStartTime() {
//		String hh, mm, ss;
//		Calendar cal = new GregorianCalendar();
//		String ist;
//		int HOURS24 = cal.get(Calendar.HOUR_OF_DAY); // 0..23
//		int MIN = cal.get(Calendar.MINUTE); // 0..59
//		int SEC = cal.get(Calendar.SECOND); // 0..59
//		hh = Integer.toString(HOURS24);
//		mm = Integer.toString(MIN);
//		ss = Integer.toString(SEC);
//		ist = hh + ":" + mm + ":" + ss;
//		return ist;
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		TCtimeCalculator= System.currentTimeMillis();
        return sdf.format(cal.getTime());
	}
}