package com.choudoufu.solr.analyzer.factory;

import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import com.choudoufu.solr.analyzer.tokenizer.PinyinTokenizer;

/**
 * <pre>
 * 描述：拼音分词器 工厂
 * 构建组：solr.analyzer
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:May 3, 2016-5:14:51 PM
 * 版权：徐浩文 版权所有
 * </pre>
 */
public class PinyinTokenizerFactory extends TokenizerFactory{  
	
	 public PinyinTokenizerFactory(Map<String, String> args) {  
		 super(args);  
	 }  
	  
	 @Override  
	 public Tokenizer create(AttributeFactory factory, Reader in) {  
		 return new PinyinTokenizer(in);  
	 }
}