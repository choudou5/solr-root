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

<sys:breadcrumbs currLevel="1" secondLevelTitle="应用列表" secondLevelView="/console/collection/data/search"/>

<!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-cogs"></i> </span>
            <h5>数据结果集</h5>
          </div>
          
          <div class="widget-content mg-10">
            <form id="searchForm" class="form-horizontal" method="post" action="${ctx }/console/collection/data/list?schemaName=${schema.name}">
            	<input type="hidden" name="pageNo"/>
            	<input type="hidden" name="pageSize"/>
            	<div class="search">
				   <c:forEach items="${fields }" var="field" varStatus="status">
	                  <c:if test="${field.isListSearch }">
	                    <c:set var="searchField" value="QF__EQ__${field.type.name }__${field.name}"/>
	                  	<input type="text" name="${searchField }" value="${searchParamMap[searchField] }" class="need_clear" placeholder="${field.label }"/>
	                  	<a id="clear_input_${searchField }" href="javascript:clearInputByName('${searchField }');" class="clear"></a>
	                  </c:if>
	               </c:forEach>
				  <button type="submit" class="tip-bottom" title="搜索关键字"><i class="icon-search icon-white"></i></button>&nbsp;&nbsp;&nbsp;
				</div>
            </form>
            <hr>
            
            <sys:paging page="${page }"/>
            
            <c:set var="showlistFieldCount" value="1"/>
            <table class="table table-bordered data-table">
              <thead>
                <tr>
                 <c:forEach items="${fields }" var="field" varStatus="status">
                 	<c:if test="${field.isListShow }">
	                  <th style="max-width: 400px;" title="${field.label }">${empty field.label?field.name:field.label }</th>
	                  <c:set var="showlistFieldCount" value="${showlistFieldCount+1 }"/>
	                </c:if>
                 </c:forEach>
                  <th width="120" style="width: 120px;">操作</th>
                </tr>
              </thead>
              <tbody>
              <c:forEach var="doc" items="${page.result }" varStatus="status">
              		<tr class="">
              		  <c:forEach items="${fields }" var="field" varStatus="status">
             		  	  <c:if test="${field.isListShow }">
		                    <td>
		                      	<p style="max-width: 400px;word-wrap:break-word;${field.type.name eq 'boolean'?'text-align:center;':'' }" fieldType="${field.type.name }">
			                      <c:if test="${field.type.name ne 'boolean' && field.type.name ne 'date'}">
			                      	${doc[field.name] }
			                      </c:if>
			                      <%--boolean类型 --%>
			                      <c:if test="${field.type.name eq 'boolean' }">
			                      	<span class="label ${doc[field.name]?'label-success':'label-important' }">${doc[field.name] }</span>
			                      </c:if>
			                      <%--date类型 --%>
			                      <c:if test="${field.type.name eq 'date' }">
			                      	<fmt:formatDate value="${doc[field.name] }" pattern="yyyy-MM-dd HH:mm:ss"/>
			                      </c:if>
		                      	</p>
		                    </td>
		                  </c:if>
	                  </c:forEach>
	                  <td class="center" style="width: 120px;">
	                  	<a href="javascript:void(0);">编辑</a>&nbsp;&nbsp;
	                  	<a href="javascript:void(0);" data-confirm="您确认删除? （不可恢复）">删除</a>&nbsp;&nbsp;
	                  	<%-- <c:if test="${schema.isSys }">
	                  		<a href="javascript:deleteColl('${schema.name }')" data-confirm="您确认删除? （不可恢复）">删除</a>
	                  	</c:if> --%>
	                  </td>
	                </tr>
              	</c:forEach>
              	
              	<c:if test="${empty page.result }">
             	 <tr>
                    <td colspan="${showlistFieldCount }" style="text-align: center;">没有搜索到相关记录! </td>
                  </td>
                 </tr>
              	</c:if>
              	
              </tbody>
            </table>
            
            <sys:paging page="${page }"/>
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
	$(document).ready(function() {
		new jBox('Confirm', {
			confirmButton: '确定',
			cancelButton: '取消',
		});
	});
	
	function searchForm(searchFormId, pageNo, pageSize){
		var form = $("#"+searchFormId);
		form.find("input[name=pageNo]").val(pageNo);
		form.find("input[name=pageSize]").val(pageSize);
    	form.submit();
	}
</script>
</body>
</html>
