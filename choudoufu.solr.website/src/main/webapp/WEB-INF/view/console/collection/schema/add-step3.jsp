<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 高级配置结构 -->
<div id="form-wizard-3" class="step hide">
  <div class="widget-box">
    <div class="widget-title">
      <h5>字段组合</h5>
    </div>
    <div class="widget-content nopadding mg-10">
      
       <c:if test="${not empty isNew && !isNew }">
      	<p class="alert alert-info alert-block">
	       <input type="checkbox" id="reloadSolrConf" name="ext.isReLoad" value="true"/>&nbsp;<span class="help-inline-tip">更新solr配置</span>
       </p>
       </c:if>
       
       <p class="alert alert-info alert-block">
	       <input type="checkbox" name="ext.genModelClass" value="true"/>&nbsp;
	       <span class="help-inline-tip">
	       	生成model类（Java对象），
	       	&nbsp;生成地址：<span class="orange">${fns:getGenCodePath()}</span>
	       </span>
       </p>
    </div>
  </div>
</div>
