package com.SeleniumFramework.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.SeleniumFramework.test.FunctionalLibrary;

public class TestExecutor extends FunctionalLibrary {

	public int reportCount, startTimeFlag, loopStartRow;
	public String action, objectName, testModulePath;
	public int TC_VAR;
	// ExcelFileUtil excelFileUtil = getInstance();

	/**
	 * Method testSuite: Opens the Module List, identifies the total number of
	 * test modules to be executed and passes the module name to keywordDriver
	 * method whose execution flag is "Yes"
	 * 
	 * @param uc
	 * 
	 * @param: none
	 */
	public void testSuite(String moduleName, String uc) {
		// TODO: it iterates through list of modules and executes test cases

		try {
			// Opens Test Suite Driver Excel
			FileInputStream TS = new FileInputStream(testSuite);
			POIFSFileSystem poifs2 = new POIFSFileSystem(TS);
			HSSFWorkbook TSUworkbook = new HSSFWorkbook(poifs2);
			HSSFSheet readtsusheet = TSUworkbook.getSheet("ModuleList");

			int MODULE_COUNT = 1;
			String flag;
			int COUNTER = 1;
			String executionFlag, moduleResultFolder;

			/**
			 * while loop below checks execution flag and calls keywordDriver
			 * method for each modulename whose flag is yes
			 */
			while (COUNTER == 1) {
				flag = getCellValue(readtsusheet, MODULE_COUNT, 0);
				if (flag.equalsIgnoreCase("End")) {
					COUNTER = 0;
					System.out.println("All Test modules execution done");
				} else {

					executionFlag = getCellValue(readtsusheet, MODULE_COUNT, 3);

					if (executionFlag.equalsIgnoreCase("Yes")) {
						moduleName = getCellValue(readtsusheet, MODULE_COUNT, 1);

						// FOR PARALLEL MODULE RUN
						// Thread controller, sets module state: executing
						storeExecuting(moduleName);
						// Check if module executing, if not set and avoid
						// others to take
						if (isItExecuting(moduleName)) {
							doneExecution(moduleName);
						} else {
							MODULE_COUNT = MODULE_COUNT + 1;
							continue;
						}
						// End of Thread controller
						// END OF PARALLEL MODULE RUN CODE

						moduleResultFolder = moduleName + "_" + uc;
						moduleResultFolder = moduleResultFolder.replaceAll(" ", "");
						String modulepath = htmlRep + File.separator + moduleResultFolder;
						scrshtPath = modulepath + File.separator + "ScreenShots";
						// Create folders only if they don't exist (Thread Safe)
						if (!isFolderCreated(moduleName)) {
							setFolderCreated(moduleName);
							createFolder(modulepath);
							createFolder(scrshtPath);
							setUp(modulepath);
						}

						PREVIOUS_TEST_CASE = "Before Test Execution";

						failedStep = " ";

						keywordDriver(moduleName, uc);

					}
					// Start the Next Script
					MODULE_COUNT = MODULE_COUNT + 1;
				}
			}
		} catch (Exception e) {
			// LogVar= e.getMessage();
			System.out.println("Exception from TestSuite Function: " + e.getMessage());
		}
	}

	/**
	 * keywordDriver Method calls teststep method for each step enlisted in test
	 * module excel sheet, it handles looping too
	 * 
	 * @param uc
	 * @param failedStep
	 * 
	 * @param moduleName:
	 *            contains the name of test module to be executed
	 * @throws IOException
	 */

