<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ include file="../shared/taglib.jsp"%>


<html>
  <head>
	
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!--css for this page-->

  </head>

  <body>

  <section id="container" >
	  
	  <!--header start-->
	  <%@ include file="../shared/pageHeader.jsp"%>
	  <!--header end-->
	  
      <!--sidebar start-->
	  <%@ include file="../shared/sidebarMenu.jsp"%>
      <!--sidebar end-->
      
      <!--main content start-->
      <section id="main-content">
          <section class="wrapper">
              <!--state overview start-->
              <div class="row state-overview">
                  <div class="col-lg-3 col-sm-6">
                      <section class="panel">
                          <div class="symbol terques">
                              <i class="icon-user"></i>
                          </div>
                          <div class="value">
                              <h1 class="count">
                                  0
                              </h1>
                              <p>用户数</p>
                          </div>
                      </section>
                  </div>
                 <a href="../user/home-user-list">
	                  <div class="col-lg-3 col-sm-6">
	                      <section class="panel">
	                          <div class="symbol red">
	                              <i class="icon-tags"></i>
	                          </div>
	                          <div class="value">
	                              <h1 class=" count2">
	                                  0
	                              </h1>
	                              <p>今日新增用户</p>
	                          </div>
	                      </section>
	                  </div>
                  </a>
                  <div class="col-lg-3 col-sm-6">
                      <section class="panel">
                          <div class="symbol yellow">
                              <i class="icon-shopping-cart"></i>
                          </div>
                          <div class="value">
                              <h1 class=" count3">
                                  0
                              </h1>
                              <p>总订单数</p>
                          </div>
                      </section>
                  </div>
                  <div class="col-lg-3 col-sm-6">
                      <section class="panel">
                          <div class="symbol blue">
                              <i class="icon-bar-chart"></i>
                          </div>
                          <div class="value">
                              <h1 class=" count4" style="margin-top: -18px">
                                  ${totalOrderToday.total }
                              </h1>
                              <p>今日订单</p>
                              <a href="../order/order-hour-list?flag=1" style="float:left;">
                              	<p><font color="blue">基础保洁</font></p>
                              	<span id="jcbjtj">${totalOrderToday.jc }</span>
                              </a>
                              <a href="../order/order-exp-list?flag=2" >
                              	<p><font color="blue">深度服务</font></p>
                              	<span id="sdfwtj">${totalOrderToday.sd }</span>
                              </a>
                          </div>
                      </section>
                  </div>
              </div>
              <!--state overview end-->
			  <hr style="width: 100%; color: black; height: 1px; background-color: black;" />
              <div class="row">
                  <div class="col-lg-12">
                      <!--custom chart start-->

                      <div id="main" style="height: 400px; width: 950px"></div>
 
                  </div>

              </div>

          </section>
      </section>
      <!--main content end-->
      
      <!--footer start-->
      <%@ include file="../shared/pageFooter.jsp"%>
      <!--footer end-->
  </section>

    <!-- js placed at the end of the document so the pages load faster -->
    <!--common script for all pages-->
    <%@ include file="../shared/importJs.jsp"%>


    <!--script for this page-->
    <script src="<c:url value='/js/count.js'/>"></script>
		<script>
		countUp("${totalUser}");
		countUp2("${totalUserToday}");
		countUp3("${totalOrder}");
		/* countUp4("${totalOrderToday}"); */
	</script>
	<script src="<c:url value='/js/jhj/index.js'/>"></script>
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script type="text/javascript" src="<c:url value='/js/chart/chartOrder.js'/>"></script>

	<script>

		var legend = ${chartDatas.legend};
		var xAxis = ${chartDatas.xAxis};
		var seriesDatas = ${chartDatas.series};
		loadOrderChart(legend, xAxis, seriesDatas);
	</script>

  </body>
</html>
