package com.choudoufu.solr.common.util;

public interface PropConsts {

	String PROP_PATH = "/system.properties";
	
	String PROP_KEY_SIG= "system.sig.key";
	
	String PROP_KEY_SIG_TIME_OUT= "system.sig.timeout";

	String PROP_KEY_ADMIN = "system.admin";
	
	String PROP_KEY_ADMIN_PWD= "system.admin.pwd";
	
	String PROP_KEY_EVENT_LISTEN_PERIOD= "event.listen.period";
	int PROP_KEY_EVENT_LISTEN_PERIOD_DEF= 20;
	
	String PROP_KEY_SEARCH_STATS_QUEUE_SIZE = "search.stats.queue.size";
	int PROP_KEY_SEARCH_STATS_QUEUE_SIZE_DEF = 20000;//搜索统计 队列大小
	String PROP_KEY_SEARCH_STATS_LISTEN_PERIOD = "search.stats.listen.period";
	int PROP_KEY_SEARCH_STATS_LISTEN_PERIOD_DEF = 5;//搜索统计 监听间隔（秒）
}
