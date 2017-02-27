package com.choudoufu.solr.analyzer.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import com.choudoufu.solr.analyzer.SeparatorAnalyzer;

/**
 * <pre>
 * 描述：分隔符 分词器 
 * 构建组：solr.analyzer 
 * 作者：xuhaowen 
 * 邮箱: xuhaowende@sina.cn 
 * 日期:Jul 15,
 * 2015-3:18:00 PM 版权：
 * 徐浩文 版权所有
 *  </pre>
 */
public class SeparatorTokenizer extends Tokenizer {

	/**分隔符 */
	private String separator;
	/** 分组符， 针对DIH的sql导入 */
	private String sqlGroupSymbol;

	private int offset = 0;
	private int dataLen = 0;
	private String[] words = null;
	private int itemCount = 0;
	private int increment = 0; //增长值
	private final char[] ioBuffer = new char[512];
	private final CharTermAttribute termAtt = (CharTermAttribute) addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = (OffsetAttribute) addAttribute(OffsetAttribute.class);

	public SeparatorTokenizer(Reader in, String separator, String sqlGroupSymbol) {
		super(in);
		this.separator = separator == null ? SeparatorAnalyzer.DEFAULT_SEPARTOR : separator;
		this.sqlGroupSymbol = sqlGroupSymbol == null? SeparatorAnalyzer.DEFAULT_SQL_GROUP_SYMBOL: sqlGroupSymbol;
	}
	
	public boolean incrementToken() throws IOException {
		clearAttributes();

		if(this.dataLen == -1) {
			this.offset = 0;
			return false;
		}else if(offset > 0 && offset == itemCount){
			this.offset = 0;
			return false;
		}
		
		if(this.dataLen == 0){
			this.dataLen = this.input.read(this.ioBuffer);
			String data = getWord(ioBuffer, dataLen);
			//sql分组处理
			if(!"".equals(sqlGroupSymbol)){
				String[] sqlSplitArray = data.split("\\"+sqlGroupSymbol);
				Map<String, String> map = new HashMap<String, String>(sqlSplitArray.length*8);
				String[] itemArray = null;
				for (String item : sqlSplitArray) {
					itemArray = item.split("\\"+separator);
					for (String node : itemArray) {
						map.put(node, null);
					}
				}
				words = map.keySet().toArray(new String[map.size()]);
				//gc回收
				map = null;
				itemArray = null;
				sqlSplitArray = null;
			}else{
				words = data.split("\\"+separator);
			}
			itemCount = words.length;
		}
		if(offset < itemCount){
			String term = words[offset];
			int len = term.length();
			this.termAtt.append(term);
			this.offsetAtt.setOffset(correctOffset(increment), correctOffset(increment+len-1));
			increment += len;
		}
		offset++;
		return true;
	}

	public final void end() {
		int finalOffset = correctOffset(this.offset);
		this.offsetAtt.setOffset(finalOffset, finalOffset);
	}

	public void reset() throws IOException {
		super.reset();
		this.offset = (this.dataLen = 0);
		increment = 0;
	}

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

	private static String getWord(char[] chars, int dataLen){
		if(dataLen > 0){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < dataLen; i++) {
				sb.append(chars[i]);
			}
			return sb.toString();
		}
		return "";
	}
}
