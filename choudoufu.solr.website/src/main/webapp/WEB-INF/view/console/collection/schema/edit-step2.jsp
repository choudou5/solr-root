<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 定义结构 -->
<div id="form-wizard-2" class="step hide">
  <div class="widget-box">
    <div class="widget-title">
      <span class="label label-none"><a href="javascript:void(0);">配置说明</a></span>
    </div>
    <div class="widget-content nopadding">
      <c:set var="fieldTypes" value="${fns:getFieldTypeArray()}" />
      <table id="fieldTable" class="table table-bordered table-striped">
        <thead>
          <%@include file="/WEB-INF/view/console/collection/schema/table-thead.jsp" %>
        </thead>
        <tbody>
          <c:forEach items="${schemaFields }" var="schemaField" varStatus="status">
          	<tr id="tr_${status.index }">
	            <td>${status.index+1 }</td>
	            <td>
	              <input class="required isVariable" type="text" attrname="fields.name" name="fields[${status.index }].name" value="${schemaField.name }" placeholder="字母 或 下划线 组成" maxlength="10" /></td>
	            <td>
	              <input class="span10" type="text" attrname="fields.label" name="fields[${status.index }].label" value="${schemaField.label }" placeholder="简单描述" maxlength="10" /></td>
	            <td>
	              <select id="selectType" attrname="fields.type.name" name="fields[${status.index }].type.name"  onchange="changeSelectType(${status.index })">
	                  <c:forEach var="fieldType" items="${fieldTypes }">
	                  	<option type="${fieldType.type }" classz="${fieldType.classz }" value="${fieldType.value }" <c:if test="${schemaField.type.name eq fieldType.value}">selected="selected"</c:if>>${fieldType.explain }</option>
	                  </c:forEach>
	              </select>
	            </td>
	            <td>
	              <div id="analyzerDiv">
	                <%@include file="/WEB-INF/view/console/collection/schema/analyzerDefHidden.jsp" %>
	              </div>
	            </td>
	            <td><input type="checkbox" attrname="fields.indexed" name="fields[${status.index }].indexed" <c:if test="${schemaField.indexed }">checked="checked"</c:if> value="${schemaField.indexed?'true':'false'}" /></td>
	            <td><input type="checkbox" attrname="fields.stored" name="fields[${status.index }].stored"  <c:if test="${schemaField.stored }">checked="checked"</c:if> value="${schemaField.stored?'true':'false'}" /></td>
	            <td><input type="checkbox" attrname="fields.required" name="fields[${status.index }].required" <c:if test="${schemaField.required }">checked="checked"</c:if> value="${schemaField.required?'true':'false'}" /></td>
	            <td><input type="checkbox" attrname="fields.multiValued" name="fields[${status.index }].multiValued" <c:if test="${schemaField.multiValued }">checked="checked"</c:if> value="${schemaField.multiValued?'true':'false'}" /></td>
	            <td><input type="checkbox" attrname="fields.isListShow" name="fields[${status.index }].isListShow" <c:if test="${schemaField.isListShow }">checked="checked"</c:if> value="${schemaField.isListShow?'true':'false'}" /></td>
	            <td><input type="checkbox" attrname="fields.isListSearch" name="fields[${status.index }].isListSearch" <c:if test="${schemaField.isListSearch }">checked="checked"</c:if> value="${schemaField.isListSearch?'true':'false'}" /></td>
	            <td><input type="radio" attrname="isPrimaryKey" value="${status.index }" name="isPrimaryKey"  class="required" <c:if test="${schema.primaryNo eq status.index}">checked="checked"</c:if> /></td>
	            <td>
	            	<c:if test="${status.index > 0 }"><a href="javascript:void(0);" onclick="removeFieldRow(this)">删除</a></c:if>
	            </td>
	          </tr>
          </c:forEach>
        </tbody>
      </table>
      <select id="tplFieldType" class="hide">
          <c:forEach var="fieldType" items="${fieldTypes }">
          <option type="${fieldType.type }" classz="${fieldType.classz }" value="${fieldType.value }">${fieldType.explain }</option>
          </c:forEach>
      </select>
    </div>
    <div class="add-row">
      	<a href="javascript:addFieldRow();">添加</a>
      </div>
  </div>
</div>
