package com.choudoufu.solr.analyzer.tokenizer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

/**
 * <pre>
 * 描述：最小分割 分词器
 * 构建组：solr.analyzer
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:May 4, 2016-10:30:56 AM
 * 版权：徐浩文 版权所有
 * </pre>
 */
public class MinSplitTokenizer extends Tokenizer {

	private int offset = 0;
	private int bufferIndex = 0;
	private int dataLen = 0;
	private final char[] ioBuffer = new char[1024];
	private int indexLength = 0;
	private final CharTermAttribute termAtt = (CharTermAttribute) addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = (OffsetAttribute) addAttribute(OffsetAttribute.class);

	public MinSplitTokenizer(Reader in) {
		super(in);
	}

	public boolean incrementToken() throws IOException {
		clearAttributes();

		this.offset += 1;

		if (this.bufferIndex >= this.dataLen) {
			this.dataLen = this.input.read(this.ioBuffer);
			this.bufferIndex = 0;
		}

		if (this.dataLen == -1) {
			this.offset = 0;
			this.indexLength = 0;
			return false;
		}
		this.bufferIndex += 1;

		this.indexLength += 1;
		this.termAtt.copyBuffer(this.ioBuffer, this.indexLength - 1, 1);
		this.offsetAtt.setOffset(correctOffset(this.indexLength - 1), correctOffset(this.indexLength));
		return true;
	}

	public final void end() {
		int finalOffset = correctOffset(this.offset);
		this.offsetAtt.setOffset(finalOffset, finalOffset);
	}

	public void reset() throws IOException {
		super.reset();
		this.offset = (this.bufferIndex = this.dataLen = 0);
	}
}