package com.choudoufu.solr.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

public class Test {

	public static void main(String[] args) throws Exception {
		String keyword = "成都建筑资质代办行业迎来高速增长期，柔洁护宝舒";
		
		// IK分词器
		print(keyword, new IKAnalyzer(true));
		
//		// mmseg4j分词器
//		print(keyword, new SimpleAnalyzer());
//
//		// 最小切割分词器
//		print("我是中国人", new MinSplitAnalyzer());
//
//		// 正向连续切割
//		print("中国北京天安门", new PositiveContinuousSplitAnalyzer());
//
//		// 符号分词器
//		print("123.456.78910,12312.8888", new SeparatorAnalyzer(".", ","));
//
//		// 地址分词器
//		print("广东省广州天河区车陂小学", new AddressAnalyzer("city"));
//
//		// 拼音分词器
//		print("中国北京", new PinyinAnalyzer());
	}
	
	private static void print(String keyword, Analyzer analyzer){
		try {
			TokenStream ts = analyzer.tokenStream("field", new StringReader(keyword));
			CharTermAttribute term = (CharTermAttribute) ts.addAttribute(CharTermAttribute.class);
			OffsetAttribute offset = (OffsetAttribute)ts.addAttribute(OffsetAttribute.class);
			TypeAttribute type = (TypeAttribute)ts.addAttribute(TypeAttribute.class);
			ts.reset();
			StringBuffer sb = new StringBuffer(62);
			System.out.println(analyzer.getClass().getSimpleName()+":【"+keyword+"】--分词结果：");
			while (ts.incrementToken()){
				System.out.println(offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | " + type.type());
				
				sb.append(term.toString() + " | ");
			}
			System.out.println("所有词组："+sb.toString());
			ts.end();
			ts.close();
			System.out.println("\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
