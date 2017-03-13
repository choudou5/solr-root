
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
				name: {required: true, rangelength: [4, 30], isVariable: true},
				title: "required",
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
 * 挑选 分析仪
 * @param index
 */
function chooseAnalyzerModal(index){
	var htm = $("#chooseAnalyzerModal").html();
	htm = htm.replaceAll(new RegExp(/(tab_index)/g), "tab_index_"+getRandom());
	htm = htm.replace(new RegExp(/(tab_query)/g), "tab_query_"+getRandom());
	
	new jBox('Confirm', {
		id: "jbox-confirm" ,
	    width: 400,
	    title: '数据加工',
	    overlay: false,
	    content: htm,
	    repositionOnOpen: false,
	    repositionOnContent: true,
	    confirmButton: '确定!',
	    cancelButton: '取消',
    	confirm: function(){
    		var chooseFilters = new Array();
    		$('.jBox-content #analyzerTemplate input:checked').each(function(){
    			chooseFilters.push($(this).val());
    		});
			if(_.isEmpty(chooseFilters)){
				return;
			}
			console.log("choose:"+chooseFilters);
			$("#fieldTable tbody tr:eq("+index+") td #filters").val(chooseFilters.join(","));
			$("#fieldTable tbody tr:eq("+index+") td #analyzerTag").text(chooseFilters.length);
			$('.jBox-content #analyzerTemplate input').prop("checked", false);
    	},
	}).open().show();
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
	tpl.append('<td><select name="fields['+newIndex+'].type">'+fieldTypeTpl.html()+'</select></td>');
	tpl.append('<td><div id="analyzerDiv">');
	tpl.append('<input id="analyzerIndex" type="hidden" name="fields['+newIndex+'].type.tokenizerClass" value=""/>');
	tpl.append('<input id="analyzerIndexUseSmart" type="hidden" name="fields['+newIndex+'].type.index.useSmart" value=""/>');
	tpl.append('<input id="analyzerIndexCode" type="hidden" name="fields['+newIndex+'].type.index.code" value=""/>');
	tpl.append('<input id="analyzerIndexSeparator" type="hidden" name="fields['+newIndex+'].type.index.separator" value=""/>');
	tpl.append('<input id="analyzerIndexSqlGroupSymbol" type="hidden" name="fields['+newIndex+'].type.index.sqlGroupSymbol" value=""/>');
	tpl.append('<input id="analyzerIndexFilters" type="hidden" name="fields['+newIndex+'].type.index.filters" value=""/>');
	
	tpl.append('<input id="analyzerQueryUseSmart" type="hidden" name="fields['+newIndex+'].type.query.useSmart" value=""/>');
	tpl.append('<input id="analyzerQueryCode" type="hidden" name="fields['+newIndex+'].type.query.code" value=""/>');
	tpl.append('<input id="analyzerQuerySeparator" type="hidden" name="fields['+newIndex+'].type.query.separator" value=""/>');
	tpl.append('<input id="analyzerQuerySqlGroupSymbol" type="hidden" name="fields['+newIndex+'].type.query.sqlGroupSymbol" value=""/>');
	tpl.append('<input id="analyzerQueryFilters" type="hidden" name="fields['+newIndex+'].type.query.filters" value=""/>');
	tpl.append('<a href="javascript:chooseAnalyzerModal('+newIndex+')">&nbsp;<i class="icon icon-plus"></i>&nbsp;</a>');
	tpl.append('<span id="analyzerTag" class="badge badge-info"></span>');
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
