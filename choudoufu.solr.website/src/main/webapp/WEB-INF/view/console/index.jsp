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

<sys:breadcrumbs requestSpace="/console/" currLevel="1"/>

  <!--container begin-->
  <div class="container-fluid">
    
    <!-- begin-statistics -->
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="icon-signal"></i> </span>
            <h5>搜索次数统计</h5>
          </div>
          <div class="widget-content">
            <div id="placeholder"></div>
            <p id="choices"></p>
          </div>
        </div>
      </div>
    </div> <!-- end-statistics -->
    
  </div><!--end-container-->
  
</div><!--end-main-content-part-->

<%@include file="/WEB-INF/view/console/include/footer.jsp" %>

<%@include file="/WEB-INF/view/console/include/scriptLib.jsp" %>

<!--Turning-series-chart-js-->
<script type="text/javascript">
$(function () {
    var datasets = {
        "user": {
            label: "用户",
            data: [[1988, 483994], [1989, 479060], [1990, 457648], [1991, 401949], [1992, 424705], [1993, 402375], [1994, 377867], [1995, 357382], [1996, 337946], [1997, 336185], [1998, 328611], [1999, 329421], [2000, 342172], [2001, 344932], [2002, 387303], [2003, 440813], [2004, 480451], [2005, 504638], [2006, 528692]]
        },        
        "sysLog": {
            label: "系统日志",
            data: [[1988, 218000], [1989, 203000], [1990, 171000], [1992, 42500], [1993, 37600], [1994, 36600], [1995, 21700], [1996, 19200], [1997, 21300], [1998, 13600], [1999, 14000], [2000, 19100], [2001, 21300], [2002, 23600], [2003, 25100], [2004, 26100], [2005, 31100], [2006, 34700]]
        },
        "collection1": {
            label: "集合1",
            data: [[1988, 62982], [1989, 62027], [1990, 69696], [1991, 62348], [1992, 58560], [1993, 59393], [1994, 54579], [1995, 59818], [1996, 50554], [1997, 48276], [1998, 47691], [1999, 247529], [2000, 47778], [2001, 48760], [2002, 50949], [2003, 57452], [2004, 60234], [2005, 60076], [2006, 59213]]
        },
        "sword": {
            label: "敏感词",
            data: [[1988, 55627], [1989, 55475], [1990, 58464], [1991, 55134], [1992, 52436], [1993, 47139], [1994, 43962], [1995, 43238], [1996, 42395], [1997, 40854], [1998, 40993], [1999, 41822], [2000, 41147], [2001, 40474], [2002, 40604], [2003, 40044], [2004, 38816], [2005, 38060], [2006, 36984]]
        },
        "product": {
            label: "商品",
            data: [[1988, 3813], [1989, 3719], [1990, 8722], [1991, 3789], [1992, 13720], [1993, 9730], [1994, 3636], [1995, 13598], [1996, 7610], [1997, 13655], [1998, 53695], [1999, 3673], [2000, 73553], [2001, 63774], [2002, 23728], [2003, 53618], [2004, 3638], [2005, 23467], [2006, 43770]]
        },
        "ebook": {
            label: "电子书",
            data: [[1988, 16402], [1989, 6474], [1990, 6605], [1991, 6209], [1992, 86035], [1993, 6020], [1994, 6000], [1995, 6018], [1996, 13958], [1997, 5780], [1998, 65954], [1999, 26178], [2000, 66411], [2001, 345993], [2002, 55833], [2003, 55791], [2004, 5450], [2005, 5521], [2006, 55271]]
        },
        "movie": {
            label: "电影",
            data: [[1988, 4382], [1989, 14498], [1990, 6535], [1991, 4398], [1992, 4766], [1993, 8441], [1994, 14670], [1995, 4217], [1996, 4275], [1997, 4203], [1998, 4482], [1999, 44506], [2000, 34358], [2001, 4385], [2002, 85269], [2003, 45066], [2004, 75194], [2005, 44887], [2006, 74891]]
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
        choiceContainer.append('<br/><input type="checkbox" name="' + key +
                               '" checked="checked" id="id' + key + '">' +
                               '<label for="id' + key + '">'
                                + val.label + '</label>');
    });
    choiceContainer.find("input").click(plotAccordingToChoices);

    
    function plotAccordingToChoices() {
        var data = [];

        choiceContainer.find("input:checked").each(function () {
            var key = $(this).attr("name");
            if (key && datasets[key])
                data.push(datasets[key]);
        });

        if (data.length > 0)
            $.plot($("#placeholder"), data, {
                yaxis: { min: 0 },
                xaxis: { tickDecimals: 0 }
            });
    }

    plotAccordingToChoices();
});
</script> 
<!--Turning-series-chart-js-->
<%-- <script src="${ctxStaticConsole }/js/matrix.dashboard.js"></script> --%>

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
</script>
</body>
</html>
