<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-控制台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<%@include file="/WEB-INF/view/console/include/cssLib.jsp" %>
	<link href="${ctxStaticConsole }/js/jquery-treetable/3.2.0/css/jquery.treetable.css" rel="stylesheet">
	<link href="${ctxStaticConsole }/js/jquery-treetable/3.2.0/css/jquery.treetable.theme.default.min.css" rel="stylesheet">
	<style type="text/css">
		.nopadding .table-bordered{border: 0;}
	</style>
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
          <li class="active"><a href="${ctx }/console/sys/menu/">菜单管理</a></li>
          <li><a href="${ctx }/console/sys/menu/form">添加菜单</a></li>
        </ul>
      	<!-- begin 添加菜单 -->
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>添加菜单
	            <a href="javascript:;" onclick="treeTblExpandAll('treeTable', true);">展开全部</a>
	            <a href="javascript:;" onclick="treeTblExpandAll('treeTable', false);">缩起全部</a>
            </h5>
          </div>
          <div class="widget-content nopadding">
            <form id="searchForm" class="form-horizontal" method="post" action="${ctx }/console/sys/role">
              <table id="treeTable" class="table table-bordered">
				<thead>
					<tr>
						<th width="5%">ID</th>
						<th width="40%">菜单名称</th>
						<th width="15%">权限标识</th>
						<th>是否显示</th>
						<th width="12%">操作</th>
					</tr>
				</thead>
				<tfoot style="display:${empty list?'grid':'none' }">
					<tr><td colspan="5">没有相关记录!</td></tr>
				</tfoot>
				<tbody>
				
				<c:forEach items="${list}" var="menu">
					<tr data-id="menu-${menu.id}" id="${menu.id}" pId="${menu.pid }"  data-tt-id="${menu.id }" data-tt-parent-id="${menu.pid }">
						<td nowrap><i class="icon-${not empty menu.icon?menu.icon:' hide'}"></i>${menu.name}</td>
						<td title="${menu.link}">${fns:abbr(menu.link,40)}</td>
						<td id="permission" title="${menu.permission}">${fns:abbr(menu.permission,40)}
							<perm:hasPermission name="sys:menu:edit" sid="${sid }">&nbsp;&nbsp;
								<a href="javascript:updatePermission(${menu.id}, '${menu.permission}')">更改</a>
							</perm:hasPermission>
						</td>
						<td id="show">
							<i class="${menu.show?'icon-ok-sign green':'icon-remove-sign red'} icon-2x"></i>
							<perm:hasPermission name="sys:menu:edit" sid="${sid }">&nbsp;&nbsp;
								<c:if test="${menu.link ne 'btn'}">
									<a href="javascript:updateShow(${menu.id}, ${menu.show?false:true})">${menu.show?'隐藏':'显示'}</a>
								</c:if>
							</perm:hasPermission>
						</td>
						<perm:hasPermission name="sys:menu:edit" sid="${sid }">
						<td nowrap>
							<c:if test="${menu.link ne 'btn'}">
								<a class="btn btn-success btn-mini" href="${ctx}/console/sys/menu/form?pid=${menu.id}">添加子项</a>&nbsp;
							</c:if>
							<a class="btn btn-warning btn-mini" href="${ctx}/console/sys/menu/form?id=${menu.id}">编辑</a>&nbsp;
							<c:if test="${!menu.show and empty menu.children}">
								<a class="btn btn-danger btn-mini" href="${ctx}/console/sys/menu/delete?id=${menu.id}" onclick="return layerConfirm('您确认删除? （不可恢复）', this.href);">删除</a>&nbsp;
							</c:if>
						</td>
						</perm:hasPermission>
					</tr>
				</c:forEach>
				</tbody>
			  </table>
            </form>
          </div>
        </div>
        <!-- end 添加 -->
        
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>
<script src="${ctxStaticConsole }/js/jquery-treetable/3.2.0/jquery.treetable.min.js"></script>
<script src="${ctxStaticConsole }/js/modules/schema-common-add2.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#treeTable").treetable({expandable : true});
	});
	
	
	function updateShow(id, show){
		$.ajax({
		   url: ctx+'/console/sys/menu/updateShow',
		   type: "get",
		   data: {"id": id, "show": show},
		   dataType: "json",
		   success: function(json) {
		 		if(json.status == 200){
		 			layerTip(json.content, "success");
		 			var htm = null;
		 			if(show){
		 				htm = '<i class="icon-ok-sign green icon-2x"></i>&nbsp;&nbsp;<a href="javascript:updateShow('+id+', false)">隐藏</a>';
		 			}else{
		 				htm = '<i class="icon-remove-sign red icon-2x"></i>&nbsp;&nbsp;<a href="javascript:updateShow('+id+', true)">显示</a>';
		 			}
		 			info(id+", show:"+show);
		 			info("html:"+$("#treeTable tbody tr[data-id=menu-"+id+"] td#show").html());
		 			$("#treeTable tbody tr[data-id=menu-"+id+"] td#show").html(htm);
		 		}else{
		 			layerTip(json.content, "error");
		 			error("e:"+json.error);
		 		}
		   },
		   error: function(XMLHttpRequest, textStatus, errorThrown) {
			   layerTip("对不起，请求出错啦！", "error");
		   }
		});
	}
	
	function updatePermission(id, oldPermission){
		layerPrompt("请输入 新权限标识", function(permission){
			if(oldPermission == permission)
				return;
			$.ajax({
				   url: ctx+'/console/sys/menu/updatePermission',
				   type: "get",
				   data: {"id": id, "permission": permission},
				   dataType: "json",
				   success: function(json) {
				 		if(json.status == 200){
				 			layerTip(json.content, "success");
				 			info(id+", permission:"+permission);
				 			var htm = permission+'&nbsp;&nbsp;<a href="javascript:updatePermission('+id+', \''+permission+'\')">更改</a>';
				 			info("html:"+$("#treeTable tbody tr[data-id=menu-"+id+"] td#permission").html());
				 			$("#treeTable tbody tr[data-id=menu-"+id+"] td#permission").html(htm);
				 		}else{
				 			layerTip(json.content, "error");
				 			error("e:"+json.error);
				 		}
				   },
				   error: function(XMLHttpRequest, textStatus, errorThrown) {
					   layerTip("对不起，请求出错啦！", "error");
				   }
			});
		});
	}
</script>
	
</body>
</html>