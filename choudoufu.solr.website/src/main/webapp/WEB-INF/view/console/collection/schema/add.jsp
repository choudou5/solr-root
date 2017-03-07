<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-控制台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<%@include file="/WEB-INF/view/console/include/cssLib.jsp" %>
	<link rel="stylesheet" href="${ctxStaticConsole }/css/uniform.css" />
	<link rel="stylesheet" href="${ctxStaticConsole }/css/select2.css" />
	<style type="text/css">
		.table tbody>tr td:nth-child(n+5){
			text-align: center;
			width: 70px;
		}
		.table tbody>tr td:nth-child(1){
			text-align: center;
			width: 50px;
		}
	</style>
</head>
<body>

<%@include file="/WEB-INF/view/console/include/head.jsp" %>

<%@include file="/WEB-INF/view/console/include/sidebar.jsp" %>

<!--main-content-part-->
<div id="content">

<sys:breadcrumbs currLevel="2" secondLevelTitle="添加集合" secondLevelView="/console/page?path=collection/schema/add"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      
      	<!-- begin 添加集合 -->
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>添加集合</h5>
          </div>
          <div class="widget-content nopadding">
            <form id="form-wizard" class="form-horizontal" method="post">
              <div id="form-wizard-1" class="step">
                <div class="control-group">
                  <label class="control-label">模块标识</label>
                  <div class="controls">
                    <input type="text" name="id" placeholder="由 字母、下划线、或数字组成" maxlength="30"/>
                  </div>
                </div>
                <div class="control-group">
                  <label class="control-label">模块名称</label>
                  <div class="controls">
                    <input type="text" name="title" class="span3" placeholder="请输入模块名称" maxlength="10"/>
                  </div>
                </div>
                <div class="control-group">
                  <label class="control-label">说明</label>
                  <div class="controls">
                    <textarea name="explain" class="span5" rows="3" maxlength="120" placeholder="请简单描述您的模块"></textarea>
                  </div>
                </div>
                <div class="control-group">
                  <label class="control-label">首页图标</label>
                  <div class="controls">
                    <!-- <input type="text" name="icon" maxlength="20" /> -->
                    <div class="input-append">
	                  <input type="text" name="icon" class="span11" readonly="readonly">
	                  <span class="add-on"><i class="icon-th"></i></span>
	                </div>
                  </div>
                  
                </div>
              </div>
              <div id="form-wizard-2" class="step">
              
              
                <div class="widget-box">
		          <div class="widget-title"> <span class="icon"> <i class="icon-cogs"></i> </span>
		            <h5>定义结构</h5>
		            <span class="label label-none"><a href="javascript:void(0);">配置说明</a></span> 
		          </div>
		          <div class="widget-content nopadding">
		            <c:set var="fieldTypes" value="${fns:getFieldTypeMap()}" />
		            <table id="fieldTable" class="table table-bordered table-striped">
		              <thead>
		              	<%@include file="/WEB-INF/view/console/collection/schema/table-thead.jsp" %>
		              </thead>
		              <tbody>
		                <tr id="templateTr">
		                  <td>1</td>
		                  <td>
							<input id="name" type="text" name="fields[0].name" placeholder="字母 或 下划线 组成" maxlength="10"/>
						  </td>
						  <td>
							<input id="label" type="text" name="fields[0].label" placeholder="简单描述" maxlength="10"/>
						  </td>
						  <td>
							<select id="type" name="fields[0].type">
							  <c:forEach var="fieldType" items="${fieldTypes }">
							  	<option value="${fieldType.key }">${fieldType.value }</option>
							  </c:forEach>
			                </select>
						  </td>
		                  <td>
		                  	<input id="indexed" type="checkbox" name="fields[0].indexed" value="true"/>
						  </td>
		                  <td>
		                  	<input id="stored" type="checkbox" name="fields[0].stored" value="true"/>
						  </td>
		                  <td>
		                  	<input id="required" type="checkbox" name="fields[0].required" value="true"/>
						  </td>
		                  <td>
		                  	<input id="multiValued" type="checkbox" name="fields[0].multiValued" value="true"/>
						  </td>
		                  <td>
		                  	<input id="isListShow" type="checkbox" name="fields[0].isListShow" value="true"/>
						  </td>
		                  <td>
		                  	<input id="isListSearch" type="checkbox" name="fields[0].isListSearch" value="true"/>
						  </td>
		                  <td>
		                	<input id="isListShow" type="radio" name="fields[0].isListShow" value="true"/>&nbsp;
                  		  </td>
                  		  <td>
		                	<a href="javascript:removeFieldRow(this);">删除</a>
                  		  </td>
		                </tr>
		                <tr id="addFieldBtns">
		                	<td colspan="12" style="text-align: center;">
		                		<a href="javascript:addFieldRow();">马上添加</a>
		                	</td>
		                </tr>
		              </tbody>
		            </table>
		            <select id="tplFieldType" class="hide">
					  <c:forEach var="fieldType" items="${fieldTypes }">
					  	<option value="${fieldType.key }">${fieldType.value }</option>
					  </c:forEach>
	                </select>
		          </div>
		        </div>
                
                
              </div>
              <div class="form-actions">
                <input id="back" class="btn btn-primary" type="reset" value="上一步" />
                <input id="next" class="btn btn-primary" type="submit" value="下一步" />
                <div id="status"></div>
              </div>
              <div id="submitted"></div>
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
<script src="${ctxStaticConsole }/js/modules/schema-add.js"></script>

<script type="text/javascript">
	function addFieldRow(){
		var newIndex = $("#fieldTable tbody tr").length-1;
		
		var tpl = new StringBuffer();
		tpl.append('<tr>');
		tpl.append('<td>'+(newIndex+1)+'</td>');
		tpl.append('<td><input id="name" type="text" name="fields['+newIndex+'].name" placeholder="字母 或 下划线 组成" maxlength="10"/></td>');
		tpl.append('<td><input id="label" type="text" name="fields['+newIndex+'].label" placeholder="简单描述" maxlength="10"/></td>');
		
		var fieldTypeTpl = $("#tplFieldType").clone();
		tpl.append('<td><select id="type" name="fields['+newIndex+'].stored">'+fieldTypeTpl.html()+'</select></td>');
		tpl.append('<td><input id="indexed" type="checkbox" name="fields['+newIndex+'].indexed" value="true"/></td>');
		tpl.append('<td><input id="stored" type="checkbox" name="fields['+newIndex+'].stored" value="true"/></td>');
		tpl.append('<td><input id="required" type="checkbox" name="fields['+newIndex+'].required" value="true"/></td>');
		tpl.append('<td><input id="multiValued" type="checkbox" name="fields['+newIndex+'].multiValued" value="true"/></td>');
		tpl.append('<td><input id="isListShow" type="checkbox" name="fields['+newIndex+'].isListShow" value="true"/></td>');
		tpl.append('<td><input id="isListSearch" type="checkbox" name="fields['+newIndex+'].isListSearch" value="true"/></td>');
		tpl.append('<td><input id="isListShow" type="checkbox" name="fields['+newIndex+'].isListShow" value="true"/></td>');
		tpl.append('<td><a href="javascript:removeFieldRow(this);">删除</a></td> ');
		tpl.append('</tr>');
		$("#addFieldBtns").before(tpl.toString());
		
		$("#fieldTable tbody tr:eq("+newIndex+") td select").select2();
		
		//fieldTypeTpl.select2();
	}
	
	
	function removeFieldRow(thi){
		
		$(thi).closest("tr").remove();
	}
</script>
</body>
</html>
