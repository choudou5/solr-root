package com.choudoufu.solr.schema.entity;

import java.io.Serializable;

/**
 * solr配置
 * @author xuhaowen
 * @date 2017年3月29日
 */
public class SolrConfig implements Serializable{

	private static final long serialVersionUID = 7170290976653274L;
	
	private String version;
	private String dataDir;
	private String updateLogDir;
	private int autoCommitMaxTime = 15000;
	private int autoSoftCommitMaxTime = -1;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDataDir() {
		return dataDir;
	}
	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
	public String getUpdateLogDir() {
		return updateLogDir;
	}
	public void setUpdateLogDir(String updateLogDir) {
		this.updateLogDir = updateLogDir;
	}
	public int getAutoCommitMaxTime() {
		return autoCommitMaxTime;
	}
	public void setAutoCommitMaxTime(int autoCommitMaxTime) {
		this.autoCommitMaxTime = autoCommitMaxTime;
	}
	public int getAutoSoftCommitMaxTime() {
		return autoSoftCommitMaxTime;
	}
	public void setAutoSoftCommitMaxTime(int autoSoftCommitMaxTime) {
		this.autoSoftCommitMaxTime = autoSoftCommitMaxTime;
	}
}
