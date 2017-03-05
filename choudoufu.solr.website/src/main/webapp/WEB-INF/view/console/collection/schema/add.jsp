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
                    <input type="text" name="id" placeholder="请输入模块标识"/>
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
                    <textarea name="explain" class="span5" rows="3" maxlength="120" placeholder="qqqq"></textarea>
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
                <div class="control-group">
                  <label class="control-label">Email</label>
                  <div class="controls">
                    <input id="email" type="text" name="email" />
                  </div>
                </div>
                <div class="control-group">
                  <label class="control-label">EULA</label>
                  <div class="controls">
                    <input id="eula" type="checkbox" name="eula" />
                  </div>
                </div>
              </div>
              <div class="form-actions">
                <input id="back" class="btn btn-primary" type="reset" value="Back" />
                <input id="next" class="btn btn-primary" type="submit" value="Next" />
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
<script src="${ctxStaticConsole }/js/modules/schema.add.js"></script>

<script type="text/javascript">

</script>
</body>
</html>
