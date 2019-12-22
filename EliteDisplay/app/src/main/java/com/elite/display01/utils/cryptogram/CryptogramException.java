package com.elite.display01.utils.cryptogram;

public class CryptogramException extends RuntimeException {

	private String code;

	public String getCode() {
		return code;
	}

	private static final long serialVersionUID = 1L;

	public CryptogramException() {
		super();
	}

	public CryptogramException(String msg) {
		super(msg);
	}

	public CryptogramException(String code, String msg) {
		super(msg);
		this.code = code;
	}

}
