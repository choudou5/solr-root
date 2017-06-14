<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/layui/_public/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>Powered By ${fns:getConfig('text.productName')}</title>
<%@include file="../_public/header.jsp" %>
</head>
<body>
<div class="sub-page">
	<div class="layui-tab layui-tab-brief sub-page-tab" lay-filter="F_sub_tab">
		<ul class="layui-tab-title">
			<li class="layui-this" data-url="${__ADMIN__ }/sys/user/update-pwd">修改密码</li>
		</ul>
		<div class="layui-tab-content">
			<div class="layui-tab-item layui-show">
				<form class="layui-form" action="${__ADMIN__ }/sys/user/do_update_pwd" method="post">
					<div class="layui-form-item">
						<label class="layui-form-label">旧密码</label>
						<div class="layui-input-block">
							<input type="password" name="oldPassword" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">新密码</label>
						<div class="layui-input-block">
							<input type="password" name="newPassword" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">确认新密码</label>
						<div class="layui-input-block">
							<input type="password" name="okNewPassword" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<button class="layui-btn" lay-submit="" lay-filter="F_updatePwd_form">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script src="${__STATIC__ }/admin/js/admin.js?t=<%=System.currentTimeMillis() %>"></script>
<script type="text/javascript">
layui.use(['element','form','jacommon'], function(){
	var $ = layui.jquery
	,layer = layui.layer
	,jacommon = layui.jacommon
	,element = layui.element()
	,form = layui.form();
	
	form.on('submit(F_updatePwd_form)', function(data){
		var $btn = $(data.elem)
		,postUrl = data.form.action
    	,btnText = $btn.html();
		
		if($('input[name="newPassword"]').val() 
				!= $('input[name="okNewPassword"]').val()) {
			jacommon.error('新密码与确认密码不一致！');
			return false;
		}
		$btn.addClass('layui-btn-disabled').text('提交中...');
		var postData = $(data.form).serialize();
		jacommon.ajaxPost(postUrl, postData, function(res) {
    		jacommon.success('密码修改成功，请重新登录！', function() {
				window.parent.location.href = "${__ADMIN__}/logout";
			});
    	}, function(res) {
    		jacommon.error('修改失败，'+res.msg+'(错误代码：'+res.errorCode+')');
    	}, function() {
    		$btn.removeClass('layui-btn-disabled').text(btnText);
    	});
		
		return false;
	});
});
</script>
</body>
</html>