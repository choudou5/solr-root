package com.choudoufu.solr.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.choudoufu.solr.constants.UserTypeEnum;


/**
 * 用户 实体
 * @author xuhaowen
 * @date 2017年2月9日
 */
public class User implements Serializable{

	private static final long serialVersionUID = -1856305488109344624L;
	
	/** 登录名 */
	private String loginName;
	/** 密码 */
	private transient String password; 
	/** 用户类型 */
	private int userType = UserTypeEnum.TEMP.getValue();
	
	public User(){}
	
	public User(String loginName, String password){
		this.loginName = loginName;
		this.password = password;
	}
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
