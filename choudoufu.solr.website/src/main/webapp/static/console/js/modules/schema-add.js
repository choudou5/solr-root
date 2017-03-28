
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
				name: {remote: "已存在此应用标识"}
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
 * @param index
 */
function changeSelectType(index){
	var selectOpt = $("#fieldTable tbody tr:eq("+index+") td #selectType option:selected");
	var tokenizerClass = selectOpt.attr("classz");
	var type = selectOpt.attr("type");
	$("#fieldTable tbody tr:eq("+index+") td #analyzer_tokenizerClass").val(tokenizerClass);
	if("analyzer" != type){
		$("#fieldTable tbody tr:eq("+index+") td #chooseAnalyzerBtn").hide();
	}else{
		$("#fieldTable tbody tr:eq("+index+") td #chooseAnalyzerBtn").show();
	}
}


/**
 * 挑选 分析器
 * @param index
 */
function chooseAnalyzerModal(index){
	var typeSelectOpt = $("#fieldTable tbody tr:eq("+index+") td #selectType option:selected");
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
		    	bindBoxParamVal(boxId, index);
	    	}, 600);
	    },
    	confirm: function(){
    		//提交参数
    		boxSubmitParam(boxId, index);
    	},
	}).open().show();
}


/**
 * 绑定 box 参数值
 * @param boxId
 * @param index 下标
 */
function bindBoxParamVal(boxId, index){
	//设置 过滤器
	bindBoxCheckedParamVal(boxId, index, "index", "filters");
	bindBoxCheckedParamVal(boxId, index, "query", "filters");
	
	//设置 IK的 useSmart
	bindBoxCheckedParamVal(boxId, index, "index", "useSmart");
	bindBoxCheckedParamVal(boxId, index, "query", "useSmart");
	
	//设置 地区提取类型code
	bindBoxCheckedParamVal(boxId, index, "index", "code");
	bindBoxCheckedParamVal(boxId, index, "query", "code");

	//设置 符号切割符号
	bindBoxInputParam(boxId, index, "index", "separator");
	bindBoxInputParam(boxId, index, "query", "separator");
	bindBoxInputParam(boxId, index, "index", "sqlGroupSymbol");
	bindBoxInputParam(boxId, index, "query", "sqlGroupSymbol");
}

/**
 * 绑定 选中参数
 * @param boxId
 * @param type {index/query}
 * @param index 下标
 * @param name 参数名
 */
