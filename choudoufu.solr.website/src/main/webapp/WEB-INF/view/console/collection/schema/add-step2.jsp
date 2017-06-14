<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sys" tagdir="/WEB-INF/tags/sys" %>

<!-- 定义结构 -->
<div id="form-wizard-2" class="step hide">
  <div class="widget-box">
    <div class="widget-title">
      <span class="label label-none"><a href="javascript:void(0);">配置说明</a></span>
    </div>
    <div class="widget-content nopadding">
      <table id="fieldTable" class="table table-bordered table-striped">
        <thead>
          <%@include file="/WEB-INF/view/console/collection/schema/table-thead.jsp" %>
        </thead>
        <tbody>
          <!-- 默认字段 -->
          <c:forEach var="defField" items="${fns:getDefFieldArray()}" varStatus="status">
          	<tr id="tr_${status.index }">
	          	<sys:fieldInputTpl fieldIndex="${status.index }" fieldName="${defField.name }" fieldDefType="${defField.type }" fieldIndexed="true" fieldStored="true" fieldIsListShow="true" fieldLabel="${defField.label }"/>
	         </tr>
          </c:forEach>
        </tbody>
      </table>
      <select id="tplFieldType" class="hide">
          <c:forEach var="fieldType" items="${fns:getFieldTypeArray()}">
          <option type="${fieldType.type }" classz="${fieldType.classz }" value="${fieldType.value }">${fieldType.explain }</option>
          </c:forEach>
      </select>
    </div>
    <div class="add-row">
      	<a href="javascript:addFieldRow('top');">添加到第一行</a>&nbsp;&nbsp;
      	<a href="javascript:addFieldRow();">添加到尾行</a>
      </div>
  </div>
</div>
