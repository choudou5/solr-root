<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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
          <tr id="tr_0">
            <td>1</td>
            <td>
              <input class="required isVariable" type="text" attrname="fields.name" name="fields.name" placeholder="字母 或 下划线 组成" maxlength="10" /></td>
            <td>
              <input class="required span10" type="text" attrname="fields.label" name="fields.label" placeholder="简单描述" name=""  minlength="2" maxlength="10" /></td>
            <td>
              <select id="selectType" attrname="fields.type.name" name="fields.type.name"  onchange="changeSelectType(0)" >
                  <c:forEach var="fieldType" items="${fieldTypes }">
                  <option type="${fieldType.type }" classz="${fieldType.classz }" value="${fieldType.value }">${fieldType.explain }</option>
                  </c:forEach>
              </select>
            </td>
            <td>
              <div id="analyzerDiv">
                <%@include file="/WEB-INF/view/console/collection/schema/analyzerDefHidden.jsp" %>
              </div>
            </td>
            <td><input type="checkbox" attrname="fields.indexed" name="fields.indexed"  value="true"  /></td>
            <td><input type="checkbox" attrname="fields.stored" name="fields.stored"  value="true" /></td>
            <td><input type="checkbox" attrname="fields.required" name="fields.required"  value="true" /></td>
            <td><input type="checkbox" attrname="fields.multiValued" name="fields.multiValued"  value="true" /></td>
            <td><input type="checkbox" attrname="fields.isListShow" name="fields.isListShow"  value="true" /></td>
            <td><input type="checkbox" attrname="fields.isListSearch" name="fields.isListSearch"  value="true" /></td>
            <td><input type="radio" attrname="isPrimaryKey" value="0" name="isPrimaryKey"  class="required" /></td>
            <td></td>
          </tr>
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
