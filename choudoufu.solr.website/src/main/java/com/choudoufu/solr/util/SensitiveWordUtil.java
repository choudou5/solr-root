package com.choudoufu.solr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.core.SolrCore;

import com.choudoufu.solr.constants.CacheConsts;
import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.model.plug.SensitiveWord;

public class SensitiveWordUtil extends BaseLog{

	private static HashMap<String, String> sensitiveWordMap = null;//敏感词 集合
	
	public static final String CACHE_KEY_LIST = "sensitiveWord_list";
	
	public enum MatchType{
		MIN, //最小匹配规则
		MAX  //最大匹配规则
	}
	
	static{
		System.out.println("init words ......");
		initWordMap();//初始化 敏感词库
	}
	
	/**
	 * 获得 敏感词集合（缓存）
	 * @return
	 */
	public static List<SensitiveWord> getCacheWordList(){
		@SuppressWarnings("unchecked")
		List<SensitiveWord> wordList = (List<SensitiveWord>)EhcacheUtil.getInstance().get(CacheConsts.CACHE_SYS, CACHE_KEY_LIST);
		if (wordList == null){
			//查询数据
			SolrQuery query = SolrJUtil.getSolrQuery(SolrJUtil.QUERY_ALL);
			SolrCore core = SolrHelper.getCore(SysConsts.MODULE_PLUG_SENSITIVE_WORD);
			wordList = SolrJUtil.listModelData(query, core, SensitiveWord.class);
			
			if(CollectionUtils.isNotEmpty(wordList)){
				EhcacheUtil.getInstance().put(CacheConsts.CACHE_SYS, CACHE_KEY_LIST, wordList);
			}
		}
		return wordList;
	}
	
	/**
	 * 清除 敏感词集合（缓存）
	 * @return
	 */
	public static void removeCacheWordList(){
		EhcacheUtil.getInstance().remove(CacheConsts.CACHE_SYS, CACHE_KEY_LIST);
		if(sensitiveWordMap != null){
			sensitiveWordMap.clear();
			sensitiveWordMap = null;
		}
		initWordMap();
	}
	
	public static int getWordSize(){
		List<SensitiveWord> list = getCacheWordList();
		return CollectionUtils.isEmpty(list)?0:list.size();
	}
	
