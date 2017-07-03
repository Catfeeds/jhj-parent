<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>
    <title>市场订单数据统计</title>

    <!-- common css for all pages -->
    <%@ include file="../shared/importCss.jsp"%>
    <!-- css for this page -->
    <link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>" rel="stylesheet"
          type="text/css"/>
    <link href="<c:url value='/assets/layui-master/src/css/layui.css'/>" rel="stylesheet" type="text/css"/>
</head>

<body>


<div class="row">
    <div class="col-lg-12">
        <form:form modelAttribute="searchVo" method="GET" onsubmit="return checkEndTime()">
            <div class="form-inline">
                <label>统计时间：</label>
                <form:input path="startTimeStr" class="form-control form-datetime" style="width:110px;"
                            readonly="true"/>
                <input type="submit" value="搜索">
            </div>
        </form:form>

        <form id="form2" name="form2" style="display:none">
            <input type="hidden" id="businessName" value=""/>
            <div class="col-md-10">
                <c:forEach items="${chartUserOrderVoList }" var="m">
                    <label class="checkbox-inline">
                        <input type="radio" name="name" value="${m.name }">
                        ${m.name }
                    </label>
                </c:forEach>
            </div>
            <div class="col-md-5">
                <button type="button" id="btn-modify-name" class="layui-btn layui-btn-normal" onclick="modifyName()">
                    修改人员
                </button>
            </div>
        </form>
        <hr style="width: 100%; color: black; height: 1px; background-color: black;"/>

        <table class="table table-bordered table-striped table-advance table-hover table2excel" id="table2excel">
            <thead>
            <tr>
                <td colspan="29" style="text-align:center">市场订单数据统计</td>
            </tr>
            <tr>
                <td rowspan="2">时间</td>
                <c:forEach items="${chartUserOrderVoList }" var="m">
                    <td colspan="${m.count }" style="text-align:center">${m.name }</td>
                </c:forEach>
            </tr>
            <tr id="business">
                <c:forEach items="${chartUserOrderVoList}" var="m">
                    <c:forEach items="${m.bussineNameList }" var="business">
                        <td class="modify-business">${business }</td>
                    </c:forEach>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${chartDatas.tableDatas}" var="item">
                <tr>
                    <td>${item.time }</td>
                    <c:forEach items="${list }" var="b">
                        <td>
                            ${item[b] }
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
            
            <c:forEach items="${chartDatas.dataList}" var="item">
                <tr>
                    <td>${item.time }</td>
                    <c:forEach items="${chartUserOrderVoList }" var="m">
	                   <td colspan="${m.count }" style="text-align:center">${item[m.name] }</td>
	                </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div>
</div>

<!--common script for all pages-->
<%@ include file="../shared/importJs.jsp"%>
<script type="text/javascript"
        src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
<script type="text/javascript"
        src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>

<%--
<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
<script type="text/javascript" src="<c:url value='/assets/jquery.table2excel.js'/>"></script>
--%>
<script type="text/javascript" src="<c:url value='/assets/layer-v3.0.3/layer/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/assets/layui-master/src/layui.js'/>"></script>
<script src="<c:url value='/js/chart/chartUserOrderCount.js'/>"></script>


<script type="text/javascript">
    $('.form-datetime').datepicker({
        format: 'yyyy-mm',
        language: "zh-CN",
        autoclose: true,
        startView: 1,
        maxViewMode: 1,
        minViewMode: 1,
        todayBtn: true
    });
</script>

</body>
</html>
