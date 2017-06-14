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

<sys:breadcrumbs currLevel="2" secondLevelTitle="角色管理" secondLevelView="/console/sys/role/"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	<ul class="nav nav-tabs">
          <li><a href="${ctx }/console/sys/role/">角色管理</a></li>
          <perm:hasPermission name="sys:role:edit" sid="${sid }">
          	<li class="active"><a href="${ctx }/console/sys/role/form?id=${role.id}&action=${action}">添加角色</a></li>
          </perm:hasPermission>
        </ul>
						
      	<!-- begin 添加 -->
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>添加角色</h5>
          </div>
          <div class="widget-content nopadding">
							 
          	<form:form id="inputForm" class="form-horizontal" modelAttribute="role" action="${ctx }/console/sys/role/save" method="post">
					<form:hidden path="id"/>
					<c:set var="isNew" value="${empty role.id }" />
					<c:set var="isAssign" value="${action eq 'assign' }" />
	
					<div class="control-group">
						<label class="control-label">角色名称<font class="red">*</font></label>
						<div class="controls">
							<c:if test="${isAssign }">
								<span class="label label-success">${role.name }</span>
								<input type="hidden" name="name" value="${role.name }" class="required input-xlarge">
							</c:if>
							<c:if test="${!isAssign }">
								<input type="text" name="name" value="${role.name }" class="required input-xlarge">
							</c:if>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">菜单<font class="red">*</font></label>
						<div class="controls">
							<input type="hidden" name="menus" value="${fn:join(role.menus, ",")}" class="required">
							<ul id="treeList" class="ztree"></ul>
						</div>
					</div>
					<div class="control-group ${isAssign?'hide':'' }">
						<label class="control-label">说明</label>
						<div class="controls">
							<textarea placeholder="请输入角色描述" name="desc" class="">${role.desc }</textarea>
						</div>
					</div>
					<div class="control-group ${isAssign?'hide':'' }">
						<label class="control-label">是否启用</label>
						<div class="controls">
							<input name="usable" type="checkbox"${role.usable ? ' checked=""' : '' } value="true">
						</div>
					</div>
					 <div class="form-actions">
		              	<label class="control-label"></label>
		                 <div class="controls">
		                 	 <a href="${ctx }/console/sys/role/"  class="btn">返回</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                 	 <perm:hasPermission name="sys:role:edit" sid="${sid }">
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
	var setting = {
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onCheck: zTreeOnCheck
		}
	};
	
	function zTreeOnCheck(event, treeId, treeNode) {
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.getCheckedNodes(true);
		var menuIds = new Array();
		for(var dex in nodes){
			var node = nodes[dex];
		    //log(node.tId+", "+node.id + ", " + node.name + "," + node.checked);
			menuIds.push(node.id);
		}
		$("input[name=menus]").val(menuIds.join(","));
	};
	
	function initTreeSelect() {
		var menuIdStr = $("input[name=menus]").val();
		if(isNotEmpty(menuIdStr)){
			var menuIds = menuIdStr.split(",");
			var treeObj = $.fn.zTree.getZTreeObj("treeList");
			for (var i=0, l=menuIds.length; i < l; i++) {
				var node = treeObj.getNodeByParam("id", menuIds[i], null);
				treeObj.checkNode(node, true, true);
			}
		}
	};
	
	$(document).ready(function(){
		
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
		
		//加载树
		loadTree();
		
		//初始化 树选中
		setTimeout(function(){
			initTreeSelect();
		}, 500);
		
	});
	
	function loadTree(){
        $.ajax({
            url: ctx+'/console/sys/menu/ajaxLoadTree',
            data:{id: ""},
            type:'post',
            dataType:'json',
            success:function(zTreeNodes){
                //初始化树
                $.fn.zTree.init($("#treeList"), setting, zTreeNodes);
            }
        })
	}
	</script>
</script>
</body>
</html>
