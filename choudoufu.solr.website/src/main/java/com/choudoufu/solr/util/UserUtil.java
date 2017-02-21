package com.choudoufu.solr.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;

import com.choudoufu.solr.constants.CacheConsts;
import com.choudoufu.solr.constants.IdGrowthConsts;
import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.entity.User;

/**
 * 用户 工具类
 * @author xuhaowen
 * @date 2017年2月9日
 */
public class UserUtil {

	
	
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(createTempUser());
		}
	}
	
	/**
	 * 创建 临时用户
	 * @return User
	 */
	public static User createTempUser(){
		String loginName = "temporary_"+IdGrowthUtil.getIncrId(IdGrowthConsts.TEMP_USER);
		String password = RandomStringUtils.randomAlphanumeric(6);
		return new User(loginName, password);
	}
	
	public static User getUser(HttpServletRequest request){
		return (User) request.getSession().getAttribute(SysConsts.USER_SESSION);
	}
	
	/**
	 * 是否 已登录
	 * @param request
	 */
	public static boolean isLogin(HttpServletRequest request){
		return getUser(request)!=null?true:false;
	}

	/**
	 *  登录成功 
	 * @param request
	 */
	public static void loginSucc(HttpServletRequest request, User user){
		//添加 session
		request.getSession().setAttribute(SysConsts.USER_SESSION, user);
		
		//累加 在线用户
		EhcacheUtil.getInstance().put(CacheConsts.CACHE_USER_SESSION, user.getLoginName(), 1);
	}
	
	
	/**
	 * 添加 登录成功 属性
	 * @param request
	 */
	public static void loginOut(HttpServletRequest request){
		User user = getUser(request);
		if(user != null){
			request.getSession().removeAttribute(SysConsts.USER_SESSION);
			//减少 在线用户
			EhcacheUtil.getInstance().remove(CacheConsts.CACHE_USER_SESSION, user.getLoginName());
		}
	}
	
	/**
	 * 获得 在线用户数
	 * @return
	 */
	public static int getOnlineNum(){
		return EhcacheUtil.getInstance().getCacheSize(CacheConsts.CACHE_USER_SESSION);
	}
	
}
