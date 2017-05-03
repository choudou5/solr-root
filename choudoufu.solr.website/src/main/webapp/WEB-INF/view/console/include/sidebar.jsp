<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!--sidebar-menu-->
<div id="sidebar"><a href="${ctx }/console/page?path=/index" class="visible-phone"><i class="icon icon-home"></i>首页</a>
  <ul id="oneLevelSideBar">
    <li class=""><a href="${ctx }/console/page?path=/index"><i class="icon icon-home"></i> <span>首页</span></a> </li>
    <li><a href="${ctx }/console/collection/list"><i class="icon icon-list"></i><span>应用列表</span></a></li>
    <li class="submenu"> 
    	<a href="#"><i class="icon icon-cog"></i><span>基本配置</span></a>
      <ul>
        <li><a href="${ctx }/console/page?path=collection/schema/add">字段配置</a></li>
        <li><a href="form-validation.html">数据源</a></li>
        <li><a href="form-wizard.html">重建索引</a></li>
      </ul>
    </li>
    <li class="submenu"> 
    	<a href="#"><i class="icon icon-cogs"></i><span>高级配置</span></a>
      <ul>
        <li><a href="form-wizard.html">竞价排名</a></li>
        <li><a href="form-wizard.html">自动分类</a></li>
        <li><a href="form-wizard.html">同义词</a></li>
      </ul>
    </li>
    <li class="submenu"> 
    	<a href="#"><i class="icon icon-magnet"></i><span>运营管理</span></a>
      <ul>
        <li><a href="form-wizard.html">关键字优化</a></li>
        <li><a href="form-validation.html">词库扩展</a></li>
      </ul>
    </li>
    <li class="submenu"> <a href="#"><i class="icon icon-bar-chart"></i> <span>统计及日志</span> <span class="label label-important">4</span></a>
      <ul>
        <li><a href="error403.html">统计</a></li>
        <li><a href="error404.html">日志</a></li>
      </ul>
    </li>
    <li><a href="${ctx }/console/collection/data/search"><i class="icon icon-play"></i><span>体验搜索</span></a></li>
    <li><a href="${ctx}/solr/admin.html" target="_blank"><i class="icon icon-sitemap"></i><span>Solr后台</span></a></li>
    <li class="content">&nbsp;&nbsp; <span>内存使用率</span>
      <div class="progress progress-mini progress-danger active progress-striped">
        <div style="width: 77%;" class="bar"></div>
      </div>
      <span class="percent">77%</span>
      <div class="stat">2604.44 / 4000 MB</div>
    </li>
    <li class="content">&nbsp;&nbsp;<span>磁盘使用率</span>
      <div class="progress progress-mini active progress-striped">
        <div style="width: 87%;" class="bar"></div>
      </div>
      <span class="percent">87%</span>
      <div class="stat">16004.44 / 20000 MB</div>
    </li>
  </ul>
</div>
<!--sidebar-menu-->