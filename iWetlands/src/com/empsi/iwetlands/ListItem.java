package com.empsi.iwetlands;

public class ListItem {
	private String title = null;
	private String scroll = null;
	private String view = null;
	
	public boolean hasParent = false;
	public int parentID = -1;
	public boolean hasChild = false;
	public int childrenID = -1;
	
	private ListItem child = null;

	public void setTitle(String title){
		title = this.title;
	}
	public String getTitle(){
		return this.title;
	}
	
	public void setScroll(String scroll){
		this.scroll = scroll;
	}
	public String getScroll(){
		return this.scroll;
	}

	public void setView(String view){
		this.view = view;
	}
	public String getView(){
		return this.view;
	}
	
	public void setChild(ListItem child){
		this.child = child;
	}
	public ListItem getChild(){
		return this.child;
	}
}
