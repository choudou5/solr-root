package com.choudoufu.solr.common.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 签名工具
 * @author xuhaowen
 * @date 2017年2月13日
 */
public class SignUtil {

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 800000; i++) {
			decryptCode(getEncryptCode());
		}
		long end = System.currentTimeMillis();
		System.out.println("time:"+(end-begin));
	}
	
	private static final char[] SPECIAL_SYMBOLS = new char[]{'a','b','c','d','e','g','f'};
	
	/**
	 * 获得 加密 code
	 * @param code
	 * @return
	 */
	public static String getEncryptCode(){
		int rdLen = 10;
		String beginCode = RandomStringUtils.randomAlphanumeric(rdLen);
		
		String key = getCodeKey(beginCode);
		String endCode = RandomStringUtils.randomAlphabetic(rdLen);
		int endCodeLen = endCode.length();
		int keyLen = key.length();
		StringBuilder codeBuilder = new StringBuilder(endCodeLen+keyLen);
		if(isReverse(key))
			key = StringUtils.reverse(key);
		
		for (int i = 0; i < endCodeLen; i++) {
			char c = endCode.charAt(i);
			codeBuilder.append(c);
			int sm = c%SPECIAL_SYMBOLS.length;
			if(sm == 0)
				codeBuilder.append(SPECIAL_SYMBOLS[sm]);
			if(keyLen > i)
				codeBuilder.append(key.charAt(i));
		}
		return beginCode+","+codeBuilder.toString();
		
	}
	
	/**
	 * 解密 encryptCode
	 * @param encryptCode
	 * @return
	 */
	public static boolean decryptCode(String encryptCode){
		if(encryptCode == null)
			return false;
		String[] codes = encryptCode.split(",");
		if(codes.length != 2)
			return false;
		
		String key = getCodeKey(codes[0]);
		String sig = codes[1];
		
		StringBuilder numStr = new StringBuilder(key.length());
		for (int i = 0; i < sig.length(); i++) {
			char c = sig.charAt(i);
			if(c >= 48 && c <= 57)//0-9
				numStr.append(c);
		}
		String decrKey = numStr.toString();
		if(isReverse(key)){
			decrKey = numStr.reverse().toString();
		}
		return key.equals(decrKey);
	}

	private static boolean isReverse(String key){
		return (Integer.parseInt(key)%2)==0?true:false;
	}
	
	private static String getCodeKey(String code){
		if(StringUtils.isBlank(code))
			return "";
		int len = code.length();
		int sum = 0;
		
		for (int i = 0; i < len; i++) {
			char c = code.charAt(i);
			int sm = c%SPECIAL_SYMBOLS.length;
			sum += (int)c+((i+1)*len*(int)SPECIAL_SYMBOLS[sm]);
		}
		return sum+"";
	}
	
}