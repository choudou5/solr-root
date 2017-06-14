<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-控制台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<%@include file="/WEB-INF/view/console/include/cssLib.jsp" %>
	<%@include file="/WEB-INF/view/console/include/cssFormLib.jsp" %>
</head>
<body>

<%@include file="/WEB-INF/view/console/include/head.jsp" %>

<%@include file="/WEB-INF/view/console/include/sidebar.jsp" %>

<!--main-content-part-->
<div id="content">

<sys:breadcrumbs currLevel="3" secondLevelTitle="应用列表" secondLevelView="/console/collection/data/list?schemaName=${schema.name}"
threeLevelTitle="${schema.title }" threeLevelView="/console/collection/data/form?schemaName=${schema.name}"
/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	
      	<!-- begin 添加集合 -->
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>添加数据</h5>
          </div>
          <div class="widget-content nopadding">
            <form id="dataSaveForm" class="form-horizontal" action="${ctx }/console/collection/data/save?schemaName=${schema.name}" method="post">
              <div id="form-wizard-1">
                
                <c:set var="pk" value="${fields[schema.primaryNo].name }"/>
                <c:forEach items="${fields }" var="field" varStatus="status">
                 	<c:if test="${field.stored }">
                 		<c:set var="isPk" value="${pk eq field.name }"/>
                 		<c:set var="fieldValLength" value="${fn:length(fn:escapeXml(inputDto.inputMap[field.name])) }"/>
                 		<c:set var="fieldValLength" value="${empty fieldValLength?10:fieldValLength }"/>
                 		<c:set var="isTextarea" value="true"/>
                 		
                 		<%--忽略字段 --%>
                 		<c:set var="isIgnoreField" value="false"/>
	                    <c:forEach var="defFiledName" items="${fns:getFormIgnoreFieldNameByType(field.type.name) }">  
						    <c:if test="${defFiledName eq field.name}">  
						       <c:set var="isIgnoreField" value="true"/>
						    </c:if>  
						</c:forEach> 
								
                 		<div class="control-group ${isIgnoreField?'hide':'' }">
		                  <label class="control-label">
		                  	${empty field.label?field.name:field.label }
		                  	<c:if test="${field.required }"><span class="red">*</span></c:if>
		                  </label>
		                  
		                  <div class="controls">
		                    <c:if test="${field.type.name eq 'string' }">
		                    	<c:if test="${fieldValLength > 20 }">
		                    		<textarea rows="3" cols="12" name="inputMap['${field.name }']" class="input-xlarge ${field.required?'required':'' }">${fn:escapeXml(inputDto.inputMap[field.name]) }</textarea>
		                    	</c:if>
		                    	<c:if test="${fieldValLength <= 20 }">
		                    		<!-- 多值 -->
		                    		<c:if test="${field.multiValued }">
		                    			<c:if test="${empty inputDto.inputMap[field.name] }">
		                    				<label id="${field.name }_0">
			                    				<input type="text" name="inputMap['${field.name }']" class="multi_val input-xlarge ${field.required?'required':'' }" value="" />
				                    			&nbsp;<a href="javascript:;" title="添加" onclick="addInput('${field.name }', 0)"><i class="icon-plus"></i></a>
			                    			</label>
		                    			</c:if>
		                    			<c:forEach items="${inputDto.inputMap[field.name] }" var="val" varStatus="vstatus">
		                    				<label id="${field.name }_${vstatus.index }">
			                    				<input type="text" name="inputMap['${field.name }']" class="multi_val input-xlarge ${field.required?'required':'' }" value="${val }" />
			                    				<c:if test="${vstatus.index eq 0 }">
													&nbsp;<a href="javascript:;" title="添加" onclick="addInput('${field.name }', ${vstatus.index })"><i class="icon-plus"></i></a>
					                    		</c:if>
					                    		<c:if test="${vstatus.index ne 0 }">
													&nbsp;<a href="javascript:;" title="移除" onclick="delInput('${field.name }_${vstatus.index }')"><i class="icon-minus"></i></a>
					                    		</c:if>
		                    				</label>
		                    			</c:forEach>
		                    		</c:if>
		                    		<c:if test="${!field.multiValued }">
		                    			<input type="text" name="inputMap['${field.name }']" <c:if test="${isPk && !inputDto.isNew}">readonly="readonly"</c:if> class="input-xlarge ${field.required?'required':'' }" value="${fn:escapeXml(inputDto.inputMap[field.name]) }" />
		                    		</c:if>
		                    	</c:if>
		                    	<c:set var="isTextarea" value="false"/>
		                    </c:if>
		                    <c:if test="${field.type.name eq 'boolean' }">
		                    	<span class=""><input type="checkbox" name="inputMap['${field.name }']" <c:if test="${inputDto.inputMap[field.name] }">checked="checked"</c:if> class="${field.required?'required':'' }" value="true"></span>
		                    	<c:set var="isTextarea" value="false"/>
		                    </c:if>
		                    <c:if test="${field.type.name eq 'date' || field.type.name eq 'tdate'}">
		                    	<div data-date="" class="input-append date datepicker">
				                  <input type="datetime" name="inputMap['${field.name }']" class="input-xlarge ${field.required?'required':'' }" value="<fmt:formatDate value="${inputDto.inputMap[field.name] }" pattern="yyyy-MM-dd HH:mm:ss" />" data-date-format="yyyy-MM-dd HH:mm:ss" >
				                  <span class="add-on"><i class="icon-th"></i></span> 
				                </div>
				                <c:set var="isTextarea" value="false"/>
		                    </c:if>
		                    <c:if test="${field.type.name eq 'int' || field.type.name eq 'long' || field.type.name eq 'float' || field.type.name eq 'double' }">
		                    	<input type="text" name="inputMap['${field.name }']" <c:if test="${isPk && !inputDto.isNew}">readonly="readonly"</c:if> value="${inputDto.inputMap[field.name] }" class="${field.required?'required':'' }"/>
		                    	<c:set var="isTextarea" value="false"/>
		                    </c:if>
		                    <c:if test="${isTextarea }">
		                    	<textarea rows="3" cols="12" name="inputMap['${field.name }']" class="input-xlarge ${field.required?'required':'' }">${fn:escapeXml(inputDto.inputMap[field.name]) }</textarea>
		                    </c:if>
		                    
		                    <c:if test="${isPk && inputDto.isNew }">
		                    	&nbsp;<span class="help-inline-tip">注意：主键（添加已存在的 则会覆盖数据哦）</span>
		                    </c:if>
		                  </div>
		                </div>
	                </c:if>
                 </c:forEach>
                 
              <div class="form-actions">
              	<label class="control-label"></label>
                 <div class="controls">
                 	 <a href="${ctx }/console/collection/data/list?schemaName=${schema.name}"  class="btn">返回</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                 <input class="btn btn-danger" type="submit" value="提交" />
	                 <input type="hidden" name="isNew" value="${inputDto.isNew }" />
                 </div>
              </div>
            </form>
          </div>
        </div>
        <!-- end 添加集合 -->
        
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>
<%@include file="/WEB-INF/view/console/include/scriptValidLib.jsp" %>

