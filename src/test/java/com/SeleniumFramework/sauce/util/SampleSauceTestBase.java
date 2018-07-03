package com.SeleniumFramework.sauce.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;



import com.SeleniumFramework.commons.util.TestExecutor;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.saucerest.SauceREST;


@RunWith(ConcurrentParameterized.class)
public class SampleSauceTestBase extends TestExecutor implements SauceOnDemandSessionIdProvider {

    public static String seleniumURI;
    public static int sauceMaxConcurrent;
    private static long totalTime;
    private static String startTime;
    public static int moduleRunCount=0;

    public static String buildTag;
    /**
     * Test decorated with @Retry will be run 3 times in case they fail using this rule.
     */
    @Rule
    public RetryRule rule = new RetryRule(3);

    /**
     * Represents the browser to be used as part of the test run.
     */
    protected String browser;
//    /**
//     * Represents the operating system to be used as part of the test run.
//     */
    protected String os;
//    /**
//     * Represents the version of the browser to be used as part of the test run.
//     */
    protected String version;
//    /**
//     * Represents the deviceName of mobile device
//     */
//    protected String deviceName;
//    /**
//     * Represents the device-orientation of mobile device
//     */
//    protected String deviceOrientation;
    
//    protected DesiredCapabilities cap;
    
    /**
     * Instance variable which contains the Sauce Job Id.
     */
    protected String sessionId;

    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param os
     * @param version
     * @param browser
     */

