package com.choudoufu.solr.util;

import com.choudoufu.solr.common.util.PropertiesUtil;
import com.choudoufu.solr.constants.SysPropConsts;

public class SysUtil extends BaseLog{

	/**
	 * 获得 站点 名称
	 * @return
	 */
	public static String getSiteName(){
		String siteName = null;
		try {
			siteName = PropertiesUtil.getString(SysPropConsts.SITE_NAME);
		} catch (Exception e) {
			logger.error(SysPropConsts.SITE_NAME+" properties is undefined.", e);
		}
		return siteName==null?SysPropConsts.SITE_DEF_NAME:siteName;
	}
}
