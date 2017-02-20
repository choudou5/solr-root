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
		return new User(loginName, null);
	}
	
	/**
	 * 是否 已登录
	 * @param request
	 */
	public static boolean isLogin(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute(SysConsts.USER_SESSION);
		return user!=null?true:false;
	}

	/**
	 * 添加 登录成功 属性
	 * @param request
	 */
	public static void addAttrByloginSucc(HttpServletRequest request, User user){
		//添加 session
		request.getSession().setAttribute(SysConsts.USER_SESSION, user);
		
		//累加 在线用户
		EhcacheUtil.getInstance().put(CacheConsts.CACEH_USER_SESSION, user.getLoginName(), 1);
	}
	
	/**
	 * 获得 在线用户数
	 * @return
	 */
	public static int getOnlineNum(){
		return EhcacheUtil.getInstance().getCacheSize(CacheConsts.CACEH_USER_SESSION);
	}
	
}
