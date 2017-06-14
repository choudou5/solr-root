<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/treeTable/themes/vsStyle/treeTable.min.css" rel="stylesheet" type="text/css" />
	<script src="${ctxStatic}/treeTable/jquery.treeTable.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/sys/menu.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 1});
			
			$("select[id=parentId]").change(function(){
				var parentId = $(this).val();
				window.location.href="${ctx}/sys/menu/list?parentId="+parentId;
			});
		});
		
	</script>
</head>
<body>
	
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/menu/">菜单列表</a></li>
		<shiro:hasPermission name="sys:menu:edit"><li><a href="${ctx}/sys/menu/form?${empty parentId?'':'parent.id=' }${parentId}">菜单添加</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
		
	<form id="listForm" method="post">
		<ul class="ul-form">
			<li>
				<label>一级菜单：</label>
				<select id="parentId" class="input-medium">
					<option></option>
					<c:forEach items="${oneLevelMenuList }" var="item">
						<option value="${item.id }" <c:if test="${parentId eq  item.id}">selected="selected"</c:if> >${item.name }</option>
					</c:forEach>
				</select>
			</li>
		</ul>
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th>名称</th><th>链接</th><th style="text-align:center;">排序</th><th>可见</th><th>权限标识</th><shiro:hasPermission name="sys:menu:edit"><th>操作</th></shiro:hasPermission></tr></thead>
			<tbody><c:forEach items="${list}" var="menu">
				<tr id="${menu.id}" pId="${empty parentId?(menu.parent.id ne '1'?menu.parent.id:'0'):(menu.parent.id eq parentId?'0':menu.parent.id)}">
					<td nowrap><i class="icon-${not empty menu.icon?menu.icon:' hide'}"></i><a href="${ctx}/sys/menu/form?id=${menu.id}">${menu.name}</a></td>
					<td title="${menu.href}">${fns:abbr(menu.href,40)}</td>
					<td style="text-align:center;">
						<shiro:hasPermission name="sys:menu:edit">
							<input type="hidden" name="ids" value="${menu.id}"/>
							<input name="sorts" type="text" value="${menu.sort}" style="width:50px;margin:0;padding:0;text-align:center;">
						</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">
							${menu.sort}
						</shiro:lacksPermission>
					</td>
					<td id="isShow">
						<span class="badge badge-${menu.isShow eq '1'?'success':'warning'}">${menu.isShow eq '1'?'显示':'隐藏'}</span>
						<shiro:hasPermission name="sys:menu:edit">&nbsp;
							<a href="javascript:updateShow(${menu.id}, '${menu.isShow eq '0'?'Y':'N'}')">
								${menu.isShow eq '0'?'显示':'隐藏'}
							</a>
						</shiro:hasPermission>
					</td>
					<td id="permission" title="${menu.permission}">${fns:abbr(menu.permission,40)}
						<shiro:hasPermission name="sys:menu:edit">&nbsp;
							<a href="javascript:updatePermission(${menu.id}, '${menu.permission}')">更改</a>
						</shiro:hasPermission>
					</td>
					<shiro:hasPermission name="sys:menu:edit">
					<td nowrap>
						<a href="${ctx}/sys/menu/form?id=${menu.id}">修改</a>&nbsp;
						<c:if test="${menu.isShow eq '0' and !menu.hasChild}">
							<a href="javascript:deleteMenu(${menu.id});">删除</a>&nbsp;
						</c:if>
						<a href="${ctx}/sys/menu/form?parent.id=${menu.id}">添加下级菜单</a>&nbsp;
						<a href="${ctx}/sys/menu/form?parent.id=${menu.id}&mtype=button">添加下级按钮</a>&nbsp;
					</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach></tbody>
		</table>
		<shiro:hasPermission name="sys:menu:edit">
			<div class="form-actions pagination-left">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
			</div>
		</shiro:hasPermission>
	 </form>
</body>
</html>