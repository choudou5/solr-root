package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.Reader;

public final class IKTokenizer extends Tokenizer
{
  private IKSegmenter _IKImplement;
  private final CharTermAttribute termAtt;
  private final OffsetAttribute offsetAtt;
  private final TypeAttribute typeAtt;
  private int endPosition;
  
  public IKTokenizer(Reader in, boolean useSmart)
  {
    this(in, useSmart, false);
  }

  public IKTokenizer(Reader in, boolean useSmart, boolean filterOneWords)
  {
    super(in);
    this.offsetAtt = ((OffsetAttribute)addAttribute(OffsetAttribute.class));
    this.termAtt = ((CharTermAttribute)addAttribute(CharTermAttribute.class));
    this.typeAtt = ((TypeAttribute)addAttribute(TypeAttribute.class));
    this._IKImplement = new IKSegmenter(this.input, useSmart, filterOneWords);
  }


  //扩展 factory
  public IKTokenizer(Reader in, AttributeFactory factory, boolean useSmart) {
    super(factory, in);
    this.offsetAtt = ((OffsetAttribute)addAttribute(OffsetAttribute.class));
    this.termAtt = ((CharTermAttribute)addAttribute(CharTermAttribute.class));
    this.typeAtt = ((TypeAttribute)addAttribute(TypeAttribute.class));
    this._IKImplement = new IKSegmenter(this.input, useSmart);
  }
  public IKTokenizer(Reader in, AttributeFactory factory, boolean useSmart, boolean filterOneWords) {
    super(factory, in);
    this.offsetAtt = ((OffsetAttribute)addAttribute(OffsetAttribute.class));
    this.termAtt = ((CharTermAttribute)addAttribute(CharTermAttribute.class));
    this.typeAtt = ((TypeAttribute)addAttribute(TypeAttribute.class));
    this._IKImplement = new IKSegmenter(this.input, useSmart);
  }

  
  public boolean incrementToken()
    throws IOException
  {
    clearAttributes();
    Lexeme nextLexeme = this._IKImplement.next();
    if (nextLexeme != null)
    {
      this.termAtt.append(nextLexeme.getLexemeText());

      this.termAtt.setLength(nextLexeme.getLength());

      this.offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());

      this.endPosition = nextLexeme.getEndPosition();

      this.typeAtt.setType(nextLexeme.getLexemeTypeString());

      return true;
    }

    return false;
  }

  public void reset()
    throws IOException
  {
    super.reset();
    this._IKImplement.reset(this.input);
  }

  public final void end()
  {
    int finalOffset = correctOffset(this.endPosition);
    this.offsetAtt.setOffset(finalOffset, finalOffset);
  }
}