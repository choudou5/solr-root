<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-控制台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<%@include file="/WEB-INF/view/console/include/cssLib.jsp" %>
	<%@include file="/WEB-INF/view/console/include/cssFormLib.jsp" %>
</head>
<body>

<%@include file="/WEB-INF/view/console/include/head.jsp" %>

<%@include file="/WEB-INF/view/console/include/sidebar.jsp" %>

<!--main-content-part-->
<div id="content">

<sys:breadcrumbs currLevel="2" secondLevelTitle="修改密码"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
						
      	<!-- begin 添加 -->
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>修改密码</h5>
          </div>
          <div class="widget-content nopadding">
							 
          	<form:form id="inputForm" class="form-horizontal" modelAttribute="user" action="${ctx }/console/sys/user/saveUpdatePwd" method="post">
					<form:hidden path="id"/>
					<div class="control-group">
						<label class="control-label">旧密码<font class="red">*</font></label>
						<div class="controls">
							<input type="password" id="oldPassword" name="oldPassword" value="" class="input-medium">
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">新密码<font class="red">*</font></label>
						<div class="controls">
							<input type="password" id="newPassword" name="newPassword" value="" class="input-medium">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">确认密码<font class="red">*</font></label>
						<div class="controls">
							<input type="password" id="confirmPassword" name="confirmPassword" value="" class="input-medium">
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
<script type="text/javascript">
	$(document).ready(function(){
		$("#inputForm").validate({
			rules:{
				oldPassword:{
					required: true,
					minlength:6,
					maxlength:20
				},
				newPassword:{
					required: true,
					minlength:6,
					maxlength:20
				},
				confirmPassword:{
					required:true,
					minlength:6,
					maxlength:20,
					equalTo:"#newPassword"
				}
			},
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