	private void keywordDriver(String moduleName, String uc) throws IOException {// TODO:
		FileInputStream TSN = null;																	// executeTestSuiteForModule()

		try {
			strModuleName = moduleName;

			// System.out.println("Current Thread:
			// "+Thread.currentThread().getName());
			// System.out.println("Index:
			// "+getTheadIDs().indexOf(Thread.currentThread().getName()));
			//
			// Thread.sleep(getTheadIDs().indexOf(Thread.currentThread().getName())*3000);

			// createUpdateExcel(moduleName,uc);
			testcaseCounter = 1;
			testModulePath = testModuleContainerPath + File.separator + strModuleName + ".xls";
			
			//Commented next line as want OR to be Application_OR name at runtime
//			testElementModulePath = elementCollection + File.separator + strModuleName + "_OR" + ".xls";

			System.out.println("                                                ");
			System.out.println(" >>>>> TestInfo : Test Execution Started <<<<<<<");
			System.out.println("TestInfo : Execution Browser :" + platform);
			System.out.println("TestInfo : Execution TestModule Name: " + testModulePath);
//			System.out.println("TestInfo : Execution OR Name: " + testElementModulePath);

			TSN = new FileInputStream(testModulePath);
			POIFSFileSystem poifs3 = new POIFSFileSystem(TSN);
			scriptWorkbook = new HSSFWorkbook(poifs3);
			readScriptSheet = scriptWorkbook.getSheet("TestScript");
			readtestcasesheet = scriptWorkbook.getSheet("TestCases");

			TCCounter = 1;
			currTestRowPtr = 1;
			reportCount = 1;
			startTimeFlag = 0;
			TCCounter = 1;
			
			/**
			 * Start Executing Test Case While loop below checks for each step
			 * in module
			 */
//			String testName;
			boolean TestCaseFlag = true;
			String ALM_TC_PATH;
			String ALM_SUITE_PATH;

			while (TestCaseFlag) {// TODO: good to use true default and delete
									// TestCaseFlag variable
				// if(Thread.currentThread().toString().equals("Thread[pool-1-thread-1,5,main]")){
				// System.out.println("Executing 2nd thread");
				// System.out.println("Sheet:
				// "+readtestcasesheet.getSheetName());
				// System.out.println("Row: "+TCCounter);
				//
				// } else {
				// System.out.println("Executing 1st thread");
				// System.out.println("Sheet:
				// "+readtestcasesheet.getSheetName());
				// System.out.println("Row: "+TCCounter);
				// }

				// TCCounter stores the row that helds testcase name

				testFlag = getCellValue(readtestcasesheet, TCCounter, 0);
				if (testFlag.equalsIgnoreCase("End")) {
					TestCaseFlag = false;
				} else if (testFlag.equalsIgnoreCase("y")) {
					TC_VAR = 1;
					environment = getCellValue(readtestcasesheet, TCCounter, 7);
					if (environment.equals(null) || environment.equals(" ") || environment.equals("")) {
						url = mainUrl;
					} else if (environment.equalsIgnoreCase("Production")) {
						url = productionUrl;
					} else if (environment.equalsIgnoreCase("Stage")) {
						url = stageUrl;
					} else if (environment.equalsIgnoreCase("Offline")) {
						url = offlineUrl;
					} else if (environment.equalsIgnoreCase("Live")) {
						url = liveUrl;
					} else if (environment.equalsIgnoreCase("test1")) {
						System.out.println("TestInfo : Execution Environment :" + environment);
						url = test1;
					} else if (environment.equalsIgnoreCase("test2")) {
						url = test2;
					} else if (environment.equalsIgnoreCase("test3")) {
						url = parallelThreadCount;
					} else {
						url = mainUrl;
					}
					LOG_VAR = 1;
					testFlag = "y";
					TEST_STEP_COUNT = 1;
					testName = getCellValue(readtestcasesheet, TCCounter, 2);
					ALM_TC_PATH = getCellValue(readtestcasesheet, TCCounter, 8);
					ALM_SUITE_PATH = getCellValue(readtestcasesheet, TCCounter, 9);
					// CODE FOR PARALLEL TC RUN: 1
					// Thread controller, set module/tc to executing state only
					// if its a fresh entry
					storeExecuting(moduleName, testName);
					// }
					// End of Thread controller
					// END OF CODE FOR PARALLEL TC RUN: 1

					System.out.println("TestInfo : Executing Testcase :" + testName);

					int[] testSteps = getTotalStepsAndStepPointer(testName);

					currTestRowPtr = testSteps[1];
					startRow = testSteps[2];
					endRow = testSteps[3];
					int testMatch = testSteps[4];
					tcStartTime = getStartTime();
					// executeTest(testName);checkForNewTestCase();

					if (testMatch == 1) {
						if (LOOP_FLAG) {
							int loopCount;
							int ParamFlag = 1;
							String tempFailedStep = " ";
							// int LoopedTestStepCount;
							loopCount = getNumberofIterations();

							// maxLoopCount = loopCount;

							// boolean First_Iteration = false;
							System.out.println("LoopCount: " + loopCount);
							if (startRow > currTestRowPtr) {
								loopStartRow = currTestRowPtr;
							} else {
								loopStartRow = startRow;
							}

							for (tempCounter = 1; tempCounter <= loopCount; tempCounter++) {

								// //*******These are the parallel thread
								// handler of loop
								//
								// if(isItExecuting(moduleName, testName)){
								// incrementLoopCount(moduleName, testName);
								// tempCounter = getLoopCount(moduleName,
								// testName);
								// if(tempCounter==loopCount)doneExecution(moduleName,
								// testName);
								//
								// //************End of parallel thread handlers

								sauceSession = "\t\t\t******************SauceTest: " + name.getMethodName()
										+ "\tModule: " + moduleName + "\tTestCase: " + testName + "\tIterating Loop: "
										+ tempCounter
										+ "\tWATCH AT: <a href=\"https://saucelabs.com/beta/tests/SESSION\">SauceLink</a>*****************";
								String Temp = "Iteration: " + tempCounter;
								f_sendTestStepResultIteration(Temp);
								System.out.println("LOG_VAR : " + LOG_VAR);
								if (LOG_VAR == 0) {
									LOG_VAR = 1;
									testFlag = "y";
								}
								while (loopStartRow <= endRow) {
									if (LOG_VAR == 0) {
										ParamFlag = LOG_VAR;
										tempFailedStep = failedStep;
										break;
									}

									validate1 = "";

									testStep(testName + "_" + tempCounter);

									{
										if (TC_VAR == 0) {
											loopStartRow = endRow;
										}
									}

									loopStartRow = loopStartRow + 1;
								}
								TEST_STEP_COUNT = (startRow - currTestRowPtr) + 1; // TEST_STEP_COUNT
																					// is
																					// for
																					// reporting
																					// looped
																					// steps
																					// for
																					// each
																					// data
																					// set.
								loopStartRow = startRow;
								String ETemp = "End-Iter: " + tempCounter;
								f_sendTestStepResultIteration(ETemp);

								System.out.println("///////////////////////////////Iteration Loop: " + tempCounter
										+ " completed for  Thread: " + Thread.currentThread().getName());

								// } //END of parallel if here
							}
							if (LOG_VAR == 1) {
								LOG_VAR = ParamFlag;
								failedStep = tempFailedStep;
							}

						}

						else {
							int teststepcount = testSteps[0]; // No Loop flags
																// it jump here

							// IF the module and tc done execution break
							// if (isItExecuting(moduleName, testName)) {
							// doneExecution(moduleName, testName);
							sauceSession = "//////////////////////////////////SauceTest: " + name.getMethodName()
									+ "\tModule: " + moduleName + "\tTestCase: " + testName
									// +"\tSESSION:
									// https://saucelabs.com/beta/tests/"+
									// (((RemoteWebDriver)
									// driver).getSessionId()).toString()+"*****************");
									+ "\tWATCH AT: <a href=\"https://saucelabs.com/beta/tests/SESSION\">SauceLink</a>*****************";
							for (int i = 1; i <= teststepcount; i++) {
								// Passing empty validate value so that
								// existing
								// value must not written for any exception
								// in
								// next validate statement.
								validate1 = "";
								testStep(testName);
								
								FileOutputStream output_file = new FileOutputStream(testModulePath); // Open
								// FileOutputStream
								// to
								// write
								// updates
								scriptWorkbook.write(output_file); // write changes
								HSSFFormulaEvaluator.evaluateAllFormulaCells(scriptWorkbook);//Evaluates all formulas in a sheet
								
								if (TC_VAR == 0) {
									i = teststepcount + 1;
								}
								currTestRowPtr = currTestRowPtr + 1;
							}
							// }
						}

						// writeStepExcel(PREVIOUS_TEST_CASE,LOG_VAR,failedStep,"N",
						// QCExcelPath,testcaseCounter);
						// writeStepExcel(PREVIOUS_TEST_CASE,TC_VAR,failedStep,"N",
						// QCExcelPath,testcaseCounter);
						testcaseCounter = testcaseCounter + 1;
						f_sendTestCaseResult(TC_ID, strModuleName, PREVIOUS_TEST_CASE, TC_DESC, tcStartTime, TC_VAR,
								failedStep);
						funExcelResult(TC_ID, PREVIOUS_TEST_CASE, strModuleName, TC_VAR, platform, failedTCount,
								passedTCount, url, ALM_TC_PATH, ALM_SUITE_PATH);
						//Quit driver with every module
//						try{driver.quit();}catch(Exception e){};
					}
				}
				TCCounter = TCCounter + 1;
			}
		} catch (Exception e) {
			LOG_VAR = 0;
			e.printStackTrace();
			// failedStep = getCellValue(readScriptSheet,currTestRowPtr,1);
			System.out.println("Exception from KeywordDriver Function: " + e.getMessage());
		} finally {
			try {
				TSN.close();
				FileOutputStream output_file = new FileOutputStream(testModulePath); // Open
																						// FileOutputStream
																						// to
																						// write
																						// updates
				scriptWorkbook.write(output_file); // write changes
				output_file.close(); // close the stream

			} catch (Exception e) {
				System.err.println("\n\nProblem writting content to workbook. May be you already have the excel opened...!!!");
				System.err.println("Please close the module sheet to update if yes");
			} 
		}
	}

