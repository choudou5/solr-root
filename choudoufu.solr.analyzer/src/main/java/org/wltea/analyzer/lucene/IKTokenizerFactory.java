package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.io.Reader;
import java.util.Map;

public class IKTokenizerFactory extends TokenizerFactory
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


  public IKTokenizerFactory(Map<String, String> args) {
    super(args);
    String useSmartArg = (String)args.get("useSmart");
    setUseSmart(useSmartArg != null ? Boolean.parseBoolean(useSmartArg) : false);
    String filterOneWordsArg = (String)args.get("filterOneWords");
    setFilterOneWords(filterOneWordsArg != null ? Boolean.parseBoolean(filterOneWordsArg) : false);
  }
  
  public Tokenizer create(AttributeFactory factory, Reader in)
  {
    Tokenizer _IKTokenizer = new IKTokenizer(in, factory, this.useSmart, this.filterOneWords);
    return _IKTokenizer;
  }
}