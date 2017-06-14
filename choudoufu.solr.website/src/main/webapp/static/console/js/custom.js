var __screenH = document.body.clientHeight;
var __screenW = document.body.clientWidth;

$(function(){
	
	/********begin 登陆超时 校验****************/
    $.ajaxSetup({
    	global: true,
    	complete: function (request, textStatus) {
    		log("complete");
    		var data = request.responseText;
            //login页面已做特殊标记    
            if(data != null && data.toString().indexOf('loginPage') != -1) {
          	  layerAlert("登录已超时，请备份好已填写内容！", "bad");
              return;  
            }
    	}
    });
    /********end 登陆超时 校验****************/
    
	$("input[class=need_clear]").bind("keydown", function(e){
		 if(e){
			 //65到90-->>a到z（A到Z） 48到57-->>0到9
			 if((e.keyCode>=48 && e.keyCode<=57) || (e.keyCode>=65 && e.keyCode<=90)){
				 var val = $(this).val();
				 var inputName = $(this).attr("name");
				 if(val.length != 0){
					 $("a#clear_input_"+inputName).show();
				 }else{
					 $("a#clear_input_"+inputName).hide();
				 }
			 }
		 }
	});
	$("input[class=need_clear]").bind("focus", function(e){
		 var val = $(this).val();
		 var inputName = $(this).attr("name");
		 if(val.length != 0){
			 $("a#clear_input_"+inputName).show();
		 }
	});
	
	// 初始化 表格 选中
	custom_initTableActiveSelect("a");
	
});


/**
 * 初始化 表格 选中 (table 的class中必须有 table_active)
 * @param triggerTag 触发标签
 */
function custom_initTableActiveSelect(triggerTag) {
    var $tbr = $('table.table_active tbody tr');  
    $tbr.find(triggerTag).click(function(event){
        var thisTr = $(this).closest("tr");
        thisTr.addClass('success');
        thisTr.siblings().removeClass("success");//移除兄弟标记样式
        event.stopPropagation();  
    });  
} 

/**
 * 清除 文本框值
 * @param inputName
 */
function clearInputByName(inputName){
	$("input[name="+inputName+"]").val('');
	$("a#clear_input_"+inputName).hide();
}


/**
 * 获得 地址栏
 * @returns
 */
function getLocalHref(){
    return window.location.href;
}

function getLocalPath(){
    return window.location.pathname;
}

function getLocalFullPath(){
    return window.location.pathname+window.location.search;
}

/**
 * 获得地址栏查询参数
 * @param paramName
 * @returns
 */
function getLocalQueryParam(paramName){
     var reg = new RegExp("(^|&)"+ paramName +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

/**
 * 获得地址栏 path
 * @param href
 * @returns
 */
function getHrefPath(href){
	 if(href==undefined) return null;
     var reg = new RegExp("path=([^&]*)(&|$)");
     var r = href.substr(1).match(reg);
     if(r!=null)return  unescape(r[1]); return null;
}

/** 日志*/
function info(message) {
	console.log("info:"+message);
}

function error(message, e) {
	console.log("error:"+message+", e:"+e);
}

/**
 * 获得 随机数
 * @returns {Number}
 */
function getRandom(){
	var time = new Date().getTime();
	var baseNum = (Math.ceil(Math.random()*1000));
	return time*baseNum;
}

function gritterTip(title, content){
	$.gritter.add({
		title: title,
		text: content,
		sticky: false,
	});
}

/**
 * 树 展开/缩起
 * @param expandAll 是否全部展开
 */
function treeTblExpandAll(treeId, expandAll){
	try{
		if(expandAll)
			$("#"+treeId).treetable('expandAll');
		else
			$("#"+treeId).treetable('collapseAll');
		return false;
	}catch(e){
		error("treeTblExpandAll:"+e);
	}
}


function isEmpty(str) {
	 return (str == undefined || str == null || str.length == 0 || str == "undefined");
}

function isNotEmpty(str) {
	 return !isEmpty(str);
}

function isArray(obj) { 
	return Object.prototype.toString.call(obj) === '[object Array]'; 
}

/**
 * 调用
 * var buffer = new StringBuffer();
 *	buffer.append("Hello ").append("javascript");
 *	alert(buffer.toString(); 
 */
/**
 *字符串 拼接
 * */
function StringBuffer() {
    this.__strings__ = new Array();
}
StringBuffer.prototype.append = function (str) {
    this.__strings__.push(str);
    return this;    //方便链式操作
}
StringBuffer.prototype.toString = function () {
    return this.__strings__.join("");
}
//清除
StringBuffer.prototype.clear = function () {
    this.__strings__ = new Array();
    return this;
}

/*扩展　String 原生方法*/
String.prototype.startWith=function(str){     
  var reg=new RegExp("^"+str);     
  return reg.test(this);        
}  
String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}
String.prototype.replaceAll = function(s1, s2){ 
	return this.replace(new RegExp(s1,"gm"), s2); 
}