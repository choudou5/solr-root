<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-控制台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<%@include file="/WEB-INF/view/console/include/cssLib.jsp" %>
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
          <li class="active"><a href="${ctx }/console/sys/role/">角色管理</a></li>
          <li><a href="${ctx }/console/sys/role/form">添加角色</a></li>
        </ul>
        
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>添加角色 </h5>
          </div>
          <div class="widget-content nopadding">
            <form id="searchForm" class="form-horizontal" method="post" action="${ctx }/console/sys/role/">
              <table class="table table-bordered">
				<thead>
					<tr>
						<th>ID</th>
						<th width="10%">角色名</th>
						<th >描述</th>
						<th width="50">是否启用</th>
						<th>添加时间</th>
						<th>添加人</th>
						<th>操作</th>
					</tr>
				</thead>
				<tfoot style="display:${empty list?'grid':'none' }">
					<tr><td colspan="7">没有相关记录!</td></tr>
				</tfoot>
				<tbody>
				
				<c:forEach items="${list}" var="role">
					<tr data-id="${role.id}">
						<td nowrap>${role.id}</td>
						<td nowrap>${role.name}</td>
						<td title="${role.desc}">${fns:abbr(role.desc,40)}</td>
						<td id="usable" class="center">
							<i class="${role.usable?'icon-ok-sign green':'icon-remove-sign red'} icon-2x"></i>
						</td>
						<td class="center"><fmt:formatDate value="${role.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
						<td>${role.createBy}</td>
						<perm:hasPermission name="sys:role:edit" sid="${sid }">
						<td nowrap>
							<a class="btn btn-success btn-mini" href="${ctx}/console/sys/role/form?id=${role.id}&action=assign">分配权限</a>&nbsp;
							<a class="btn btn-warning btn-mini" href="${ctx}/console/sys/role/form?id=${role.id}">编辑</a>&nbsp;
							<c:if test="${!role.usable }">
								<a class="btn btn-danger btn-mini" href="${ctx}/console/sys/role/delete?id=${role.id}" onclick="return layerConfirm('您确认删除? （不可恢复）', this.href);">删除</a>&nbsp;
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
        <!-- end 添加-->
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>
<script src="${ctxStaticConsole }/js/modules/schema-common-add2.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
		
	});
	
</script>
	
</body>
</html>