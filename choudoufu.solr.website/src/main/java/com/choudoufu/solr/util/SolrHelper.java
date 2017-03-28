package com.choudoufu.solr.util;

import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 描述：Spring 工具类
 * 构建组：setsearch
 * 作者：xuhaowen
 * 邮箱: xuhaowen@skg.com
 * 日期:Jun 4, 2016-12:16:40 PM
 * 版权：SKG 公司版权所有
 * </pre>
 */
public class SolrHelper {

	
	private static volatile CoreContainer coreContainer;

	protected static final Logger LOGGER = LoggerFactory.getLogger(SolrHelper.class);
	
	public static void init(final CoreContainer coreContainer){
		SolrHelper.coreContainer = coreContainer;
	}
	

	/**
	 * 获取Spring容器的Bean
	 * @param beanClass
	 * @return T
	 * @exception
	 * @since 1.0.0
	 */
	public static SolrCore getCore(String coreName) {
		try{
			return coreContainer.getCore(coreName);
		}
		catch(Exception ex){
			LOGGER.debug("getCore:" + coreName +"," + ex.getMessage());
		}
		return null;
	}
	
	public static String getSolrHome() {
		return coreContainer.getSolrHome();
	}

}
