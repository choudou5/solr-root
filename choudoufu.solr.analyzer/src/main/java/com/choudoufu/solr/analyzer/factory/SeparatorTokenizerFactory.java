package com.choudoufu.solr.analyzer.factory;

import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import com.choudoufu.solr.analyzer.tokenizer.SeparatorTokenizer;

/**
 * <pre>
 * 描述：分隔符 分词器工厂
 * 构建组：solr.analyzer 
 * 作者：xuhaowen 
 * 邮箱: xuhaowende@sina.cn 
 * 日期:Jul 15,
 * 2015-3:18:00 PM 版权：
 * 徐浩文 版权所有
 *  </pre>
 */
public class SeparatorTokenizerFactory extends TokenizerFactory{  
	 
	 private final String separator;
	 private final String sqlGroupSymbol;
	 
	 public SeparatorTokenizerFactory(Map<String, String> args) {  
		 super(args);  
		 this.separator = get(args, "separator", ".");  
		 this.sqlGroupSymbol = get(args, "sqlGroupSymbol", "");
	 }  
	  
	 @Override  
	 public Tokenizer create(AttributeFactory factory, Reader in) {  
		 return new SeparatorTokenizer(in, this.separator, this.sqlGroupSymbol);  
	 }
}