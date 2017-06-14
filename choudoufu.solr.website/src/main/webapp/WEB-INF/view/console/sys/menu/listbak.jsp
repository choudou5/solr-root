<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/layui/_public/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>Powered By ${fns:getConfig('text.productName')}</title>
<%@include file="../_public/header.jsp" %>
</head>
<body>
<div class="sub-page">
	<div class="layui-tab layui-tab-brief sub-page-tab" lay-filter="F_sub_tab">
		<ul class="layui-tab-title">
			<li class="layui-this" data-url="${__ADMIN__ }/sys/menu/">菜单管理</li>
			<li data-url="${__ADMIN__ }/sys/menu/form/">添加菜单</li>
		</ul>
		<div class="layui-tab-content">
			<div class="layui-tab-item layui-show">
				<div class="layui-form">
					<table class="layui-table">
						<colgroup>
							<col width="5%">
							<col width="40%">
							<col width="15%">
							<col width="10%">
							<col>
						</colgroup>
						<thead>
							<tr>
								<th>ID</th>
								<th>菜单名称</th>
								<th>权限标识</th>
								<th>是否显示</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="J_tree">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="${__STATIC__ }/admin/js/admin.js?t=<%=System.currentTimeMillis() %>"></script>
<script id="T_tree_item" type="text/html">
{{#  layui.each(d.list, function(index, item){ }}
<tr data-parentid="{{ item.parentId }}">
	<td>{{ item.id }}</td>
	<td data-id="{{ item.id }}" data-depth="{{ d.depths.length }}" class="menu-open-tree" title="展开子项">
		{{#  layui.each(d.depths, function(j, dep){ }}
		<span class="menu-tree-nbsp"></span>
		{{#  }); }}
		{{#  if(item.hasChildren){ }}
		<i class="layui-icon">&#xe623;</i>
		{{#  } }}
		{{ item.name }}
	</td>
	<td>{{ item.permission }}</td>
	<td>
		<i class="layui-icon menu-isshow {{ item.showClass }}">{{ item.showIcon }}</i>
	</td>
	<td>
		<div class="layui-btn-group">
			<button data-url="${__ADMIN__ }/sys/menu/form?parentId={{ item.id }}" class="do-action layui-btn layui-btn-small">添加子项</button>
			<button data-url="${__ADMIN__ }/sys/menu/form?id={{ item.id }}" class="do-action layui-btn layui-btn-small">编辑</button>
			<button data-url="${__ADMIN__ }/sys/menu/do_delete?id={{ item.id }}" data-type="ajaxDelete" class="do-action layui-btn layui-btn-small">删除</button>
		</div>
	</td>
</tr>
{{#  }); }}
</script>
<script type="text/javascript">
var treeJson = ${treeJson};
layui.use(['element','laytpl'], function(){
	var $ = layui.jquery
	,layer = layui.layer
	,element = layui.element()
	,laytpl = layui.laytpl;
	
	function _treeOpenItem(id, callback) {
		var $item = $("tr[data-parentid='"+id+"']");
		if(!$item.length) {
			var depth = id == '0' ? 0 : $("td[data-id='"+id+"']").data('depth')*1 + 1;
			laytpl($("#T_tree_item").html()).render({
				list: $.map(treeJson[id], function(item) {
					item.hasChildren = treeJson[item.id] != null;
					item.isShow = item.isShow == 1;
					item.showClass = item.isShow ? 'menu-isshow_y' : 'menu-isshow_n';
					item.showIcon = item.isShow ? '&#xe605;' : '&#x1006;';
					return item;
				}), 
				depths: new Array(depth)
			}, callback);
		}else {
			$item.toggle();
		}
	}
	
	// 初始化菜单
	_treeOpenItem('0', function(html) {
		$("#J_tree").html(html);
	});
	
	// 展开子菜单
	$('#J_tree').on('click', '.menu-open-tree', function() {
		var id = $(this).data('id')
		, thiz = this
		, $item = $("tr[data-parentid='"+id+"']");
		$(this).find('.layui-icon').html(!$item.length || $item.is(':hidden') ? '&#xe625;' : '&#xe623;');
		_treeOpenItem(id, function(html) {
			$(thiz).parent().after(html);
		});
	});
	
});
</script>
</body>
</html>