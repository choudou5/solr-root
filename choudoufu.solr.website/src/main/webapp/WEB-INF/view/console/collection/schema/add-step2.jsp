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
          <tr id="tr_0">
          	<sys:fieldInputTpl fieldIndex="0" fieldName="createTime" fieldLabel="创建时间" fieldType="${schemaField.type }"/>
          </tr>
          <tr id="tr_1">
          	<sys:fieldInputTpl fieldIndex="1" fieldName="createBy" fieldLabel="创建人" fieldType="${schemaField.type }"/>
          </tr>
          <tr id="tr_2">
          	<sys:fieldInputTpl fieldIndex="2" fieldName="updateTime" fieldLabel="更新时间" fieldType="${schemaField.type }"/>
          </tr>
          <tr id="tr_3">
          	<sys:fieldInputTpl fieldIndex="3" fieldName="updateBy" fieldLabel="更新人" fieldType="${schemaField.type }"/>
          </tr>
        </tbody>
      </table>
      <select id="tplFieldType" class="hide">
          <c:forEach var="fieldType" items="${fns:getFieldTypeArray()}">
          <option type="${fieldType.type }" classz="${fieldType.classz }" value="${fieldType.value }">${fieldType.explain }</option>
          </c:forEach>
      </select>
    </div>
    <div class="add-row">
      	<a href="javascript:addFieldRow();">添加</a>
      </div>
  </div>
</div>
