<%
response.setStatus(500);
// 获取异常类
Throwable ex = Exceptions.getThrowable(request);
// 编译错误信息
StringBuilder sb = new StringBuilder("错误信息：\n");
if (ex != null) {
	sb.append(Exceptions.getStackTraceAsString(ex));
} else {
	sb.append("未知错误.\n\n");
}
%>
<%@page import="com.choudoufu.solr.util.Exceptions"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true" pageEncoding="UTF-8"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<!DOCTYPE html>

<html lang="en">
<head>
<title>500-服务器内部错误</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="shortcut icon" href="/static/images/favicon.ico">
<link rel="stylesheet" href="/static/console/css/bootstrap.min.css" />
<link rel="stylesheet" href="/static/console/css/matrix-style.css" />
<link href='/static/console/css/fonts.googleapis.com.css' rel='stylesheet' type='text/css'>
<script src="/static/js/jquery.min-v1.11.1.js"></script>
</head>
<body>

<div id="content" style="margin-left: 0px;min-height:90%;">
  <div id="content-header" style="margin-top: -20px;">
    <h1>Error 500</h1>
  </div>
  <div class="container-fluid">
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-content">
            <div class="error_ex">
              <h1>500</h1>
              <h3>很抱歉，服务器内部错误！</h3>
              <p>请联系管理员</p>
              <a class="btn btn-warning btn-big" href="/console/page?path=/index">返回首页</a>&nbsp;&nbsp;&nbsp;
              <a class="btn btn-big" href="javascript:history.go(-1);">返回上一页</a>&nbsp;&nbsp;&nbsp;
              <a href="javascript:" onclick="$('.errorMessage').toggle();" class="btn">查看详细信息</a>
              <div class="errorMessage hide" style="background: #fff;text-align: left;margin-top: 15px;">
				<%=StringEscapeUtils.unescapeHtml(sb.toString())%>
			  </div>
             </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!--Footer-part-->
<div class="row-fluid">
  <div id="footer" class="span12"> 2017 &copy; Solr Home by <a href="#">choudoufu</a> </div>
</div>
<!--end-Footer-part-->
</body>
</html>
