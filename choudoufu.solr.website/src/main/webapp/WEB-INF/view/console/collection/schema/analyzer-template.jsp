<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div id="analyzerTemplate" class="modal-body">
	<!-- 
	private boolean useSmart = false;
	private String code;
	private String separator;
	private String sqlGroupSymbol;
	-->
	<div class="widget-box">
	  <div class="widget-title">
	    <ul class="nav nav-tabs nav-tabs-center">
	      <li class="active"><a data-toggle="tab" href="#tab_index">索引(index)</a></li>
	      <li><a data-toggle="tab" href="#tab_query">查询(query)</a></li>
	    </ul>
	  </div>
	  <div class="widget-content tab-content">
	  	<div id="tab_index" class="tab-pane active">
	      <p>说明：存储原始数据到索引库时，针对数据进行处理.</p>
	      	<p><input type="checkbox" name="index.filter" value="html"/>HTML过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="lowerCase"/>小写转换过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="stop"/>停词过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="synonym"/>同义词过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="removeDuplicatesToken"/>RemoveDuplicatesToken过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="trim"/>空格过滤器</p>
	    </div>
	    <div id="tab_query" class="tab-pane">
	      <p>说明：查询时，语法分析器 对输入关键字进行处理.</p>
	      	<p><input type="checkbox" name="index.filter" value="html"/>HTML过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="lowerCase"/>小写转换过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="stop"/>停词过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="synonym"/>同义词过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="removeDuplicatesToken"/>RemoveDuplicatesToken过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="trim"/>空格过滤器</p>
	    </div>
	  </div>
	</div>

</div> 