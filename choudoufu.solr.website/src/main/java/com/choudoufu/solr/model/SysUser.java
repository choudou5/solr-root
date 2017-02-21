package com.choudoufu.solr.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import com.choudoufu.solr.entity.User;

public class SysUser extends SysUserEventHistory{

	private static final long serialVersionUID = -218902660217120481L;

	/** 密码 */
	@Field
	private transient String password; 
	
	/** 用户类型 */
	@Field
	private int userType;
	
	public User toUser(){
		User user = new User();
		user.setLoginName(getLoginName());
		user.setUserType(getUserType());
		user.setPassword(getPassword());
		return user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
