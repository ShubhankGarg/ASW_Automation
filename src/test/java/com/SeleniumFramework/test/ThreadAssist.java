package com.SeleniumFramework.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ThreadAssist {
	protected static ExecutorService executor;
	private static int threadCount = 0;
	private static int executionCounter = 0;
//	public static int LoopCount = 0;
	
	private static HashMap<String, Integer> LoopCount = new HashMap<String, Integer>();
	
	private static HashMap<String, Boolean> executionStatus =  new HashMap<String, Boolean>();
	
	private static HashMap<String, Boolean> FolderCreatedFlagStore = new HashMap<String, Boolean>();	
	
	public Boolean isFolderCreated(String module) {
		if(FolderCreatedFlagStore.containsKey(module))
			return FolderCreatedFlagStore.get(module);
		else
			return false;
	}

	protected void setFolderCreated(String module) {
		if(!FolderCreatedFlagStore.containsKey(module))
			FolderCreatedFlagStore.put(module, true);
	}

	protected void setLoopCount(String Module, String tc){
		LoopCount.put(Module+"#"+tc, 0);
		System.out.println("Set LoopCount: Module-"+Module+" Testcase-"+tc+"	"+ getLoopCount(Module,tc));
	}
	
	/**
	 * @param Module
	 * @param tc
	 * @Description Increment a loop counter for Module+Testcase
	 */
	protected void incrementLoopCount(String Module, String tc){
		LoopCount.put(Module+"#"+tc, getLoopCount(Module,tc)+1);
		System.out.println("Increment LoopCount: Module-"+Module+" Testcase-"+tc+"	"+getLoopCount(Module,tc));
	}
	
	public int getLoopCount(String Module, String tc){
		return LoopCount.get(Module+"#"+tc);
	}
	
	
	//This will return false if the module/tc completed execution else will return true
	/**
	 * @param Module
	 * @param tc
	 * @return Boolean false if Done with executing a specific Module+Testcase combination
	 */
	public boolean isItExecuting(String Module, String tc){
		return executionStatus.get(Module+"#"+tc);
	}
	
	/**
	 * @param Module
	 * @return Boolean false if Done with executing a specific Module+Testcase combination
	 */
	public boolean isItExecuting(String Module){
		return executionStatus.get(Module);
	}
	
	// This will store status as true if module/tc is currently executing
	/**
	 * @Description Stores the executing in Hashmap with Key as 'Module +
	 *              Testcase' and boolean true for executing
	 * @Condition Only if a fresh entry
	 * @param Module
	 * @param Testcase
	 */
	protected void storeExecuting(String Module, String tc) {
		if (!executionStatus.containsKey(Module +"#"+ tc)) {
			executionStatus.put(Module +"#"+ tc, true);
			setLoopCount(Module, tc);
		}
	}
	
	// This will store status as true if module/tc is currently executing
		/**
		 * @Description Stores the executing in Hashmap with Key as 'ModuleName' and boolean true for executing
		 * @Condition Only if a fresh entry
		 * @param Module
		 * @param Testcase
		 */
		protected void storeExecuting(String Module) {
			if (!executionStatus.containsKey(Module)) {
				executionStatus.put(Module, true);
//				setLoopCount(Module, tc);
			}
		}
	
	//This will store status to false if execution is completed
	/**
	 * @param Module
	 * @param tc
	 * @Description turns a running flag to false when done with executing a Module+Testcase
	 */
	protected void doneExecution(String Module, String tc){
		executionStatus.put(Module+"#"+tc, false);
		System.out.println("Completed Execution for Module: "+Module+" TestName: "+tc);
//		LoopCount = 0;
	}
	
	//This will store status to false if Module execution is completed
	/**
	 * @param Module
	 * @Description turns a running flag to false when done with executing a Module
	 */
	protected void doneExecution(String Module){
		executionStatus.put(Module, false);
		System.out.println("Completed Execution for Module: "+Module);
//		LoopCount = 0;
	}
	
	/**
	 * @return List<String> of Module+Testcase who started executing
	 */
//	protected List<String> getCurrentModuleTcs(){
//		return (new ArrayList<String>( executionStatus.keySet()));
//	}
	
	public static int maxLoopCount =0;
	
	private static List<String> theadIDs = new ArrayList<String>();
	
	public List<String> getTheadIDs() {
		return theadIDs;
	}

	protected void setTheadID(String theadID) {
		this.theadIDs.add(theadID);
	}

	public static int getThreadCount() {
		return threadCount;
	}

	protected static void setThreadCount(int threadCount) {
		ThreadAssist.threadCount = threadCount;
	}
	
	public int getExecutionCounter() {
		return executionCounter;
	}

	protected void setExecutionCounter(int executionCount) {
		ThreadAssist.executionCounter = executionCount;
	}
	
	

}
