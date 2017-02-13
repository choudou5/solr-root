package org.apache.solr.common.params;

import java.util.Locale;

/**
 * 自定义参数
 * @author xuhaowen
 * @serial 2017-1-7
 */
public abstract class CustomParams {

	/** What action **/
	public final static String ACTION = "action";
	
	/** 请求地址：上传配置 **/
	public final static String REQ_PATH_UPLOAD_CONF = "/custom";
	
	public final static String CONF_TYPE_FILE = "file";
	public final static String CONF_TYPE_ZIP = "zip";
	
	public enum CustomAction {
		UPLOAD_CONF,
		DELETE_ZK_CONF_FILE,
		RELOAD_IK_DICT;
	    
	    public static CustomAction get(String p)
	    {
	      if( p != null ) {
	        try {
	          return CustomAction.valueOf(p.toUpperCase(Locale.ROOT) );
	        }
	        catch( Exception ex ) {}
	      }
	      return null; 
	    }
	  }
	
}
