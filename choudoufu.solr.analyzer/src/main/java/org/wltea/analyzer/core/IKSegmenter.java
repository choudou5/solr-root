package org.wltea.analyzer.core;

import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public final class IKSegmenter
{
  private Reader input;
  private Configuration cfg;
  private AnalyzeContext context;
  private List<ISegmenter> segmenters;
  private IKArbitrator arbitrator;

  public IKSegmenter(Reader input, boolean useSmart)
  {
    this(input, useSmart, false);
  }

  public IKSegmenter(Reader input, boolean useSmart, boolean filterOneWords)
  {
    this.input = input;
    this.cfg = DefaultConfig.getInstance();
    this.cfg.setUseSmart(useSmart);
    this.cfg.setFilterOneWords(filterOneWords);
    init();
  }


  public IKSegmenter(Reader input, Configuration cfg)
  {
    this.input = input;
    this.cfg = cfg;
    init();
  }

  private void init()
  {
    Dictionary.initial(this.cfg);

    this.context = new AnalyzeContext(this.cfg);

    this.segmenters = loadSegmenters();

    this.arbitrator = new IKArbitrator();
  }

  private List<ISegmenter> loadSegmenters()
  {
    List segmenters = new ArrayList(4);

    segmenters.add(new LetterSegmenter());

    segmenters.add(new CN_QuantifierSegmenter());

    segmenters.add(new CJKSegmenter());
    return segmenters;
  }

  public synchronized Lexeme next()
    throws IOException
  {
    Lexeme l = null;
    while ((l = this.context.getNextLexeme()) == null)
    {
      int available = this.context.fillBuffer(this.input);
      if (available <= 0)
      {
        this.context.reset();
        return null;
      }

      this.context.initCursor();
      do
      {
        for (ISegmenter segmenter : this.segmenters) {
          segmenter.analyze(this.context);
        }
      }
      while ((!this.context.needRefillBuffer()) && (
        this.context.moveCursor()));

      for (ISegmenter segmenter : this.segmenters) {
        segmenter.reset();
      }

      this.arbitrator.process(this.context, this.cfg.useSmart());

      this.context.outputToResult();

      this.context.markBufferOffset();
    }
    return l;
  }

  public synchronized void reset(Reader input)
  {
    this.input = input;
    this.context.reset();
    for (ISegmenter segmenter : this.segmenters)
      segmenter.reset();
  }
}