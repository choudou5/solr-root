<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/console/include/tagLib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>${fns:getSiteName()}-控制台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<%@include file="/WEB-INF/view/console/include/cssLib.jsp" %>
</head>
<body>

<%@include file="/WEB-INF/view/console/include/head.jsp" %>

<%@include file="/WEB-INF/view/console/include/sidebar.jsp" %>

<!--main-content-part-->
<div id="content">

<sys:breadcrumbs currLevel="1"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <div class="row-fluid">
      <div class="span12">
      
      	<div id="app-list" class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-hand-right"></i> </span>
            <h5>应用列表</h5>
            <a href="${ctx }/console/page?path=collection/schema/add" class="fl btn head-btn"> <i class="icon-plus-sign"></i>&nbsp;新建应用</a>
          </div>
          <div class="widget-content">
            <ul class="quick-actions"></ul>
          </div>
        </div>
        
        <!-- begin-statistics -->
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-signal"></i> </span>
            <h5>搜索次数统计</h5>
          </div>
          <div class="widget-content">
            <div id="placeholder"></div>
            <p id="choices"></p>
          </div>
        </div>
        <!-- end-statistics -->
        
      </div>
    </div> 
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>

<!--Turning-series-chart-js-->
<script type="text/javascript">
$(function () {
	
	//生成 随机数据
	function genRandomData(startYear, endYear, cardinal){
	   var datas = new Array();
	   for(var i = 0; startYear <= endYear; i++, startYear++){
	     datas[i] = [startYear, Math.ceil((Math.random()*cardinal))];
	   }
	   return datas;
	}
	
    var datasets = {
        "user": {
            label: "用户",
            data: genRandomData(1988, 2006, 40000)
        },        
        "sysLog": {
            label: "系统日志",
            data: genRandomData(1988, 2006, 10000)
        },
        "collection1": {
            label: "集合1",
            data: genRandomData(1988, 2006, 30000)
        },
        "sword": {
            label: "敏感词",
            data: genRandomData(1988, 2006, 70000)
        },
        "product": {
            label: "商品",
            data: genRandomData(1988, 2006, 60000)
        },
        "ebook": {
            label: "电子书",
            data: genRandomData(1988, 2006, 3000)
        },
        "movie": {
            label: "电影",
            data: genRandomData(1988, 2006, 50000)
        }
    };

    // hard-code color indices to prevent them from shifting as
    // countries are turned on/off
    var i = 0;
    $.each(datasets, function(key, val) {
        val.color = i;
        ++i;
    });
    
    // insert checkboxes 
    var choiceContainer = $("#choices");
    $.each(datasets, function(key, val) {
        choiceContainer.append('<br/><input type="checkbox" name="'+key+'" checked="checked" id="id'+key+'">');
        choiceContainer.append('<label for="id'+key+'">'+val.label+'</label>');
    });
    choiceContainer.find("input").click(plotAccordingToChoices);

    function plotAccordingToChoices() {
        var data = [];
        choiceContainer.find("input:checked").each(function () {
            var key = $(this).attr("name");
            if (key && datasets[key])
                data.push(datasets[key]);
        });

        if (data.length > 0){
            $.plot($("#placeholder"), data, {
                yaxis: { min: 0 },
                xaxis: { tickDecimals: 0 },
                series: {
                    lines: { show: true },
                    points: { show: true }
                },
                grid: { hoverable: true, clickable: true },
            });
            
            // === Point hover in chart === //
            var previousPoint = null;
            $("#placeholder").bind("plothover", function (event, pos, item) {
                if (item) {
                    if (previousPoint != item.dataIndex) {
                        previousPoint = item.dataIndex;
                        $('#tooltip').fadeOut(200,function(){$(this).remove();});
                        var x = item.datapoint[0],
        					y = item.datapoint[1];//.toFixed(2)
                        maruti.flot_tooltip(item.pageX, item.pageY,item.series.label + "： " + y);
                    }
                } else {
        			$('#tooltip').fadeOut(200,function(){$(this).remove();});
                    previousPoint = null;           
                }   
            });	
        }
    }

    plotAccordingToChoices();
});
</script> 
<!--Turning-series-chart-js-->

<script type="text/javascript">
  function goPage (newURL) {
      if (newURL != "") {
          if (newURL == "-" ) {
              resetMenu();            
          }else {  
            document.location.href = newURL;
          }
      }
  }

// resets the menu selection upon entry to this page:
function resetMenu() {
   document.gomenu.selector.selectedIndex = 2;
}

$.ajax({
    type: "GET",
    cache: false,
    async: true,
    dataType: "json",
    url: "/console/index/appList",
    data: {},
    success: function(data, textStatus){
    	if(!_.isEmpty(data)){
    		var bg_list = ["bg_lb", "bg_dy", "bg_lg", "bg_lr", "bg_ly", "bg_db", "bg_ls", "bg_lo", "bg_dg", "bg_lv", "bg_lh"];
    		for (var index in data) {
    			var color = (parseInt(index)+1)%bg_list.length;
				var schema = data[index];
				var name = _.isEmpty(schema.title)?schema.name:schema.title;
				$("#app-list ul.quick-actions").append('<li class="'+bg_list[color]+'"><a href="#" title="'+schema.name+'">'+name+'</a></li>');
			}
    	}else{
			$("#app-list div.widget-content").append('<p align="center" class="font16">您还没有创建应用，立即新建吧！</a>');
    	}
    }
});


</script>
</body>
</html>
