<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 基本信息 -->
<div id="form-wizard-1" class="step">
	<div class="control-group">
    <label class="control-label">应用标识</label>
    <div class="controls">
      <c:if test="${isNew }">
      	<input type="checkbox" name="isSys" value="true"/>&nbsp;<span class="help-inline-tip">系统表（系统表将不可删除）</span>
      </c:if>
      <c:if test="${!isNew }">
      	<input type="checkbox" name="isSys" <c:if test="${schema.isSys }">checked="checked" disabled="disabled"</c:if> value="true"/>&nbsp;<span class="help-inline-tip">系统表（系统表将不可删除）</span>
      </c:if>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">应用标识</label>
    <div class="controls">
      <input type="text" name="name" placeholder="由 字母、下划线、或数字组成" maxlength="30" <c:if test="${not empty schema.name }">readonly="readonly"</c:if> value="${schema.name }"/>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">应用名称</label>
    <div class="controls">
      <input type="text" name="title" class="span3" placeholder="请输入应用名称" maxlength="10" value="${schema.title }"/>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">说明</label>
    <div class="controls">
      <textarea name="explain" class="span5" rows="3" maxlength="120" placeholder="请简单描述您的应用">${schema.explain }</textarea>
    </div>
  </div>
</div>