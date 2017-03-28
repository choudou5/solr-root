
$(document).ready(function(){
	
	$("#addTableForm").formwizard({ 
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
				name: {required: true, rangelength: [4, 30], isVariable: true, remote: ctx+"/console/collection/schema/checkSchemaName"},
				title: "required",
			},
			messages: {
				name: {remote: "应用标识 已存在"}
			},
			errorClass: "help-inline",
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
				    $("#selectType").select2();
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
	
	$("#addTableForm").validate();
	
});

/**
 * 数据类型 change事件
 * @param groupId
 */
function changeSelectType(groupId){
	var selectOpt = $("#fieldTable tbody tr#tr_"+groupId+" td #selectType option:selected");
	var tokenizerClass = selectOpt.attr("classz");
	var type = selectOpt.attr("type");
	$("#fieldTable tbody tr#tr_"+groupId+" td #analyzer_tokenizerClass").val(tokenizerClass);
	if("analyzer" != type){
		$("#fieldTable tbody tr#tr_"+groupId+" td #chooseAnalyzerBtn").hide();
	}else{
		$("#fieldTable tbody tr#tr_"+groupId+" td #chooseAnalyzerBtn").show();
	}
}


/**
 * 挑选 分析器
 * @param groupId
 */
function chooseAnalyzerModal(groupId){
	var typeSelectOpt = $("#fieldTable tbody tr#tr_"+groupId+" td #selectType option:selected");
	var typeVal = typeSelectOpt.attr("value");
	if("analyzer" != typeSelectOpt.attr("type")){
		jNoticeCenter("基本类型 无需加工!", 'red');
		return;
	}
	
	var htm = $("#chooseAnalyzerModal").html();
	var rd = getRandom();
	var boxId = "analyzerBox_"+rd;
	htm = htm.replace("analyzerBox_", boxId);
	htm = htm.replace(new RegExp(/(tab_index)/g), "tab_index_"+rd);
	htm = htm.replace(new RegExp(/(tab_query)/g), "tab_query_"+rd);
	htm = htm.replace(new RegExp(typeVal+" hide", 'g'), "show");
	
	var w = 400;
	if(typeVal == "word_separator")
		w = 580;
	new jBox('Confirm', {
		id: "jbox-confirm" ,
	    width: w,
	    maxWidth: 600,
	    title: '数据加工',
	    overlay: false,
	    content: htm,
	    repositionOnOpen: false,
	    repositionOnContent: true,
	    confirmButton: '确定!',
	    cancelButton: '取消',
	    onInit: function(){
	    	setTimeout(function(){
	    		//绑定参数值
		    	bindBoxParamVal(boxId, groupId);
	    	}, 500);
	    },
    	confirm: function(){
    		//提交参数
    		boxSubmitParam(boxId, groupId);
    	},
	}).open().show();
}


/**
 * 绑定 box 参数值
 * @param boxId
 * @param groupId
 */
function bindBoxParamVal(boxId, groupId){
	//设置 过滤器
	bindBoxCheckedParamVal(boxId, groupId, "index", "filters");
	bindBoxCheckedParamVal(boxId, groupId, "query", "filters");
	
	//设置 IK的 useSmart
	bindBoxCheckedParamVal(boxId, groupId, "index", "useSmart");
	bindBoxCheckedParamVal(boxId, groupId, "query", "useSmart");
	
	//设置 地区提取类型code
	bindBoxCheckedParamVal(boxId, groupId, "index", "code");
	bindBoxCheckedParamVal(boxId, groupId, "query", "code");

	//设置 符号切割符号
	bindBoxInputParam(boxId, groupId, "index", "separator");
	bindBoxInputParam(boxId, groupId, "query", "separator");
	bindBoxInputParam(boxId, groupId, "index", "sqlGroupSymbol");
	bindBoxInputParam(boxId, groupId, "query", "sqlGroupSymbol");
}

/**
 * 绑定 选中参数
 * @param boxId
 * @param type {index/query}
 * @param groupId
 * @param name 参数名
 */
function bindBoxCheckedParamVal(boxId, groupId, type, name){
	var paramVal = getAnalyzerHiddenParamVal(type, groupId, name);
	if(!_.isEmpty(paramVal)){
		var array = paramVal.split(",");
		for (var i in array) {
			var val = array[i];
			console.log(" set: "+boxId+":input[attrname="+type+"_"+name+"]:"+val);
			$('#'+boxId+' input[name='+type+'_'+name+']').each(function(){
				var currVal = $(this).val().toString();
				if(currVal == val){
					$(this).prop("checked", true);
				}
			});
		}
	}
}

/**
 * 绑定 输入框参数
 * @param boxId
 * @param groupId 
 * @param type {index/query}
 * @param name 参数名
 */
function bindBoxInputParam(boxId, groupId, type, name){
	var paramVal = getAnalyzerHiddenParamVal(type, groupId, name);
	if(!_.isEmpty(paramVal)){
		$('#'+boxId+' input[name='+type+'_'+name+']').val(paramVal);
	}
}


/**
 * 提交参数
 * @param boxId
 * @param groupId
 */
function boxSubmitParam(boxId, groupId){
	var setCount = 0;
	//设置 过滤器
	setCount += boxSubmitCheckedParam(boxId, groupId, "index", "filters");
	setCount += boxSubmitCheckedParam(boxId, groupId, "query", "filters");
	
	//设置 IK的 useSmart
	setCount += boxSubmitCheckedParam(boxId, groupId, "index", "useSmart");
	setCount += boxSubmitCheckedParam(boxId, groupId, "query", "useSmart");
	
	//设置 地区提取类型code
	setCount += boxSubmitCheckedParam(boxId, groupId, "index", "code");
	setCount += boxSubmitCheckedParam(boxId, groupId, "query", "code");

	//设置 符号切割符号
	setCount += boxSubmitInputParam(boxId, groupId, "index", "separator");
	setCount += boxSubmitInputParam(boxId, groupId, "query", "separator");
	setCount += boxSubmitInputParam(boxId, groupId, "index", "sqlGroupSymbol");
	setCount += boxSubmitInputParam(boxId, groupId, "query", "sqlGroupSymbol");
	
	var starTag = setCount!=0?"<i class='icon-star'></i>":"";
	$("#fieldTable tbody tr#tr_"+groupId+" td #analyzerTag").html(starTag);
}

/**
 * 设置 输入框参数
 * @param boxId
 * @param groupId
 * @param type {index/query}
 * @param name 参数名
 */
function boxSubmitInputParam(boxId, groupId, type, name){
	var inputVal = $('#'+boxId+' input[name='+type+'_'+name+']').val();
	//设置 分析器隐藏参数
	return setAnalyzerHiddenParam(type, groupId, name, inputVal);
}

/**
 * 设置 选中参数
 * @param boxId
 * @param type {index/query}
 * @param index 下标
 * @param name 参数名
 */
function boxSubmitCheckedParam(boxId, groupId, type, name){
	var chooseFilters = new Array();
	$('#'+boxId+' input[name='+type+'_'+name+']:checked').each(function(){
		chooseFilters.push($(this).val());
	});
	//设置 分析器隐藏参数
	return setAnalyzerHiddenParam(type, groupId, name, chooseFilters.join(","));
}


/**
 * 设置 分析器隐藏参数
 * @param type {index/query}
 * @param groupId
 * @param name 参数名
 * @param val
 * @return 状态（0=未设值，1=设值成功）
 */
function setAnalyzerHiddenParam(type, groupId, name, val){
	console.log("groupId:"+groupId+", "+type+"_"+name+", val:"+val);
	$("#fieldTable tbody tr#tr_"+groupId+" td #analyzer_"+type+"_"+name).val(val);
	return _.isEmpty(val)?0:1;
}

/**
 * 获取 分析器隐藏参数
 * @param type {index/query}
 * @param groupId
 * @param name 参数名
 * @return val
 */
function getAnalyzerHiddenParamVal(type, groupId, name){
	var val = $("#fieldTable tbody tr#tr_"+groupId+" td #analyzer_"+type+"_"+name).val();
	console.log(groupId+", get:"+type+"."+name+":"+val);
	return val;
}



/**
 * 添加 字段
 */
function addFieldRow(){
	var tbody = $("#fieldTable tbody");
	var newIndex = tbody.find("tr").length-1;
	var tpl = new StringBuffer();
	var groupId = getRandom();
	tpl.append('<tr id="tr_'+groupId+'">');
	tpl.append('<td>'+(newIndex+2)+'</td>');
	tpl.append('<td><input class="required isVariable" type="text" attrname="fields.name" placeholder="字母 或 下划线 组成" maxlength="10"/></td>');
	tpl.append('<td><input class="required" type="text" attrname="fields.label" placeholder="简单描述" minlength="2" maxlength="10"/></td>');
		
	var fieldTypeTpl = $("#tplFieldType").clone();
	tpl.append('<td><select id="selectType" attrname="fields.type.name" onchange="changeSelectType(\''+groupId+'\')">'+fieldTypeTpl.html()+'</select></td>');
	tpl.append('<td><div id="analyzerDiv">');
	tpl.append('<input id="analyzer_tokenizerClass" type="hidden" attrname="fields.type.tokenizerClass" value=""/>');
	tpl.append('<input id="analyzer_index_useSmart" type="hidden" attrname="fields.type.index.useSmart" value=""/>');
	tpl.append('<input id="analyzer_index_code" type="hidden" attrname="fields.type.index.code" value=""/>');
	tpl.append('<input id="analyzer_index_separator" type="hidden" attrname="fields.type.index.separator" value=""/>');
	tpl.append('<input id="analyzer_index_sqlGroupSymbol" type="hidden" attrname="fields.type.index.sqlGroupSymbol" value=""/>');
	tpl.append('<input id="analyzer_index_filters" type="hidden" attrname="fields.type.index.filters" value=""/>');
	
	tpl.append('<input id="analyzer_query_useSmart" type="hidden" attrname="fields.type.query.useSmart" value=""/>');
	tpl.append('<input id="analyzer_query_code" type="hidden" attrname="fields.type.query.code" value=""/>');
	tpl.append('<input id="analyzer_query_separator" type="hidden" attrname="fields.type.query.separator" value=""/>');
	tpl.append('<input id="analyzer_query_sqlGroupSymbol" type="hidden" attrname="fields.type.query.sqlGroupSymbol" value=""/>');
	tpl.append('<input id="analyzer_query_filters" type="hidden" attrname="fields.type.query.filters" value=""/>');
	tpl.append('<a id="chooseAnalyzerBtn" href="javascript:chooseAnalyzerModal(\''+groupId+'\')" class="mgl-10">&nbsp;<i class="icon icon-plus"></i>&nbsp;</a>');
	tpl.append('<span id="analyzerTag" class="mgl-10 label label-success"></span>');
	tpl.append('</div></td>');
	tpl.append('<td><input type="checkbox" attrname="fields.indexed" value="true"/></td>');
	tpl.append('<td><input type="checkbox" attrname="fields.stored" value="true"/></td>');
	tpl.append('<td><input type="checkbox" attrname="fields.required" value="true"/></td>');
	tpl.append('<td><input type="checkbox" attrname="fields.multiValued" value="true"/></td>');
	tpl.append('<td><input type="checkbox" attrname="fields.isListShow" value="true"/></td>');
	tpl.append('<td><input type="checkbox" attrname="fields.isListSearch" value="true"/></td>');
	tpl.append('<td><input type="radio" attrname="isPrimaryKey" value="'+newIndex+'"/></td>');
	tpl.append('<td><a href="javascript:void(0);" onclick="removeFieldRow(this)">删除</a></td> ');
	tpl.append('</tr>');
	tbody.append(tpl.toString());
	tbody.find("tr#tr_"+groupId+" td select").select2();
	resetFieldIndex();//重置 字段下班
}

/**
 * 重置 字段下班
 */
function resetFieldIndex(){
	var trIndex = 0;
	$("#fieldTable tbody tr").each(function(){
		$(this).find("td:eq(0)").text(trIndex+1);
		$(this).find("td input,select").each(function(){
			var ele = $(this);
			var oldAttrName = ele.attr("attrname");
			if(oldAttrName){
				console.log("oldAttrName："+oldAttrName);
				var newName = oldAttrName.toString().replace("fields.", "fields["+trIndex+"].");
				ele.attr("name", newName);
				if(oldAttrName == "isPrimaryKey")
					ele.val(trIndex);
			}
		});
		trIndex++;
	});
}


/**
 * 移除 字段
 * @param thi
 */
function removeFieldRow(thi){
	$(thi).closest("tr").remove();
	resetFieldIndex();//重置 字段下班
}
