<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!--Header-part-->
<div id="header">
  <h1><a href="dashboard.html">SolrHome</a></h1>
</div>
<!--close-Header-part--> 

<!--top-Header-menu-->
<div id="user-nav" class="navbar navbar-inverse">
  <ul class="nav">
    <li class="dropdown" id="menu-messages">
      <a href="#" data-toggle="dropdown" data-target="#menu-messages" class="dropdown-toggle">
      	<i class="icon icon-comments"></i> 
      	<span class="text">消息</span> <span class="label label-important">5</span> <b class="caret"></b>
      </a>
      <ul class="dropdown-menu">
        <li><a class="sAdd" title="" href="#"><i class="icon-comments"></i> 留言</a></li>
        <li class="divider"></li>
        <li><a class="sInbox" title="" href="#"><i class="icon-bell"></i> 公告</a></li>
       <!--  <li class="divider"></li>
        <li><a class="sOutbox" title="" href="#"><i class="icon-arrow-up"></i> outbox</a></li>
        <li class="divider"></li>
        <li><a class="sTrash" title="" href="#"><i class="icon-trash"></i> trash</a></li> -->
      </ul>
    </li>
    <li  class="dropdown" id="profile-messages" >
   	  <a title="" href="#" data-toggle="dropdown" data-target="#profile-messages" class="dropdown-toggle"><i class="icon icon-user"></i><span class="text">欢迎 ${fns:getSessionUserName(sid) }</span><b class="caret"></b></a>
      <ul class="dropdown-menu">
        <li><a href="${ctx }/console/sys/user/updatePwd""><i class="icon-user"></i> 修改密码</a></li>
        <li class="divider"></li>
        <li><a href="${ctx }/console/login/out"><i class="icon icon-share-alt"></i> 退出</a></li>
      </ul>
    </li>
    <!-- <li class=""><a title="" href="#"><i class="icon icon-cog"></i> <span class="text">Settings</span></a></li> 
    <li class=""><a title="" href="${ctx }/console/login/out"><i class="icon icon-share-alt"></i> <span class="text">退出</span></a></li>
    -->
  </ul>
</div>
<!--close-top-Header-menu-->

<!--start-top-serch-->
<div id="search">
  <input type="text" placeholder="Search here..."/>
  <button type="submit" class="tip-bottom" title="Search"><i class="icon-search icon-white"></i></button>
</div>
<!--close-top-serch-->