<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-控制台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<%@include file="/WEB-INF/view/console/include/cssLib.jsp" %>
	<style type="text/css">
	
		.search{
			min-width: 300px;
			margin: 5px;
		}
		.search input[type=text] {
		    background-color: #47515b;
		    color: #fff;
		    padding: 12px;
		    border: 0;
		    width: 300px;
		    font-size: 19px;
		}
		.search button {
			background-color: #28b779;
		    border: 0;
		    margin-left: -7px;
		    padding: 12px;
		    width: 60px;
		}
		.search button i {
		    opacity: 0.8;
		    color: #fff;
		}
		.search button:hover i, .search button:active i {
			opacity: 1;
		}
	</style>
</head>
<body>

<%@include file="/WEB-INF/view/console/include/head.jsp" %>

<%@include file="/WEB-INF/view/console/include/sidebar.jsp" %>

<!--main-content-part-->
<div id="content">

<sys:breadcrumbs currLevel="1" secondLevelTitle="应用列表" secondLevelView="/console/collection/data/search"/>

<!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-cogs"></i> </span>
            <h5>体验搜索</h5>
          </div>
          
          <div class="widget-content nopadding mg-10">
            <form id="listForm" class="form-horizontal" method="post" action="${ctx }/console/collection/data/search">
            	<div class="search">
	              <select name="schemaName"  onchange="changeSelectType(0)">
		            <c:forEach var="select2Vo" items="${schemaSelect2Vos }">
	                  <option value="${select2Vo.code }">${select2Vo.name }</option>
	                </c:forEach>
		          </select>
				  <input type="text" name="keyword" placeholder="关键字..."/>
				  <button type="submit" class="tip-bottom" title="搜索关键字"><i class="icon-search icon-white"></i></button>&nbsp;&nbsp;&nbsp;
				  <a href="javascript:void(0);">高级</a>
				</div>
            </form>
            
            <h5>搜索结果：</h5>
            <table class="table table-bordered data-table">
              <thead>
                <tr>
                  <th width="180">应用标识</th>
                  <th width="180">应用名称</th>
                  <th>描述</th>
                  <th width="100">创建人</th>
                  <th width="120">创建时间</th>
                  <th width="80px">是否为系统表</th>
                  <th width="15%">操作</th>
                </tr>
              </thead>
              <tbody>
              <%-- 	<c:forEach var="schema" items="${schemas }" varStatus="status">
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
	                  <td class="center">
	                  	<a href="javascript:void(0);">修改结构</a>&nbsp;&nbsp;
	                  	<a href="javascript:void(0);">数据源</a>&nbsp;&nbsp;
	                  	<a href="javascript:void(0);">重建索引</a>&nbsp;&nbsp;
	                  	<c:if test="${schema.isSys }">
	                  		<a href="javascript:deleteColl('${schema.name }')" data-confirm="您确认删除? （不可恢复）">删除</a>
	                  	</c:if>
	                  </td>
	                </tr>
              	</c:forEach> --%>
              </tbody>
            </table>
          </div>
          
        </div>
        <!-- end 集合列表 -->
        
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>

<script type="text/javascript">

</script>
</body>
</html>
