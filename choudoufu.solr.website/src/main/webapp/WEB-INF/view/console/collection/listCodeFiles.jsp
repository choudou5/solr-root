<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-代码文件列表</title>
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
<sys:breadcrumbs currLevel="2" secondLevelTitle="应用列表" threeLevelTitle="代码文件列表" secondLevelView="/console/collection/list"/>


  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-cogs"></i> </span>
            <h5>代码文件列表</h5>
            <a href="javascript:history.go(-1);"  class="fl btn head-btn">返回</a>
            
            <a href="${ctx }/console/collection/schema/downloadCode?type=zip" class="fl btn head-btn"> <i class="icon-download-alt"></i>&nbsp;打包zip</a>
          </div>
          
          <div class="widget-content nopadding">
            <table class="table table-bordered data-table">
              <thead>
                <tr> <th>文件地址</th> </tr>
              </thead>
              <tbody>
              <c:forEach var="path" items="${listCodePath }" >
              		<tr class="">
	                    <td>
	                      	${path }
	                      	&nbsp;&nbsp;
	                      	<a href="${ctx }/console/collection/schema/downloadCode?type=file&fileName=${fns:substringAfterLast(path, '\\')}">下载</a>
	                    </td>
	                </tr>
              </c:forEach>
              	
              <c:if test="${empty listCodePath }">
           	  	<tr><td style="text-align: center;">没有相关记录!</td></tr>
              </c:if>
              	
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
</body>
</html>
