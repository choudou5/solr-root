package com.choudoufu.solr.schema;

public enum FieldBaseTypeEnum {

	TEXT("text", "solr.TextField"),
	STRING("string", "solr.StrField"),
	BOOLEAN("boolean", "solr.BoolField"),
	INT("int", "solr.TrieIntField"),
	LONG("long", "solr.TrieLongField"),
	FLOAT("float", "solr.TrieFloatField"),
	DOUBLE("double", "solr.TrieDoubleField"),
	DATE("date", "solr.TrieDateField");
	
	private String value;
	private String className;
	
	FieldBaseTypeEnum(String value, String className){
		this.value = value;
		this.className = className;
	}
	
//	<fieldType name="text" class="" sortMissingLast="true" />
//    <fieldType name="" class="" sortMissingLast="true" />
//    <fieldType name="" class="" sortMissingLast="true"/>
//    <fieldType name="" class="" precisionStep="0" positionIncrementGap="0"/>
//    <fieldType name="" class="" precisionStep="0" positionIncrementGap="0"/>
//    <fieldType name="" class="" precisionStep="0" positionIncrementGap="0"/>
//    <fieldType name="" class="" precisionStep="0" positionIncrementGap="0"/>
//    <fieldType name="" class="" precisionStep="0" positionIncrementGap="0"/>
}