    public SampleSauceTestBase(String browser, String browerVersion, String Platform) {
        super();
        this.os = Platform;
        this.version = browerVersion;
        this.browser = browser;
    }

    


    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the {@link #browser},
     * {@link #version} and {@link #os} instance variables, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
    	System.out.println("Executing Before");
    	
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (browser != null) capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        if (version != null) capabilities.setCapability(CapabilityType.VERSION, version);
//        if (deviceName != null) capabilities.setCapability("deviceName", deviceName);
//        if (deviceOrientation != null) capabilities.setCapability("device-orientation", deviceOrientation);

        capabilities.setCapability(CapabilityType.PLATFORM, os);
		//For using optum shared sauce tunnel
		capabilities.setCapability("parent-tunnel", "sauce_admin");
		capabilities.setCapability("tunnelIdentifier", "OptumSharedTunnel-Stg");
		//This sets the timeout if no input
		capabilities.setCapability("maxDuration", 10800);
        capabilities.setCapability("commandTimeout", 300);
        capabilities.setCapability("idleTimeout", 300);
//        //Way to increase performance
//        capabilities.setCapability("recordVideo", false);
//        capabilities.setCapability("videoUploadOnPass", false);
//        capabilities.setCapability("recordScreenshots", false);
//        capabilities.setCapability("recordLogs", false);
        cap = capabilities;
        if(cap==null){
        	throw new Exception("Capabilites are null for: "+Thread.currentThread().getName());
        	}
        //Getting the build name.
        //Using the Jenkins ENV var. You can use your own. If it is not set test will run without a build id.
        if (buildTag != null) {
            capabilities.setCapability("build", buildTag);
        }
        SauceHelpers.addSauceConnectTunnelId(capabilities);
    }

    @After
    public void tearDown() throws Exception {
    	System.out.println("Executing After");
        try{
        	driver.quit();
        }catch(Exception e){
        	//Do nothing
        }
    }

    /**
     *
     * @return the value of the Sauce Job id.
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }

    @BeforeClass
    public static void setupClass() throws IOException{
    	System.out.println("Executing BeforeClass");
    	
    	
    	String JTESTEXL = "SeleniumFramework"+File.separator+"Test_Excel"+File.separator+"Tester.xls";
		fileOut = new FileOutputStream(JTESTEXL);
		workbook = new HSSFWorkbook();
		worksheet = workbook.createSheet("TestResult");
    }
    
    @AfterClass
    public static void prepareResiduals() throws Exception {
    	System.err.println("Executing AfterClass");
    	Date now = new Date();
		zipdate = DateFormat.getDateTimeInstance().format(now).toString();
		zipdate = zipdate.replaceAll(":", "_");
		File zipfolder = new File("SeleniumFramework"+File.separator+"TestExecutionZip_Reports");
		if (!zipfolder.exists()) {
			zipfolder.mkdir();
		}

		reportzip = "SeleniumFramework"+File.separator+"TestExecutionZip_Reports"+File.separator+"" + result_backup_name + "_" + zipdate
				+ ".zip";
		zipDir(reportzip, htmlRep, zipdate);

		System.out.println("Total Testcases Executed: " + totalTCount);
		System.out.println("Failed Test Cases: " + failedTCount);
		File deldr = new File("SeleniumFramework"+File.separator+"Test_Reports"+File.separator+"Test_Reports_" + zipdate);
		deleteDir(deldr);

		workbook.write(fileOut);
		fileOut.flush();
		fileOut.close();

		JasperReportExecut();
		
        System.out.println("START TIME ==> "+startTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        startTime = sdf.format(cal.getTime());
        System.out.println("END TIME ==> "+startTime);
		
	    totalTime = (System.currentTimeMillis() - totalTime) / 1000;
	    String format = String.format("%%0%dd", 2);
	    String seconds = String.format(format, totalTime % 60);
	    String minutes = String.format(format, (totalTime % 3600) / 60);
	    String hours = String.format(format, totalTime / 3600);
	    String time =  hours + ":" + minutes + ":" + seconds;
		
		System.out.println(">>>>>>>>>>>>>>>   TOTAL TIME: "+time+" <<<<<<<<<<<<<<<<<<<<");
    }
    
    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     * @throws JSONException 
     * @throws InterruptedException 
     * @throws IOException 
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() throws JSONException, InterruptedException, IOException {
    	loadSeleniumUtilityFile();
    	
    	FileInputStream TS = new FileInputStream(testSuite);
		POIFSFileSystem poifs2 = new POIFSFileSystem(TS);
		HSSFWorkbook TSUworkbook = new HSSFWorkbook(poifs2);
		HSSFSheet readtsusheet = TSUworkbook.getSheet("ModuleList");
		int MODULE_COUNT = 1;
		String flag1;
		int COUNTER = 1;
		String executionFlag1;
		
		/**
		 * while loop below checks execution flag and calls keywordDriver
		 * method for each modulename whose flag is yes
		 */
		while (COUNTER == 1) {
			flag1 = getCellValue(readtsusheet, MODULE_COUNT, 0);
			if (flag1.equalsIgnoreCase("End")) {
				COUNTER = 0;
				System.out.println("All Test modules counted");break;
			} else {

				executionFlag1 = getCellValue(readtsusheet, MODULE_COUNT, 3);

				if (executionFlag1.equalsIgnoreCase("Yes")) {
					moduleRunCount=moduleRunCount+1;
				}MODULE_COUNT = MODULE_COUNT+1;
			}
		}
    	
    	totalTime = System.currentTimeMillis();
    	
    	Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        startTime = sdf.format(cal.getTime());
        System.out.println("START TIME ==> "+startTime);
    	
    	System.out.println("Executing ConcurrentParameterized.Parameters");
    	LinkedList browsers = new LinkedList();
    	
    	String brw="";
    	
    	if(parallelThreadCount.equals("")||parallelThreadCount.matches(".*[a-zA-Z]+.*"))parallelThreadCount="1";
		if (!platform.equalsIgnoreCase("sauce"))
			moduleRunCount = 1;
		else if (Integer.parseInt(parallelThreadCount) <= moduleRunCount)
			moduleRunCount = Integer.parseInt(parallelThreadCount);

    	
		if (platform.equalsIgnoreCase("sauce")) {
			SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
			SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
			SauceREST r = new SauceREST(SAUCE_USERNAME, SAUCE_ACCESS_KEY);
			String tunnels = r.getTunnels();
			System.out.println("TUNNEL INFO: " + tunnels.toString());
			String jsonResponse = r.getConcurrency();

			org.json.JSONObject job = new org.json.JSONObject(jsonResponse);
			sauceMaxConcurrent = Integer.parseInt(job.getJSONObject("concurrency").getJSONObject(SAUCE_USERNAME)
					.getJSONObject("remaining").get("overall").toString());
			System.out.println(">>>>>>>>>>>>>>>>>SAUCE CONCURRENT SESSIONS ALLOWED: " + job.getJSONObject("concurrency")
					.getJSONObject(SAUCE_USERNAME).getJSONObject("remaining").get("overall").toString());
		}
    	if(System.getenv("SAUCE_ONDEMAND_BROWSERS")==null){
    		brw = 
    		"[";
//    			for(int i =0;i<sauceMaxConcurrent;i++){
    		for(int i =0;i<moduleRunCount;i++){
    				if(i==0)
    				brw = brw + "{\"os\":\"Windows 2008\",\"platform\":\"VISTA\",\"browser\":\"firefox\",\"browser-version\":\"52\"}";
    				else
    					brw = brw + ",{\"os\":\"Windows 2008\",\"platform\":\"VISTA\",\"browser\":\"firefox\",\"browser-version\":\"52\"}";
    			}
    		brw = brw+"]";
    	} else {
    		
//    		System.out.println("Current Sys Variables: "+ System.getenv());
    		System.out.println("::::::::::::::::::::: SAUCE_ONDEMAND_BROWSERS: "+System.getenv("SAUCE_ONDEMAND_BROWSERS"));
    		String temp = System.getenv("SAUCE_ONDEMAND_BROWSERS").replace("[", "").replace("]", "");
    		System.out.println("EXECUTING FROM ENVIRONMENT VAR FOR BROWSERS");
    		brw = "[";
    		for(int i =0;i<moduleRunCount;i++){
    			if(i==0) brw = brw + temp;
    			else brw = brw +","+temp;
			}
    		brw = brw+"]";
//    		brw = System.getenv("SAUCE_ONDEMAND_BROWSERS");
    	}
    		JSONParser parser = new JSONParser();
    		JSONArray ja = null;
			try {
				ja = (JSONArray) parser.parse(brw);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		JSONObject js = null;
    		
    		for (Object object : ja) {
    			js = (JSONObject) object;    			
    			browsers.add(new String[]{js.get("browser").toString(), js.get("browser-version").toString(), js.get("platform").toString()});
    		}
        return browsers;
    }
}
