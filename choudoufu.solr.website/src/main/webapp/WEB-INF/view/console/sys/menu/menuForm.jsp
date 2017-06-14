<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#menuInputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
		function submitFun(){
			var form = $("#menuInputForm");
			if(!form. valid()){
				return false;
			}
			var mNamess = getInputGroupVal("mNames");
			var mPermissions = getInputGroupVal("mPermissions");
			var len = $("#mNames").length;
			if(len != mNamess.length || len != mPermissions.length){
				showTip("请填表单不能为空！");
				return false;
			}
			form.submit();
		}
		
		/**
		 * 复制 按钮
		 */
		function copyButton(ths){
			var groupId = getRandom();
			var htm = new StringBuffer();
			htm.append('<div group="'+groupId+'" class="control-group" style="background: #FCFCFC;"><label class="control-label">名称:</label>');
			htm.append('<div class="controls"><input id="mNames" name="mNames" maxlength="50" type="text" class="input-xlarge"/><span class="help-inline"><font color="red">*</font> 按钮名称</span> ');
			htm.append('&nbsp;<a href="javascript:removeButton('+groupId+');" class="icon-minus" title="移除"></a>');
			htm.append('</div></div>');
			htm.append('<div group="'+groupId+'" class="control-group" style="background: #FCFCFC;"><label class="control-label">权限标识:</label>');
			htm.append('<div class="controls"><input id="mPermissions" name="mPermissions" maxlength="50" type="text" class="input-xxlarge"/><span class="help-inline"><font color="red">*</font> 控制器中定义的权限标识，如：@RequiresPermissions("权限标识")</span> ');
			htm.append('</div></div>');
			$("#buttons").before(htm.toString());
		}

		/**
		 * 移除 按钮
		 */
		function removeButton(groupId){
			$("div[group="+groupId+"]").remove();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/menu/">菜单列表</a></li>
		<li class="active"><a href="${ctx}/sys/menu/form?id=${menu.id}&parent.id=${menu.parent.id}">菜单<shiro:hasPermission name="sys:menu:edit">${not empty menu.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="menuInputForm" modelAttribute="menu" action="${ctx}/sys/menu/save" method="post" class="form-horizontal" >
		<form:hidden path="id"/>
		<input type="hidden" name="mtype" value="${mtype }"/>
		<c:if test="${mtype eq 'button' }">
			<input type="hidden" name="isShow" value="0"/>
		</c:if>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">上级菜单:</label>
			<div class="controls">
                <sys:treeselect id="menu" name="parent.id" value="${menu.parent.id}" labelName="parent.name" labelValue="${menu.parent.name}"
					title="菜单" url="/sys/menu/treeData" extId="${menu.id}" cssClass="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required input-xlarge"/>
				<span class="help-inline"><font color="red">*</font>${mtype eq 'button'?' 按钮名称':''}</span>
				<c:if test="${mtype eq 'button'}">
					&nbsp;<a href="javascript:copyButton();" class="icon-plus" title="复制"></a>
				</c:if>
			</div>
		</div>
		<div class="control-group ${mtype eq 'button'?'hide':''}">
			<label class="control-label">链接:</label>
			<div class="controls">
				<form:input path="href" htmlEscape="false" maxlength="2000" class="input-xxlarge"/>
				<span class="help-inline">点击菜单跳转的页面</span>
			</div>
		</div>
		<div class="control-group ${mtype eq 'button'?'hide':''}">
			<label class="control-label">目标:</label>
			<div class="controls">
				<form:input path="target" htmlEscape="false" maxlength="10" class="input-small"/>
				<span class="help-inline">链接地址打开的目标窗口，默认：mainFrame</span>
			</div>
		</div>
		<div class="control-group ${mtype eq 'button'?'hide':''}">
			<label class="control-label">图标:</label>
			<div class="controls">
				<sys:iconselect id="icon" name="icon" value="${menu.icon}"/>
			</div>
		</div>
		<div class="control-group ${mtype eq 'button'?'hide':''}">
			<label class="control-label">排序:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="50" class="required digits input-small"/>
				<span class="help-inline">排列顺序，升序。</span>
			</div>
		</div>
		
		<c:if test="${mtype ne 'button' }">
			<div class="control-group">
			<label class="control-label">可见:</label>
				<div class="controls">
					<form:radiobuttons path="isShow" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
					<span class="help-inline">该菜单或操作是否显示到系统菜单中</span>
				</div>
			</div>
		</c:if>
		
		<div class="control-group">
			<label class="control-label">权限标识:</label>
			<div class="controls">
				<form:input path="permission" htmlEscape="false" maxlength="200" class="${mtype eq 'button'?'required':''} input-xxlarge" />
				<span class="help-inline">
				<c:if test="${mtype eq 'button'}"><font color="red">*</font></c:if>
				 控制器中定义的权限标识，如：@RequiresPermissions("权限标识")</span>
			</div>
		</div>
		<div class="control-group ${mtype eq 'button'?'hide':''}">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<div id="buttons" class="form-actions">
			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="button" onclick="submitFun()" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>