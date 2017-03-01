package com.choudoufu.solrj.model;

import org.apache.solr.client.solrj.beans.Field;

import com.choudoufu.client.model.SolrBaseModel;

public class SysTestModel extends SolrBaseModel {

	private static final long serialVersionUID = 4998589955082717576L;
	
	@Field
	private String title;
	@Field
	private String[] tree;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getTree() {
		return tree;
	}
	public void setTree(String[] tree) {
		this.tree = tree;
	}
	
}
