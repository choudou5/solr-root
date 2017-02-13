package com.chenlb.mmseg4j.analysis.factory;

import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;

/**
 * 最多分词方式
 * @author xuhaowen
 */
public class MaxWordTokenizerFactory extends TokenizerFactory{  
	 
	 private Dictionary dic;
	 
	 public MaxWordTokenizerFactory(Map<String, String> args) {  
		 super(args);
		 dic = Dictionary.getInstance();
	 }
	 
	 private Seg newSeg() {
		return new MaxWordSeg(dic);
	 }
	 
	 @Override  
	 public Tokenizer create(AttributeFactory factory, Reader in) {  
		 return new MMSegTokenizer(newSeg(), in);  
	 }
}