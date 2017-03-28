package com.choudoufu.solr.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.schema.FieldTypeEnum;
import com.choudoufu.solr.schema.entity.Schema;
import com.choudoufu.solr.schema.entity.SolrConfig;
import com.choudoufu.solr.schema.entity.SolrField;


/**
 * 生产文件 工具类
 * @author xuhaowen
 * @date 2017年3月13日
 */
public class GenFileUtil extends BaseLog{
	
	/**
	 * 生成 schema配置文件
	 * */
	public static void genSchema(String savePath, SolrConfig solrConfig, Schema schema){
		String schemaName = schema.getName();
		String schemaXmlStr = getSchemaXmlStr(schema);
		File schemaFile = new File(savePath+"/"+schemaName+"/schema.xml");
		try {
			FileUtils.write(schemaFile, schemaXmlStr, SysConsts.ENCODING);
		} catch (IOException e) {
			logger.error("生成配置 schema配置文件出错！", e);
		}
		
		String coreConf = getSolrConfigStr(schemaName, solrConfig);
		File coreFile = new File(savePath+"/"+schemaName+"/core.properties");
		try {
			FileUtils.write(coreFile, coreConf, SysConsts.ENCODING);
		} catch (IOException e) {
			logger.error("生成配置 core.properties出错！", e);
		}
	}
	
	private static String getSolrConfigStr(String name, SolrConfig solrConfig){
		StringBuilder coreBuilder = new StringBuilder(50);
		coreBuilder.append("name="+name+SysConsts.CHAR_NEW_LINE);
		coreBuilder.append("name="+solrConfig.getVersion()+SysConsts.CHAR_NEW_LINE);
		coreBuilder.append("name="+solrConfig.getDataDir()+SysConsts.CHAR_NEW_LINE);
		coreBuilder.append("name="+solrConfig.getUpdateLogDir()+SysConsts.CHAR_NEW_LINE);
		coreBuilder.append("name="+solrConfig.getAutoCommitMaxTime()+SysConsts.CHAR_NEW_LINE);
		coreBuilder.append("name="+solrConfig.getAutoSoftCommitMaxTime()+SysConsts.CHAR_NEW_LINE);
System.out.println("getSolrConfigStr len:"+coreBuilder.length());
		return coreBuilder.toString();
	}
	
	private static String getSchemaXmlStr(Schema schema){
		SolrField[] fields = schema.getFields();
		
		StringBuilder strBuilder = new StringBuilder(350);
		
		strBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"\" >");
		strBuilder.append("<schema name=\""+schema.getName()+"\" title="+schema.getTitle()+" version=\"1.5\">");
		
		//字段（必要）
		strBuilder.append("<field name=\"_version_\" type=\"long\" indexed=\"true\" stored=\"true\"/>");
		strBuilder.append("<field name=\"_root_\" type=\"string\" indexed=\"true\" stored=\"false\"/>");
		//字段（自定义）
		
		for (SolrField field : fields) {
			strBuilder.append("<field name=\""+field.getName()+"\" type=\""+FieldTypeEnum.getTypeName(field.getName(), field.getType().getName())
					+"\" indexed=\""+field.getIndexed()+"\" stored=\""+field.getStored()
					+"\" required=\""+field.getRequired()+"\" multiValued=\""+field.getMultiValued()+"\"/>");
		}
		
		strBuilder.append("");
		strBuilder.append("");
		
		//uniqueKey（主键）
		String uniqueKey = fields[schema.getPrimaryNo()].getName();
		strBuilder.append("<uniqueKey>"+uniqueKey+"</uniqueKey>");
		
		//<defaultSearchField>text</defaultSearchField>
		
		//字段类型（基本）
		strBuilder.append("<fieldType name=\"string\" class=\"solr.StrField\" sortMissingLast=\"true\" />");
		strBuilder.append("<fieldType name=\"boolean\" class=\"solr.BoolField\" sortMissingLast=\"true\"/>");
		strBuilder.append("<fieldType name=\"int\" class=\"solr.TrieIntField\" precisionStep=\"0\" positionIncrementGap=\"0\"/>");
		strBuilder.append("<fieldType name=\"float\" class=\"solr.TrieFloatField\" precisionStep=\"0\" positionIncrementGap=\"0\"/>");
		strBuilder.append("<fieldType name=\"long\" class=\"solr.TrieLongField\" precisionStep=\"0\" positionIncrementGap=\"0\"/>");
		strBuilder.append("<fieldType name=\"double\" class=\"solr.TrieDoubleField\" precisionStep=\"0\" positionIncrementGap=\"0\"/>");
		strBuilder.append("<fieldType name=\"date\" class=\"solr.TrieDateField\" precisionStep=\"0\" positionIncrementGap=\"0\"/>");
		//字段类型（分词数据）
		for (SolrField field : fields) {
			strBuilder.append(FieldTypeEnum.getFieldAnalyzerTypeXmlStr(field.getType())+SysConsts.CHAR_NEW_LINE);
		}
		strBuilder.append("</schema>");
System.out.println("getSchemaXmlStr len:"+strBuilder.length());
		return strBuilder.toString();
	}
}
