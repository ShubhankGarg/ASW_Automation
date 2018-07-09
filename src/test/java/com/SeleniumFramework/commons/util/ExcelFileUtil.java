package com.SeleniumFramework.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.SeleniumFramework.test.ThreadAssist;

public class ExcelFileUtil extends ThreadAssist{

	//These Variable are defined in Selenium_Utility.xls file.
	/**
	 * testSuite: Contains the path of an excelsheet where names of TestModules are enlisted.test_data
	 * testModuleContainerPath: Contains the path of folder where all test modules are stored.
	 * elementCollection: Contains the path of excelsheet where all objects are stored.
	 * platform: Contains the medium on which the test is to be performed(e.g. Chrome/Android)
	 * uaStrings: Contains the path of excelsheet where all the user strings are enlisted.
	 */
	public static String testSuite;
	public static String testModuleContainerPath;
	public static String elementCollection; 
	public String testElementModulePath;
	public static String environment;
	public static String htmlRep;
	public static String screenShots;
	public static String updateQC;
	public static String platform;
	public static String uaStrings;
	public static String sendMail;
	public static String mailsubject;
	public static String mailinglist;
	public static String outlookvbspath;
	public static String driverInstance;
	public static String mainUrl;
	public static String devUrl;
	public static String productionUrl;
	public static String stageUrl;
	public static String liveUrl;
	public static String offlineUrl;
	public static String test1;
	public static String test2;
	public static String parallelThreadCount;
	public static String suName;
	public static String suPassword;

	//Modified for MA-HIX user
	public String MAHIX_UserId = "";
	public String MAHIX_UserEmailId = "";
	public String MAHIX_UserPassword = "";
	
	
	public static String result_backup_name;
	public static String test_data;
	public static boolean isremoterun;
	public String tmpBrowserVer="";
	public static String db_username, db_password, db_driver, db_url;  //Database attributes
	private static ExcelFileUtil excelFileUtil = null;
	static String folderName;

//	public ExcelFileUtil() {
//		loadSeleniumUtilityFile();
//	}
	
