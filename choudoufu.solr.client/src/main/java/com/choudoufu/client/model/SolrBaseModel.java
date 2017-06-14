package com.choudoufu.client.model;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;

import com.choudoufu.solr.common.util.StringUtils;

public class SolrBaseModel implements Serializable{
	
	private static final long serialVersionUID = -7581235090503479908L;

	/** id: 主键 */
	@Field
	protected String id;

	/** id: 主键 */
	public String getId() {
		return id;
	}
	
	public boolean isNew(){
		return StringUtils.isBlank(id);
	}

	public void setId(String id) {
		this.id = id;
	}

}
