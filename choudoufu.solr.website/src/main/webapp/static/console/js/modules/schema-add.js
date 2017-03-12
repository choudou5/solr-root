
$(document).ready(function(){
	
	fw = $("#addTableForm").formwizard({ 
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
				id: {required: true, rangelength: [4, 30], isVariable: true},
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
 * 挑选 过滤器
 * @param index
 */
function chooseFilterModal(index){
	var htm = $("#chooseFilterModal").html();
	new jBox('Confirm', {
		id: "jbox-confirm" ,
	    width: 220,
	    title: '添加过滤器',
	    overlay: false,
	    content: htm,
	    repositionOnOpen: false,
	    repositionOnContent: true,
	    confirmButton: '确定!',
	    cancelButton: '取消',
    	confirm: function(){
    		var chooseFilters = new Array();
    		$('.jBox-content #filterTemplate input:checked').each(function(){
    			chooseFilters.push($(this).val());
    		});
			if(_.isEmpty(chooseFilters)){
				return;
			}
			console.log("choose:"+chooseFilters);
			$("#fieldTable tbody tr:eq("+index+") td #filters").val(chooseFilters.join(","));
			$("#fieldTable tbody tr:eq("+index+") td #fltersTagCount").text(chooseFilters.length);
			$('.jBox-content #filterTemplate input').prop("checked", false);
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
	    tpl.append('<td><div id="filterDiv"><input id="filters" type="hidden" name="fields['+newIndex+'].filters" value=""/><a id="chooseFilterModalBtn" href="javascript:chooseFilterModal('+newIndex+')">&nbsp;<i class="icon icon-plus"></i>&nbsp;</a><span id="fltersTagCount" class="badge badge-info"></span></div></td>');
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
