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

<sys:breadcrumbs currLevel="2" threeLevelTitle="创建应用" secondLevelTitle="应用列表" secondLevelView="/console/collection/schema/list"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      	<sys:message content="${message }"/>
      	<!-- begin 添加应用 -->
      	<div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-pencil"></i> </span>
            <h5>添加应用</h5>
          </div>
          <div class="widget-content nopadding">
            <form id="editSchemaForm" class="form-horizontal" method="post" action="${ctx }/console/collection/schema/save">
              <c:set var="isNew" value="${empty schema.name?'true':'false' }" />
              <ol class="formwizard-steps">
				<li class="active current"><span class="label">1</span>填写基本信息</li>
				<li><span class="label">2</span>字段配置</li>
				<li><span class="label">3</span>高级配置</li>
				<li><span class="label">4</span>创建成功</li>
			  </ol>
              <%@include file="/WEB-INF/view/console/collection/schema/add-step1.jsp" %>
              <%@include file="/WEB-INF/view/console/collection/schema/edit-step2.jsp" %>
              <%@include file="/WEB-INF/view/console/collection/schema/add-step3.jsp" %>

              <div class="form-actions">
                 <label class="control-label"></label>
                 <div class="controls">
                 	 <a href="javascript:history.go(-1);"  class="btn">返回</a>&nbsp;&nbsp;&nbsp;&nbsp;
	                 <input id="back" class="btn btn-primary" type="reset" value="上一步" />&nbsp;&nbsp;&nbsp;
		             <input id="next" class="btn btn-danger" type="submit" value="下一步" />
		             <input type="hidden" name="ext.isNew" value="${isNew }" />
		             <div id="status"></div>
                 </div>
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


<div id="chooseAnalyzerModal" class="hide">
	<%@include file="/WEB-INF/view/console/collection/schema/analyzer-template.jsp" %>
</div>

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>
<%@include file="/WEB-INF/view/console/include/scriptValidLib.jsp" %>
<script src="${ctxStaticConsole }/js/modules/schema-common-add.js"></script>
<script type="text/javascript">
	$("#editSchemaForm").formwizard({ 
		formPluginEnabled: true,
		validationEnabled: true,
		focusFirstInput : true,
		disableUIStyles : true,
		textSubmit : '提 交',
		textNext : '下一步',
		textBack : '上一步',
		formOptions :{
			success: function(data){
				console.log(11111);
				$("#status").fadeTo(500,1,function(){
					$(this).html("<span>Form was submitted!</span>").fadeTo(5000, 0); 
				});
			},
			dataType: 'json',
			resetForm: true
		},
		validationOptions : {
			rules: {
				title: {required: true},
			},
			errorClass: "help-inline-error",
			errorElement: "span",
			highlight:function(element, errorClass, validClass) {
				$(element).parents('.control-group').addClass('error');
			},
			unhighlight: function(element, errorClass, validClass) {
				$(element).parents('.control-group').removeClass('error');
			}
		},
		nextSuccess: function(beforeStep){
			var bofore_step = beforeStep.split("-")[2];
			$(".formwizard-steps li:eq("+bofore_step+")").addClass("active current");
			$(".formwizard-steps li:eq("+bofore_step+")").siblings().removeClass("current");
			$("#wordSeg").hide();
			if(bofore_step == 1){
				$("#wordSeg").show();
				setTimeout(function(){
				    $("select[id=selectType]").select2();
				}, 500);
			}
		},
		backEvent: function(beforeStep){
			var bofore_step = beforeStep.split("-")[2];
			console.log("backEvent bofore_step:"+bofore_step);
			$(".formwizard-steps li:eq("+(bofore_step-1)+")").removeClass("current");
			$(".formwizard-steps li:eq("+(bofore_step-2)+")").addClass("current");
		}
	});
	
	// 变量验证   
	jQuery.validator.addMethod("isVariable", function(value, element) {   
	    var expr = /^\d+[a-zA-Z_]\d*$|^[a-zA-Z_][\w_]*$/;
	    //^[A-Za-z0-9_]{2,20}$
	    // /^\d+[a-zA-Z_]\d*$|^[a-zA-Z_][\w_]*$/
	    // 英文字母、数字、下划线组成，非纯数字，不超过30个字符；命名后不可更改 
	    return this.optional(element) || (expr.test(value));
	}, "由：英文字母、数字、下划线组成，非纯数字");
	
	$("#editSchemaForm").validate();
	

	$(document).ready(function(){
		checkboxEvent("reloadSolrConf", function(){
			layerAlert("勾选后，会更新solr配置喔！", "warning");
		});
	});
	
</script>
</body>
</html>