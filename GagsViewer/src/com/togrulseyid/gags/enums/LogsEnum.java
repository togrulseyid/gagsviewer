package com.togrulseyid.gags.enums;

public enum LogsEnum implements Command1{
//	GENERAL(1, "testA", CategoriesActivity.class),
	GENERAL(1, "testA"),
	INPUT(1, "inputX"),
	OUTPUT(2, "outputX"),
	LANG(3, "langX"), 
	PULL(4, "onPullX"),
	POST_MODEL(5, "postModelX"), 
	SEND_ACTION_SERVICE(6, "SendActionX"),	
	SUBMIT_CHANNEL_MODEL_LIST(6, "submitChannelModelListX");

	private LogsEnum(int logId, String logName) {
		this.logId = logId;
		this.logName = logName;
	}
	
	private LogsEnum(int logId, String logName, Class<?> T) {
		this.T = T;
		this.logId = logId;
		this.logName = logName;
	}
	

	public String getLogName2() {
		return T.getCanonicalName();
	}

	public String getLogName() {
		return logName;
	}

	public int getLogId() {
		return logId;
	}

	private String logName;
	private int logId;
	private Class<?> T;
	
	@Override
	public <E extends Enum<E> & Command1> void execute(E command) {
		
	}
	
}

interface Command1{
	abstract <E extends Enum<E> & Command1> void execute(E command);
}
