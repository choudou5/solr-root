package org.wltea.analyzer.cfg;

import java.util.List;

public abstract interface Configuration
{
  public abstract boolean useSmart();

  public abstract void setUseSmart(boolean paramBoolean);

  public abstract String getMainDictionary();

  public abstract String getQuantifierDicionary();

  public abstract List<String> getExtDictionarys();

  public abstract List<String> getExtStopWordDictionarys();

  public abstract boolean filterOneWords();

  public abstract void setFilterOneWords(boolean paramBoolean);
}