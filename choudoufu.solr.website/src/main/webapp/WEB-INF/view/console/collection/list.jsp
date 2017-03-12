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
		.table tbody>tr td:nth-child(n+6){text-align: center;width: 70px;}
		.table tbody>tr td:nth-child(1){text-align: center;width: 50px;}
		#filterDiv{width: 60px;padding: 5px;}
		.formwizard-steps{width: 100%}
		.formwizard-steps li{width: 150px}
	</style>
</head>
<body>

<%@include file="/WEB-INF/view/console/include/head.jsp" %>

<%@include file="/WEB-INF/view/console/include/sidebar.jsp" %>

<!--main-content-part-->
<div id="content">

<sys:breadcrumbs currLevel="2" secondLevelTitle="创建集合" secondLevelView="/console/page?path=collection/schema/add"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	<!-- begin 添加集合 -->
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>添加集合</h5>
          </div>
          <div class="widget-content nopadding">
            <form id="addTableForm" class="form-horizontal" method="post" action="${ctx }/console/collection/schema/save">
              
              <ol class="formwizard-steps">
				<li class="active current"><span class="label">1</span>填写基本信息</li>
				<li><span class="label">2</span>定义索引结构</li>
				<li><span class="label">3</span>创建成功</li>
			  </ol>
              
              <div id="form-wizard-1" class="step">
                <div class="control-group">
                  <label class="control-label">模块标识</label>
                  <div class="controls">
                    <input type="text" name="id" placeholder="由 字母、下划线、或数字组成" maxlength="30" value="test"/>
                  </div>
                </div>
                <div class="control-group">
                  <label class="control-label">模块名称</label>
                  <div class="controls">
                    <input type="text" name="title" class="span3" placeholder="请输入模块名称" maxlength="10" value="test..."/>
                  </div>
                </div>
                <div class="control-group">
                  <label class="control-label">说明</label>
                  <div class="controls">
                    <textarea name="explain" class="span5" rows="3" maxlength="120" placeholder="请简单描述您的模块"></textarea>
                  </div>
                </div>
              </div>
              <div id="form-wizard-2" class="step hide">
                <div class="widget-box">
		          <div class="widget-title">
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
		                  <td><input class="required isVariable" type="text" name="fields[0].name" placeholder="字母 或 下划线 组成" maxlength="10"/> </td>
						  <td>
							<input class="required" type="text" name="fields[0].label" placeholder="简单描述" minlength="2" maxlength="10"/>
						  </td>
						  <td>
							<select id="selectType" name="fields[0].type">
							  <c:forEach var="fieldType" items="${fieldTypes }">
							  	<option value="${fieldType.key }">${fieldType.value }</option>
							  </c:forEach>
			                </select>
						  </td>
						  <td>
						  	<div id="filterDiv">
							  	<input id="filters" type="hidden" name="fields[0].filters" value=""/>
							  	<a id="chooseFilterModalBtn" href="javascript:chooseFilterModal(0)">&nbsp;<i class="icon icon-plus"></i>&nbsp;</a>
							  	<span id="fltersTagCount" class="badge badge-info"></span>
						  	</div>
						  </td>
		                  <td><input type="checkbox" name="fields[0].indexed" value="true"/></td>
		                  <td><input type="checkbox" name="fields[0].stored" value="true"/></td>
		                  <td><input type="checkbox" name="fields[0].required" value="true"/></td>
		                  <td><input type="checkbox" name="fields[0].multiValued" value="true"/></td>
		                  <td><input type="checkbox" name="fields[0].isListShow" value="true"/></td>
		                  <td><input type="checkbox" name="fields[0].isListSearch" value="true"/></td>
		                  <td><input type="radio" name="isPrimaryKey" value="0" class="required"/></td>
                  		  <td></td>
		                </tr>
		                <tr id="addFieldBtns">
		                	<td colspan="13" style="text-align: center;">
		                		<a href="javascript:addFieldRow();">添加</a>
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
        
        
       <!-- 分词说明 -->
       <%@include file="/WEB-INF/view/console/collection/schema/word-segmentation-desc.jsp" %>
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<div id="chooseFilterModal" class="hide">
  <div id="filterTemplate" class="modal-body">
    <p><input type="checkbox" name="filter" value="html"/>HTML过滤器</p>
    <p><input type="checkbox" name="filter" value="lowerCase"/>小写转换过滤器</p>
    <p><input type="checkbox" name="filter" value="stop"/>停词过滤器</p>
    <p><input type="checkbox" name="filter" value="synonym"/>同义词过滤器</p>
    <p><input type="checkbox" name="filter" value="englishPossessive"/>EnglishPossessive过滤器</p>
    <p><input type="checkbox" name="filter" value="removeDuplicatesToken"/>RemoveDuplicatesToken过滤器</p>
    <p><input type="checkbox" name="filter" value="trim"/>空格过滤器</p>
  </div> 
</div>

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>
<%@include file="/WEB-INF/view/console/include/scriptValidLib.jsp" %>
<script src="${ctxStaticConsole }/js/modules/schema-add.js"></script>
</body>
</html>
