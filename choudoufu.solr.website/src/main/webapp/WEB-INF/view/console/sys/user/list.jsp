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

<sys:breadcrumbs currLevel="2" secondLevelTitle="用户管理" secondLevelView="/console/sys/user/"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	<ul class="nav nav-tabs">
          <li class="active"><a href="${ctx }/console/sys/user/">用户管理</a></li>
          <li><a href="${ctx }/console/sys/user/form">添加用户</a></li>
        </ul>
        
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>用户管理 </h5>
          </div>
          <div class="widget-content nopadding">
            <form id="searchForm" class="form-horizontal" method="post" action="${ctx }/console/sys/user/">
            	<input type="hidden" name="pageNo" value="${pageNo }"/>
            	<input type="hidden" name="pageSize" value="${pageSize }"/>
            	<div class="search">
                  <input type="text" name="QF__EQ__string__loginName" value="${searchParamMap['QF__EQ__string__loginName'] }" class="need_clear" placeholder="账号"/>
                  <a id="clear_input_QF__EQ__string__loginName" href="javascript:clearInputByName('QF__EQ__string__loginName');" class="clear"></a>
				  &nbsp;
				  <input type="text" name="QF__EQ__string__realName" value="${searchParamMap['QF__EQ__string__realName'] }" class="need_clear" placeholder="姓名"/>
                  <a id="clear_input_QF__EQ__string__realName" href="javascript:clearInputByName('QF__EQ__string__realName');" class="clear"></a>
				  &nbsp;
				  <select name="QF__EQ__boolean__usable" class="input-medium">
				    <option value="">状态</option>
				  	<option value="true" ${searchParamMap['QF__EQ__boolean__usable']?'selected=\"selected\"':'' }>可用</option>
				  	<option value="false">禁用</option>
				  </select>
				  &nbsp;
				  <button type="submit" class="tip-bottom" title="搜索关键字"><i class="icon-search icon-white"></i></button>&nbsp;&nbsp;
				</div>
            </form>
            <table class="table table-bordered">
				<thead>
					<tr>
						<th>ID</th>
						<th width="10%">账号</th>
						<th>姓名</th>
						<th width="60">是否启用</th>
						<th>登录IP</th>
						<th>创建人</th>
						<th>创建时间</th>
						<th>上次登录时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tfoot style="display:${empty page.result?'grid':'none' }">
					<tr><td colspan="9">没有相关记录!</td></tr>
				</tfoot>
				
				<tbody>
				<c:forEach items="${page.result}" var="user">
					<tr data-id="${user.id}">
						<td nowrap>${user.id}</td>
						<td nowrap>${user.loginName}</td>
						<td title="${user.realName}">${user.realName}</td>
						<td id="usable" class="center">
							<i class="${user.usable?'icon-ok-sign green':'icon-remove-sign red'} icon-2x"></i>
						</td>
						<td>${user.ip}</td>
						<td>${user.createBy}</td>
						<td class="center"><fmt:formatDate value="${user.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
						<td class="center"><fmt:formatDate value="${user.loginTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
						<td nowrap>
							<perm:hasPermission name="sys:user:edit" sid="${sid }">
								<a class="btn btn-success btn-mini" href="${ctx}/console/sys/user/form?id=${user.id}">编辑</a>&nbsp;
								<c:set var="actionTit" value="${user.usable?'禁用':'启用'}"  />
								<a class="btn btn-warning btn-mini" href="${ctx}/console/sys/user/updateUsable?id=${user.id}&usable=${!user.usable}" onclick="return layerConfirm('您确认<b>${actionTit} ${user.loginName }</b> ? ', this.href);">${actionTit }</a>&nbsp;
							</perm:hasPermission>
						</td>
					</tr>
				</c:forEach>
				</tbody>
		  	</table>
			<sys:paging page="${page }"/>
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