function bindBoxCheckedParamVal(boxId, index, type, name){
	var paramVal = getAnalyzerHiddenParamVal(type, index, name);
	if(!_.isEmpty(paramVal)){
		var array = paramVal.split(",");
		for (var i in array) {
			var val = array[i];
			console.log(" set: "+boxId+":input[name="+type+"_"+name+"]:"+val);
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
 * @param index 下标
 * @param type {index/query}
 * @param name 参数名
 */
function bindBoxInputParam(boxId, index, type, name){
	var paramVal = getAnalyzerHiddenParamVal(type, index, name);
	if(!_.isEmpty(paramVal)){
		$('#'+boxId+' input[name='+type+'_'+name+']').val(paramVal);
	}
}


/**
 * 提交参数
 * @param boxId
 * @param index 下标
 */
function boxSubmitParam(boxId, index){
	var setCount = 0;
	//设置 过滤器
	setCount += boxSubmitCheckedParam(boxId, index, "index", "filters");
	setCount += boxSubmitCheckedParam(boxId, index, "query", "filters");
	
	//设置 IK的 useSmart
	setCount += boxSubmitCheckedParam(boxId, index, "index", "useSmart");
	setCount += boxSubmitCheckedParam(boxId, index, "query", "useSmart");
	
	//设置 地区提取类型code
	setCount += boxSubmitCheckedParam(boxId, index, "index", "code");
	setCount += boxSubmitCheckedParam(boxId, index, "query", "code");

	//设置 符号切割符号
	setCount += boxSubmitInputParam(boxId, index, "index", "separator");
	setCount += boxSubmitInputParam(boxId, index, "query", "separator");
	setCount += boxSubmitInputParam(boxId, index, "index", "sqlGroupSymbol");
	setCount += boxSubmitInputParam(boxId, index, "query", "sqlGroupSymbol");
	
	var starTag = setCount!=0?"<i class='icon-star'></i>":"";
	$("#fieldTable tbody tr:eq("+index+") td #analyzerTag").html(starTag);
}

/**
 * 设置 输入框参数
 * @param boxId
 * @param index 下标
 * @param type {index/query}
 * @param name 参数名
 */
function boxSubmitInputParam(boxId, index, type, name){
	var inputVal = $('#'+boxId+' input[name='+type+'_'+name+']').val();
	//设置 分析器隐藏参数
	return setAnalyzerHiddenParam(type, index, name, inputVal);
}

/**
 * 设置 选中参数
 * @param boxId
 * @param type {index/query}
 * @param index 下标
 * @param name 参数名
 */
function boxSubmitCheckedParam(boxId, index, type, name){
	var chooseFilters = new Array();
	$('#'+boxId+' input[name='+type+'_'+name+']:checked').each(function(){
		chooseFilters.push($(this).val());
	});
	//设置 分析器隐藏参数
	return setAnalyzerHiddenParam(type, index, name, chooseFilters.join(","));
}


/**
 * 设置 分析器隐藏参数
 * @param type {index/query}
 * @param index 下标
 * @param name 参数名
 * @param val
 * @return 状态（0=未设值，1=设值成功）
 */
function setAnalyzerHiddenParam(type, index, name, val){
	console.log("index:"+index+", "+type+"_"+name+", val:"+val);
	$("#fieldTable tbody tr:eq("+index+") td #analyzer_"+type+"_"+name).val(val);
	return _.isEmpty(val)?0:1;
}

/**
 * 获取 分析器隐藏参数
 * @param type {index/query}
 * @param index 下标
 * @param name 参数名
 * @return val
 */
function getAnalyzerHiddenParamVal(type, index, name){
	var val = $("#fieldTable tbody tr:eq("+index+") td #analyzer_"+type+"_"+name).val();
	console.log(index+", get:"+type+"."+name+":"+val);
	return val;
}



/**
 * 添加 字段
 */
function addFieldRow(){
	var newIndex = $("#fieldTable tbody tr").length-1;
	
	var tpl = new StringBuffer();
	tpl.append('<tr>');
	tpl.append('<td>'+(newIndex+1)+'</td>');
	tpl.append('<td><input class="required isVariable" type="text" name="fields['+newIndex+'].name" placeholder="字母 或 下划线 组成" maxlength="10"/></td>');
	tpl.append('<td><input class="required" type="text" name="fields['+newIndex+'].label" placeholder="简单描述" minlength="2" maxlength="10"/></td>');
		
	var fieldTypeTpl = $("#tplFieldType").clone();
	tpl.append('<td><select id="selectType" name="fields['+newIndex+'].type.name" onchange="changeSelectType('+newIndex+')">'+fieldTypeTpl.html()+'</select></td>');
	tpl.append('<td><div id="analyzerDiv">');
	tpl.append('<input id="analyzer_tokenizerClass" type="hidden" name="fields['+newIndex+'].type.tokenizerClass" value=""/>');
	tpl.append('<input id="analyzer_index_useSmart" type="hidden" name="fields['+newIndex+'].type.index.useSmart" value=""/>');
	tpl.append('<input id="analyzer_index_code" type="hidden" name="fields['+newIndex+'].type.index.code" value=""/>');
	tpl.append('<input id="analyzer_index_separator" type="hidden" name="fields['+newIndex+'].type.index.separator" value=""/>');
	tpl.append('<input id="analyzer_index_sqlGroupSymbol" type="hidden" name="fields['+newIndex+'].type.index.sqlGroupSymbol" value=""/>');
	tpl.append('<input id="analyzer_index_filters" type="hidden" name="fields['+newIndex+'].type.index.filters" value=""/>');
	
	tpl.append('<input id="analyzer_query_useSmart" type="hidden" name="fields['+newIndex+'].type.query.useSmart" value=""/>');
	tpl.append('<input id="analyzer_query_code" type="hidden" name="fields['+newIndex+'].type.query.code" value=""/>');
	tpl.append('<input id="analyzer_query_separator" type="hidden" name="fields['+newIndex+'].type.query.separator" value=""/>');
	tpl.append('<input id="analyzer_query_sqlGroupSymbol" type="hidden" name="fields['+newIndex+'].type.query.sqlGroupSymbol" value=""/>');
	tpl.append('<input id="analyzer_query_filters" type="hidden" name="fields['+newIndex+'].type.query.filters" value=""/>');
	tpl.append('<a id="chooseAnalyzerBtn" href="javascript:chooseAnalyzerModal('+newIndex+')" class="mgl-10">&nbsp;<i class="icon icon-plus"></i>&nbsp;</a>');
	tpl.append('<span id="analyzerTag" class="mgl-10 label label-success"></span>');
	tpl.append('</div></td>');
	tpl.append('<td><input type="checkbox" name="fields['+newIndex+'].indexed" value="true"/></td>');
	tpl.append('<td><input type="checkbox" name="fields['+newIndex+'].stored" value="true"/></td>');
	tpl.append('<td><input type="checkbox" name="fields['+newIndex+'].required" value="true"/></td>');
	tpl.append('<td><input type="checkbox" name="fields['+newIndex+'].multiValued" value="true"/></td>');
	tpl.append('<td><input type="checkbox" name="fields['+newIndex+'].isListShow" value="true"/></td>');
	tpl.append('<td><input type="checkbox" name="fields['+newIndex+'].isListSearch" value="true"/></td>');
	tpl.append('<td><input type="radio" name="isPrimaryKey" value="'+newIndex+'"/></td>');
	tpl.append('<td><a href="javascript:void(0);" onclick="removeFieldRow(this)">删除</a></td> ');
	tpl.append('</tr>');
	$("#addFieldBtns").before(tpl.toString());
	
	$("#fieldTable tbody tr:eq("+newIndex+") td select").select2();
}

/**
 * 移除 字段
 * @param thi
 */
function removeFieldRow(thi){
	$(thi).closest("tr").remove();
}
