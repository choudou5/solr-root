package com.choudoufu.solr.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.choudoufu.solr.util.EhcacheUtil;

public class LoginFilter implements Filter {

	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
	
	private static String[] EXCLUDE_PAGES = new String[]{
		"/website/index.html",
		"/console/login.html",
		"/console/img",
		"/console/js",
		"/console/css",
		"/static/"
	};  
	
	/**
	 * 是否 排除页面
	 * @param req
	 * @return
	 */
	private boolean isExcludePage(String path){
		System.out.println("path:"+path);
		for (String page : EXCLUDE_PAGES) {
			if(path.startsWith(page))
				return true;
		}
		return false;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
		String path = req.getServletPath();
		if (req.getPathInfo() != null) {
			path += req.getPathInfo();
		}
		//排除页面
		if(isExcludePage(path)){
			chain.doFilter(request, response);
			return;
		}
		
//		//校验登录
//		String encryptCode = req.getParameter("sigCode");
//		if(SignUtil.validCode(encryptCode) || UserUtil.isLogin(req)){
//			chain.doFilter(request, response);
//		}else{
//			//
//	        if(path.equals("/console/login") ) {
//	        	SolrRequestHandler handler = cores.getCustomHandler();
//	            handleCustomRequest(req, response, handler, solrReq);
//	            return;
//	        }
//			request.getRequestDispatcher("/login").forward(request, response);
//		}
	}

	@Override
	public void destroy() {
		EhcacheUtil.getInstance().shutdown();
	}
}