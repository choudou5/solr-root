package com.choudoufu.solr.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.core.SolrCore;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.choudoufu.solr.constants.ActionTypeEnum;
import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.entity.User;
import com.choudoufu.solr.model.SysLogHistory;

public class SysLogUtil {

	private static ThreadPoolTaskExecutor taskExecutor = null;
	private static Queue<SysLogHistory> logQueue = new ArrayBlockingQueue<SysLogHistory>(200);
	private static volatile long lastAccessTime = System.currentTimeMillis();
	

	static{
		taskExecutor = SpringContextHolder.getBean("taskExecutor");
	}
	   
	/**
	 * 保存日志
	 * @param request
	 * @param title 标题
	 * @param accessType 访问类型
	 */
	public static void saveLog(HttpServletRequest request, String title, String accessType){
		saveLog(request, title, accessType, null);
	}
	
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, String title, String accessType, Exception ex){
		User user = UserUtil.getSessionUser(request);
		if (user != null){
			SysLogHistory log = new SysLogHistory();
			log.setId(IdGrowthUtil.getIncrIdStr(SysConsts.MODULE_LOG_HI));
			log.setLoginName(user.getLoginName());
			log.setTitle(title);
			log.setAccessType(accessType);
			log.setAction(ActionTypeEnum.VIEW.getValue());
			log.setUrl(request.getRequestURI());
			log.setMethod(request.getMethod());
			log.setParams(ToStringBuilder.reflectionToString(request.getParameterMap()));
			log.setIp(IpUtil.getIpAddr(request));
			log.setUserAgent(request.getHeader("user-agent"));
			log.setCreateTime(new Date());
			// 如果有异常，设置异常信息
			log.setException(ExceptionUtil.getStackTraceAsString(ex));
			
			//日志 保存到队列（缓和--延迟插入）
			logQueue.add(log);
			
			long now = System.currentTimeMillis();
			if((now-lastAccessTime) > 5000){//间隔5秒 批量保存一次
				lastAccessTime = now;
//				异步保存日志
				taskExecutor.execute(new SaveLogThread());
			}
		}
	}

	
	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread implements Runnable{
		
		@Override
		public void run() {
			List<SysLogHistory> entitys = new ArrayList<SysLogHistory>(10);
			for (int i = 0; i < 10; i++) {
				SysLogHistory log = logQueue.poll();
				if(log == null)
					break;
				entitys.add(log);
			}
			//批量保存日志
			if(CollectionUtils.isNotEmpty(entitys)){
				//保存记录
				SolrCore core = SolrHelper.getCore(SysConsts.MODULE_LOG_HI);
				SolrJUtil.addModelDatas(entitys, core);
			}
		}
	}

}

