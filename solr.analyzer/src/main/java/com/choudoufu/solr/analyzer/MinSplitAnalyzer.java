package com.choudoufu.solr.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import com.choudoufu.solr.analyzer.tokenizer.MinSplitTokenizer;

/**
 * <pre>
 * 描述：最小细粒度 分词器
 * 构建组：solr.analyzer
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jul 15, 2015-3:17:42 PM
 * 版权：徐浩文 版权所有
 * </pre>
 */
public class MinSplitAnalyzer extends Analyzer
{
  protected Analyzer.TokenStreamComponents createComponents(String fieldName, Reader in)
  {
    Tokenizer token = new MinSplitTokenizer(in);
    return new Analyzer.TokenStreamComponents(token);
  }
}