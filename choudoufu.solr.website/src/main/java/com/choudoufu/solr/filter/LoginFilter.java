package com.choudoufu.solr.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.choudoufu.solr.common.util.SignUtil;
import com.choudoufu.solr.util.EhcacheUtil;
import com.choudoufu.solr.util.UserUtil;

public class LoginFilter implements Filter {

	private static final long serialVersionUID = 1L;

	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String path = req.getServletPath();
		if (req.getPathInfo() != null) {
			path += req.getPathInfo();
		}
		
		String encryptCode = req.getParameter("sigCode");
		if(SignUtil.decryptCode(encryptCode) || UserUtil.isLogin(req)){
			chain.doFilter(request, response);
		}else{
			request.getRequestDispatcher("/login").forward(request, response);
		}
	}

	@Override
	public void destroy() {
		EhcacheUtil.getInstance().shutdown();
	}
}