package com.choudoufu.client.server.util;

import java.io.Serializable;


public class HttpProp implements Serializable{
	
	private static final long serialVersionUID = -3058182169859500379L;
	
	private String host;
	private int maxPoolSize = 10;
	private long checkExpirePeriod = 60000;//检查过期周期
	private Integer soTimeOut = 10000;
    private Integer connectionTimeOut = 10000;
    private Integer maxConnectionsPerHost = 1000;
    private Integer maxTotalConnections = 1000;
    private Integer maxRetries = 1;
    
	public HttpProp(String host, int maxPoolSize, long checkExpirePeriod,
			Integer soTimeOut, Integer connectionTimeOut,
			Integer maxConnectionsPerHost, Integer maxTotalConnections,
			Integer maxRetries) {
		super();
		this.host = host;
		this.maxPoolSize = maxPoolSize;
		this.checkExpirePeriod = checkExpirePeriod;
		this.soTimeOut = soTimeOut;
		this.connectionTimeOut = connectionTimeOut;
		this.maxConnectionsPerHost = maxConnectionsPerHost;
		this.maxTotalConnections = maxTotalConnections;
		this.maxRetries = maxRetries;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public long getCheckExpirePeriod() {
		return checkExpirePeriod;
	}

	public void setCheckExpirePeriod(long checkExpirePeriod) {
		this.checkExpirePeriod = checkExpirePeriod;
	}

	public Integer getSoTimeOut() {
		return soTimeOut;
	}

	public void setSoTimeOut(Integer soTimeOut) {
		this.soTimeOut = soTimeOut;
	}

	public Integer getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(Integer connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public Integer getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	public void setMaxConnectionsPerHost(Integer maxConnectionsPerHost) {
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	public Integer getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(Integer maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public Integer getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(Integer maxRetries) {
		this.maxRetries = maxRetries;
	}
}
