<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-控制台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<%@include file="/WEB-INF/view/console/include/cssLib.jsp" %>
	<link href="${ctxStaticConsole }/js/zTree/v3.5.28/css/demo.css" rel="stylesheet">
	<link href="${ctxStaticConsole }/js/zTree/v3.5.28/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
	<%@include file="/WEB-INF/view/console/include/cssFormLib.jsp" %>
	<style type="text/css">
		ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;min-width:220px;min-height:300px;overflow-y:scroll;overflow-x:auto;}
	</style>
</head>
<body>

<%@include file="/WEB-INF/view/console/include/head.jsp" %>

<%@include file="/WEB-INF/view/console/include/sidebar.jsp" %>

<!--main-content-part-->
<div id="content">

<sys:breadcrumbs currLevel="2" secondLevelTitle="角色管理" secondLevelView="/console/sys/user/"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	<ul class="nav nav-tabs">
          <li><a href="${ctx }/console/sys/user/">角色管理</a></li>
          <perm:hasPermission name="sys:user:edit" sid="${sid }">
          	<li class="active"><a href="${ctx }/console/sys/user/form?id=${user.id}">添加用户</a></li>
          </perm:hasPermission>
        </ul>
						
      	<!-- begin 添加 -->
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>添加用户</h5>
          </div>
          <div class="widget-content nopadding">
							 
          	<form:form id="inputForm" class="form-horizontal" modelAttribute="user" action="${ctx }/console/sys/user/save" method="post">
					<form:hidden path="id"/>
					<c:set var="isNew" value="${empty user.id }" />
					<div class="control-group">
						<label class="control-label">账号<font class="red">*</font></label>
						<div class="controls">
							<input type="text" name="loginName" value="${user.loginName }" class="required input-medium">
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">密码<font class="red">*</font></label>
						<div class="controls">
							<input type="password" id="password" name="password" value="" class="input-medium">
							<c:if test="${!isNew }">
								<span class="help-inline-tip">&nbsp;为空则不修改密码</span>
							</c:if>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">确认密码<font class="red">*</font></label>
						<div class="controls">
							<input type="password" id="confirmPassword" name="confirmPassword" value="" class="input-medium">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">真实姓名<font class="red">*</font></label>
						<div class="controls">
							<input type="text" name="realName" value="${user.realName }" class="required input-medium">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">角色<font class="red">*</font></label>
						<div class="controls">
							<c:forEach items="${roles }" var="role">
								<input type="checkbox" value="${role.id }" name="roleIds" class="required" <c:if test="${fns:containRoleId(userRoleIds, role.id) }">checked="checked"</c:if> />
								<span>${role.name }</span>&nbsp;&nbsp;
							</c:forEach>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">是否启用</label>
						<div class="controls">
							<input name="usable" type="checkbox"${user.usable ? ' checked=""' : '' } value="true">
						</div>
					</div>
					<div class="form-actions">
		              	<label class="control-label"></label>
		                 <div class="controls">
		                 	 <a href="${ctx }/console/sys/user/"  class="btn">返回</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                 	 <perm:hasPermission name="sys:user:edit" sid="${sid }">
			                	<input class="btn btn-danger" type="submit" value="确认提交" />
							 </perm:hasPermission>
		                 </div>
		            </div>
				</form:form>
          </div>
        </div>
        <!-- end 添加 -->
        
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>
<%@include file="/WEB-INF/view/console/include/scriptValidLib.jsp" %>
<script src="${ctxStaticConsole }/js/zTree/v3.5.28/jquery.ztree.core.min.js" type="text/javascript"></script>
<script src="${ctxStaticConsole }/js/zTree/v3.5.28/jquery.ztree.excheck.min.js" type="text/javascript" async="async"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#inputForm").validate({
			<c:if test="${empty user.id }">
			rules:{
				password:{
					required: true,
					minlength:6,
					maxlength:20
				},
				confirmPassword:{
					required:true,
					minlength:6,
					maxlength:20,
					equalTo:"#password"
				}
			},
			</c:if>
			messages: {
				confirmPassword: {equalTo: "请输入相同的密码！"}
			},
			errorClass: "help-inline",
			errorElement: "span",
			errorPlacement: function(error, element) {  
			    error.appendTo(element.parent());  
			},
			highlight:function(element, errorClass, validClass) {
				$(element).parents('.control-group').addClass('error');
			},
			unhighlight: function(element, errorClass, validClass) {
				$(element).parents('.control-group').removeClass('error');
				$(element).parents('.control-group').addClass('success');
			}
		});
		
		
	});
</script>
</body>
</html>
