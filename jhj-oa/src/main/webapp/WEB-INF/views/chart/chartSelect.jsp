<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>
    <title>市场订单数据统计</title>

    <!-- common css for all pages -->
    <%@ include file="../shared/importCss.jsp"%>
    <!-- css for this page -->
    <link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>" rel="stylesheet" type="text/css" />
    <link href="<c:url value='/assets/layui-master/src/css/layui.css'/>" rel="stylesheet" type="text/css" />
</head>

<body>

<section id="container">
    <!--header start-->
    <%@ include file="../shared/pageHeader.jsp"%>
    <!--header end-->
    <!--sidebar start-->
    <%@ include file="../shared/sidebarMenu.jsp"%>
    <!--sidebar end-->

    <!--main content start-->
    <section id="main-content">
        <section class="wrapper">
            <!-- page start-->

            <div class="row">
                <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
                    <ul class="layui-tab-title">
                        <li class="layui-this">市场订单图表</li>
                        <li>市场人员销售统计</li>
                    </ul>
                    <div class="layui-tab-content">
                        <div class="layui-tab-item layui-show" style="margin:5px">
                            <iframe src="chartOrderFromCount" id="chart-1" name="chart-1" width="100%" scrolling="auto" frameborder="0"></iframe>
                        </div>
                        <div class="layui-tab-item">
                            <iframe src="chartUserOrderNum" id="chart-2" name="chart-2" width="100%" scrolling="auto" frameborder="0"></iframe>
                        </div>
                    </div>
                </div>
            </div>

            <!-- page end--> </section> </section>
    <!--main content end-->
    <!--footer start-->
    <%@ include file="../shared/pageFooter.jsp"%>
    <!--footer end-->
</section>
<!--common script for all pages-->
<%@ include file="../shared/importJs.jsp"%>
<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>

<script type="text/javascript" src="<c:url value='/assets/layer-v3.0.3/layer/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/assets/layui-master/src/layui.js'/>"></script>

<script type="text/javascript">
    $('.form-datetime').datepicker({
        format: 'yyyy-mm',
        language : "zh-CN",
        autoclose : true,
        startView: 1,
        maxViewMode: 1,
        minViewMode:1,
        todayBtn : true
    });

    $('#chart-1').load(function() { //方法2  
        var iframeHeight=$(this).contents().height();  
        $(this).height(iframeHeight+'px');   
    });
    
    $('#chart-2').load(function() { //方法2  
        var iframeHeight=$(this).contents().height();  
        $(this).height(iframeHeight+'px');   
    });
    
    $(function(){
        layui.use('element', function(){
            var element = layui.element();
            console.log("element="+element)
            //一些事件监听

            element.on('tab(docDemoTabBrief)', function(data){
                console.log(data);
            });
        });
        
    })
</script>

</body>
</html>
