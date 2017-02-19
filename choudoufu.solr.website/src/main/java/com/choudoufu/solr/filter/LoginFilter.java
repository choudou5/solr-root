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
	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
		String path = req.getServletPath();
		if (req.getPathInfo() != null) {
			path += req.getPathInfo();
		}
		
	}

	@Override
	public void destroy() {
		EhcacheUtil.getInstance().shutdown();
	}
}