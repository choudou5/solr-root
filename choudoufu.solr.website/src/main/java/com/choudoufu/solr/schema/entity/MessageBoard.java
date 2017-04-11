package com.choudoufu.solr.schema.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 留言板
 * @author xuhaowende
 * @date 2017年4月9日
 */
public class MessageBoard implements Serializable{
	
	private static final long serialVersionUID = 3903399109211939201L;
	
	@Field
	private String id;
	
	/** 联系人 */
	@NotNull(message="联系人 不能为空")
	@Field
	private String linkman;
	
	/** 邮箱 */
	@Field
	private String email;
	
	/** 留言 */
	@NotNull(message="留言信息 不能为空")
	@Field
	private String message;
	
	/** ip */
	@Field
	private String ip;
	
	/** 创建时间 */
	@Field
	private Date createTime;
	
	/** 已读 */
	@Field
	private boolean read = false;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	public boolean getRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	
	
}
