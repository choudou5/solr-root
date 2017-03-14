package com.choudoufu.solr.schema.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import com.choudoufu.solr.constants.SysConsts.DataStatusEnum;
import com.choudoufu.sys.entity.User;

public class SysUser implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;

	/** id: 主键 */
	@Field
	private String id;

	/** 登录名 */
	@Field
	private String loginName;
	
	/** 密码 */
	@Field
	private transient String password; 
	
	/** 用户类型 */
	@Field
	private int userType;
	
	/** ip */
	@Field
	private String ip;
	
	/** 状态： DataStatusEnum.YES/NO   */
	@Field
	private String status = DataStatusEnum.NO.getValue();
	
	
	/** 创建时间 */
	@Field
	private Date createTime;
	
	public User toUser(){
		User user = new User();
		user.setLoginName(getLoginName());
		user.setUserType(getUserType());
		user.setPassword(getPassword());
		return user;
	}
	
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
