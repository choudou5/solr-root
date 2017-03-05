package com.choudoufu.solr.schema;

import java.util.LinkedHashMap;

/**
 * 字段类型 枚举
 * @author xuhaowen
 * @date 2017年3月5日
 */
public enum FieldTypeEnum {

	TEXT("text", "text"),
	STRING("string", "string"),
	BOOLEAN("boolean", "boolean"),
	INT("int", "int"),
	LONG("long", "long"),
	FLOAT("float", "float"),
	DOUBLE("double", "double"),
	DATE("date", "date"),
	
	binary("binary", "二进制"),
	random("random", "随机数"),
	
	WORD_IK("word_ik", "IK分词"),
	WORD_CITY("word_city", "城市分词"),
	WORD_SPLIT_DOT("word_split_dot", ".号切割分词"),
	WORD_PINYIN("word_pinyin", "拼音分词"),
	WORD_MIN_SPLIT("word_min_split", "最小字分词"),
	WORD_CONTINUOUS_SPLIT("word_continuous_split", "正向连续切割分词");
	
	private String value;
	private String explain;
	
	FieldTypeEnum(String value, String explain){
		this.value = value;
		this.explain = explain;
	}

	/**
	 * 获得 字段类型集合
	 * @return
	 */
	public static LinkedHashMap<String, String> getFieldTypeMap(){
		FieldTypeEnum[] enums = FieldTypeEnum.values();
		LinkedHashMap<String, String> ftMap = new LinkedHashMap<String, String>(enums.length);
		for (FieldTypeEnum ftEnum : enums) {
			ftMap.put(ftEnum.getValue(), ftEnum.getExplain());
		}
		return ftMap;
	}
	
	public String getValue() {
		return value;
	}

	public String getExplain() {
		return explain;
	}

}
