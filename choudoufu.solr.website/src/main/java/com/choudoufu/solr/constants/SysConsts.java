package com.choudoufu.solr.constants;

public interface SysConsts {
	
	String USER_SESSION = "user_session";
	
	/** 模块 */
	String MODULE_TABLE = "sys_table";
	String MODULE_TABLE_FIELD = "sys_table_field";
	String MODULE_USER = "sys_user";
	String MODULE_LOG_HI = "sys_log_history";
	String MODULE_PLUG_SENSITIVE_WORD = "plug_sensitive_word";
	
	String VIEW_INDEX = "/console/page?path=/index";	//首页
	String VIEW_LOGIN = "/console/page?path=/login";	//登录页
	
	/**
	 * 数据状态: YES/NO
	 */
	enum DataStatusEnum{
		YES("Y", "可用"),
		NO("N", "禁用");
		
		private String value;
		private String name;
		
		private DataStatusEnum(String value, String name){
			this.value = value;
			this.name = name;
		}

		public String getValue() {
			return value;
		}
		public String getName() {
			return name;
		}
	}
}
