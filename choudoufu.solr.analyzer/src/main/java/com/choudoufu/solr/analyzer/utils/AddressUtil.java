package com.choudoufu.solr.analyzer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 描述：地址工具类
 * 构建组：solr.analyzer
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:May 4, 2016-11:11:06 AM
 * 版权：徐浩文 版权所有
 * </pre>
 */
public class AddressUtil {

	private static final String DIC_FILE_PROVINCE = "dict/address-province.dic";//省份
	private static final String DIC_FILE_CITY = "dict/address-city.dic";//城市
	private static final String ENCODING = "UTF-8";
	
	private static List<String> provinceList = new ArrayList<String>(34);
	private static List<String> cityList = new ArrayList<String>(702);
	
	static{
		initProvince();
		initCity();
	}
	
	/** 获得 省份 */
	public static String getProvince(String address) {
		String provice = ""; 
		for (String str : provinceList) {
			if(address.indexOf(str) != -1){
				provice = str;
				break;
			}
		}
		return provice;
	}
	
	/** 获得 城市 */
	public static String getCity(String address) {
		String city = ""; 
		for (String str : cityList) {
			if(address.indexOf(str) != -1){
				city = str;
				break;
			}
		}
		return city;
	}
	
	
	/** 初始化 省份列表 */
	private static void initProvince() {
		initAddress(DIC_FILE_PROVINCE, provinceList);
	}
	
	/** 初始化 城市列表 */
	private static void initCity() {
		initAddress(DIC_FILE_CITY, cityList);
	}
	
	/** 初始化 地址列表 */
	private static void initAddress(String filePath, List<String> result) {
		if(result != null && result.size() > 0)
			return;
		
		InputStreamReader read = null;
		try {
			read = new InputStreamReader(AddressUtil.class.getClassLoader().getResourceAsStream(filePath), ENCODING);
			BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
            	String[] array = lineTxt.split("\\|");
            	for (String city : array) {
            		result.add(city);
				}
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(read != null) read.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}