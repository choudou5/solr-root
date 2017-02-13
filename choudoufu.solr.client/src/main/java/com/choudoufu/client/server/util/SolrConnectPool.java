package com.choudoufu.client.server.util;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.MapUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：Solr连接池
 * 构建组：solr-client
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:Mar 14, 2016-1:55:01 PM
 * 版权：徐浩文版权所有
 */
public class SolrConnectPool {

	private final static Logger LOGGER = LoggerFactory.getLogger(SolrConnectPool.class);
	
	private boolean isCloudModel = false;
	private int maxPoolSize = 10;
	private long checkExpirePeriod;
	
	private static final int CORE_SIZE_MAX = 2000;//最大连接池数（实际 *1000）
	private static final int CORE_SIZE_DEF = 10;//默认10个 连接数
	
	private static final int MINUTE_FOR_MS = 1000*60;//分钟分钟 过期
	private final ReentrantLock mainLock = new ReentrantLock();
    
//    private Map<String, Map<String, SolrWorker>> pool = new ConcurrentHashMap<String, Map<String, SolrWorker>>();
	private Map<String, ArrayBlockingQueue<SolrServer>> pool = new ConcurrentHashMap<String, ArrayBlockingQueue<SolrServer>>();
//    private ArrayBlockingQueue<SolrServer> queue = new ArrayBlockingQueue<SolrServer>(100);
    
    
    private SolrConnectPool() {}
    
    /**内部类，用于实现lzay机制 */
    private static class SingletonHolder{
        /** 单例变量  */
        private static SolrConnectPool instance = new SolrConnectPool();
    }
    
    /**
     * 初始化 连接池信息
     * @param maxPoolSize 最大连接数
     * @param checkExpirePeriod 检查过期周期（毫秒）
     * @return boolean
     */
    public static SolrConnectPool initialized(int maxPoolSize, long checkExpirePeriod) {
    	final SolrConnectPool instance = SingletonHolder.instance;
    	instance.setMaxPoolSize(maxPoolSize);
    	instance.setCheckExpirePeriod(checkExpirePeriod);
        return instance	;
    }
//    
    /**获取单例对象实例 */
    public static SolrConnectPool getInstance() {
        return SingletonHolder.instance;
    }
    
    /**
     * 是否 创建新的连接对象
     * @param module 模块名
     * @return boolean
     */
    public boolean isCreate(String module) {
		boolean flag = false;
		ArrayBlockingQueue<SolrServer> queue = pool.get(module);
		if(queue == null || queue.isEmpty()|| queue.size() < maxPoolSize){
			flag = true;
		}
		return flag;
	}
    
    /**
     * 获得 连接对象
     * @param module 模块名
     * @return boolean
     */
	public SolrServer getConnection(String module) {
		try {
			ArrayBlockingQueue<SolrServer> queue = pool.get(module);
			if(queue == null || queue.isEmpty()){
				return null;
			}
//			LOGGER.debug("getConnection accessCount:"+accessCount+", total:"+workers.size()+", index:"+index);
			return queue.peek();
		} catch (Exception e) {
			LOGGER.error("get solr connnection exception", e);
		}
		return null;
	}
	
	/**
     * 移除 连接对象
     * @param module 模块名
     * @return boolean
     */
	public void remove(String module) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
        	ArrayBlockingQueue<SolrServer> queue = pool.get(module);
        	if(!queue.isEmpty()){
        		SolrServer solrServer = queue.poll();
        		if(solrServer != null) solrServer = null;
        	}
        } finally {
            mainLock.unlock();
        }
    }
	
	/**
     * 添加 连接对象
     * @param module 模块名
     * @return boolean
     */
	public boolean add(String module, SolrServer server) {
        boolean workerAdded = false;
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
        	ArrayBlockingQueue<SolrServer> queue = pool.get(module);
        	if(queue == null){
        		queue = new ArrayBlockingQueue<SolrServer>(maxPoolSize);
        	}
        	queue.add(server);
    		pool.put(module, queue);
            workerAdded = true;
        } finally {
            mainLock.unlock();
        }
        return workerAdded;
    }
	
//	/**
//     * 清理 过期连接对象
//     * @param module 模块名
//     * @return boolean
//     */
//	private void clean() {
//		final ReentrantLock mainLock = this.mainLock;
//        mainLock.lock();
//        try {
//        	for (String module : pool.keySet()) {
//        		ArrayBlockingQueue<SolrServer> queue = pool.get(module);
//        		Iterator<SolrWorker> it = workers.iterator();
//                while (it.hasNext()) {
//                	SolrWorker worker = it.next();
//                	long lastAccessTime = worker.getLastAccessTime()+checkExpirePeriod;
//                	long now = System.currentTimeMillis();
//                	//LOGGER.debug("clean() lastAccessTime:"+lastAccessTime+", now:"+now+", isShutdown:"+(now>lastAccessTime));
//                    if (now < lastAccessTime){
//                    	worker.shutdown();
//                    	it.remove();
//                    	worker = null;
//                    	break;
//                    }
//                }
//			}
//        } finally {
//            mainLock.unlock();
//        }
//    }

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		if(maxPoolSize == 0){
    		maxPoolSize = CORE_SIZE_DEF;
    	}else if(maxPoolSize > CORE_SIZE_MAX){
    		maxPoolSize = CORE_SIZE_MAX;
    	}
		this.maxPoolSize = maxPoolSize;
	}

	public long getCheckExpirePeriod() {
		return checkExpirePeriod;
	}

	public void setCheckExpirePeriod(long checkExpirePeriod) {
		if(checkExpirePeriod == 0){
			checkExpirePeriod = MINUTE_FOR_MS;
    	}
		this.checkExpirePeriod = checkExpirePeriod;
	}
	
	public void clear(){
		final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
        	if(MapUtils.isNotEmpty(pool)){
        		for (String module : pool.keySet()) {
        			ArrayBlockingQueue<SolrServer> queue = pool.get(module);
        			if(queue.isEmpty()){
        				queue.clear();
        			}
        		}
        		pool.clear();
        	}
        } finally {
            mainLock.unlock();
        }
	}
	
	public void clear(String module){
		final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
        	if(MapUtils.isNotEmpty(pool)){
    			ArrayBlockingQueue<SolrServer> queue = pool.get(module);
    			if(!queue.isEmpty()){
    				queue.clear();
    			}
        	}
        } finally {
            mainLock.unlock();
        }
	}

	public boolean isCloudModel() {
		return isCloudModel;
	}

	public void setCloudModel(boolean isCloudModel) {
		this.isCloudModel = isCloudModel;
	}
	
}
