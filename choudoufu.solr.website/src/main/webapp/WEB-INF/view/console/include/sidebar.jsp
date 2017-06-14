<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!--sidebar-menu-->
<div id="sidebar"><a href="${ctx }/console/page?path=/index" class="visible-phone"><i class="icon icon-home"></i>首页</a>
  <ul id="oneLevelSideBar">
    <li class=""><a href="${ctx }/console/page?path=/index"><i class="icon icon-home"></i> <span>首页</span></a> </li>
    <li class="submenu"> 
    	<a href="#"><i class="icon icon-sitemap"></i><span>系统设置</span></a>
      <ul>
        <li><a href="${ctx }/console/sys/menu/">菜单管理</a></li>
        <li><a href="${ctx }/console/sys/role/">角色管理</a></li>
        <li><a href="${ctx }/console/sys/user/">用户管理</a></li>
      </ul>
    </li>
    <li><a href="${ctx }/console/collection/schema/list"><i class="icon icon-list"></i><span>应用列表</span></a></li>
    <li class="submenu"> 
    	<a href="#"><i class="icon icon-cog"></i><span>基本配置</span></a>
      <ul>
        <li><a href="${ctx }/console/page?path=collection/schema/add">字段配置</a></li>
        <li><a href="#">数据源</a></li>
        <li><a href="#">重建索引</a></li>
      </ul>
    </li>
    <li class="submenu"> 
    	<a href="#"><i class="icon icon-cogs"></i><span>高级配置</span></a>
      <ul>
        <li><a href="#">竞价排名</a></li>
        <li><a href="#">自动分类</a></li>
        <li><a href="#">同义词</a></li>
      </ul>
    </li>
    <li class="submenu"> 
    	<a href="#"><i class="icon icon-magnet"></i><span>运营管理</span></a>
      <ul>
        <li><a href="#">关键字优化</a></li>
        <li><a href="#">词库扩展</a></li>
      </ul>
    </li>
    <li class="submenu"> <a href="#"><i class="icon icon-bar-chart"></i> <span>统计及日志</span> <span class="label label-important">4</span></a>
      <ul>
        <li><a href="${ctx }/console/sys/search_stats/list">统计</a></li>
        <li><a href="${ctx }/console/sys/log/list">日志</a></li>
      </ul>
    </li>
    <li><a href="${ctx }/console/collection/data/search"><i class="icon icon-play"></i><span>体验搜索</span></a></li>
    <li><a href="${ctx}/solr/admin.html" target="_blank"><i class="icon icon-sitemap"></i><span>Solr后台</span></a></li>
    <li class="content">&nbsp;&nbsp; <span>内存使用率</span>
      <div class="progress progress-mini progress-danger active progress-striped">
        <div id="memory_percent_css" style="width: 0%;" class="bar"></div>
      </div>
      <span id="memory_percent_text" class="percent">0%</span>
      <div id="memory_stat" class="stat">0 / 0 MB</div>
    </li>
    <li class="content">&nbsp;&nbsp;<span>磁盘使用率</span>
      <div class="progress progress-mini active progress-striped">
        <div id="disk_percent_css" style="width: 0%;" class="bar"></div>
      </div>
      <span id="disk_percent_text" class="percent">0%</span>
      <div id="disk_stat" id="disc_percent_css" class="stat">0 / 0 MB</div>
    </li>
  </ul>
</div>
<!--sidebar-menu-->