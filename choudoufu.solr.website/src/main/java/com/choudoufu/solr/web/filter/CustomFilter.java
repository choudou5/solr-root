package com.choudoufu.solr.web.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.apache.solr.servlet.cache.Method;
import org.apache.solr.util.FastWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
import com.choudoufu.solr.util.SensitiveWordUtil;
import com.choudoufu.solr.util.SolrHelper;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.util.SpringContextHolder;
import com.choudoufu.solr.util.SysLogUtil;
import com.choudoufu.solr.util.UserUtil;


/**
 * 自定义 过滤器
 * @author xuhaowen
 * @serial 2017-1-7
 */
public class CustomFilter extends SolrDispatchFilter{
	
	static final Logger log = LoggerFactory.getLogger(CustomFilter.class);
	
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	
	public static final Package PACKAGE = SolrCore.class.getPackage();
	
	protected volatile CustomContainer customContainer;
	
	public CustomFilter() {
		
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		
		log.info("SolrCustomFilter.init()");
		try {
			SolrResourceLoader loader = new SolrResourceLoader(SolrResourceLoader.locateSolrHome());
			customContainer = new CustomContainer(loader, super.getCores());
			customContainer.load();
			
			
			initSystem(config.getServletContext());
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
	private void initSystem(ServletContext servletContext){
		log.info("SolrCustomFilter.initSystem()");
		
		//初始化 上下文
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		SpringContextHolder.setContext(context);
		
		//初始化 solr信息
		SolrHelper.init(cores);
		
		//ID增长
		IdGrowthUtil.init(cores.getCore(SysConsts.MODULE_TABLE));
		//属性资源
		PropertiesUtil.init(SysPropConsts.PROP_PATH);
		
		//初始化 敏感词
		SensitiveWordUtil.initBaseDictData();
		
		//初始化 系统管理员
		SysUser sysUser = UserUtil.initSystemAdmin();
		SolrCore core = cores.getCore(SysConsts.MODULE_USER);
		SolrQueryRequest solrReq = new SolrQueryRequestBase(core, SolrJUtil.getSolrQuery(true)) {};
		SolrQueryResponse solrResp = new SolrQueryResponse();
		SolrCore.preDecorateResponse(solrReq, solrResp);
		SolrJUtil.addModelData(sysUser, core);
		log.info("SolrCustomFilter.initSystem() done");
	}
	
	 
	@Override
	public void destroy() {
		super.destroy();
		EhcacheUtil.getInstance().shutdown();
		SpringContextHolder.clearHolder();
	}
	
	//未登录 是否允许请求
	private boolean unLoginAllowRequest(HttpServletRequest req, String path){
		String viewName = req.getParameter("path");
		System.out.println(path+"?path="+viewName);
		if("/console/login".equals(path) || "/console/login/visitor".equals(path))
			return true;
		return (path.startsWith("/console/page") && "/login".equals(viewName));
	}
	
	//是否为 solr静态文件
	private boolean isSolrStaticFile(String path){
		return (path.startsWith("/css") || path.startsWith("/img") 
				|| path.startsWith("/js"));
	}
	
	private boolean isUserRequestCollection(String[] array){
		if(array.length > 1){
			if(array[1].equals("tpl") || array[1].equals("admin") || array[1].startsWith("admin.html") || getRequestCore(array) != ""){
				return true;
			}
		}
		return false;
	}
	
	private String getRequestCore(String[] array){
		boolean isReqCore = false;
		if(array.length > 1){
			isReqCore = cores.getAllCoreNames().contains(array[1]);
		}
		return isReqCore?array[1]:"";
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (this.customContainer == null) {
	      ((HttpServletResponse)response).sendError( 503, "Server is shutting down or failed to initialize" );
	      return;
	    }
		
	    SolrQueryRequest solrReq = null;
	    if(request instanceof HttpServletRequest) {
	      HttpServletRequest req = (HttpServletRequest)request;
	      HttpServletResponse resp = (HttpServletResponse)response;
	      try {
	    	req.setAttribute("org.apache.solr.CoreContainer", cores);
	        String path = req.getServletPath();
	        if( req.getPathInfo() != null ) {
	        	path += req.getPathInfo();
	        }
			
	        //静态资源 放行
	        if(path.startsWith("/website") || path.startsWith("/static") || isSolrStaticFile(path)){
	        	chain.doFilter(request, response);
	        	return;
	        }
	        
	        boolean isLogin = UserUtil.isLogin(req);
			String encryptCode = req.getParameter("sigCode");
			String[] array = path.split("/");
			boolean isApi = SignUtil.validCode(encryptCode);
			boolean isReqCore = isUserRequestCollection(array);
			String reqCoreName = getRequestCore(array);
			if(isApi || (isLogin && isReqCore)){
				//无权限
				if(!isApi && !UserUtil.isPermission(req, reqCoreName)){
					resp.sendError(403);
					return;
				}
				//日志记录
//				SysLogUtil.saveLog(req, "api调用", "api");
				super.doFilter(request, response, chain);
				return;
			}else{
				//未登录  去登录
				if(!isLogin){
					if(unLoginAllowRequest(req, path)){
						//日志记录
						SysLogUtil.saveLog(req, "系统请求", "sys");
						chain.doFilter(request, response);
						return;
					}
					resp.sendRedirect(SysConsts.VIEW_LOGIN);
					return;
				}else{
					//无权限
					if(isReqCore && !UserUtil.isPermission(req, reqCoreName)){
						resp.sendError(403);
						return;
					}
					if(unLoginAllowRequest(req, path)){
						resp.sendRedirect(SysConsts.VIEW_INDEX);
						return;
					}
				}
			}
	      }
	      catch (Throwable ex) {
	        sendError(solrReq, request, (HttpServletResponse)response, ex );
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
	    chain.doFilter(request, response);
	}
	
	//即将弃用
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
