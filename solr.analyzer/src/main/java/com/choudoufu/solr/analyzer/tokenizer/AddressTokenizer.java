package com.choudoufu.solr.analyzer.tokenizer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import com.choudoufu.solr.analyzer.utils.AddressUtil;

/**
 * <pre>
 * 描述：地址 分词器 (支持 省市)
 * 构建组：solr.analyzer
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:May 4, 2016-10:30:56 AM
 * 版权：徐浩文 版权所有
 * </pre>
 */
public class AddressTokenizer extends Tokenizer {

	private final static String DEFAULT_CODE = "city";

	/** 分词编码 （province/city）省/市*/
	private String code;

	private int offset = 0;
	private int dataLen = 0;
	private String word;
	private final char[] ioBuffer = new char[256];
	private final CharTermAttribute termAtt = (CharTermAttribute) addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = (OffsetAttribute) addAttribute(OffsetAttribute.class);

	public AddressTokenizer(Reader in, String code) {
		super(in);
		this.code = code == null ? DEFAULT_CODE : code;
	}
	
	public boolean incrementToken() throws IOException {
		clearAttributes();

		if (this.dataLen == 0) {
			this.dataLen = this.input.read(this.ioBuffer);
			String address = String.valueOf(ioBuffer).trim();
			if("city".equals(code)){
				word = AddressUtil.getCity(address);
			}else if("province".equals(code)){
				word = AddressUtil.getProvince(address);
			}else{
				word = "";
			}
			offset = address.indexOf(word);
		}else{
			dataLen = -1;
		}
		
		int len = word.length();
		if(this.dataLen == -1) {
			return false;
		}else if(len == 0){
			return false;
		}
		this.termAtt.append(word);
		this.offsetAtt.setOffset(correctOffset(offset), correctOffset(offset+len-1));
		return true;
	}

	public final void end() {
		this.offsetAtt.setOffset(0, 0);
	}
	
	public void reset() throws IOException {
		super.reset();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}