package com.choudoufu.solr.util;

import java.net.URL;

import com.choudoufu.solr.constants.CacheConsts;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheUtil {

    private static final String CONF_PATH = "/ehcache.xml";
    
    private URL url;
  
    private CacheManager manager;  
  
    private static EhcacheUtil ehCache;  
  
    private EhcacheUtil(String path) {  
        url = getClass().getResource(path);  
        manager = CacheManager.create(url);  
    }  
  
    public static EhcacheUtil getInstance() {  
        if (ehCache== null) {  
            ehCache= new EhcacheUtil(CONF_PATH);  
        }  
        return ehCache;  
    }  
    
    public void put(String key, Object value) {  
        Cache cache = manager.getCache(CacheConsts.CACHE_SYS);
        Element element = new Element(key, value);  
        cache.put(element);
    }
    
    public void put(String cacheName, String key, Object value) {  
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);  
        cache.put(element);
    }
    
    public void putDisk(String cacheName, String key, Object value) {  
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);  
        cache.put(element);
        cache.flush();
    }
  
    public Object get(String cacheName, String key) {  
        Cache cache = manager.getCache(cacheName);  
        Element element = cache.get(key);  
        return element == null ? null : element.getObjectValue();  
    }
  
    public Object getCacheVal(String key) {  
        Cache cache = manager.getCache(CacheConsts.CACHE_SYS);  
        Element element = cache.get(key);  
        return element == null ? null : element.getObjectValue();  
    }
    
    public Object getCacheStrVal(String key) {  
        Cache cache = manager.getCache(CacheConsts.CACHE_SYS);  
        Element element = cache.get(key);  
        return element == null ? null : element.getObjectValue().toString();  
    }
    
    public Cache get(String cacheName) {  
        return manager.getCache(cacheName);  
    } 
    
    public int getCacheSize(String cacheName) {  
        return manager.getCache(cacheName).getSize();  
    }
    
    public void remove(String cacheName, String key) {  
        Cache cache = manager.getCache(cacheName);  
        cache.remove(key);  
    }
    
    public void remove(String key) {  
        Cache cache = manager.getCache(CacheConsts.CACHE_SYS);  
        cache.remove(key);  
    }
    
    public void shutdown(){
    	manager.shutdown();
    }
  
}
