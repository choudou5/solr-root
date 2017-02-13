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

	private final static String DEFAULT_SEPARTOR = ".";
	/**
	 * 分隔符
	 */
	private String separator;

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public SeparatorAnalyzer() {
		this(DEFAULT_SEPARTOR);
	}

	public SeparatorAnalyzer(String separator) {
		this.separator = separator;
	}

	protected Analyzer.TokenStreamComponents createComponents(String fieldName, Reader in) {
		Tokenizer token = new SeparatorTokenizer(in, separator);
		return new Analyzer.TokenStreamComponents(token);
	}
}
