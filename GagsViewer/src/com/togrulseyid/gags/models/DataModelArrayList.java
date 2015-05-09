package com.togrulseyid.gags.models;

import java.util.ArrayList;

public class DataModelArrayList extends CoreModel {
	ArrayList<DataModel> list;

	public ArrayList<DataModel> getList() {
		return list;
	}

	public void setList(ArrayList<DataModel> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ImageModelArrayList [list=" + list + "]";
	}
}
