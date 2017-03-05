package com.choudoufu.solr.web.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.View;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.request.SolrRequestInfo;
import org.apache.solr.response.BinaryQueryResponseWriter;
import org.apache.solr.response.JSONResponseWriter;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.servlet.ResponseUtils;
import org.apache.solr.servlet.SolrDispatchFilter;
import org.apache.solr.servlet.SolrRequestParsers;
import org.apache.solr.servlet.cache.Method;
import org.apache.solr.util.FastWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.choudoufu.solr.common.params.CustomParams;
import com.choudoufu.solr.common.util.PropertiesUtil;
import com.choudoufu.solr.common.util.SignUtil;
import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.constants.SysPropConsts;
import com.choudoufu.solr.core.CustomContainer;
import com.choudoufu.solr.core.request.CustomRequestHandler;
import com.choudoufu.solr.schema.entity.SysUser;
import com.choudoufu.solr.util.EhcacheUtil;
import com.choudoufu.solr.util.IdGrowthUtil;
import com.choudoufu.solr.util.IpUtil;
import com.choudoufu.solr.util.RequestFilterUtil;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.util.UserUtil;
import com.choudoufu.solr.util.ViewUtil;


/**
 * 自定义 过滤器
 * @author xuhaowen
 * @serial 2017-1-7
 */
public class CustomFilterBak extends SolrDispatchFilter{
	
	static final Logger log = LoggerFactory.getLogger(CustomFilterBak.class);
	
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	
	public static final Package PACKAGE = SolrCore.class.getPackage();
	
	protected volatile CustomContainer customContainer;
	
	public CustomFilterBak() {
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		
		log.info("SolrCustomFilter.init()");
		try {
			SolrResourceLoader loader = new SolrResourceLoader(SolrResourceLoader.locateSolrHome());
			customContainer = new CustomContainer(loader, super.getCores());
			customContainer.load();
			
			initSystem();
	    }catch( Throwable t ) {
	      log.error( "SolrCustomFilter init error");
	      SolrCore.log( t );
	      if (t instanceof Error) {
	        throw (Error) t;
	      }
	    }
		
	    log.info("SolrCustomFilter.init() done");
	}
	
	/**
	 * 初始化 系统
	 */
	private void initSystem(){
		log.info("SolrCustomFilter.initSystem()");
		//ID增长
		IdGrowthUtil.init(cores.getCore(SysConsts.MODULE_TABLE));
		//属性资源
		PropertiesUtil.init(SysPropConsts.PROP_PATH);
		
		//初始化 系统管理员
		SysUser sysUser = UserUtil.initSystemAdmin();
		SolrCore core = cores.getCore(SysConsts.MODULE_USER);
		SolrQueryRequest solrReq = new SolrQueryRequestBase(core, SolrJUtil.getSolrQuery(true)) {};
		SolrQueryResponse solrResp = new SolrQueryResponse();
		SolrCore.preDecorateResponse(solrReq, solrResp);
//		SolrJUtil.addModel(sysUser, core, solrReq, solrResp);
		log.info("SolrCustomFilter.initSystem() done");
	}
	
	 
	@Override
	public void destroy() {
		super.destroy();
		EhcacheUtil.getInstance().shutdown();
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (this.customContainer == null) {
	      ((HttpServletResponse)response).sendError( 503, "Server is shutting down or failed to initialize" );
	      return;
	    }
		
		CustomContainer cores = this.customContainer;
	    SolrQueryRequest solrReq = null;
	    
	    if(request instanceof HttpServletRequest) {
	      HttpServletRequest req = (HttpServletRequest)request;
	      HttpServletResponse resp = (HttpServletResponse)response;
	      try {
	        String path = req.getServletPath();
	        if( req.getPathInfo() != null ) {
	        	path += req.getPathInfo();
	        }
	        
	        //排除页面
			if(RequestFilterUtil.isExcludePage(path)){
				super.doFilter(request, response, chain);
				return;
			}
			
			String encryptCode = req.getParameter("sigCode");
			if(!"/console".startsWith(path) && SignUtil.validCode(encryptCode)){
				super.doFilter(request, response, chain);
			}else{
				//已登录 校验登录
				boolean isLogin = UserUtil.isLogin(req);
				String action = req.getParameter("action");
				if(path.startsWith(CustomParams.REQ_PATH_CONSOLE_USER) && RequestFilterUtil.isLoginReq(action) 
						&& isLogin){
					resp.sendRedirect("/console/index.html");
					return;
				}
				if(isLogin && ("/console/index.html".equals(path) || !path.startsWith(CustomParams.REQ_PATH_CONSOLE_USER))){
		        	chain.doFilter(request, response);
		        	return;
		        }
				
				solrReq = SolrRequestParsers.DEFAULT.parse(null,path, req);
		        //上传 core配置
		        if(path.equals(CustomParams.REQ_PATH_CCUSTOM) ) {
		        	SolrRequestHandler handler = cores.getCustomHandler();
		            handleCustomRequest(req, response, handler, solrReq);
		            return;
		        }
		        log.info("[custom filter] path=" + path + " params="+solrReq.toString());
		        
				//控制台-用户
		        if(path.equals(CustomParams.REQ_PATH_CONSOLE_USER) ) {
		        	CustomRequestHandler handler = cores.getUserHandler();
		        	handleConsoleRequest(req, resp, handler, solrReq);
		            return;
		        }
//		        resp.sendRedirect("/console/login.html");
		        ViewUtil.goView("/console/login.html", req, resp, "");
		        return;
			}
	        
	      }
	      catch (Throwable ex) {
	        sendError(solrReq, request, (HttpServletResponse)response, ex );
	        // walk the the entire cause chain to search for an Error
	        Throwable t = ex;
	        while (t != null) {
	          if (t instanceof Error)  {
	            if (t != ex)  {
	              log.error("An Error was wrapped in another exception - please report complete stacktrace on SOLR-6161", ex);
	            }
	            throw (Error) t;
	          }
	          t = t.getCause();
	        }
	        return;
	      } finally {
	        try {
	          if (solrReq != null) {
	            log.debug("Closing out SolrRequest: {}", solrReq);
	            solrReq.close();
	          }
	        } finally {
	            SolrRequestInfo.clearRequestInfo();
	        }
	      }
	    }
//	    chain.doFilter(request, response);
	}
	
