package com.empsi.inepa;

import java.util.ArrayList;
import java.util.List;

public class ParsedRow {
	private String title = null;
	private String scroll = null;
	private String view = null;
	private int index;
	private List<ParsedRow> children = new ArrayList<ParsedRow>();
	private int icon;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getScroll() {
		return scroll;
	}

	public void setScroll(String scroll) {
		this.scroll = scroll;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public List<ParsedRow> getChildren() {
		return children;
	}

	public void setEntries(List<ParsedRow> entries) {
		this.children = entries;
	}
	
	public void addChild(ParsedRow dataSet){
		this.children.add(dataSet);
	}
}