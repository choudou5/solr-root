package com.choudoufu.solr.handler;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.choudoufu.solr.common.params.CustomParams;
import com.choudoufu.solr.common.params.CustomParams.CustomAction;
import com.choudoufu.solr.core.CustomContainer;

/**
 * 用户 管理器
 * @author xuhaowen
 * @date 2017年2月17日
 */
public class UserHandler extends RequestHandlerBase {

	protected static Logger log = LoggerFactory.getLogger(UserHandler.class);

	protected final CustomContainer customContainer;
	
	protected static final String SOLR_HOME = SolrResourceLoader.locateSolrHome();
	    
	public UserHandler() {
		super();
		this.customContainer = null;
	}

	public UserHandler(final CustomContainer customContainer) {
		this.customContainer = customContainer;
	}

	@SuppressWarnings("rawtypes")
	@Override
	final public void init(NamedList args) {
		throw new SolrException(
				SolrException.ErrorCode.SERVER_ERROR,
				"UserHandler should n" + "it is a special Handler configured directly by the RequestDispatcher");
	}

	public CustomContainer getCustomContainer() {
		return this.customContainer;
	}

	@Override
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {
		// Make sure the cores is enabled
		CustomContainer cores = getCustomContainer();
		if (cores == null) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Core container instance missing");
		}

		// 挑选 action
		SolrParams params = req.getParams();
		CustomAction action = null;
		String actionParam = params.get(CustomParams.ACTION);
		if (actionParam != null)
			action = CustomAction.get(actionParam);
		if (action == null){
			this.handleUnknownAction(req);
			return;
		}
		handleRequestInternal(req, rsp, action);
	}

	protected void handleRequestInternal(SolrQueryRequest req,
			SolrQueryResponse rsp, CustomAction action) throws Exception {
		if (action != null) {
			switch (action) {
				default: {
					this.handleUnknownAction(req);
					break;
				}
			}
		}
		rsp.setHttpCaching(false);
	}

	/**
	 * Handle Unknown Action.
	 */
	protected void handleUnknownAction(SolrQueryRequest req) {
		throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Unsupported operation: "+ req.getParams().get(CustomParams.ACTION));
	}

	// ////////////////////// SolrInfoMBeans methods //////////////////////

	@Override
	public String getDescription() {
		return "Manage Multiple Solr Cores";
	}

	@Override
	public String getSource() {
		return null;
	}

}