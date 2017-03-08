<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html>
<head>
   <title>${fns:getSiteName()}-登录</title>
   <meta name="viewport" content="width=device-width, initial-scale=1.0" />
   <link rel="shortcut icon" href="${ctxStatic }/images/favicon.ico">
   <link rel="stylesheet" href="${ctxStaticConsole }/css/bootstrap.min.css" />
   <link rel="stylesheet" href="${ctxStaticConsole }/css/bootstrap-responsive.min.css" />
   <link rel="stylesheet" href="${ctxStaticConsole }/font-awesome/css/font-awesome.css" />
   <link rel='stylesheet' href='${ctxStaticConsole }/css/fonts.googleapis.com.css' type='text/css'>
   <link rel="stylesheet" href="${ctxStaticConsole }/css/system-login.css" />
        
</head>
    <body>
        <div id="loginbox">
        	<!-- 消息提示 -->
        	<sys:message content="${message }"/>
            <form id="loginform" class="form-vertical" action="${ctx }/console/login" method="post" enctype="multipart/form-data">
				<div class="control-group normal_text"> <h3><img src="${ctxStatic }/images/logo.png" alt="Logo" /></h3></div>
                <div class="control-group">
                    <div class="controls">
                        <div class="main_input_box">
                            <span class="add-on bg_lg"><i class="icon-user"> </i></span><input value="admin" type="text" name="username" placeholder="账号" id="required"/>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="main_input_box">
                            <span class="add-on bg_ly"><i class="icon-lock"></i></span><input value="adminadmin" type="password" name="password" placeholder="密码" />
                        </div>
                    </div>
                </div>
                【${isValidateCodeLogin}】
                <%--需要验证码登录 --%>
                <c:if test="${isValidateCodeLogin}">
	                <div class="control-group">
	                    <div class="controls">
	                        <div class="main_input_box" style="text-align: left; padding-left: 12px;">
	                            <span class="add-on bg_ly"><i class="icon-lock"></i></span>
	                            <sys:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
	                        </div>
	                    </div>
	                </div>
                </c:if>
                
                <div class="form-actions">
                    <!-- <span class="pull-left"><a href="#" class="flip-link btn btn-info" id="to-recover">Lost password?</a></span> -->
                    <span class="pull-left"><a href="${ctx }/console/login/visitor" class="flip-link btn btn-info">我是游客</a></span>
                    <span class="pull-right"><input type="submit" class="btn btn-success" value=" 登 录 " /></span>
                </div>
            </form>
            <form id="recoverform" action="#" class="form-vertical">
				<p class="normal_text">Enter your e-mail address below and we will send you instructions how to recover a password.</p>
				
                    <div class="controls">
                        <div class="main_input_box">
                            <span class="add-on bg_lo"><i class="icon-envelope"></i></span><input type="text" placeholder="E-mail address" />
                        </div>
                    </div>
               
                <div class="form-actions">
                    <span class="pull-left"><a href="#" class="flip-link btn btn-success" id="to-login">&laquo; Back to login</a></span>
                    <span class="pull-right"><a class="btn btn-info"/>Reecover</a></span>
                </div>
            </form>
        </div>
        
        <script src="${ctxStaticConsole }/js/jquery.min.js"></script>
		<script src="${ctxStaticConsole }/js/bootstrap.min.js"></script>
		<script src="${ctxStaticConsole }/js/jquery.validate.js"></script>
		<script src="${ctxStatic }/js/lodash-4.17.4/lodash.min.js"></script>
        <script src="${ctxStaticConsole }/js/modules/system-login.js"></script>
    </body>

</html>
