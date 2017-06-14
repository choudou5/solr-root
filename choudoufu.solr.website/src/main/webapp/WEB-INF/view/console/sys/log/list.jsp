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

<sys:breadcrumbs currLevel="2" secondLevelTitle="日志管理" secondLevelView="/console/sys/log/list"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	<ul class="nav nav-tabs">
          <li class="active"><a href="${ctx }/console/sys/log/list">日志管理</a></li>
        </ul>
        
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>日志管理 </h5>
          </div>
          <div class="widget-content nopadding">
            <form id="searchForm" class="form-horizontal" method="post" action="${ctx }/console/sys/log/list">
            	<input type="hidden" name="pageNo" value="${pageNo }"/>
            	<input type="hidden" name="pageSize" value="${pageSize }"/>
            	<div class="search">
                  <input type="text" name="QF__EQ__string__loginName" value="${searchParamMap['QF__EQ__string__loginName'] }" class="need_clear" placeholder="操作者"/>
                  <a id="clear_input_QF__EQ__string__loginName" href="javascript:clearInputByName('QF__EQ__string__loginName');" class="clear"></a>
				  &nbsp;
				  <input type="text" name="QF__EQ__string__action" value="${searchParamMap['QF__EQ__string__action'] }" class="need_clear" placeholder="操作类型"/>
                  <a id="clear_input_QF__EQ__string__action" href="javascript:clearInputByName('QF__EQ__string__action');" class="clear"></a>
				  &nbsp;
				  <select name="QF__EQ__string__method" class="input-medium">
				    <option value="">操作方式</option>
				  	<option value="GET" ${searchParamMap['QF__EQ__string__method'] eq 'GET'?'selected=\"selected\"':'' }>GET</option>
				  	<option value="POST" ${searchParamMap['QF__EQ__string__method'] eq 'POST'?'selected=\"selected\"':'' }>POST</option>
				  </select>
				  &nbsp;
				  <button type="submit" class="tip-bottom" title="搜索关键字"><i class="icon-search icon-white"></i></button>&nbsp;&nbsp;
				</div>
            </form>
            <table class="table table-bordered">
				<thead>
					<tr>
						<th width="50">ID</th>
						<th width="10%">标题</th>
						<th>操作者</th>
						<th>操作类型</th>
						<th width="">操作时间</th>
						<th>IP</th>
						<th>操作内容</th>
					</tr>
				</thead>
				<tfoot style="display:${empty page.result?'grid':'none' }">
					<tr><td colspan="9">没有相关记录!</td></tr>
				</tfoot>
				<tbody>
				<c:forEach items="${page.result}" var="log">
					<tr data-id="${log.id}">
						<td nowrap>${log.id}</td>
						<td nowrap>${log.title}</td>
						<td title="">${log.loginName}</td>
						<td>${log.action}</td>
						<td class="center"><fmt:formatDate value="${log.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
						<td>${log.ip}</td>
						<td class="">
							<p><span class="green">请求地址：</span>${log.url }</p>
							<p><span class="green">操作方式：</span>${log.method }</p>
							<p><span class="green">提交的数据：</span>${log.params }</p>
							<p><span class="green">代理信息：</span>${log.userAgent }</p>
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