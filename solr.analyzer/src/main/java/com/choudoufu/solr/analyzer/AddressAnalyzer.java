package com.choudoufu.solr.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import com.choudoufu.solr.analyzer.tokenizer.AddressTokenizer;

/**
 * <pre>
 * 描述：地址 分析器 (支持 省市)
 * 构建组：solr.analyzer
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:May 4, 2016-10:31:42 AM
 * 版权：徐浩文 版权所有
 * </pre>
 */
public class AddressAnalyzer extends Analyzer {

	private final static String DEFAULT_CODE = "city";
	/**
	 * 分词编码（province/city）省/市
	 */
	private String code;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public AddressAnalyzer() {
		this(DEFAULT_CODE);
	}

	/**
	 * @param code {province/city}
	 */
	public AddressAnalyzer(String code) {
		this.code = code;
	}

	protected Analyzer.TokenStreamComponents createComponents(String fieldName, Reader in) {
		Tokenizer token = new AddressTokenizer(in, code);
		return new Analyzer.TokenStreamComponents(token);
	}
}
