package com.togrulseyid.gags.operations;

import java.io.IOException;
import java.io.StringWriter;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectConvertor<T> {

	private T object;
	private String strObject;
	private StringWriter strWriter;
	private ObjectMapper mapper;

	public T getClassObject(String jsonObject, Class<T> classType)
			throws IOException {
		mapper = new ObjectMapper();
		try {
			setObject(mapper.readValue(jsonObject, classType));
		} catch (Exception e) {
			Log.d("exception","exception: "  + e.getMessage());
		}
		return getObject();
	}

	public String getClassString(T object) throws IOException {
		strWriter = new StringWriter();
		mapper = new ObjectMapper();
		try {
			mapper.writeValue(strWriter, object);
			strObject = strWriter.toString();
		} catch (Exception e) {
			Log.d("exception","exception: "  + e.getMessage());
		}
		return strObject;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
}