	/**
	* 
	* @modified by Vivek Mamgain
	* @description Copy source results to other another directory with time stamp.
	* @create date 
	* @modified date 06-July-2018
	* 
	*/
	protected static void loadSeleniumUtilityFile() {
		String utilityFilePath = "SeleniumFramework"+File.separator+"Test_Utility"+File.separator+"Selenium_Utility.xls";
		String ApputilityFilePath =
				"SeleniumFramework"+File.separator+"Test_Utility"+File.separator+"Application_Config.xls";
		JiraConfigManager jcm = new JiraConfigManager(System.getProperty("user.dir")+File.separator+"SeleniumFramework"+File.separator+"Test_Utility"+File.separator, "jira.properties");
		folderName = "Regression";
		try {
			jcm.getJiraConfigrationDetails();
			if (!StringUtils.isBlank(JiraConfigManager.JiraId))
				folderName = JiraConfigManager.JiraId;
				
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
			
			System.out.println((new File(ApputilityFilePath).exists()));
			FileInputStream afis = new FileInputStream(ApputilityFilePath);  
			
			POIFSFileSystem apoifs = new POIFSFileSystem(afis);
			HSSFWorkbook aworkbook = new HSSFWorkbook(apoifs);
			HSSFSheet areadsheet = aworkbook.getSheet("TestExecution");// Opens the Location worksheet of Selenium_Utility.xls	
			HSSFSheet SU_Credentials = aworkbook.getSheet("MasterCredentials");
			
			FileInputStream fis = new FileInputStream(utilityFilePath);  
			POIFSFileSystem poifs = new POIFSFileSystem(fis);
			HSSFWorkbook workbook = new HSSFWorkbook(poifs);
			HSSFSheet readsheet = workbook.getSheet("Location");// Opens the Location worksheet of Selenium_Utility.xls	
		
		
		suName = getCellValue(SU_Credentials, 1, 0); // Master User Credentials
		suPassword = getCellValue(SU_Credentials, 1, 1);
		
		System.out.println("Check path for files:");
		testSuite = new File(getCellValue(readsheet,1,1).replace("\\", File.separator)).getCanonicalPath();
		System.out.println(testSuite);
		testModuleContainerPath = new File(getCellValue(readsheet,2,1).replace("\\", File.separator)).getCanonicalPath();
		System.out.println(testModuleContainerPath);
		elementCollection = new File(getCellValue(readsheet,3,1).replace("\\", File.separator)).getCanonicalPath();
		System.out.println(elementCollection);
		environment = getCellValue(readsheet,4,1);
		htmlRep= new File(getCellValue(readsheet, 5,1).replace("\\", File.separator)).getCanonicalPath();	    
		htmlRep = htmlRep+File.separator+folderName;
	    System.out.println(htmlRep);
	    screenShots = new File(getCellValue(readsheet,6,1).replace("\\", File.separator)).getCanonicalPath();
	    updateQC = getCellValue(readsheet,7,1);
	    uaStrings = new File(getCellValue(readsheet, 9,1).replace("\\", File.separator)).getCanonicalPath();
		System.out.println(uaStrings);
	    
	    
	    sendMail = getCellValue(readsheet,10,1);
	    mailsubject=getCellValue(readsheet,11,1);
	    //System.out.println("mailsubject:" + mailsubject);
	    mailinglist=getCellValue(readsheet,12,1);
	   // reportzip=new File(getCellValue(readsheet,12,1)).getCanonicalPath();
	   outlookvbspath=new File(getCellValue(readsheet,14,1)).getCanonicalPath();
       isremoterun = getCellValue(readsheet,18,1).equalsIgnoreCase("Yes")?true:false;
       driverInstance = getCellValue(readsheet,19,1);
		
       	
       platform = getCellValue(areadsheet,1,1);
		mainUrl = getCellValue(areadsheet,2,1);
		devUrl = getCellValue(areadsheet,3,1);
		productionUrl = getCellValue(areadsheet,4,1);
		stageUrl = getCellValue(areadsheet,5,1);
		offlineUrl = getCellValue(areadsheet,6,1);		
		liveUrl = getCellValue(areadsheet,7,1);
		test1 = getCellValue(areadsheet,8,1);
		test2 = getCellValue(areadsheet,9,1);
		parallelThreadCount = getCellValue(areadsheet,10,1);
		test_data = getCellValue(areadsheet,11,1);
		
		result_backup_name = getCellValue(areadsheet,12,1);
		
		// Adding Database keywords
		
		db_username = getCellValue(areadsheet,13,1);
		db_password = getCellValue(areadsheet,14,1);
		db_driver = getCellValue(areadsheet,15,1);
		db_url = getCellValue(areadsheet,16,1);
	    
       createFolder(htmlRep);
	    Thread.sleep(2000);
		if(isremoterun) 
		{
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec("tsdiscon.exe");
		} 
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method getCellValue: Gets the value from excelsheet's cell from a given row and column position and returns it in string form
	 * @param sheet
	 * @param rowPosition
	 * @param columnPosition
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static String getCellValue(HSSFSheet sheet, int rowPosition, int columnPosition) throws IOException 
	{
		String cellFlag  = null;	
		Row row;
		Cell cell = null;
		try{
		row = sheet.getRow(rowPosition);
		cell = row.getCell(columnPosition);
		}catch(Exception e){
			System.out.println("Exception Here===================== >  Thread: "+Thread.currentThread().getName()+"\n"+
					"===================== >  Sheet: "+sheet.getSheetName()+"\n"+
					"===================== >  Exception: "+sheet.getSheetName());
		}
try{
		switch (cell.getCellTypeEnum()) {
		case STRING:
			cellFlag  = cell.getStringCellValue().trim().toString();    
			break; 
		case FORMULA: 		      
	    	cellFlag = cell.getStringCellValue().toString().trim(); 		       
            break;		   
		case NUMERIC: 		    
		   if (DateUtil.isCellDateFormatted(cell)) { 		         
			   cellFlag  = cell.getDateCellValue().toString().trim(); 		     
		   } else { 
			   String[] tempFlag;
		     
			  tempFlag  = Double.toString(cell.getNumericCellValue()).split("\\.");
			  cellFlag=tempFlag[0].trim().toString();
		   } 		     
		   break;	    
		case BLANK:    
		    cellFlag = ""; 		    
		    break; 		    
		case BOOLEAN: 		      
		   cellFlag  = Boolean.toString(cell.getBooleanCellValue()); 		      
		   break; 
		}
		//String flag = cell.toString();
		sheet=null;
		return cellFlag;
}catch(Exception e)
{
	return "";
}
    	
	}

	public static void setCellValue(HSSFSheet sheet, int rowPosition, int columnPosition, String Value) throws IOException 
	{
		Row row,Formattedrow;
		Cell cell = null,Formattedcell=null;
		try{
			Formattedrow=sheet.getRow(rowPosition);
			Formattedcell = Formattedrow.getCell(columnPosition-1);
			row = sheet.getRow(rowPosition);
			cell = row.getCell(columnPosition);
			CellStyle cellStyle = Formattedcell.getCellStyle();
			if (cell==null)
			{
				cell = row.createCell(columnPosition);
				cell.setCellValue(Value);
				cell.setCellStyle(cellStyle);
			} else { 
				cell.setCellValue(Value); 
				}
		}catch(Exception e){
			System.out.println("Exception Here===================== >  Thread: "+Thread.currentThread().getName()+"\n"+
					"===================== >  Sheet: "+sheet.getSheetName()+"\n"+
					"===================== >  Exception: "+sheet.getSheetName());
		}
	}

	public static void zipDir(String zipFileName, String dir, String zipDate) throws Exception
	{
		boolean copyflag = true;

		System.out.println("Source Result Path: " + dir);		
		File sourceLocation = new File(dir); 
		File targetLocation = new File("SeleniumFramework"+File.separator+"Results"+File.separator+folderName+File.separator+"Test_Reports_"+ zipDate);
		copyflag = copyDirectory(sourceLocation,targetLocation);
		Thread.sleep(2000);
		if(copyflag) 
		{
			dir = "SeleniumFramework"+File.separator+"Results"+File.separator+folderName+File.separator+"Test_Reports_" + zipDate;
		}
		File f = new File(zipFileName);
		boolean exists = f.exists();
		if (exists)
		{
			f.delete();			  
		}
		File dirObj = new File(dir);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		System.out.println("Creating : " + zipFileName);
		addDir(dirObj, out);
		out.close();
	}

    public static void addDir(File dirObj, ZipOutputStream out) throws IOException {
    	File[] files = dirObj.listFiles();
		byte[] tmpBuf = new byte[1024];
	    for (int i = 0; i < files.length; i++) {
	    	if (files[i].isDirectory()) {
	    		System.out.println("Adding Directory: "+ files[i].getName());
	    		addDir(files[i], out);
			    continue;
			}
			FileInputStream in = new FileInputStream(files[i].getCanonicalPath());
			System.out.println(" Adding file: " + files[i].getCanonicalPath());
			out.putNextEntry(new ZipEntry(files[i].getCanonicalPath()));
			int len;
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			in.close();
		}
	}
    
    /**
    * 
    * @author Vivek Mamgain
    * @description Copy source results to other another directory with time stamp.
    * @create date 
    * @modified by 06-July-2018
    * @modify date
    */
        public static  boolean copyDirectory(File sourceLocation , File targetLocation) throws IOException {
        boolean dirFlag;
    dirFlag = true;
    try {
    if (sourceLocation.isDirectory()) {
    if (!targetLocation.exists()) {
    targetLocation.mkdir();
    }
    /**
    String[] children = sourceLocation.list();
    for (int i=0; i<children.length; i++) {
    copyDirectory(new File(sourceLocation, children[i]),
        new File(targetLocation, children[i]));
    }
            } else {

            InputStream in = new FileInputStream(sourceLocation);
    OutputStream out = new FileOutputStream(targetLocation);
                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
    }
    in.close();
    out.close();
    */
    String folderName = sourceLocation.getAbsolutePath();
    File[] listDir = new File(folderName).listFiles();

    for (int i = 0; i < listDir.length; i++) {
     
        if (listDir[i].isDirectory()) {
            String fileName = listDir[i].getName();
            if (fileName.contains("_"+platform.toLowerCase()+"_")) {
                System.out.println("found file" + " " + fileName);
                sourceLocation = new File(sourceLocation + File.separator +fileName);
                FileUtils.copyDirectory(sourceLocation, targetLocation);
            }
        }
    }
     
    //sourceLocation = new File(sourceLocation + m.group());
     
    }
    } catch(NullPointerException e) {
    dirFlag = false;
    }
    return dirFlag;
    }
	private static String Lowercase(String platform2) {
		// TODO Auto-generated method stub
		return null;
	}

	public  void writeStepExcel(String previoustc, int testFlag, String failedStep, String updateFlag, String qcExcelPath, int testcaseCounter) throws InvalidFormatException, IOException {
    	InputStream inp = new FileInputStream(qcExcelPath);
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheetAt(0);			    
		Cell cell;
		Row row;
		String status=" ";
		if(testFlag == 1) {
			status = "PASSED";
		} else if(testFlag == 0) {
			status = "FAILED";
		}
		row = sheet.getRow(testcaseCounter);	    
		cell = row.getCell(0);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(previoustc);		    
		cell = row.getCell(1);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(status);		    
		cell = row.getCell(2);
		if(!failedStep.equalsIgnoreCase(" ")) {
			cell.setCellType(CellType.NUMERIC);
			int tempft = Integer.parseInt(failedStep);
			cell.setCellValue(tempft);
		}

       cell = row.getCell(3);
	   cell.setCellType(CellType.STRING);
	   cell.setCellValue(updateFlag);		    
	   // Write the output to a file
	   FileOutputStream fileOut = new FileOutputStream(qcExcelPath);
	   wb.write(fileOut);
	   fileOut.close();
	}

	public static void createFolder(String folderPath)
	{
		System.out.println(folderPath);
		File f = new File(folderPath);
		boolean exists = f.exists();
		  if (!exists) {
			  deleteDir(f);
			  f = new File(folderPath);
			  f.mkdir();
		  }else{
		 f.mkdir();
		  }
	}
	
	
	public static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }	    
	    return dir.delete();
	}

