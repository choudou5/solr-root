package com.choudoufu.solr.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.choudoufu.solr.constants.CacheConsts;
import com.choudoufu.solr.filter.CustomFilter;

public class ViewUtil {

	/**
	 * 跳转 视图
	 * @param view 视图地址
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void goView(String view, HttpServletRequest request, HttpServletResponse response) throws IOException{
		goView(view, request, response, null, null);
	}
	
	/**
	 * 跳转 视图
	 * @param view 视图地址
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void goView(String view, HttpServletRequest request, HttpServletResponse response, String message) throws IOException{
		goView(view, request, response, message, null);
	}
	
	/**
	 * 跳转 视图
	 * @param view 视图地址
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void goView(String view, HttpServletRequest request, HttpServletResponse response, String message, Map<String, String> respParam) throws IOException{
		String html = getCacheHtml(view);
		InputStream in = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			Writer out = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
			
			if(html == null){
				in = request.getSession().getServletContext().getResourceAsStream(view);
				if(in == null){
					response.sendError(404);
					return;
				}
				html = IOUtils.toString(in, "UTF-8");
				cacheHtml(view, html);
			}
			
			//相应 参数
			ArrayListExt<String> search = new ArrayListExt<String>(10);
			ArrayListExt<String> replace = new ArrayListExt<String>(10);
			initSystemParam(search, replace, message, request);
			if(MapUtils.isNotEmpty(respParam)){
				for (String paramName : respParam.keySet()) {
					search.add(paramName);
				}
			}
			
			out.write(StringUtils.replaceEach(html, search.toArrayStr(), replace.toArrayStr()));
			out.flush();
			out.close();
		} finally {
			if(in != null) IOUtils.closeQuietly(in);
		}
	}
	
	//初始化 系统参数
	private static void initSystemParam(List<String> search, List<String> replace, String message, HttpServletRequest request){
		search.add("${contextPath}");
		search.add("${version}");
		search.add("${message}");
		replace.add(StringEscapeUtils.escapeJavaScript(request.getContextPath()));
		replace.add(CustomFilter.PACKAGE.getSpecificationVersion());
		replace.add(message);
		if(StringUtils.isBlank(message)){
			search.add("${messageCss}");
			replace.add("hide");
		}
	}
	
	private static void cacheHtml(String view, String html){
		EhcacheUtil.getInstance().put(CacheConsts.CACHE_SYS, view, html);
	}
	
	private static String getCacheHtml(String view){
		return (String)EhcacheUtil.getInstance().get(CacheConsts.CACHE_SYS, view);
	}
	
	
	/**
	 * 重定向 视图
	 * @param view 视图地址
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void redirect(String view, HttpServletRequest request, HttpServletResponse response) throws IOException{
		InputStream in = request.getSession().getServletContext().getResourceAsStream(view);
		if (in != null) {
			try {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				response.sendRedirect(view);
			} finally {
				IOUtils.closeQuietly(in);
			}
		} else {
			response.sendError(404);
		}
	}

}
