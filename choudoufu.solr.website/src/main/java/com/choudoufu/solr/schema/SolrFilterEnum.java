package com.choudoufu.solr.schema;

import java.util.LinkedHashMap;

/**
 * 字段类型 枚举
 * @author xuhaowen
 * @date 2017年3月5日
 */
public enum SolrFilterEnum {

	HTML("html", "过滤html标记", "<charFilter class=\"solr.HTMLStripCharFilterFactory\" />"),
	LOWER_CASE("lowerCase", "取消大小写区分", "<filter class=\"solr.LowerCaseFilterFactory\"/>"),
	STOP("stop", "去掉通用词", "<filter class=\"solr.StopFilterFactory\" ignoreCase=\"true\" words=\"stopwords.txt\" />"),
	SYNONYM("synonym", "关于同义词的处理", "<filter class=\"solr.SynonymFilterFactory\" synonyms=\"synonyms.txt\" ignoreCase=\"true\" expand=\"true\"/>"),
	REMOVE_DUPLICATES_TOKEN("removeDuplicatesToken", "移除重复处理", "<filter class=\"solr.RemoveDuplicatesTokenFilterFactory\"/>"),
	TRIM("trim", "去掉Token两端的空白符", "<filter class=\"solr.TrimFilterFactory\" />");
	
	private String value;
	private String explain;
	private String tag;
	
	SolrFilterEnum(String value, String explain, String tag){
		this.value = value;
		this.explain = explain;
		this.tag = tag;
	}

	/**
	 * 获得 过滤器集合
	 * @return
	 */
	public static LinkedHashMap<String, String> getSolrFilterExplainMap(){
		SolrFilterEnum[] enums = SolrFilterEnum.values();
		LinkedHashMap<String, String> ftMap = new LinkedHashMap<String, String>(enums.length);
		for (SolrFilterEnum ftEnum : enums) {
			ftMap.put(ftEnum.getValue(), ftEnum.getExplain());
		}
		return ftMap;
	}
	
	/**
	 * 获得 过滤器tag集合
	 * @return
	 */
	public static LinkedHashMap<String, String> getSolrFilterTagMap(){
		SolrFilterEnum[] enums = SolrFilterEnum.values();
		LinkedHashMap<String, String> ftMap = new LinkedHashMap<String, String>(enums.length);
		for (SolrFilterEnum ftEnum : enums) {
			ftMap.put(ftEnum.getValue(), ftEnum.getTag());
		}
		return ftMap;
	}
	
	public static String getTagXml(String type){
		SolrFilterEnum[] enums = SolrFilterEnum.values();
		for (SolrFilterEnum ftEnum : enums) {
			if(ftEnum.getValue().equals(type))
				return ftEnum.getTag();
		}
		return "";
	}
	
	public String getValue() {
		return value;
	}

	public String getExplain() {
		return explain;
	}

	public String getTag() {
		return tag;
	}
}
