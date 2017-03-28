package com.choudoufu.solr.schema;

import org.apache.commons.lang.StringUtils;

import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.schema.entity.Analyzer;
import com.choudoufu.solr.schema.entity.FieldType;


/**
 * 字段类型 枚举
 * @author xuhaowen
 * @date 2017年3月5日
 */
public enum FieldTypeEnum {

	TEXT("analyzer", "text", "text", "solr.TextField"),
	STRING("analyzer", "string", "string", "solr.StrField"),
	BOOLEAN("base", "boolean", "boolean", "solr.BoolField"),
	INT("base", "int", "int", "solr.TrieIntField"),
	LONG("base", "long", "long", "solr.TrieLongField"),
	FLOAT("base", "float", "float", "solr.TrieFloatField"),
	DOUBLE("base", "double", "double", "solr.TrieDoubleField"),
	DATE("base", "date", "date", "solr.TrieDateField"),
	
	binary("base", "binary", "二进制", "solr.BinaryField"),
	random("base", "random", "随机数", "solr.RandomSortField"),
	
	WORD_IK("analyzer", "word_ik", "IK分词", "org.wltea.analyzer.lucene.IKTokenizerFactory"),
	WORD_CITY("analyzer", "word_address", "城市分词", "com.choudoufu.solr.analyzer.factory.AddressTokenizerFactory"),
	WORD_SPLIT_DOT("analyzer", "word_separator", "符号切割分词", "com.choudoufu.solr.analyzer.factory.SeparatorTokenizerFactory"),
	WORD_PINYIN("analyzer", "word_pinyin", "拼音分词", "com.choudoufu.solr.analyzer.factory.PinyinTokenizerFactory"),
	WORD_MIN_SPLIT("analyzer", "word_min_split", "最小字分词", "com.choudoufu.solr.analyzer.factory.MinSplitTokenizerFactory"),
	WORD_CONTINUOUS_SPLIT("analyzer", "word_continuous_split", "正向连续切割分词", "com.choudoufu.solr.analyzer.factory.ContinuousTokenizerFactory");
	
	private String type;
	private String value;
	private String explain;
	private String classz;
	
	FieldTypeEnum(String type, String value, String explain, String classz){
		this.type = type;
		this.value = value;
		this.explain = explain;
		this.classz = classz;
	}

	/**
	 * 获得 字段类型集合
	 * @return
	 */
	public static FieldTypeEnum[] getFieldTypeArray(){
		 return FieldTypeEnum.values();
	}
	
	/**
	 * 获得 字段分词类型Xml字符串
	 * @return
	 */
	public static String getFieldAnalyzerTypeXmlStr(FieldType fieldType){
		FieldTypeEnum[] enums = FieldTypeEnum.values();
		String typeName = fieldType.getName();
		for (FieldTypeEnum ftEnum : enums) {
			if(ftEnum.getValue().equals(typeName)){
				if("analyzer".equals(ftEnum.getType())){
					//分词器类型
					StringBuilder strBuilder = new StringBuilder(120);
					strBuilder.append("<fieldType name=\""+typeName+""+"\" class=\""+ftEnum.getClassz()+"\">");
					strBuilder.append("<analyzer type=\"index\">");
					boolean isTokenizer = StringUtils.isBlank(fieldType.getTokenizerClass())?false:true;
					strBuilder.append(getAnalyzerXml(isTokenizer, "index", fieldType.getIndex(), fieldType));
					strBuilder.append(getAnalyzerXml(isTokenizer, "query", fieldType.getQuery(), fieldType));
					strBuilder.append("</analyzer>");
	System.out.println("for analyzer len:"+strBuilder.length());
				}
			}
		}
		return "";
	}
	
	public static String getTypeName(String fieldName, String typeName){
		return isBaseType(fieldName, typeName)?typeName:(typeName+SysConsts.CHAR_UNDERLINE+fieldName);
	}
	
	private static String getAnalyzerXml(boolean isTokenizer, String analyzerType, Analyzer analyzer, FieldType fieldType){
		if(analyzer != null){
			StringBuilder strBuilder = new StringBuilder(50);
			strBuilder.append("<analyzer type=\""+analyzerType+"\">");
			
			//tokenizer
			if(isTokenizer){
				strBuilder.append("<tokenizer class=\""+getFieldTypeClass(fieldType)+"\" ");
				//地区
				if(StringUtils.isNotBlank(analyzer.getCode()))
					strBuilder.append(" code=\""+analyzer.getCode()+"\" ");
				//ik
				if(analyzer.getUseSmart() != null)
					strBuilder.append(" useSmart=\""+analyzer.getUseSmart()+"\" ");
				
				//自定义切割
				if(StringUtils.isNotBlank(analyzer.getSeparator()))
					strBuilder.append(" separator=\""+analyzer.getSeparator()+"\" ");
				if(StringUtils.isNotBlank(analyzer.getSqlGroupSymbol()))
					strBuilder.append(" sqlGroupSymbol=\""+analyzer.getSqlGroupSymbol()+"\" ");
				
				strBuilder.append("/>");
			}
			
			//过滤器
			String filterStr = analyzer.getFilters();
			if(StringUtils.isNotBlank(filterStr)){
				String[] filters = filterStr.split(SysConsts.CHAR_COMMA);
				for (String filter : filters) {
					strBuilder.append(SolrFilterEnum.getTagXml(filter));
				}
			}
			strBuilder.append("</analyzer>");
System.out.println("getAnalyzerXml len:"+strBuilder.length());
			return strBuilder.toString();
		}
		return "";
	}
	
	/**
	 * 获得 字段类型 Class
	 * @return
	 */
	public static String getFieldTypeClass(FieldType fieldType){
		FieldTypeEnum[] enums = FieldTypeEnum.values();
		String typeName = fieldType.getName();
		for (FieldTypeEnum ftEnum : enums) {
			if(ftEnum.getValue().equals(typeName)){
				return ftEnum.getClassz();
			}
		}
		return "";
	}
	
	
	private static boolean isBaseType(String fieldName, String typeName){
		FieldTypeEnum[] enums = FieldTypeEnum.values();
		for (FieldTypeEnum ftEnum : enums) {
			if(ftEnum.getValue().equals(typeName)){
				if("base".equals(ftEnum.getType())){
					return true;
				}
			}
		}
		return false;
	}
	
	public String getValue() {
		return value;
	}

	public String getExplain() {
		return explain;
	}

	public String getType() {
		return type;
	}

	public String getClassz() {
		return classz;
	}
}
