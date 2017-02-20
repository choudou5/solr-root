package com.choudoufu.solr.handler;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.choudoufu.solr.common.params.CustomParams;
import com.choudoufu.solr.common.params.CustomParams.CustomAction;
import com.choudoufu.solr.common.util.SignUtil;
import com.choudoufu.solr.core.request.CustomRequestHandlerBase;
import com.choudoufu.solr.entity.User;
import com.choudoufu.solr.model.SysUser;
import com.choudoufu.solr.model.UserHistory;
import com.choudoufu.solr.util.IdGrowthUtil;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.util.UserUtil;

/**
 * 用户 管理器
 * @author xuhaowen
 * @date 2017年2月17日
 */
public class UserHandler extends CustomRequestHandlerBase {

	protected static Logger log = LoggerFactory.getLogger(UserHandler.class);

	protected final CoreContainer coreContainer;
	
	protected static final String SOLR_HOME = SolrResourceLoader.locateSolrHome();
	    
	public UserHandler() {
		super();
		this.coreContainer = null;
	}

	public UserHandler(final CoreContainer coreContainer) {
		this.coreContainer = coreContainer;
	}

	@SuppressWarnings("rawtypes")
	@Override
	final public void init(NamedList args) {
		throw new SolrException(
				SolrException.ErrorCode.SERVER_ERROR,
				"UserHandler should n" + "it is a special Handler configured directly by the RequestDispatcher");
	}

	public CoreContainer getCoreContainer() {
		return this.coreContainer;
	}

	@Override
	public void handleRequest(SolrQueryRequest req, SolrQueryResponse rsp) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void handleRequestBody(HttpServletRequest req, SolrQueryRequest solrReq, SolrQueryResponse rsp)
			throws Exception {
		// Make sure the cores is enabled
		CoreContainer cores = getCoreContainer();
		if (cores == null) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Core container instance missing");
		}

		// 挑选 action
		SolrParams params = solrReq.getParams();
		CustomAction action = null;
		String actionParam = params.get(CustomParams.ACTION);
		if (actionParam != null)
			action = CustomAction.get(actionParam);
		if (action == null){
			this.handleUnknownAction(solrReq);
			return;
		}
		handleRequestInternal(req, solrReq, rsp, action);
	}

	protected void handleRequestInternal(HttpServletRequest req, SolrQueryRequest solrReq,
			SolrQueryResponse rsp, CustomAction action) throws Exception {
		if (action != null) {
			switch (action) {
			    case VISITOR: {
				    this.handleVisitorAction(req, solrReq, rsp);
					break;
			    }
			    case LOGIN: {
				    this.handleLoginAction(req, solrReq, rsp);
					break;
			    }
				default: {
					this.handleUnknownAction(solrReq);
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

	protected void handleLoginAction(HttpServletRequest req, SolrQueryRequest solrReq, SolrQueryResponse resp) {
		SolrParams params = solrReq.getParams();
		String loginName = params.get("user");
		String loginPwd = params.get("password");
		
		String module = "sysUser";
		SolrCore core = coreContainer.getCore(module);
		
		SysUser sysUser = SolrJUtil.queryModel("loginName:"+loginName, core, solrReq, resp, SysUser.class);
		if(sysUser != null){
			String pwd = sysUser.getPassword();
			String sig = SignUtil.encrypt(loginPwd);
			if(sig.equals(pwd)){
				System.out.println("login success.");
				return;
			}
		}
	}
	
	protected void handleVisitorAction(HttpServletRequest req, SolrQueryRequest solrReq, SolrQueryResponse resp) {
		String ip = solrReq.getContext().get(CustomParams.ACCESS_IP).toString();
		//创建 临时用户
		User user = UserUtil.createTempUser();
		//登录成功
		UserUtil.addAttrByloginSucc(req, user);
		
		//保存记录
		String module = "userHistory";
		SolrCore core = coreContainer.getCore(module);
		SolrJUtil.addModel(buildUserHistory(module, ip, user.getLoginName()), core, solrReq, resp);
	}
	
	private UserHistory buildUserHistory(String module, String ip, String loginName){
		UserHistory model = new UserHistory();
		model.setId(IdGrowthUtil.getIncrIdStr(module)+22);
		model.setAction(CustomAction.VISITOR.name());
		model.setCreateTime(new Date());
		model.setIp(ip);
		model.setLoginName(loginName);
		return model;
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