package com.choudoufu.solr.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

public class SysUser extends UserHistory{

	private static final long serialVersionUID = -218902660217120481L;

	/** 密码 */
	@Field
	private transient String password; 
	
	/** 用户类型 */
	@Field
	private String userType;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