	/**
	 * 判断文字是否包含敏感字符
	 * @param txt  文字
	 * @param matchType  匹配规则
	 * @return 若包含返回true，否则返回false
	 */
	public boolean isContaintSensitiveWord(String txt, MatchType matchType){
		boolean flag = false;
		for(int i = 0 ; i < txt.length() ; i++){
			int matchFlag = checkSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
			if(matchFlag > 0){    //大于0存在，返回true
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 获取文字中的敏感词
	 * @param txt 文字
	 * @param matchType 匹配规则
	 * @return
	 */
	public static List<String> getSensitiveWord(String txt , MatchType matchType){
		List<String> sensitiveWordList = new ArrayList<String>();
		for(int i = 0 ; i < txt.length() ; i++){
			int length = checkSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
			if(length > 0){    //存在,加入list中
				sensitiveWordList.add(txt.substring(i, i+length));
				i = i + length - 1;    //减1的原因，是因为for会自增
			}
		}
		return sensitiveWordList;
	}
	
	/**
	 * 替换敏感字字符
	 * @param txt
	 * @param matchType
	 * @param replaceChar 替换字符，默认*
	 */
	public String replaceSensitiveWord(String txt, MatchType matchType,String replaceChar){
		if(replaceChar == null){
			replaceChar = "*";
		}
		String resultTxt = txt;
		List<String> wordList = getSensitiveWord(txt, matchType);     //获取所有的敏感词
		String replaceString = null;
		for (String word : wordList) {
			replaceString = getReplaceChars(replaceChar, word.length());
			resultTxt = resultTxt.replaceAll(word, replaceString);
		}
		return resultTxt;
	}
	
	/**
	 * 获取替换字符串
	 * @param replaceChar
	 * @param length
	 */
	private String getReplaceChars(String replaceChar,int length){
		String resultReplace = replaceChar;
		for(int i = 1 ; i < length ; i++){
			resultReplace += replaceChar;
		}
		return resultReplace;
	}
	
	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：<br>
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return，如果存在，则返回敏感词字符的长度，不存在返回0
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes"})
	public static int checkSensitiveWord(String txt,int beginIndex, MatchType matchType){
		boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
		int matchFlag = 0;     //匹配标识数默认为0
		char word = 0;
		Map nowMap = sensitiveWordMap;
		for(int i = beginIndex; i < txt.length() ; i++){
			word = txt.charAt(i);
			nowMap = (Map) nowMap.get(word);     //获取指定key
			if(nowMap != null){     //存在，则判断是否为最后一个
				matchFlag++;     //找到相应key，匹配标识+1 
				if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
					flag = true;       //结束标志位为true   
					if(MatchType.MIN == matchType){    //最小规则，直接返回,最大规则还需继续查找
						break;
					}
				}
			}
			else{     //不存在，直接返回
				break;
			}
		}
		if(matchFlag < 2 || !flag){        //长度必须大于等于1，为词 
			matchFlag = 0;
		}
		return matchFlag;
	}

	/**
	 * 初始化 敏感词库，将敏感词放入HashMap中，构建一个DFA算法模型：<br>
	 * 中 = {
	 *      isEnd = 0
	 *      国 = {<br>
	 *      	 isEnd = 1
	 *           人 = {isEnd = 0
	 *                民 = {isEnd = 1}
	 *                }
	 *           男  = {
	 *           	   isEnd = 0
	 *           		人 = {
	 *           			 isEnd = 1
	 *           			}
	 *           	}
	 *           }
	 *      }
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void initWordMap() {
		List<SensitiveWord> wordList = getCacheWordList();
		if(sensitiveWordMap == null && CollectionUtils.isNotEmpty(wordList)){
			sensitiveWordMap = new HashMap(wordList.size());
			Map nowMap = null;
			Map<String, String> newWorMap = null;
			String word = null;
			for (SensitiveWord obj : wordList) {
				word = obj.getWord();
				int wordLen = word.length();
				nowMap = sensitiveWordMap;
				for(int i = 0 ; i < wordLen ; i++){
					char keyChar = word.charAt(i);       //转换成char型
					Object wordMap = nowMap.get(keyChar);       //获取
					
					if(wordMap != null){        //如果存在该key，直接赋值
						nowMap = (Map) wordMap;
					}
					else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
						newWorMap = new HashMap<String,String>();
						newWorMap.put("isEnd", "0");     //不是最后一个
						nowMap.put(keyChar, newWorMap);
						nowMap = newWorMap;
					}
					
					if(i == wordLen - 1){
						nowMap.put("isEnd", "1");    //最后一个
					}
				}
			}
		}
	}

	/**
	 * 初始化 敏感词据 到索引库
	 */
	public static void initBaseDictData(){
		if(isInitBaseDictData()){
			readSensitiveWordFile();
		}
	}
	
	/**
	 * 是否 已初始化敏感词
	 * @return
	 */
	private static boolean isInitBaseDictData(){
		//查询数据
		SolrQuery query = SolrJUtil.getSolrQuery("word:*");
		query.setRows(1);
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_PLUG_SENSITIVE_WORD);
		SensitiveWord badWord = SolrJUtil.getModelData(query, core, SensitiveWord.class);
		if(badWord == null){
			readSensitiveWordFile();
			return true;
		}else{
			logger.debug("已经初始化 基础敏感词典");
			return false;
		}
	}
	
	
	/**
	 * 读取敏感词库中的内容，将内容添加到集合中
	 * @throws Exception 
	 */
	private static void readSensitiveWordFile(){
		logger.debug("初始化 基础敏感词典.....");
		InputStream is = null;
		InputStreamReader read = null;
		try {
			is = SensitiveWord.class.getResourceAsStream("/dict/sensitive-word.dic");
			read = new InputStreamReader(is, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			List<SensitiveWord> words = new ArrayList<SensitiveWord>(500);
			String word = null;
			int count = 0;
			SolrCore core = SolrHelper.getCore(SysConsts.MODULE_PLUG_SENSITIVE_WORD);
			while((word = bufferedReader.readLine()) != null){    //读取文件，将文件内容放入到set中
				words.add(new SensitiveWord(word));
				count++;
				if(count%500 == 0){
					SolrJUtil.addModelDatas(words, core);
					words.clear();
				}
		    }
		} catch (Exception e) {
			logger.error("读取 敏感词文件出错.", e);
		}finally{
			if(read !=null)try {read.close();} catch (IOException e) {e.printStackTrace();}  //关闭文件流
			if(is !=null)try {is.close();} catch (IOException e) {e.printStackTrace();}  //关闭输入流
		}
	}
}
