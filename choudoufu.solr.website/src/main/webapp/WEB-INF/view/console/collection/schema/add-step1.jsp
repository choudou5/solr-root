<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 基本信息 -->
<div id="form-wizard-1" class="step">
  <div class="control-group">
    <label class="control-label">应用标识</label>
    <div class="controls">
      <input type="text" name="name" placeholder="由 字母、下划线、或数字组成" maxlength="30" value="test"/>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">应用名称</label>
    <div class="controls">
      <input type="text" name="title" class="span3" placeholder="请输入应用名称" maxlength="10" value="test..."/>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">说明</label>
    <div class="controls">
      <textarea name="explain" class="span5" rows="3" maxlength="120" placeholder="请简单描述您的应用"></textarea>
    </div>
  </div>
</div>