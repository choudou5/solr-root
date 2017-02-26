package com.choudoufu.solr.web.modules.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.core.SolrCore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choudoufu.solr.common.util.SignUtil;
import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.model.SysUser;
import com.choudoufu.solr.util.SolrHelper;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.util.UserUtil;
import com.choudoufu.solr.web.modules.BaseController;

@Controller  
@Scope("prototype")   
@RequestMapping("/console/login")  
public class LoginController extends BaseController{

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @param req
	 * @param resp
	 * @param attributes
	 */
	@RequestMapping(value={"", "/"} ,method=RequestMethod.POST)  
    public String login(String username, String password,
    		HttpServletRequest req, HttpServletResponse resp, RedirectAttributes attributes){  
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
			return redirect(SysConsts.VIEW_LOGIN, attributes, "账号密码不能为空！");
		}
		
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_USER);
		SysUser sysUser = SolrJUtil.getModelData(SolrJUtil.getSolrQuery("loginName:"+username), core, SysUser.class);
		if(sysUser != null){
			String pwd = sysUser.getPassword();
			String sig = SignUtil.encrypt(password);
			if(sig.equals(pwd)){
				//登录成功
				UserUtil.loginSucc(req, sysUser.toUser());
				
				log.debug(username+", login success.");
				return redirect(SysConsts.VIEW_INDEX);
			}
		}
		return redirect(SysConsts.VIEW_LOGIN, attributes, "账号密码有误!");
    }
	
	/**
	 * 退出登录
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value="/out",method=RequestMethod.GET)
    public String loginOut(HttpServletRequest req, HttpServletResponse resp){
		UserUtil.loginOut(req);
		return redirect(SysConsts.VIEW_LOGIN);
    }
	
	/**
	 * 游客快捷登录
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	@RequestMapping(value="/visitor",method=RequestMethod.GET)
	public String visitorLogin(HttpServletRequest req, HttpServletResponse resp, RedirectAttributes attributes) throws Exception {
		if(UserUtil.isLogin(req)){
			return redirect(SysConsts.VIEW_INDEX);
		}
		
		try {
			//创建 临时用户
			UserUtil.createTempUser(req);
		} catch (Exception e) {
			log.error("visitorLogin fail.", e);
			return redirect(SysConsts.VIEW_LOGIN, attributes, "/(ㄒoㄒ)/~~服务器内部出了点问题.");
		}
		return redirect(SysConsts.VIEW_INDEX);
	}
	
}
