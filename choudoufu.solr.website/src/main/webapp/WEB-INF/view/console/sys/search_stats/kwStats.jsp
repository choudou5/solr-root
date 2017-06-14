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

<sys:breadcrumbs currLevel="2" secondLevelTitle="搜索记录" secondLevelView="/console/sys/search_stats/"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	<ul class="nav nav-tabs">
          <li><a href="${ctx }/console/sys/search_stats/">搜索记录</a></li>
          <li class="active"><a href="${ctx }/console/sys/search_stats/kwStats">关键字统计列表</a></li>
        </ul>
        
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>关键字统计列表 </h5>
          </div>
          <div class="widget-content nopadding">
            <form id="searchForm" class="form-horizontal" method="post" action="${ctx }/console/sys/search_stats/kwStats">
            	<input type="hidden" name="pageNo" value="${pageNo }"/>
            	<input type="hidden" name="pageSize" value="${pageSize }"/>
            	<div class="search">
                  <input type="text" name="QF__EQ__string__keyword" value="${searchParamMap['QF__EQ__string__keyword'] }" class="need_clear" placeholder="账号"/>
                  <a id="clear_input_QF__EQ__string__keyword" href="javascript:clearInputByName('QF__EQ__string__keyword');" class="clear"></a>
				  &nbsp;
				  <select name="QF__EQ__string__platform" class="input-medium">
				    <option value="">状态</option>
				  	<option value="web" ${searchParamMap['QF__EQ__string__platform'] eq 'web'?'selected=\"selected\"':'' }>Web</option>
				  	<option value="android" ${searchParamMap['QF__EQ__string__platform'] eq 'android'?'selected=\"selected\"':'' }>Android</option>
				  	<option value="ios" ${searchParamMap['QF__EQ__string__platform'] eq 'ios'?'selected=\"selected\"':'' }>IOS</option>
				  </select>
				  &nbsp;
				  <button type="submit" class="tip-bottom" title="搜索关键字"><i class="icon-search icon-white"></i></button>&nbsp;&nbsp;
				</div>
            </form>
            <table class="table table-bordered">
				<thead>
					<tr>
						<th width="10%">应用</th>
						<th>关键字 </th>
						<th>搜索次数</th>
					</tr>
				</thead>
				<tfoot style="display:${empty page.result?'grid':'none' }">
					<tr><td colspan="3">没有相关记录!</td></tr>
				</tfoot>
				
				<tbody>
				<c:forEach items="${page.result}" var="searchStats">
					<tr>
						<td>${searchStats.schemaName}</td>
						<td>${searchStats.keyword}</td>
						<td class="center">${searchStats.counts}</td>
					</tr>
				</c:forEach>
				</tbody>
		  	</table>
			<sys:paging page="${page }"/>
          </div>
        </div>
        <!-- end-->
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>
	
</body>
</html>