<script type="text/javascript">
	$(document).ready(function(){
		$("#dataSaveForm").validate({
			errorClass: "help-inline",
			errorElement: "span",
			highlight:function(element, errorClass, validClass) {
				$(element).parents('.control-group').addClass('error');
			},
			unhighlight: function(element, errorClass, validClass) {
				$(element).parents('.control-group').removeClass('error');
				$(element).parents('.control-group').addClass('success');
			}
		});
	});
	
	//添加 input框
	function addInput(fieldName, index){
		var thisLabel = $("#"+fieldName+"_"+index);
		var thisIndex = $(thisLabel).index();
		var subCount = thisLabel.siblings().length+1;
		var str = new StringBuffer();
		str.append('<label id="'+fieldName+'_'+subCount+'">');
		str.append('<input type="text" name="inputMap[\''+fieldName+'\']" class="multi_val input-xlarge required}" value="" />');
		str.append('&nbsp;&nbsp;<a href="javascript:;" title="移除" onclick="delInput(\''+fieldName+'_'+subCount+'\')"><i class="icon-minus"></i></a>');
		str.append('</lable>');
		var parent = thisLabel.parent();
		parent.append(str.toString());
	}
	
	function delInput(tagId){
		$("label#"+tagId).remove();
	}
</script>
</body>
</html>
