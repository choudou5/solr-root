package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.io.Reader;

public final class IKAnalyzer extends Analyzer
{
  private boolean useSmart;
  private boolean filterOneWords;

  public boolean useSmart()
  {
    return this.useSmart;
  }

  public void setUseSmart(boolean useSmart) {
    this.useSmart = useSmart;
  }

  public boolean filterOneWords() {
    return filterOneWords;
  }
  public void setFilterOneWords(boolean filterOneWords) {
    this.filterOneWords = filterOneWords;
  }

  public IKAnalyzer()
  {
    this(false);
  }

  public IKAnalyzer(boolean useSmart)
  {
    this.useSmart = useSmart;
  }

  public IKAnalyzer(boolean useSmart, boolean filterOneWords) {
    this.useSmart = useSmart;
    this.filterOneWords = filterOneWords;
  }

  protected Analyzer.TokenStreamComponents createComponents(String fieldName, Reader in)
  {
    Tokenizer _IKTokenizer = new IKTokenizer(in, useSmart(), filterOneWords());
    return new Analyzer.TokenStreamComponents(_IKTokenizer);
  }
}