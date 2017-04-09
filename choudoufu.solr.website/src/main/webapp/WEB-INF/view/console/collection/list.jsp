<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-控制台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<%@include file="/WEB-INF/view/console/include/cssLib.jsp" %>
	<link rel="stylesheet" href="${ctxStaticConsole }/css/select2.css" />
	<style type="text/css">
		/* .table tbody>tr td:nth-child(n+6){text-align: center;width: 70px;} */
	</style>
</head>
<body>

<%@include file="/WEB-INF/view/console/include/head.jsp" %>

<%@include file="/WEB-INF/view/console/include/sidebar.jsp" %>

<!--main-content-part-->
<div id="content">
<sys:breadcrumbs currLevel="2" secondLevelTitle="应用列表" secondLevelView="/console/collection/list"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-cogs"></i> </span>
            <h5>应用管理</h5>
            <a href="${ctx }/console/page?path=collection/schema/add" class="fl label label-info"> <i class="icon-plus-sign"></i>&nbsp;新建</a>
          </div>
          
          <div class="widget-content nopadding">
            <form id="listForm" class="form-horizontal" method="post" action="${ctx }/console/collection/schema/">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>应用标识</th>
	                  <th>应用名称</th>
	                  <th>创建人</th>
	                  <th>创建时间</th>
	                  <th width="15%">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<c:forEach var="schema" items="${schemas }" varStatus="status">
	              		<tr class="">
		                  <td>${schema.name }</td>
		                  <td>${schema.title }</td>
		                  <td>${schema.createBy }</td>
		                  <td>${schema.updateBy }</td>
		                  <td class="center">
		                  	<a href="javascript:void(0);">修改结构</a>&nbsp;&nbsp;
		                  	<a href="javascript:void(0);">数据源</a>&nbsp;&nbsp;
		                  	<a href="javascript:void(0);">重建索引</a>&nbsp;&nbsp;
		                  	<a href="javascript:deleteColl('${schema.name }')" data-confirm="您确认删除? （不可恢复）">删除</a>
		                  </td>
		                </tr>
	              	</c:forEach>
	              </tbody>
	            </table>
            </form>
          </div>
          
        </div>
        <!-- end 集合列表 -->
        
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>
<script src="${ctxStaticConsole }/js/select2.min.js"></script>
<script src="${ctxStaticConsole }/js/jquery.dataTables.min.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.tables.js"></script>

<script>
	$(document).ready(function() {
		new jBox('Confirm', {
			confirmButton: '确定',
			cancelButton: '取消',
		});
	});
	
	function deleteColl(schemaName){
		
		$.ajax({
		   url: ctx+'/console/collection/delete',
		   type: "get",
		   data: {schemaName: schemaName},
		   dataType: "json",
		   success: function(json) {
		 		if(json.status == 200){
		 			jNoticeCenter("删除成功!");
		 			setTimeout(function(){
		 				location.reload();
		 			}, 1500);
		 		}else{
		 			jNoticeCenter("删除失败，"+json.content, 'red');
		 		}
		   },
		   error: function(XMLHttpRequest, textStatus, errorThrown) {
			   jNoticeCenter("对不起，请求出错啦！", 'red');
		   }
		 });
	}
</script>

</body>
</html>
