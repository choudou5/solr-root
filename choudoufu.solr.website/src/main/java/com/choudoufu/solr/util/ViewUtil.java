package com.choudoufu.solr.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.core.SolrCore;

public class ViewUtil {

	/**
	 * 跳转 视图
	 * @param view 视图地址
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void goView(String view, HttpServletRequest request, HttpServletResponse response) throws IOException{
		goView(view, request, response, null);
	}
	
	/**
	 * 跳转 视图
	 * @param view 视图地址
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void goView(String view, HttpServletRequest request, HttpServletResponse response, String message) throws IOException{
		InputStream in = request.getSession().getServletContext().getResourceAsStream(view);
		if (in != null) {
			try {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				Writer out = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);

				String html = IOUtils.toString(in, "UTF-8");
				Package pack = SolrCore.class.getPackage();

				String[] search = new String[] { "${contextPath}", "${version}", "message"};
				String[] replace = new String[] {
						StringEscapeUtils.escapeJavaScript(request.getContextPath()),
						StringEscapeUtils.escapeJavaScript(pack.getSpecificationVersion()), message};

				out.write(StringUtils.replaceEach(html, search, replace));
				out.flush();
			} finally {
				IOUtils.closeQuietly(in);
			}
		} else {
			response.sendError(404);
		}
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
