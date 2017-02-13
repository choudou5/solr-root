package com.choudoufu.solr.analyzer.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

/**
 * <pre>
 * 描述：分隔符 分词器 
 * 构建组：solr.analyzer 
 * 作者：xuhaowen 
 * 邮箱: xuhaowende@sina.cn 
 * 日期:Jul 15, 2015-3:18:00 PM 
 * 版权： 徐浩文 版权所有
 * </pre>
 */
public class PinyinTokenizer extends Tokenizer {

	private int dataLen = 0;
	private int dex = 0;
	private int increment = 0; //增长值
	private int wordCount = 0;
	private List<String> words;

	private final char[] ioBuffer = new char[512];
	private final CharTermAttribute termAtt = (CharTermAttribute) addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = (OffsetAttribute) addAttribute(OffsetAttribute.class);

	public PinyinTokenizer(Reader in) {
		super(in);
	}

	public boolean incrementToken() throws IOException {
		clearAttributes();
		if (this.dataLen == 0) {
			this.dataLen = this.input.read(this.ioBuffer);
			words = chineseToPinyin(ioBuffer);
			wordCount = words.size();
		}

		if (this.dataLen == -1) {
			return false;
		} else if (dex == wordCount) {
			return false;
		}
		String word = words.get(dex);
		int len = word.length();
		this.termAtt.append(word);
		this.offsetAtt.setOffset(correctOffset(increment), correctOffset(increment+len-1));
		
		increment += len;
		dex++;
		return true;
	}

	public void reset() throws IOException {
		super.reset();
		this.offsetAtt.setOffset(0, 0);
	}


	/**
	 * 中文 转 拼音
	 * @param str
	 */
	private static List<String> chineseToPinyin(char[] chars) {
		List<String> pinyins = new ArrayList<String>();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		StringBuffer simplePin = new StringBuffer(10);// 简拼
		StringBuffer fullPin = new StringBuffer(chars.length*3);//全拼
		for (int i = 0; i < chars.length; i++) {
			if (isChinese(chars[i])) {// 中文
				try {
					String[] strs = PinyinHelper.toHanyuPinyinStringArray(chars[i], defaultFormat);
					if (strs != null) {
						pinyins.add(strs[0]);
						simplePin.append(strs[0].charAt(0));
						fullPin.append(strs[0]);
						// 多音字
						if (strs.length > 1 && !strs[0].equals(strs[1])) {
							pinyins.add(strs[1]);
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		if (simplePin.length() > 0) {
			pinyins.add(fullPin.toString());
			pinyins.add(simplePin.toString());
		}
		simplePin = null;
		fullPin = null;
		return pinyins;
	}

	public static boolean isChinese(char c) {
		return (c >= 19968) && (c <= 171941);
	}

}
