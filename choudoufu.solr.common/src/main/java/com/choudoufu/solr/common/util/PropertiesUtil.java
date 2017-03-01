package com.choudoufu.solr.common.util;

import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {

	private static Properties props;
	
	private PropertiesUtil() {}
	
	public static void init(String filePath){
		try {
            props = new Properties();
            InputStreamReader inputStream = new InputStreamReader(
            		PropertiesUtil.class.getResourceAsStream(filePath), "UTF-8");
            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    /** 
     * 通过key值获取文件的String类型数据 
     * @param key 
     * @return 
     */  
    public static String getString(String key){  
        return props.getProperty(key);  
    }
    
    /** 
     * 通过key值获取文件的int类型数据 
     * @param key 
     * @return 
     */  
    public static Integer getInteger(String key){  
        return Integer.parseInt(props.getProperty(key));  
    }
    
    /** 
     * 通过key值获取文件的double类型数据 
     * @param key 
     * @return 
     */  
    public static Double getDouble(String key){  
        return Double.parseDouble(props.getProperty(key));  
    }
    
    /** 
     * 通过key值获取文件的boolean类型数据 
     * @param key 
     * @return 
     */  
    public static Boolean getBoolean(String key){  
        return Boolean.parseBoolean(props.getProperty(key));  
    }  

    /**
     * 得到所有的配置信息
     * 
     * @return
     *//*
    public Map<?, ?> getAll() {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<?> enu = props.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = props.getProperty(key);
            map.put(key, value);
        }
        return map;
    }*/
    
}
