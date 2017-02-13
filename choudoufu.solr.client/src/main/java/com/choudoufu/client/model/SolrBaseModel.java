package com.choudoufu.client.model;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;

public class SolrBaseModel implements Serializable{
	
	private static final long serialVersionUID = -7581235090503479908L;

	/** id: 主键 */
	@Field
	public String id;

	/** id: 主键 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
