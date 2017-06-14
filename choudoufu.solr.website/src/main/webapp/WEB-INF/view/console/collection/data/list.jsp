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
<sys:breadcrumbs currLevel="3" secondLevelTitle="应用列表" secondLevelView="/console/collection/schema/list"
threeLevelTitle="数据结果集-${schema.title}" threeLevelView="/console/collection/data/list?schemaName=${schema.name}"/>

<!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-cogs"></i> </span>
            <h5>数据结果集</h5>
            <a href="javascript:history.go(-1);"  class="fl btn head-btn">返回</a>
            <a href="${ctx }/console/collection/data/form?schemaName=${schema.name}"  class="fl btn head-btn">添加数据</a>
          </div>
          
          <div class="widget-content mg-10">
            <form id="searchForm" class="form-horizontal" method="post" action="${ctx }/console/collection/data/list?schemaName=${schema.name}">
            	<input type="hidden" name="pageNo" value="${pageNo }"/>
            	<input type="hidden" name="pageSize" value="${pageSize }"/>
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
              		  <c:set var="pk" value="${fields[schema.primaryNo].name }"/>
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
	                 	<c:if test="${!schema.canOperData }">
		                  	<a href="${ctx }/console/collection/data/form?schemaName=${schema.name}&pk=${pk}&pkVal=${doc[pk]}">编辑</a>&nbsp;&nbsp;
		                  	<c:if test="${!fns:isAdmin(sid)}">
	                 			<a href="javascript:deleteData('${schema.name}', '${pk}', '${doc[pk]}');" onclick="return layerConfirm('您确认删除? （不可恢复）', this.href);">删除</a>&nbsp;&nbsp;
	                 		</c:if>
	                 		<c:if test="${fns:isAdmin(sid)}">
			                  	<a href="javascript:;" onclick="applyAuthCode('${pk}', '${doc[pk]}', '${schema.name}', 'delete', '${pk }:${doc[pk]}');">删除</a>&nbsp;&nbsp;
	                 		</c:if>
	                  	</c:if> 
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
	function searchForm(searchFormId, pageNo, pageSize){
		var form = $("#"+searchFormId);
		form.find("input[name=pageNo]").val(pageNo);
		form.find("input[name=pageSize]").val(pageSize);
    	form.submit();
	}
	
	// 申请授权码
	function applyAuthCode(pk, pkVal, key, action, actionDesc){
		loading("正在申请授权编码...请稍等");
		$.ajax({
		    url: ctx + "/console/auth/applyAuthCode",
		    type:'POST',
		    data:{key: key, action: action, actionDesc: actionDesc},
		    timeout:2000,    //超时时间
		    dataType:'json', //返回的数据格式：json/xml/html/script/jsonp/text
		    success:function(data, status, xhr){
		    	closeloading();
		    	if(data.status == 200){
			    	layerPrompt("请输入授权编码（授权码3分钟过期）", function(authCode){
			    		log(authCode+", "+ action);
			    		if(action=="delete"){
				    		deleteData(key, pk, pkVal, authCode);
			    		}else if(action=="deleteAll"){
			    			deleteAllData(key, authCode);
			    		}
			    	});
		    		layerTip(data.content, "success", 5000, 't');
		    	}else{
		    		layerTip(data.content, "error");
		    	}
		    }
		})
	}
	
	function deleteData(schemaName, pk, pkVal, authCode){
		$.ajax({
		    url: ctx + "/console/collection/data/delete",
		    type:'POST',
		    async:true,    //或false,是否异步
		    data:{
		    	schemaName: schemaName, pk: pk, pkVal: pkVal, authCode: authCode
		    },
		    timeout:2000,    //超时时间
		    dataType:'json', //返回的数据格式：json/xml/html/script/jsonp/text
		    beforeSend:function(xhr){
		    	loading("正在处理...请稍等");
		    },
		    success:function(data, status, xhr){
		    	closeloading();
		    	if(data.status == 200){
		    		layerTip(data.content);
		    		setTimeout(function(){
		    			location.reload();
		    		}, 1500);
		    	}else{
		    		layerTip(data.content);
		    	}
		    },
		    complete:function(){
		        closeloading();
		    }
		})
	}
	
	function deleteAllData(schemaName, authCode){
		var url = ctx + "/console/collection/data/deleteAll";
		$.ajax({
		    url: url,
		    type:'POST',
		    async:true,
		    data:{
		    	schemaName: schemaName, authCode: authCode
		    },
		    timeout:2000,    //超时时间
		    dataType:'json', //返回的数据格式：json/xml/html/script/jsonp/text
		    beforeSend:function(xhr){
		    	loading("正在处理...请稍等");
		    },
		    success:function(data, status, xhr){
		    	closeloading();
		    	if(data.status == 200){
		    		layerTip(data.content);
		    		setTimeout(function(){
		    			location.reload();
		    		}, 1500);
		    	}else{
		    		layerTip(data.content);
		    	}
		    },
		    complete:function(){
		        closeloading();
		    }
		});
	}
	
</script>
</body>
</html>
