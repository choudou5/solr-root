package com.choudoufu.solr.util;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class AntUtil extends BaseLog{
	
	
	/**
	 * 重新 发布solr配置文件
	 * */
	public static String exeBuildFile(){
		String build = new File(AntUtil.class.getResource("/").getFile()).getAbsolutePath();
		build += "/build.xml";
		File buildFile = new File(build);
		Project p = new Project();
		//添加日志输出		
		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		//输出信息级别
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(consoleLogger);
		try {
			p.fireBuildStarted();
			p.init();
			ProjectHelper helper = ProjectHelper.getProjectHelper();
			helper.parse(p, buildFile);
			p.executeTarget(p.getDefaultTarget());
			p.fireBuildFinished(null);
		} catch (BuildException e) {
			logger.error(e);
			
		}
		return null;
	}
}
