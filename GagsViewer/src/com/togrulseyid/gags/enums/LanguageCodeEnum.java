package com.togrulseyid.gags.enums;

import java.util.ArrayList;

public enum LanguageCodeEnum {

	AZERBAIJAN(1, "az"), ENGLISH(2, "en"), TURKISH(3, "tr"), RUSSIAN(4, "ru");

	private LanguageCodeEnum(int id, String lang) {
		this.id = id;
		this.lang = lang;
	}

	public int getLangCode() {
		return id;
	}

	public String getCode() {
		return "" + lang;
	}

	public int getAreaCode() {
		return id;
	}

	public static ArrayList<Integer> getIDsAsList() {
		ArrayList<Integer> langs = new ArrayList<Integer>();
		langs.add(AZERBAIJAN.id);
		langs.add(ENGLISH.id);
		langs.add(TURKISH.id);
		langs.add(RUSSIAN.id);
		return langs;
	}

	public static ArrayList<String> getLangAsList() {
		ArrayList<String> langs = new ArrayList<String>();
		langs.add(AZERBAIJAN.lang);
		langs.add(ENGLISH.lang);
		langs.add(TURKISH.lang);
		langs.add(RUSSIAN.lang);
		return langs;
	}


	public int getId() {
		return id;
	}

	public String getLang() {
		return lang;
	}

	private int id;
	private String lang;
}
