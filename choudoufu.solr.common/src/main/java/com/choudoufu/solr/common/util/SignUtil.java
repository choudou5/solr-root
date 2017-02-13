package com.choudoufu.solr.common.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * 签名工具
 * @author xuhaowen
 * @date 2017年2月13日
 */
public class SignUtil {

	public static void main(String[] args) {
		//Random
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 50; i++) {
			String sKey = getSKey();
			decrypt(sKey, "1000");
		}
		long end = System.currentTimeMillis();
		System.out.println("time:"+(end-begin));
	}
	
	public static String getSKey(){
		return RandomStringUtils.randomAlphanumeric((RandomUtils.nextInt(5)+2));
	}
	
	public static String decrypt(String sKey, String sVal){
		int ascilSum = getAscilSum(sKey);
		System.out.println(sKey+" is ascilSum: "+ascilSum +" , %10 = "+(ascilSum%10));
		return null;
	}
	
	private static int getAscilSum(String sKey){
		if(StringUtils.isBlank(sKey))
			return -1;
		int len = sKey.length();
		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum += (int)sKey.charAt(i);
		}
		return sum;
	}
	
	private static String getKey(int keyType){
		String key = null;
		switch (keyType) {
		case 0:
			
			break;

		default:
			break;
		}
		
		return key;
	}
	
}
