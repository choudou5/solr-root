package com.choudoufu.solr.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import com.choudoufu.solr.analyzer.tokenizer.PositiveContinuousTokenizer;

/**
 * <pre>
 * 描述：正向连续 分词器
 * 构建组：solr.analyzer
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jul 15, 2015-3:18:44 PM
 * 版权：徐浩文 版权所有
 * </pre>
 */
public class PositiveContinuousSplitAnalyzer extends Analyzer {

	protected Analyzer.TokenStreamComponents createComponents(String fieldName,
			Reader in) {
		Tokenizer token = new PositiveContinuousTokenizer(in);
		return new Analyzer.TokenStreamComponents(token);
	}
}
