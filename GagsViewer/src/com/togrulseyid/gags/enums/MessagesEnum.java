package com.togrulseyid.gags.enums;

public enum MessagesEnum {
	 /*
	 * Message codes from server. DO NOT CHANGE THESE VALUES BEFORE GET
	 * INFORMATION FROM SERVER
	 */
	SUCCESSFUL(1000),
	APP_SIGNATURE_ERROR(1001),
	TOKEN_ERROR(1002),
	NOT_FOUND_DATA_ERROR(1013),
	SERVER_ERROR_1(1015),
	SERVER_ERROR_2(1010),
	SERVER_ERROR_3(1003),
	APP_VERSION_INCORRECT(1028),

	/*
	 * Local Message codes. Must be different values
	 */
	NO_INTERNET_CONNECTION(1), 
	NO_NETWORK_CONNECTION(2),
	WRONG_TOKEN(3),
	EXCEPTION_ERROR(6),
	SERVER_CONNECTION_PROBLEM(7),
	UN_SUCCESSFUL(16),
	CATEGORY_ID_IS_NULL(1031);
	
	private MessagesEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	private int code;

}
