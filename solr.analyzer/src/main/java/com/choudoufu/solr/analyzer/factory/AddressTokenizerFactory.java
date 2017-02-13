package com.choudoufu.solr.analyzer.factory;

import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import com.choudoufu.solr.analyzer.tokenizer.AddressTokenizer;

/**
 * <pre>
 * 描述：地址 分词器工厂 (支持 省市)
 * 构建组：solr.analyzer
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:May 4, 2016-10:23:25 AM
 * 版权：徐浩文 版权所有
 * </pre>
 */
public class AddressTokenizerFactory extends TokenizerFactory{  
	 
	 private final String code;//分词编码（province/city）省/市
	 
	 public AddressTokenizerFactory(Map<String, String> args) {  
		 super(args);  
		 this.code = get(args, "code", "city");  
	 }
	  
	 @Override  
	 public Tokenizer create(AttributeFactory factory, Reader in) {  
		 return new AddressTokenizer(in, this.code);  
	 }
}
