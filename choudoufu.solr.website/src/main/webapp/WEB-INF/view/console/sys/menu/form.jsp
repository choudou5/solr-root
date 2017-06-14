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

<sys:breadcrumbs currLevel="2" secondLevelTitle="菜单管理" secondLevelView="/console/sys/menu/"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	<ul class="nav nav-tabs">
          <li><a href="${ctx }/console/sys/menu/">菜单管理</a></li>
          <perm:hasPermission name="sys:menu:edit" sid="${sid }">
          	<li class="active"><a href="${ctx }/console/sys/menu/form">添加菜单</a></li>
          </perm:hasPermission>
        </ul>
						
      	<!-- begin 添加集合 -->
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>添加菜单</h5>
          </div>
          <div class="widget-content nopadding">
							 
          	<form:form id="inputForm" class="form-horizontal" modelAttribute="menu" action="${ctx }/console/sys/menu/save" method="post">
					<form:hidden path="id"/>
					<c:set var="isNew" value="${empty menu.id }" />
					<c:set var="isBtn" value="${menu.link eq 'btn' }" />
					
					<div class="control-group">
						<label class="control-label">上级</label>
						<div class="controls">
							<input type="hidden" id="_pid"  value="${pid }"/>
							<form:select id="pid" path="pid" cssClass="input-xlarge" >
								<form:option value="1" label="作为一级菜单"/>
								<form:options items="${treeList }" itemLabel="alias" itemValue="id" htmlEscape="false"/>
							</form:select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">名称<font class="red">*</font></label>
						<div class="controls">
							<input type="text" name="name" value="${menu.name }" class="required input-xlarge">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">权限标识<font class="red">*</font></label>
						<div class="controls">
							<input type="text" name="permission" value="${menu.permission }" class="required input-xlarge">
							<span class="help-inline-tip">格式为：模块:功能:权限（例如：sys:user:view）</span>
						</div>
					</div>
					<div class="control-group ${isBtn?'hide':'' }">
						<label class="control-label">跳转链接<font class="red">*</font></label>
						<div class="controls">
							<input type="text" name="link" value="${menu.link }" class="required input-xlarge">
						</div>
					</div>
					<div class="control-group ${isBtn?'hide':'' }">
						<label class="control-label">图标</label>
						<div class="controls">
							<input type="text" name="icon" value="${menu.icon }" class="input-xlarge">
							<span class="help-inline-tip"><a href="http://fontawesome.io/3.2.1/icons/" target="_blank">font-awesome</a>图标名（例如：fa-user）</span>
						</div>
					</div>
					<div class="control-group ${isBtn?'hide':'' }">
						<label class="control-label">是否显示</label>
						<div class="controls">
							<input type="checkbox" name="show" value="true" ${menu.show?'checked="checked"':'' } ${isNew?'checked="checked"':'' } />
						</div>
					</div>
					
					<div class="control-group ${!isNew?'hide':'' }">
						<label class="control-label">按钮权限<font class="red">*</font></label>
						<div id="btns" class="controls">
							<label id="btns-0">
								<input type="hidden" name="btns[0].link" data-name="link" value="btn"/>
								<input type="text" name="btns[0].name" data-name="name" value="${isNew?'编辑':'' }" class="required" placeholder="${isNew?'按钮名称':'' }" />&nbsp;&nbsp;
								<input type="text" name="btns[0].permission" data-name="permission" class="required" value="${isNew?'demo:edit':'' }" />&nbsp;
								<a href="javascript:addFieldRow();"><i class="icon-plus"></i></a>
							</label>
		
						</div>
					</div>
					
					 <div class="form-actions">
		              	<label class="control-label"></label>
		                 <div class="controls">
		                 	 <a href="${ctx }/console/sys/menu/"  class="btn">返回</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                 	 <perm:hasPermission name="sys:menu:edit" sid="${sid }">
			                	<input class="btn btn-danger" type="submit" value="确认提交" />
							 </perm:hasPermission>
		                 </div>
		            </div>
				</form:form>
          </div>
        </div>
        <!-- end 添加集合 -->
        
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>
<%@include file="/WEB-INF/view/console/include/scriptValidLib.jsp" %>

<script type="text/javascript">
	$(document).ready(function(){
		var pid = $("#_pid").val();
		$("#pid").val(pid);
		
		$("#inputForm").validate({
			errorClass: "help-inline",
			errorElement: "span",
			highlight:function(element, errorClass, validClass) {
				$(element).parents('.control-group').addClass('error');
			},
			unhighlight: function(element, errorClass, validClass) {
				$(element).parents('.control-group').removeClass('error');
				$(element).parents('.control-group').addClass('success');
			}
		});
	});
	
	function addFieldRow(){
		var btns = $("#btns");
		var newIndex = btns.find("label").length-1;
		var tpl = new StringBuffer();
		tpl.append('<label id="btns-'+newIndex+'">');
		tpl.append('<input type="hidden" name="btns['+newIndex+'].link" data-name="link"  value="btn"/>');
		tpl.append('<input type="text" name="btns['+newIndex+'].name" data-name="name" value="xxx" class="required" placeholder="按钮名称" />&nbsp;&nbsp;&nbsp;');
		tpl.append('<input type="text" name="btns['+newIndex+'].permission" data-name="permission" class="required"  value="demo:xxx" />&nbsp;&nbsp;');
		tpl.append('<a href="javascript:void(0);" onclick="removeBtnRow(this);"><i class="icon-remove"></i></a>');
		tpl.append('</label>');
		btns.append(tpl.toString());
		resetBtnsIndex();//重置 下标
	}
	
	/**
	 * 重置 字段下班
	 */
	function resetBtnsIndex(){
		var trIndex = 0;
		$("#btns label").each(function(){
			$(this).attr("id", "btns-"+trIndex);
			$(this).find("input").each(function(){
				var ele = $(this);
				var dataName = ele.attr("data-name");
				if(dataName){
					console.log("dataName："+dataName);
					var newName = "btns["+trIndex+"]."+dataName;
					ele.attr("name", newName);
				}
			});
			trIndex++;
		});
	}
	
	/** 移除 按钮  */
	function removeBtnRow(thi){
		$(thi).parent().remove();
		resetBtnsIndex();//重置 下标
	}
	</script>
</script>
</body>
</html>
