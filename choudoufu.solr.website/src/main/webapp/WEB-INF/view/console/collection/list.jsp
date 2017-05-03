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
            <a href="javascript:history.go(-1);"  class="fl btn head-btn">返回</a>
            <a href="${ctx }/console/page?path=collection/schema/add" class="fl btn head-btn width-60x"> <i class="icon-plus-sign"></i>&nbsp;新建</a>
            <a href="${ctx }/console/collection/schema/listCodeFiles" class="fl btn head-btn">&nbsp;查看代码生成</a>
          </div>
          
          <div class="widget-content nopadding">
            <form id="listForm" class="form-horizontal" method="post" action="${ctx }/console/collection/schema/">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th width="180">应用标识</th>
	                  <th width="180">应用名称</th>
	                  <th>描述</th>
	                  <th width="100">创建人</th>
	                  <th width="120">创建时间</th>
	                  <th width="80">是否为系统表</th>
	                  <th width="50">状态</th>
	                  <th width="15%">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<c:forEach var="schema" items="${schemas }" varStatus="status">
	              		<tr class="">
		                  <td>${schema.name }</td>
		                  <td>${schema.title }</td>
		                  <td>${schema.explain }</td>
		                  <td style="text-align: center">${schema.createBy }</td>
		                  <td><fmt:formatDate value="${schema.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td>
		                  <td style="text-align: center">
		                  	<c:if test="${schema.isSys }"><span class="label label-important">是</span></c:if>
		                  	<c:if test="${!schema.isSys }"><span class="label">否</span></c:if>
		                  </td>
		                  <td style="text-align: center">
		                  	<c:if test="${schema.ext.isRun }"><span class="label label-success">运行中</span></c:if>
		                  	<c:if test="${!schema.ext.isRun }"><span class="label label-important">停用中</span></c:if>
		                  </td>
		                  <td class="center">
		                  	<c:if test="${schema.isSys && fns:isAdmin(sid)}">
		                    	<a href="${ctx }/console/collection/data/list?schemaName=${schema.name }">查看数据</a>&nbsp;&nbsp;
		                  	</c:if>
		                  	<c:if test="${!schema.isSys }">
		                    	<a href="${ctx }/console/collection/data/list?schemaName=${schema.name }">查看数据</a>&nbsp;&nbsp;
			                  	<a href="${ctx }/console/collection/schema/add?schemaName=${schema.name }">修改结构</a>&nbsp;&nbsp;
			                  	<a href="javascript:void(0);">数据源</a>&nbsp;&nbsp;
			                  	<a href="javascript:reBuildIndex();">重建索引</a>&nbsp;&nbsp;
		                  		<a href="javascript:changeStatusColl('${schema.name }', ${!schema.ext.isRun })" onclick="return layerConfirm('您是否${schema.ext.isRun?'停用':'启用' }此应用？', this.href);">${schema.ext.isRun?'停用':'启用' }</a>
			                  	<c:if test="${!schema.ext.isRun }">
		                  			&nbsp;&nbsp;<a href="javascript:deleteColl('${schema.name }')" onclick="return layerConfirm('您是否确认删除？', this.href);">删除</a>
		                  		</c:if>
		                  	</c:if>
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
	
	function deleteColl(schemaName){
		$.ajax({
		   url: ctx+'/console/collection/delete',
		   type: "get",
		   data: {schemaName: schemaName},
		   dataType: "json",
		   success: function(json) {
		 		if(json.status == 200){
		 			layerTip("删除成功!", "success");
		 			setTimeout(function(){
		 				location.reload();
		 			}, 1500);
		 		}else{
		 			layerTip("删除失败，"+json.content, "error");
		 		}
		   },
		   error: function(XMLHttpRequest, textStatus, errorThrown) {
			   layerTip("对不起，请求出错啦！", "error");
		   }
		 });
	}
	
	function changeStatusColl(schemaName, run){
		$.ajax({
		   url: ctx+'/console/collection/schema/changeStatus',
		   type: "get",
		   data: {"schemaName": schemaName, "run": run},
		   dataType: "json",
		   success: function(json) {
		 		if(json.status == 200){
		 			layerTip("操作成功!", "success");
		 			setTimeout(function(){
		 				location.reload();
		 			}, 1500);
		 		}else{
		 			layerTip("操作失败，"+json.content, "error");
		 		}
		   },
		   error: function(XMLHttpRequest, textStatus, errorThrown) {
			   layerTip("对不起，请求出错啦！", "error");
		   }
		 });
	}
	
	//重建索引
	function reBuildIndex(){
		layer.open({title: "提示", content: "", btn: ["取消", "立即执行", "延迟执行"]
		  ,yes: function(index, layero){
		    //按钮【按钮一】的回调
			  console.log("click yes btn");
			  layer.close(index);
		  }
		  ,btn2: function(index, layero){
		    //按钮【按钮二】的回调
		    
			  console.log("click btn2");
			  layer.close(index);
		  }
		  ,btn3: function(index, layero){
			  layer.close(index);
			  console.log("click btn3");
		    //return false 开启该代码可禁止点击该按钮关闭
		  }
		  ,cancel: function(index){ 
			  console.log("click cancel");
			  layer.close(index);
		  }
		});
	}
</script>

</body>
</html>