	/**
	 * Method getObject: takes screenName and fieldName as input, matches in element collection sheet and returns object name and its element type.
	 * @param screenName: Acts as foreign key for identifying a unique object in element collection sheet.
	 * @param fieldName:Acts as foreign key for identifying a unique object in element collection sheet.
	 * @return Array returnObject: contains Object name and element type.
	 * @throws IOException if exception occurs .
	 */
	
	public String[] getObject(String screenName, String fieldName) throws IOException {
	   
	    //Open the Object Repository Excel sheet
		FileInputStream EC = new FileInputStream(testElementModulePath);  
		POIFSFileSystem poifs4 = new POIFSFileSystem(EC);
		HSSFWorkbook ORworkbook = new HSSFWorkbook(poifs4);
		HSSFSheet readorsheet = ORworkbook.getSheet(screenName);
		String[] returnObject;              
		returnObject = new String[2];      
	//get the number of used row
	int FLAG_COUNTER = 1;
	int COUNTER = 1;
	String scrName,fName;
	returnObject[0] = "";
	returnObject[1] = "";
	while (FLAG_COUNTER == 1)
	{
	//String	flag = readorsheet.getRow(counter).getCell(3).getStringCellValue().toString().trim();
		  String flag = getCellValue(readorsheet,COUNTER,0).toString();
		 
		  if(flag.equalsIgnoreCase("End")||flag.isEmpty()||flag.equals(""))
		  {
			  FLAG_COUNTER = 0;
		  	}
//		  else if (flag.contains("END OF TESTCASE"))
//			{
//				COUNTER=COUNTER+1;
//			}
		  else
		  {
//			  scrName = getCellValue(readorsheet,COUNTER,0);//readorsheet.getRow(counter).getCell(0).getStringCellValue().trim();
			  fName = getCellValue(readorsheet,COUNTER,0);//readorsheet.getRow(counter).getCell(2).getStringCellValue().trim();
			
			if(fName.equalsIgnoreCase(fieldName))
			{
				returnObject[0] = getCellValue(readorsheet,COUNTER,2);//readorsheet.getRow(counter).getCell(3).getStringCellValue().trim();
				returnObject[1] = getCellValue(readorsheet,COUNTER,1);//readorsheet.getRow(counter).getCell(1).getStringCellValue().trim();
				break;
			}
		}
		COUNTER=COUNTER+1;
	}  
	  
	   return returnObject;
	}
	/**
	* 
	* @author Vivek Mamgain
	* @description Get failed test cases.
	* @create date 06-July-2018
	* @modified by 
	* @modified date 
	*/
	public static String[] getFailedTestCases() throws IOException
	{
		FileInputStream afis = new FileInputStream(System.getProperty("user.dir")+"\\SeleniumFramework\\Test_Excel\\Tester.xls");

		POIFSFileSystem apoifs = new POIFSFileSystem(afis);
		HSSFWorkbook aworkbook = new HSSFWorkbook(apoifs);

		Sheet sheet = aworkbook.getSheet("TestResult");

		// Get row headers
		Row rowheaders = sheet.getRow(0);
		Cell cell;
		int testStatusColumnNo = -1, testCaseNameColumnNo = -1;

		for (int i = 0; i < rowheaders.getLastCellNum(); i++) {
			cell = rowheaders.getCell(i);
			if (cell.getStringCellValue().equals("TESTSTATUS")) {
				testStatusColumnNo = i;
			}
			if (cell.getStringCellValue().equals("TESTCASENAME")) {
				testCaseNameColumnNo = i;
			}
		}

		// Get data from columns
		int totalRows = sheet.getLastRowNum();
		String[] failedTestCases = new String[totalRows];
		int index = 0;
		for (int i = 1; i <= totalRows; i++) {
			Row dataRow = sheet.getRow(i);
			Cell testStatusData = dataRow.getCell(testStatusColumnNo);
			Cell testCaseNameData = dataRow.getCell(testCaseNameColumnNo);

			if (testStatusData.getStringCellValue().equalsIgnoreCase("FAILED")) {
				failedTestCases[index] = testCaseNameData.getStringCellValue();
				index++;
			}
		}
		return failedTestCases;
	}
}
