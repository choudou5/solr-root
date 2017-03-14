package com.choudoufu.solr.schema.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import com.choudoufu.solr.constants.ActionTypeEnum;


public class SysLogHistory implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;

	/** id: 主键 */
	@Field
	private String id;

	/** 登录名 */
	@Field
	private String loginName;
	
	/** 标题 */
	@Field
	private String title;
	
	/** 访问类型 */
	@Field
	private String accessType;
	
	/** 动作 */
	@Field
	private String action = ActionTypeEnum.VIEW.getValue();
	
	/** 操作的URI */
	@Field
	private String url;
	
	/** 操作的方式 */
	@Field
	private String method;
	
	/** 提交的数据 */
	@Field
	private String params;
	
	/** 代理信息 */
	@Field
	private String userAgent;
	
	/** ip */
	@Field
	private String ip;
	
	/** 异常信息 */
	@Field
	private String exception;
	
	/** 时间 */
	@Field
	private Date createTime;
	
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
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
