/**
 * 询问框
 * @param content
 * @param href 链接地址或函数
 * @param title
 * @param okBtnText 确认按钮文本，选填
 * @param noBtnText 取消按钮文本，选填
 * @returns {Boolean}
 */
function layerConfirm(content, href, title, okBtnText, noBtnText){
	title = title==undefined?'提示':title;
	okBtnText = okBtnText==undefined?'确定':okBtnText;
	noBtnText = noBtnText==undefined?'取消':noBtnText;
	
	layer.confirm(content, {title: title, icon: 3, btn: [okBtnText, noBtnText]}, function(index){
		layer.close(index);
		if(typeof href == 'function'){
			return href();
		}else{
			window.location.href = href;
		}
	}, function(){
		  console.log("click no btn");
	});
	return false;
}

/**
 * 弹出框
 * @param title
 * @param type 类型：success/error/warning
 */
function layerAlert(title, type){
	type = type==undefined?1:type;
	layer.alert(title, {icon: layerGetIcon(type), btn:["知道了"]});
}

/**
 * 提示框
 * @param title
 * @param type 类型：success/error/warning
 * @param autoCloseTime 自动关闭时间（毫秒）
 */
function layerTip(title, type, autoCloseTime){
	type = type==undefined?1:type;
	autoCloseTime = autoCloseTime==undefined?3000:autoCloseTime;
	layer.msg(title, {icon: layerGetIcon(type), time: autoCloseTime});
}

/**
 * 提示框
 * @param title
 * @param type 类型：success/error/warning
 * @param autoCloseTime 自动关闭时间（毫秒）
 */
function loading(text, autoCloseTime){
	autoCloseTime = autoCloseTime==undefined?3000:autoCloseTime;
	layer.msg('正在加载...请稍等', { icon: 6, time: autoCloseTime});
}

var __loadingIndex = 9999999999;
function loading(text, autoCloseTime){
	__loadingIndex = layer.load();
	autoCloseTime = autoCloseTime==undefined?1000:autoCloseTime;
	setTimeout(function(){
		layer.close(__loadingIndex); 
	},autoCloseTime );
}

function closeloading(){
	layer.close(__loadingIndex); 
}


/**
 * @param type success/error/warning
 * @returns {0=感叹号, 1=成功, 2=错误, 3=疑问, 4=加密, 5=坏脸, 6=笑脸}
 */
function layerGetIcon(type){
	if("success" == type){
		return 1;
	}else if("error" == type){
		return 2;
	}else if("warning" == type){
		return 3;
	}else{
		return 0;
	}
}

/**
 * 复选框选中事件
 * @param attId
 * @param checkedCall
 * @param unCheckedCall
 */
function checkboxEvent(attId, checkedCall, unCheckedCall){
	$("#"+attId).click(function(){
		if($(this).is(":checked")){
			checkedCall();
			if(typeof checkedCall == 'function'){
				checkedCall();
			}
		}else{
			if(typeof unCheckedCall == 'function'){
				unCheckedCall();
			}
		}
	});
}
