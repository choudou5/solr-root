package com.choudoufu.client.model;

import java.io.Serializable;

public class DataImport implements Serializable{
	
	private static final long serialVersionUID = -7581235090503479908L;

	/**
	 * 命令
	 */
	public interface Command{
		public final String STATUS = "status";
		public final String IMPORT_DELTA = "delta-import";
		public final String IMPORT_FULL = "full-import";
	}
	
	public String status;//idle、busy
	public int fetchedCount;//获取
	public int skippedCount;//跳过
	public int updateCount;//更新个数
	public int processedCount;//处理个数
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getFetchedCount() {
		return fetchedCount;
	}
	public void setFetchedCount(int fetchedCount) {
		this.fetchedCount = fetchedCount;
	}
	public int getSkippedCount() {
		return skippedCount;
	}
	public void setSkippedCount(int skippedCount) {
		this.skippedCount = skippedCount;
	}
	public int getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}
	public int getProcessedCount() {
		return processedCount;
	}
	public void setProcessedCount(int processedCount) {
		this.processedCount = processedCount;
	}
	
}
