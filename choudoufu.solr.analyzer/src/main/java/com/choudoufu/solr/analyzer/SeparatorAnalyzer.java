package com.choudoufu.solr.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import com.choudoufu.solr.analyzer.tokenizer.SeparatorTokenizer;

/**
 * <pre>
 * 描述：分隔符 分析器 
 * 构建组：solr.analyzer 
 * 作者：xuhaowen 
 * 邮箱: xuhaowende@sina.cn 
 * 日期:Jul 15,
 * 2015-3:18:44 PM 版权：徐浩文 版权所有 
 * </pre>
 */
public class SeparatorAnalyzer extends Analyzer {

	public final static String DEFAULT_SEPARTOR = ".";
	public final static String DEFAULT_SQL_GROUP_SYMBOL = "";//SQL分组符， 针对DIH的sql导入
	
	/**
	 * 分隔符
	 */
	private String separator;
	
	/** 分组符， 针对DIH的sql导入 */
	private String sqlGroupSymbol;

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getSqlGroupSymbol() {
		return sqlGroupSymbol;
	}

	public void setSqlGroupSymbol(String sqlGroupSymbol) {
		this.sqlGroupSymbol = sqlGroupSymbol;
	}

	public SeparatorAnalyzer() {
		this(DEFAULT_SEPARTOR, DEFAULT_SQL_GROUP_SYMBOL);
	}

	public SeparatorAnalyzer(String separator) {
		this(separator, DEFAULT_SQL_GROUP_SYMBOL);
	}
	
	public SeparatorAnalyzer(String separator, String sqlGroupSymbol) {
		this.separator = separator;
		this.sqlGroupSymbol = sqlGroupSymbol;
	}

	protected Analyzer.TokenStreamComponents createComponents(String fieldName, Reader in) {
		Tokenizer token = new SeparatorTokenizer(in, separator, sqlGroupSymbol);
		return new Analyzer.TokenStreamComponents(token);
	}
}
