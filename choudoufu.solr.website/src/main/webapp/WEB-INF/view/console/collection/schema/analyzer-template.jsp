<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div id="analyzerTemplate">
	<div class="widget-box">
	  <div class="widget-title">
	    <ul class="nav nav-tabs nav-tabs-center">
	      <li class="active"><a data-toggle="tab" href="#tab_index">&nbsp; 索引数据(index) &nbsp;</a></li>
	      <li><a data-toggle="tab" href="#tab_query">&nbsp; 查询数据(query) &nbsp;</a></li>
	    </ul>
	  </div>
	  <div class="widget-content tab-content">
	  	<div id="tab_index" class="tab-pane active">
	  		<%-- 扩展 参数 --%>
	  	    <div class="alert alert-info word_ik hide">
		    	<span>是否智能：</span>
		    	<input type="radio" name="index.useSmart" value="true" checked="checked"/>是否 &nbsp;&nbsp;
		    	<input type="radio" name="index.useSmart" value="false"/>否
		    </div>
		    <div class="alert alert-info word_address hide">
		    	<span>提取地区：</span>
		    	<input type="radio" name="index.code" value="city"/>城市 &nbsp;&nbsp;
		    	<input type="radio" name="index.code" value="province"/>省份
		    </div>
		    <div class="alert alert-info word_separator hide">
		    	<span>符号切割：</span>
		    	<input type="text" name="index.separator" value="" maxlength="3" placeholder="请输入 分割符" class="input-medium" />&nbsp;
		    	<input type="text" name="index.sqlGroupSymbol" value="" maxlength="3" placeholder="group_concat分割符" class="input-medium"/>
		    </div>
		    <%-- end 扩展 参数 --%>
	  	
	      <p class="orange">说明：存储原始数据到索引库时，针对数据进行处理.</p>
	      	<p><input type="checkbox" name="index.filter" value="html"/>HTML过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="lowerCase"/>小写转换过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="stop"/>停词过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="synonym"/>同义词过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="removeDuplicatesToken"/>RemoveDuplicatesToken过滤器</p>
		    <p><input type="checkbox" name="index.filter" value="trim"/>空格过滤器</p>
	    </div>
	    <div id="tab_query" class="tab-pane">
	    	<%-- 扩展 参数 --%>
	  	    <div class="alert alert-info word_ik hide">
		    	<span>是否智能：</span>
		    	<input type="radio" name="query.useSmart" value="true" checked="checked"/>是否 &nbsp;&nbsp;
		    	<input type="radio" name="query.useSmart" value="false"/>否
		    </div>
		    <div class="alert alert-info word_address hide">
		    	<span>提取地区：</span>
		    	<input type="radio" name="query.code" value="city"/>城市 &nbsp;&nbsp;
		    	<input type="radio" name="query.code" value="province"/>省份
		    </div>
		    <div class="alert alert-info word_separator hide">
		    	<span>符号切割：</span>
		    	<input type="text" name="squery.eparator" value="" maxlength="3" placeholder="请输入 分割符" class="input-medium" />&nbsp;
		    	<input type="text" name="query.sqlGroupSymbol" value="" maxlength="3" placeholder="group_concat分割符" class="input-medium"/>
		    </div>
		    <%-- end 扩展 参数 --%>
		    
	      <p class="orange">说明：查询时，语法分析器 对输入关键字进行处理.</p>
	      	<p><input type="checkbox" name="query.filter" value="html"/>HTML过滤器</p>
		    <p><input type="checkbox" name="query.filter" value="lowerCase"/>小写转换过滤器</p>
		    <p><input type="checkbox" name="query.filter" value="stop"/>停词过滤器</p>
		    <p><input type="checkbox" name="query.filter" value="synonym"/>同义词过滤器</p>
		    <p><input type="checkbox" name="query.filter" value="removeDuplicatesToken"/>RemoveDuplicatesToken过滤器</p>
		    <p><input type="checkbox" name="query.filter" value="trim"/>空格过滤器</p>
	    </div>
	  </div>
	</div>

</div> 