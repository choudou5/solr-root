package com.choudoufu.solr.schema;


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
