<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 高级配置结构 -->
<div id="form-wizard-3" class="step hide">
  <div class="widget-box">
    <div class="widget-title">
      <h5>字段组合</h5>
    </div>
    <div class="widget-content nopadding">
      <c:set var="fieldTypes" value="${fns:getFieldTypeArray()}" />
      <table id="fieldTable" class="table table-bordered table-striped">
        <thead>
          <%@include file="/WEB-INF/view/console/collection/schema/table-thead.jsp" %>
        </thead>
        <tbody>
          <tr id="templateTr">
            <td>1</td>
            <td>
              <input class="required isVariable" type="text" name="fields[0].name" placeholder="字母 或 下划线 组成" maxlength="10" /></td>
            <td>
              <input class="required" type="text" name="fields[0].label" placeholder="简单描述" minlength="2" maxlength="10" /></td>
            <td>
              <select id="selectType" name="fields[0].type.name" onchange="changeSelectType(0)" >
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
            <td><input type="checkbox" name="fields[0].indexed" value="true" /></td>
            <td><input type="checkbox" name="fields[0].stored" value="true" /></td>
            <td><input type="checkbox" name="fields[0].required" value="true" /></td>
            <td><input type="checkbox" name="fields[0].multiValued" value="true" /></td>
            <td><input type="checkbox" name="fields[0].isListShow" value="true" /></td>
            <td><input type="checkbox" name="fields[0].isListSearch" value="true" /></td>
            <td><input type="radio" name="isPrimaryKey" value="0" class="required" /></td>
            <td></td>
          </tr>
          <tr id="addFieldBtns">
            <td colspan="13" style="text-align: center;">
              <a href="javascript:addFieldRow();">添加</a>
            </td>
          </tr>
        </tbody>
      </table>
      <select id="tplFieldType" class="hide">
          <c:forEach var="fieldType" items="${fieldTypes }">
          <option type="${fieldType.type }" classz="${fieldType.classz }" value="${fieldType.value }">${fieldType.explain }</option>
          </c:forEach>
      </select>
    </div>
  </div>
</div>
