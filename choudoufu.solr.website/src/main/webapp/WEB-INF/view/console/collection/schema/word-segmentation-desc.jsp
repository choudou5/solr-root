<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div id="wordSeg" class="widget-box hide">
  <div class="widget-title"><h4>分词效果示例</h4></div>
  <div class="widget-title">
    <ul class="nav nav-tabs nav-tabs-center">
      <li class="active"><a data-toggle="tab" href="#tab_base">不分词</a></li>
      <li><a data-toggle="tab" href="#tab_word_ik">IK分词</a></li>
      <li><a data-toggle="tab" href="#tab_word_city">城市提取</a></li>
      <li><a data-toggle="tab" href="#tab_word_split_dot">逗号分词</a></li>
      <li><a data-toggle="tab" href="#tab_word_pinyin">拼音分词</a></li>
      <li><a data-toggle="tab" href="#tab_word_min_split">最小细粒度分词</a></li>
      <li><a data-toggle="tab" href="#word_continuous_split">正向连续分词</a></li>
    </ul>
  </div>
  <div class="widget-content tab-content">
  	<div id="tab_base" class="tab-pane active">
      <p>适用场景：唯一标识，回复关键字</p>
      <p>text、string类型都是不分词的.</p>
    </div>
    <div id="tab_word_ik" class="tab-pane">
      <p>适用场景：标题，标签，内容</p>
      <p>例如：“2017年SolrHome正式对外商业化”</p>
    </div>
    <div id="tab_word_city" class="tab-pane">
      <p>城市分词</p>
    </div>
    <div id="tab_word_split_dot" class="tab-pane">
      <p>.号切割分词</p>
    </div>
    <div id="tab_word_pinyin" class="tab-pane">
      <p>拼音分词</p>
    </div>
    <div id="tab_word_min_split" class="tab-pane">
      <p>最小字分词</p>
    </div>
     <div id="word_continuous_split" class="tab-pane">
      <p>正向连续切割分词</p>
    </div>
  </div>
</div>