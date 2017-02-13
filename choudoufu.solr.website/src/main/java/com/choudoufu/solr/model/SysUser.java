package com.choudoufu.solr.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

public class SysUser implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;

	/** ID */
	@Field
	private Long id;
	
	/** 登录名 */
	@Field
	private String loginName;
	
	/** 密码 */
	@Field
	private transient String password; 
	
	/** 用户类型 */
	@Field
	private String userType;
	
	/** ip */
	@Field
	private String ip;
	
	/** 创建时间 */
	@Field
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
