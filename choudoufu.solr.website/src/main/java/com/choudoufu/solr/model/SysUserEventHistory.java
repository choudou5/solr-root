package com.choudoufu.solr.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;


public class SysUserEventHistory implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;

	/** id: 主键 */
	@Field
	public String id;

	/** 登录名 */
	@Field
	private String loginName;
	
	/** 动作 */
	@Field
	private String action;
	
	/** ip */
	@Field
	private String ip;
	
	/** 创建时间 */
	@Field
	private Date createTime;

	/** id: 主键 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
