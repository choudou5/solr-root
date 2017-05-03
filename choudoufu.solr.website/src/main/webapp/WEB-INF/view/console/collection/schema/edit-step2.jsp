<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
          <c:forEach items="${schemaFields }" var="schemaField" varStatus="status">
          	<c:set var="index" value="${status.index}" />
          	<tr id="tr_${index }">
	            <sys:fieldInputTpl fieldIndex="${index }" schemaField="${schemaField }" fieldName="${schemaField.name }" fieldLabel="${schemaField.label }" fieldType="${schemaField.type }" canDel="${index > 0 }"/>
	        </tr>
          </c:forEach>
        </tbody>
      </table>
      <select id="tplFieldType" class="hide">
          <c:forEach var="fieldType" items="${fns:getFieldTypeArray() }">
          <option type="${fieldType.type }" classz="${fieldType.classz }" value="${fieldType.value }">${fieldType.explain }</option>
          </c:forEach>
      </select>
    </div>
    <div class="add-row">
      	<a href="javascript:addFieldRow();">添加</a>
      </div>
  </div>
</div>
