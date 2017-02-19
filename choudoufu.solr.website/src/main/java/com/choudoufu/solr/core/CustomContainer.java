package com.choudoufu.solr.core;

import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrResourceLoader;

import com.choudoufu.solr.handler.CustomHandler;
import com.choudoufu.solr.handler.UserHandler;

/**
 * 自定义 容器
 * @author xuhaowen
 * @serial 2017-1-7
 */
public class CustomContainer {
	
	protected CustomHandler customHandler = null;
	protected UserHandler userHandler = null;
	
	protected CoreContainer coreContainer;
	protected final SolrResourceLoader loader;
	
	public CustomContainer(SolrResourceLoader loader, CoreContainer coreContainer) {
		this.coreContainer = coreContainer;
		this.loader = loader;
	}

	/**
    * Load the cores defined for this CoreContainer
    */
    public void load()  {
    	customHandler = createHandler("com.choudoufu.solr.handler.CustomHandler", CustomHandler.class);
    	userHandler = createHandler("com.choudoufu.solr.handler.UserHandler", UserHandler.class);
    }

    public <T> T createHandler(String handlerClass, Class<T> clazz) {
        return loader.newInstance(handlerClass, clazz, null, new Class[] { CoreContainer.class }, new Object[] { coreContainer });
    }

	public CustomHandler getCustomHandler() {
		return customHandler;
	}
	
	public UserHandler getUserHandler() {
		return userHandler;
	}
	
}
