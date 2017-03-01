<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<script src="${ctxStaticConsole }/js/jquery.min.js"></script> 
<script src="${ctxStaticConsole }/js/bootstrap.min.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.flot.min.js"></script>
<%-- <script src="${ctxStaticConsole }/js/jquery.flot.pie.min.js"></script>  --%>
<%-- <script src="${ctxStaticConsole }/js/matrix.charts.js"></script>  --%>
<%-- <script src="${ctxStaticConsole }/js/jquery.flot.resize.min.js"></script> --%>
<script src="${ctxStaticConsole }/js/matrix.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.peity.min.js"></script> 

<script src="${ctxStaticConsole }/js/fullcalendar.min.js"></script>

<%-- 

<script src="${ctxStaticConsole }/js/fullcalendar.min.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.chat.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.validate.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.form_validation.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.wizard.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.uniform.js"></script> 
<script src="${ctxStaticConsole }/js/select2.min.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.popover.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.dataTables.min.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.tables.js"></script> 
 --%>
<%-- 
<script src="${ctxStaticConsole }/js/excanvas.min.js"></script>
<script src="${ctxStaticConsole }/js/jquery.min.js"></script>
<script src="${ctxStaticConsole }/js/jquery.ui.custom.js"></script> 
<script src="${ctxStaticConsole }/js/bootstrap.min.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.flot.min.js"></script>
<script src="${ctxStaticConsole }/js/jquery.flot.resize.min.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.peity.min.js"></script> 
<script src="${ctxStaticConsole }/js/fullcalendar.min.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.flot.pie.min.js"></script>
<script src="${ctxStaticConsole }/js/matrix.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.charts.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.dashboard.js"></script> 

<script src="${ctxStaticConsole }/js/jquery.gritter.min.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.interface.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.chat.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.validate.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.form_validation.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.wizard.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.uniform.js"></script> 
<script src="${ctxStaticConsole }/js/select2.min.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.popover.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.dataTables.min.js"></script> 
<script src="${ctxStaticConsole }/js/matrix.tables.js"></script>

 --%>
<%-- tools --%>
<script src="${ctxStatic }/js/lodash-4.17.4/lodash.min.js"></script>

<script type="text/javascript">
	var message='${message}';
	console.log("message:"+message);
	if(!_.isEmpty(message)){
		$("#messageAlert").show().find("h4").text(message);
	}
</script>
