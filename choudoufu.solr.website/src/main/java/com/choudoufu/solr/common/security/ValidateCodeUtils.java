package com.choudoufu.solr.common.security;

import java.util.HashMap;
import java.util.Map;

import com.choudoufu.solr.constants.CacheConsts;
import com.choudoufu.solr.util.EhcacheUtil;

/**
 * 验证码 工具类
 * @author xuhaowen
 * @date 2017年3月8日
 */
public class ValidateCodeUtils {

	public static final String VALIDATE_CODE = "validateCode";
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)EhcacheUtil.getInstance().get(CacheConsts.CACHE_SYS, "loginFailMap");
		if (loginFailMap==null){
			loginFailMap = new HashMap<String, Integer>(5);
			EhcacheUtil.getInstance().put(CacheConsts.CACHE_SYS, "loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	
}
