package com.choudoufu.solr.handler;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.core.request.CustomRequestHandlerBase;
import com.choudoufu.solr.entity.User;
import com.choudoufu.solr.model.SysUser;
import com.choudoufu.solr.model.SysUserEventHistory;
import com.choudoufu.solr.util.IdGrowthUtil;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.util.UserUtil;
import com.choudoufu.solr.util.ViewUtil;

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
	public void handleRequestBody(HttpServletRequest req, HttpServletResponse resp, SolrQueryRequest solrReq, SolrQueryResponse rsp)
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
		handleRequestInternal(req, resp, solrReq, rsp, action);
	}

	protected void handleRequestInternal(HttpServletRequest req, HttpServletResponse resp, SolrQueryRequest solrReq,
			SolrQueryResponse rsp, CustomAction action) throws Exception {
		if (action != null) {
			switch (action) {
			    case VISITOR: {
				    this.handleVisitorAction(req, resp, solrReq, rsp);
					break;
			    }
			    case LOGIN: {
				    this.handleLoginAction(req, resp, solrReq, rsp);
					break;
			    }
			    case LOGIN_OUT: {
				    this.handleLoginOutAction(req, resp, solrReq, rsp);
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

	/**
	 * 登录
	 * @param req
	 * @param resp
	 * @param solrReq
	 * @param solrResp
	 * @throws IOException
	 */
	protected void handleLoginAction(HttpServletRequest req, HttpServletResponse resp, SolrQueryRequest solrReq, SolrQueryResponse solrResp) throws IOException {
		SolrParams params = solrReq.getParams();
		String loginName = params.get("username");
		String loginPwd = params.get("password");
		if(StringUtils.isBlank(loginName) || StringUtils.isBlank(loginPwd)){
			ViewUtil.goView(SysConsts.VIEW_LOGIN, req, resp, "账号密码不能为空！");
			return;
		}
		
		SolrCore core = coreContainer.getCore(SysConsts.SYS_MODULE_USER);
		SysUser sysUser = SolrJUtil.getModel("loginName:"+loginName, core, solrReq, solrResp, SysUser.class);
		if(sysUser != null){
			String pwd = sysUser.getPassword();
			String sig = SignUtil.encrypt(loginPwd);
			if(sig.equals(pwd)){
				//登录成功
				UserUtil.loginSucc(req, sysUser.toUser());
				
				log.debug(loginName+", login success.");
				ViewUtil.redirect(SysConsts.VIEW_INDEX, req, resp);
				return;
			}
		}
		log.debug(loginName+", login fail.");
		ViewUtil.goView(SysConsts.VIEW_LOGIN, req, resp, "账号密码有误!");
	}
	
	/**
	 * 退出登录
	 * @param req
	 * @param resp
	 * @param solrReq
	 * @param solrResp
	 * @throws IOException
	 */
	protected void handleLoginOutAction(HttpServletRequest req, HttpServletResponse resp, SolrQueryRequest solrReq, SolrQueryResponse solrResp) throws IOException {
		UserUtil.loginOut(req);
		ViewUtil.redirect("/", req, resp);
	}
	
	/**
	 * 游客快捷登录
	 * @param req
	 * @param resp
	 * @param solrReq
	 * @param sresp
	 * @throws IOException
	 */
	protected void handleVisitorAction(HttpServletRequest req, HttpServletResponse resp, SolrQueryRequest solrReq, SolrQueryResponse sresp) throws IOException {
		String ip = solrReq.getContext().get(CustomParams.ACCESS_IP).toString();
		//创建 临时用户
		User user = UserUtil.createTempUser();
		//登录成功
		UserUtil.loginSucc(req, user);
		
		//保存记录
		SolrCore core = coreContainer.getCore(SysConsts.SYS_MODULE_USER_EVENT_HI);
		SolrJUtil.addModel(buildUserHistory(SysConsts.SYS_MODULE_USER_EVENT_HI, ip, user.getLoginName()), core, solrReq, sresp);
		ViewUtil.redirect(SysConsts.VIEW_INDEX, req, resp);
	}
	
	private SysUserEventHistory buildUserHistory(String module, String ip, String loginName){
		SysUserEventHistory model = new SysUserEventHistory();
		model.setId(IdGrowthUtil.getIncrIdStr(module));
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