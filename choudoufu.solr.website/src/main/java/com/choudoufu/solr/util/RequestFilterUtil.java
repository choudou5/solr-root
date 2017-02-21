package com.choudoufu.solr.util;

/**
 * 请求过滤 工具类
 * @author xuhaowen
 * @date 2017年2月21日
 */
public class RequestFilterUtil {

	/**
	 * 是否 排除页面
	 * @param path
	 * @return
	 */
	public static boolean isExcludePage(String path){
		String[] EXCLUDE_PAGES = new String[]{
				"/website/images",
				"/website/js",
				"/website/css",
				"/website/fonts",
				"/website/index.html",
				"/console/login.html",
				"/console/img",
				"/console/js",
				"/console/css",
				"/console/font-awesome",
				"/console/static",
				"/static/",
				"/favicon.ico",
				"/admin",
				"/css",
				"/js",
				"/img",
				"/font-awesome",
				"/tpl",
				"/userHistory",
				"/collection1",
				"/index.js"
			};
		System.out.println("path:"+path);
		for (String page : EXCLUDE_PAGES) {
			if(path.startsWith(page))
				return true;
		}
		return false;
	}
	
	/**
	 * 是否 为登录请求
	 * @param action
	 * @return
	 */
	public static boolean isLoginReq(String action){
		String[] LOGIN_PAGES = new String[]{
				"visitor",
				"login",
		};
		for (String page : LOGIN_PAGES) {
			if(page.equalsIgnoreCase(action))
				return true;
		}
		return false;
	}
	
}
