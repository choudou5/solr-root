package com.choudoufu.client.server;

/**
 * solr服务工厂 接口
 * @author xuhaowende
 * @date 2017年4月14日
 */
public interface ISolrServerFactory {

	/**
	 * 初始化（分布式）
	 * @param zk
	 * @param collNames 集合名称,多个逗号隔开
	 * @param clientTimeOut 客户的超时时间(毫秒)
	 * @param connTimeOut 连接超时时间(毫秒)
	 */
	void init(String zk, String collNames, int clientTimeOut, int connTimeOut);
	
	/**
	 * 初始化
	 * @param httpUrl
	 * @param collNames 集合名称,多个逗号隔开
	 * @param soTimeOut 读取超时时间(read timeout 毫秒)
	 * @param connTimeOut 连接超时时间(毫秒)
	 * @param poolSize 连接池个数
	 * @param maxTotalConnSize 最大总连接数（针对 httpClient）
	 * @param maxRetries 最大重试次数
	 */
	void init(String httpUrl, String collNames, int soTimeOut, int connTimeOut, int poolSize, int maxTotalConnSize, int maxRetries);
	
	org.apache.solr.client.solrj.SolrServer getSolrServer(String collName);
	
	void destroy();
	
	/**
	 * 检查 是否宕机 (针对 主从模式)
	 * @param collName
	 * @param e 异常
	 * @return -1表示正常 ，其他 表示 切换后的新机器下标
	 */
	int checkCrash(String collName, Exception e);
}