	private void handleCustomRequest(HttpServletRequest req, ServletResponse response, SolrRequestHandler handler,
            SolrQueryRequest solrReq) throws IOException {
		SolrQueryResponse solrResp = new SolrQueryResponse();
		SolrCore.preDecorateResponse(solrReq, solrResp);
		handler.handleRequest(solrReq, solrResp);
		SolrCore.postDecorateResponse(handler, solrReq, solrResp);
		if (log.isInfoEnabled() && solrResp.getToLog().size() > 0) {
			log.info(solrResp.getToLogAsString("[custom] "));
		}
		QueryResponseWriter respWriter = SolrCore.DEFAULT_RESPONSE_WRITERS.get(solrReq.getParams().get(CommonParams.WT));
		if (respWriter == null) respWriter = SolrCore.DEFAULT_RESPONSE_WRITERS.get("standard");
		writeResponse(solrResp, response, respWriter, solrReq, Method.getMethod(req.getMethod()));
	}
	
	private void handleConsoleRequest(HttpServletRequest req, HttpServletResponse resp, CustomRequestHandler handler,
            SolrQueryRequest solrReq) throws IOException {
		SolrQueryResponse solrResp = new SolrQueryResponse();
		SolrCore.preDecorateResponse(solrReq, solrResp);
		solrReq.getContext().put(CustomParams.ACCESS_IP, IpUtil.getIpAddr(req));
		handler.handleRequest(req, resp, solrReq, solrResp);
		if (log.isInfoEnabled() && solrResp.getToLog().size() > 0) {
			log.info(solrResp.getToLogAsString("[custom] "));
		}
	}

	 @SuppressWarnings("rawtypes")
	protected void sendError(SolrQueryRequest req, 
	      ServletRequest request, 
	      HttpServletResponse response, 
	      Throwable ex) throws IOException {
	    Exception exp = null;
	    try {
  	      SolrQueryResponse solrResp = new SolrQueryResponse();
	      if(ex instanceof Exception) {
	        solrResp.setException((Exception)ex);
	      }
	      else {
	        solrResp.setException(new RuntimeException(ex));
	      }
	      QueryResponseWriter writer = new JSONResponseWriter();
	      writeResponse(solrResp, response, writer, req, Method.GET);
	    }
	    catch (Exception e) { // This error really does not matter
	         exp = e;
	    } finally {
	      try {
	        if (exp != null) {
	          SimpleOrderedMap info = new SimpleOrderedMap();
	          int code = ResponseUtils.getErrorInfo(ex, info, log);
	          response.sendError(code, info.toString());
	        }
	      }finally {
	        
	      }
	   }
	  }
	
	@SuppressWarnings("rawtypes")
	private void writeResponse(SolrQueryResponse solrRsp, ServletResponse response,
             QueryResponseWriter responseWriter, SolrQueryRequest solrReq, Method reqMethod)
		throws IOException {
		
		// Now write it out
		final String ct = responseWriter.getContentType(solrReq, solrRsp);
		// don't call setContentType on null
		if (null != ct) response.setContentType(ct); 
		
		if (solrRsp.getException() != null) {
			System.err.println("---"+solrRsp.getException().getMessage());
			NamedList info = new SimpleOrderedMap();
			int code = ResponseUtils.getErrorInfo(solrRsp.getException(), info, log);
			solrRsp.add("error", info);
			((HttpServletResponse) response).setStatus(code);
		}
		
		if (Method.HEAD != reqMethod) {
			if (responseWriter instanceof BinaryQueryResponseWriter) {
				BinaryQueryResponseWriter binWriter = (BinaryQueryResponseWriter) responseWriter;
				binWriter.write(response.getOutputStream(), solrReq, solrRsp);
			} else {
				String charset = ContentStreamBase.getCharsetFromContentType(ct);
				Writer out = (charset == null)
				? new OutputStreamWriter(response.getOutputStream(), UTF8)
				: new OutputStreamWriter(response.getOutputStream(), charset);
				out = new FastWriter(out);
				responseWriter.write(out, solrReq, solrRsp);
				out.flush();
			}
		}
		//else http HEAD request, nothing to write out, waited this long just to get ContentType
     }
}
