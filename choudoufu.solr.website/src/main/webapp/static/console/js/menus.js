/**
 * 激活 菜单样式
 */
function activeMenuCss(){
	var localPath = getLocalFullPath();
	info(localPath);
	var sidebar_li = $("#sidebar ul li").each(function(){
		var href = $(this).find("a").attr("href");
		if(localPath == href){
			$(this).addClass('active');//激活当前菜单
			var ul_id = $(this).closest("ul").attr("id");
			//是否为二级菜单
			if(ul_id == undefined){
				var pLi = $(this).parent().closest("li");
				//是否撑开父节点
				if(!pLi.hasClass("open")){
					setTimeout(function(){
						pLi.find("a:first").trigger("click");
					}, 300);
					pLi.siblings().removeClass("active");//移除兄弟标记样式
				}
			}else{
				$(this).siblings().removeClass("active");//移除兄弟标记样式
			}
			return false;
		}
	});
}