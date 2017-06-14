<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script src="${ctxStaticConsole }/js/jquery.min.js"></script>
<script src="${ctxStaticConsole }/js/jquery.ui.custom.js"></script>
<script src="${ctxStaticConsole }/js/bootstrap.min.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.flot.min.js"></script>
<script src="${ctxStaticConsole }/js/matrix.js"></script> 
<script src="${ctxStaticConsole }/js/jquery.peity.min.js"></script> 
<script src="${ctxStaticConsole }/js/fullcalendar.min.js"></script>

<script src="${ctxStatic }/js/layer/layer.js"></script>
<script src="${ctxStatic }/js/common.js"></script>
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
	
	loadSystemInfo();
	
	//加载 系统信息
	function loadSystemInfo(){
		$.ajax({
		    url: ctx + "/admin/info/system?wt=json",
		    type:'GET',
		    data:{'_': getRandom()},
		    timeout:2000,    //超时时间
		    dataType:'json', //返回的数据格式：json/xml/html/script/jsonp/text
		    success:function(jsonData, status, xhr){
		    	var jvm = jsonData.jvm;
		    	if(jvm){
		    		var memory = jvm.memory;
		    		var raw = memory.raw;
		    		var memory_percent = (raw.used/raw.max*100).toFixed(2);
		    		$("#memory_percent_css").css("width", memory_percent+"%");
		    		$("#memory_percent_text").text(memory_percent+"%");
		    		$("#memory_stat").text(getMemoryText(raw.used, false)+" / "+getMemoryText(raw.max));
		    	}
		    	var system = jsonData.system;
		    	if(system){
		    		var disk_total = system.totalPhysicalMemorySize;
		    		var disk_used = disk_total - system.freePhysicalMemorySize;
		    		var disk_percent = (disk_used/disk_total*100).toFixed(2);
		    		$("#disk_percent_css").css("width", disk_percent+"%");
		    		$("#disk_percent_text").text(disk_percent+"%");
		    		$("#disk_stat").text(getMemoryText(disk_used, false)+" / "+getMemoryText(disk_total));
		    	}
		    }
		});
	}
	
	function getMemoryText(memory, retUnit){
		retUnit = retUnit==undefined?true:retUnit;
		var byte_value = parseInt(memory);
	    byte_value /= (1024*1024);
	    unit = ' MB';
	    if( 1024 <= byte_value ) {
	      byte_value /= 1024;
	      unit = ' GB';
	    }
	    return byte_value.toFixed(2) + (retUnit?unit:'');
	}
</script>
