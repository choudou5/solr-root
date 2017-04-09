<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script src="${ctxStaticConsole }/js/jquery.min.js"></script>
<script src="${ctxStaticConsole }/js/jquery.ui.custom.js"></script>
<script src="${ctxStaticConsole }/js/bootstrap.min.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.flot.min.js"></script>
<script src="${ctxStaticConsole }/js/matrix.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.peity.min.js"></script> 
<script src="${ctxStaticConsole }/js/fullcalendar.min.js"></script>
<%--jBox --%>
<script src="${ctxStaticConsole }/js/jBox-0.4.7/jBox.js"></script>
<script src="${ctxStaticConsole }/js/jBox-0.4.7/jBox.Confirm.js"></script>
<script src="${ctxStaticConsole }/js/jBox-0.4.7/jBox.Notice.min.js"></script>
<script src="${ctxStaticConsole }/js/custom.js"></script>
<script src="${ctxStaticConsole }/js/menus.js"></script>

<%-- tools --%>
<script src="${ctxStatic }/js/lodash-4.17.4/lodash.min.js"></script>

<script type="text/javascript">
	var message='${message}';
	info("message:"+message);
	if(!_.isEmpty(message)){
		$("#messageAlert").show().find("h4").text(message);
	}
	
	//激活 菜单样式
	activeMenuCss();
</script>