	/**
	 * Method testStep: Gets element and performs action over the element
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */

	private void testStep(String testCaseName) throws IOException, InterruptedException { // TODO:
																							// executeTestStep
		try {
			int tempStartRow;
			if (LOOP_FLAG) {
				tempStartRow = loopStartRow;
			} else {
				tempStartRow = currTestRowPtr;
			}
			if (testFlag.equalsIgnoreCase("y")) {
				applicationName = getCellValue(readScriptSheet, tempStartRow, 0);//Reading application name from testcase
				testElementModulePath = elementCollection + File.separator + applicationName + "_OR" + ".xls";//Assuming OR to be applicationName_OR naming format
				screenName = getCellValue(readScriptSheet, tempStartRow, 3);
				action = getCellValue(readScriptSheet, tempStartRow, 4);
				inputSheet = getCellValue(readScriptSheet, tempStartRow, 5);

				System.out.println("Thread Name: "+Thread.currentThread().getName()+" Testcase: " + testCaseName + " Screen Name:" + screenName + "||" + "Action :"
						+ action + " ||" + "TestData Sheet :" + inputSheet);
				
				
				//If test data sheet not being used	
				if (inputSheet.trim().isEmpty()) {
					
					//by Shubhank
					if(action.equals("CallFunction")){
						
						String functionName  = getCellValue(readScriptSheet, tempStartRow, 6).trim();
						FileInputStream TSN = new FileInputStream("SeleniumFramework" + File.separator + "Function_Repository" + File.separator + screenName + File.separator + functionName + ".xls" );
						POIFSFileSystem poifs3 = new POIFSFileSystem(TSN);
						
						readScriptSheets.push(readScriptSheet);
						currTestRowPtrs.push(currTestRowPtr);
						
						readScriptSheet = new HSSFWorkbook(poifs3).getSheet("TestScript");
						currTestRowPtr=1;
						
						f_sendFunctionalResult(strModuleName, TC_ID, PREVIOUS_TEST_CASE, screenName, functionName+"_Start");
						
						boolean nestedFun = true;
						while(nestedFun==true &&  LOG_VAR == 1){
							testStep(testCaseName);
							currTestRowPtr++;
							
							String appName = getCellValue(readScriptSheet, currTestRowPtr, 0).trim();
							
							if(appName.equalsIgnoreCase("END_OF_TEST")){
								nestedFun=false;
							}
						}
						
						if(LOG_VAR == 1){
							f_sendFunctionalResult(strModuleName, TC_ID, PREVIOUS_TEST_CASE, screenName, functionName+"_End");
						}
						
						readScriptSheet = readScriptSheets.pop();
						currTestRowPtr = currTestRowPtrs.pop();
						return;
					}
					
					
					int FIELD_INDEX = 0;
					int INDEX_COUNTER = 0;
					boolean ROW_FLAG = true;
					/**
					 * while loop below gets field name and field value from
					 * script sheet untill fields are empty.
					 */
					while (INDEX_COUNTER < 500 && ROW_FLAG) {
						fieldName = getCellValue(readScriptSheet, tempStartRow, 6 + FIELD_INDEX).trim();// readscriptsheet.getRow(temp_Start_Row).getCell(7+FieldIndex).getStringCellValue().trim();
						fieldValue = getCellValue(readScriptSheet, tempStartRow, 7 + FIELD_INDEX).trim();// readscriptsheet.getRow(temp_Start_Row).getCell(8+FieldIndex).getStringCellValue().trim();		
						currentDataCell = readScriptSheet.getRow(tempStartRow).getCell(7+FIELD_INDEX);

						if ((fieldName.isEmpty()) || (fieldName.equals("")) || (fieldName.equals(null))) {
							if ((fieldValue.isEmpty()) || (fieldValue.equals("")) || (fieldValue.equals(null))) {
								ROW_FLAG = false;
								break;
							} else {
								FIELD_INDEX = FIELD_INDEX + 2;
							}
						} else {
							FIELD_INDEX = FIELD_INDEX + 2;
						}
						
						
						// Get Object and its Element Type from Object
						// Repository
						String[] actionObject = new String[]{"",""};
						if(!action.isEmpty()) actionObject = getObject(screenName, fieldName);
						
						System.out.println("Field Locater Type :" + actionObject[1] + "||" + " Field Locater Value: "
								+ actionObject[0]);
						fieldElementType = actionObject[1];
						objectName = actionObject[0];

						// Passes (objectName, fieldElementType,fieldValue,
						// action) to keyword method where desired action is
						// performed over the object.
						System.out.println("FieldName: " + fieldName + "|| " + "FieldValue: " + fieldValue);

						LOG_VAR = 1;
						TC_VAR = 1;
						keyword(objectName, fieldElementType, fieldValue, action, fieldName);
						if (LOG_VAR == 1) {
							failedStep = " ";
							failedStep = getCellValue(readScriptSheet, tempStartRow, 1);
							System.out.println("TestInfo: Test Step passed !!");
						} else if (LOG_VAR == 0) {
							try {
								TC_VAR = 0;
								failedStep = getCellValue(readScriptSheet, tempStartRow, 1);
								if (failedStep.isEmpty() || failedStep.equals(null) || failedStep.equals(" ")
										|| failedStep.equals("")) {
									System.out.println("TestInfo: Test Step failed :" + failedStep);
									failedStep = " ";
								}
							} catch (Exception e) {
								failedStep = " ";
								System.out.println("TestError :Exception handling, Passing white space to failed Step");
							}
						}
						//////// THIS SECTION TAKES CARE OF WRITTING THE STEP
						//////// RESULT//////////
						try {
							f_sendTestStepResult(TC_ID, strModuleName, PREVIOUS_TEST_CASE,
									currTestRowPtrs, currTestRowPtr, screenName, action, fieldName, fieldValue, LOG_VAR);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//////// END OF WRITTING THE STEP RESULT//////////

						System.out.println("Test Case Name: " + PREVIOUS_TEST_CASE);

						INDEX_COUNTER = INDEX_COUNTER + 1;
						fieldName = "";
						fieldValue = "";
						if (LOG_VAR == 0) {

							TC_VAR = 0;
							TEST_STEP_COUNT = TEST_STEP_COUNT + 1;

						} else {
							TEST_STEP_COUNT = TEST_STEP_COUNT + 1;
						}
					}
				} else if (action.equalsIgnoreCase("ValidateResponse")) {
					System.out.println("TestInfo : Getting info Webservice Validation Sheet");

					int FIELD_INDEX = 0;
					int INDEX_COUNTER = 0;
					boolean ROW_FLAG = true;
					/**
					 * while loop below gets field name and field value from
					 * script sheet untill fields are empty.
					 */
					while (INDEX_COUNTER < 500 && ROW_FLAG) {
						fieldName = getCellValue(readScriptSheet, tempStartRow, 6 + FIELD_INDEX).trim();// readscriptsheet.getRow(temp_Start_Row).getCell(7+FieldIndex).getStringCellValue().trim();
						fieldValue = getCellValue(readScriptSheet, tempStartRow, 7 + FIELD_INDEX).trim();// readscriptsheet.getRow(temp_Start_Row).getCell(8+FieldIndex).getStringCellValue().trim();

						if ((fieldName.isEmpty()) || (fieldName.equals("")) || (fieldName.equals(null))) {
							if ((fieldValue.isEmpty()) || (fieldValue.equals("")) || (fieldValue.equals(null))) {
								ROW_FLAG = false;
								break;
							} else {
								FIELD_INDEX = FIELD_INDEX + 2;
							}
						} else {
							FIELD_INDEX = FIELD_INDEX + 2;
						}
						// Get Object and its Element Type from Object
						// Repository
						String[] actionObject = getObject(screenName, fieldName);

						System.out.println("Field Locater Type :" + actionObject[1] + "||" + " Field Locater Value: "
								+ actionObject[0]);
						fieldElementType = actionObject[1];
						objectName = actionObject[0];

						// Passes (objectName, fieldElementType,fieldValue,
						// action) to keyword method where desired action is
						// performed over the object.
						System.out.println("FieldName: " + fieldName + "|| " + "FieldValue: " + fieldValue);

						LOG_VAR = 1;
						keyword(objectName, fieldElementType, fieldValue, action, fieldName);
						if (LOG_VAR == 1) {
							failedStep = " ";
							failedStep = getCellValue(readScriptSheet, tempStartRow, 1);
							System.out.println("TestInfo: Test Step passed !!");
						} else if (LOG_VAR == 0) {
							try {
								TC_VAR = 0;
								failedStep = getCellValue(readScriptSheet, tempStartRow, 1);
								if (failedStep.isEmpty() || failedStep.equals(null) || failedStep.equals(" ")
										|| failedStep.equals("")) {
									System.out.println("TestInfo: Test Step failed :" + failedStep);
									failedStep = " ";
								}
							} catch (Exception e) {
								failedStep = " ";
								System.out.println("TestError :Exception handling, Passing white space to failed Step");
							}
						}
						// System.out.println("Action:" + action);
						try {
							f_sendTestStepResult(TC_ID, strModuleName, PREVIOUS_TEST_CASE,
									currTestRowPtrs, currTestRowPtr, screenName, action, fieldName, fieldValue, LOG_VAR);
						} catch (Exception e) {
							e.printStackTrace();
						}

						System.out.println("Test Case Name: " + PREVIOUS_TEST_CASE);

						INDEX_COUNTER = INDEX_COUNTER + 1;
						fieldName = "";
						fieldValue = "";
						if (LOG_VAR == 0) {

							TC_VAR = 0;
							TEST_STEP_COUNT = TEST_STEP_COUNT + 1;

						} else {
							TEST_STEP_COUNT = TEST_STEP_COUNT + 1;
						}
					}
				} else if (action.equalsIgnoreCase("ValidateResponseExcel")) {
					System.out.println("TestInfo : Getting info from Soap Outputfile Sheet");

					int FIELD_INDEX = 0;
					int INDEX_COUNTER = 0;
					boolean ROW_FLAG = true;
					/**
					 * while loop below gets field name and field value from
					 * script sheet untill fields are empty.
					 */
					while (INDEX_COUNTER < 500 && ROW_FLAG) {
						fieldName = getCellValue(readScriptSheet, tempStartRow, 7 + FIELD_INDEX).trim();// readscriptsheet.getRow(temp_Start_Row).getCell(7+FieldIndex).getStringCellValue().trim();
						fieldValue = getCellValue(readScriptSheet, tempStartRow, 6 + FIELD_INDEX).trim();// readscriptsheet.getRow(temp_Start_Row).getCell(8+FieldIndex).getStringCellValue().trim();

						if ((fieldName.isEmpty()) || (fieldName.equals("")) || (fieldName.equals(null))) {
							if ((fieldValue.isEmpty()) || (fieldValue.equals("")) || (fieldValue.equals(null))) {
								ROW_FLAG = false;
								break;
							} else {
								FIELD_INDEX = FIELD_INDEX + 2;
							}
						} else {
							FIELD_INDEX = FIELD_INDEX + 2;
						}
						// Get Object and its Element Type from Object
						// Repository
						String[] actionObject;

						fieldElementType = " ";
						objectName = "";
						// String Manualsheet = fieldName;
						// Passes (objectName, fieldElementType,fieldValue,
						// action) to keyword method where desired action is
						// performed over the object.
						System.out.println("FieldName: " + fieldName + "|| " + "FieldValue: " + fieldValue);

						LOG_VAR = 1;
						keyword(objectName, fieldElementType, fieldValue, action, fieldName);
						if (LOG_VAR == 1) {
							failedStep = " ";
							failedStep = getCellValue(readScriptSheet, tempStartRow, 1);
							System.out.println("TestInfo: Test Step passed !!");
						} else if (LOG_VAR == 0) {
							try {
								TC_VAR = 0;
								failedStep = getCellValue(readScriptSheet, tempStartRow, 1);
								if (failedStep.isEmpty() || failedStep.equals(null) || failedStep.equals(" ")
										|| failedStep.equals("")) {
									System.out.println("TestInfo: Test Step failed :" + failedStep);
									failedStep = " ";
								}
							} catch (Exception e) {
								failedStep = " ";
								System.out.println("TestError :Exception handling, Passing white space to failed Step");
							}
						}
						// System.out.println("Action:" + action);
						try {
							f_sendTestStepResult(TC_ID, strModuleName, PREVIOUS_TEST_CASE,
									currTestRowPtrs, currTestRowPtr, screenName, action, fieldName, fieldValue, LOG_VAR);
						} catch (Exception e) {
							e.printStackTrace();
						}

						System.out.println("Test Case Name: " + PREVIOUS_TEST_CASE);

						INDEX_COUNTER = INDEX_COUNTER + 1;
						fieldName = "";
						fieldValue = "";
						if (LOG_VAR == 0) {
							TC_VAR = 0;
							TEST_STEP_COUNT = TEST_STEP_COUNT + 1;
						} else {
							TEST_STEP_COUNT = TEST_STEP_COUNT + 1;
						}
					}
				} else {

					String Test_Data = test_data;// WB - Here need
													// to pass
													// service name

					// performing action for parameterized steps
					readLoopSheet = scriptWorkbook.getSheet(Test_Data); // Input
																		// sheet
																		// not
																		// empty
																		// it
																		// jumps
																		// here

					// inputSheet;
					int LOOP_INDEX = 0;
					int LOOP_INDEX_COUNTER = 0;
					// int DATALIST_HEADER = 0;
					String loopedFieldName, loopedFieldValue, delimiter;
					int dataListPointer = 0;
					int tempCounternoloop = 1;

					delimiter = getCellValue(readLoopSheet, dataListPointer, 0);
					while (!delimiter.equalsIgnoreCase("End")) // May be this
																// condition not
																// required for
																// WB
					{
						if (delimiter.equalsIgnoreCase(inputSheet)) {
							break;
						} else {
							dataListPointer = dataListPointer + 1;
							delimiter = getCellValue(readLoopSheet, dataListPointer, 0);
						}
					}
					/**
					 * This loop will get Datasheet's name and Column name from
					 * FieldName and(or) FieldValue of the current row of
					 * Script. It will Split the Datasheet and Value/Name column
					 * Names. Opens the Datasheet starts getting FieldName
					 * and(or)FieldValue one by one.
					 */

					
					while (LOOP_INDEX_COUNTER < 500) {
						loopedFieldName = getCellValue(readScriptSheet, tempStartRow,
								6 + LOOP_INDEX);// readscriptsheet.getRow(temp_Start_Row).getCell(7+loopIndex).getStringCellValue().trim();
						loopedFieldValue = getCellValue(readScriptSheet, tempStartRow,
								7 + LOOP_INDEX);// readscriptsheet.getRow(temp_Start_Row).getCell(8+loopIndex).getStringCellValue().trim();

						if (loopedFieldName.isEmpty()) {
							if (loopedFieldValue.isEmpty()) {
								break;
							}

							else {
								LOOP_INDEX = LOOP_INDEX + 2;
								int FIELD_VALUE_CLMN_NO1 = 0;
								int FIELD_VALUE_LOOP_CNTR1 = 0;

								String getFieldValueColumnHeader1;
								while (FIELD_VALUE_LOOP_CNTR1 < 500) {
									getFieldValueColumnHeader1 = getCellValue(
											readLoopSheet, dataListPointer, FIELD_VALUE_LOOP_CNTR1);// readloopsheet.getRow(0).getCell(dataloopcounter1).getStringCellValue().trim();
									if (getFieldValueColumnHeader1.equalsIgnoreCase(loopedFieldValue)) {
										FIELD_VALUE_CLMN_NO1 = FIELD_VALUE_LOOP_CNTR1;
										break;
									}
									FIELD_VALUE_LOOP_CNTR1 = FIELD_VALUE_LOOP_CNTR1 + 1;
								}

								fieldValue = getCellValue(readLoopSheet,
										dataListPointer + tempCounter, FIELD_VALUE_CLMN_NO1);// readloopsheet.getRow(Temp_Counter).getCell(ColumnNumber1).getStringCellValue().trim();
								currentDataCell = readLoopSheet.getRow(dataListPointer + tempCounter).getCell(FIELD_VALUE_CLMN_NO1);
								System.out.println("FieldValue: " + fieldValue);
								tempCounternoloop = tempCounternoloop + 1;
							}
						} else {
							LOOP_INDEX = LOOP_INDEX + 2;
                                                        String queryvalue = "";
							String columnValue = null;
							if (!(loopedFieldValue.isEmpty())) {
                                                                if (loopedFieldValue.contains("#")) {
									queryvalue = "#";
									String loopedParts[] = loopedFieldValue.split("#");
									loopedFieldValue = loopedParts[0];
									columnValue = loopedParts[1];
								}
								int FIELD_VALUE_CLMN_NO2 = 0;
								int FIELD_VALUE_LOOP_CNTR2 = 0;
								String getFieldValueColumnHeader2;
								while (FIELD_VALUE_LOOP_CNTR2 < 500) {
									getFieldValueColumnHeader2 = getCellValue(
											readLoopSheet, dataListPointer, FIELD_VALUE_LOOP_CNTR2);// readloopsheet.getRow(0).getCell(dataloopcounter2).getStringCellValue().trim();
									if (getFieldValueColumnHeader2.equalsIgnoreCase(loopedFieldValue)) {
										FIELD_VALUE_CLMN_NO2 = FIELD_VALUE_LOOP_CNTR2;
										break;
									}
									FIELD_VALUE_LOOP_CNTR2 = FIELD_VALUE_LOOP_CNTR2 + 1;
								}
                                                                if (!delimiter.isEmpty()) {
									if (queryvalue.contains("#")) {
										fieldValue = getCellValue(
												readLoopSheet,
												dataListPointer + tempCounter, FIELD_VALUE_CLMN_NO2);
										// readloopsheet.getRow(Temp_Counter).getCell(ColumnNumber1).getStringCellValue().trim();
										fieldValue = fieldValue.concat("#")
												.concat(columnValue);
										System.out.println("FieldValue: " + fieldValue);
									}
                                                                        else {
										fieldValue = getCellValue(readLoopSheet,
										dataListPointer + tempCounter, FIELD_VALUE_CLMN_NO2);
                                                                                System.out.println("FieldValue: " + fieldValue); 
                                        currentDataCell = readLoopSheet.getRow(dataListPointer + tempCounter).getCell(FIELD_VALUE_CLMN_NO2);
									}

								// readloopsheet.getRow(Temp_Counter).getCell(ColumnNumber2).getStringCellValue().trim();
								
							}
							int FIELD_NAME_CLMN_CNTR = 0;
							int FIELD_NAME_CLMN_NO = 0;
							String getFieldNameColumnHeader;
							while (FIELD_NAME_CLMN_CNTR < 500) {
								getFieldNameColumnHeader = getCellValue(readLoopSheet,
										dataListPointer, FIELD_NAME_CLMN_CNTR);// readloopsheet.getRow(0).getCell(dataloopcounter).getStringCellValue().trim();
								if (getFieldNameColumnHeader.equalsIgnoreCase(loopedFieldName)) {
									FIELD_NAME_CLMN_NO = FIELD_NAME_CLMN_CNTR;
									break;
								}

								FIELD_NAME_CLMN_CNTR = FIELD_NAME_CLMN_CNTR + 1;
							}
							// fieldName=getCellValue(readLoopSheet,
							// dataListPointer+tempCounter,FIELD_NAME_CLMN_NO);//readloopsheet.getRow(Temp_Counter).getCell(ColumnNumber).getStringCellValue().trim();
							fieldName = loopedFieldName; // Shubhank
							System.out.println("FieldName: " + fieldName);
						}
						// Get Object and its Element Type from Object
						// Repository
						String[] actionObject = getObject(screenName,
								fieldName);
						fieldElementType = actionObject[1];
						objectName = actionObject[0];
						LOG_VAR = 1;
						// Performs Action
						keyword(objectName, fieldElementType,
								fieldValue, action, fieldName);
						if (LOG_VAR == 1) {
							failedStep = " ";
							System.out.println("TestInfo: Test Step passed !!");
						} else if (LOG_VAR == 0) {
							try {
								TC_VAR = 0;
								failedStep = getCellValue(readScriptSheet,
										tempStartRow, 1);
								if (failedStep.isEmpty() || failedStep.equals(null)
										|| failedStep.equals(" ") || failedStep.equals("")) {
									System.out.println("TestError :Failed Step : " + failedStep);
									failedStep = " ";
								}
							} catch (Exception e) {
								failedStep = " ";
								System.out.println("Exception handling, Passing white space to failed Step");
							}
						}

						try {
							f_sendTestStepResult(TC_ID, strModuleName,
									PREVIOUS_TEST_CASE, currTestRowPtrs,currTestRowPtr, 
									screenName, action, fieldName,
									fieldValue, LOG_VAR);
						} catch (Exception e) {
							e.printStackTrace();
						}

						fieldName = "";
						fieldValue = "";
						if (LOG_VAR == 0) {
							// failedStep =
							// getCellValue(readScriptSheet,currTestRowPtr,1);
							TC_VAR = 0;
							break;
						} else {
							TEST_STEP_COUNT = TEST_STEP_COUNT + 1;
						}
						LOOP_INDEX_COUNTER = LOOP_INDEX_COUNTER + 1;
					}
                                    }
				}
			}
		} catch (Exception e) {
			TC_VAR = 0;
			LOG_VAR = 0;
			System.out.println("Exception from TestStep Function: " + e.getMessage());
			String Trace = "Exception from TestStep Function: " + e.getMessage();
			sendLog(Trace, PREVIOUS_TEST_CASE, currTestRowPtrs,currTestRowPtr);
			testFlag = "n";
		}
	}

}
