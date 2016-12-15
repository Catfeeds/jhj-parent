<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<!-- taglib for this page -->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<html>
<head>
    <!--common css for all pages-->
    <%@ include file="../shared/importCss.jsp"%>
    <!--css for this page-->
    <link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>" rel="stylesheet"
          type="text/css" />
    <link href="<c:url value='/assets/bootstrap-datetimepicker/css/datetimepicker.css'/>" rel="stylesheet" type="text/css" />
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
                <div class="col-lg-12">
                    <section class="panel">
                       
                        <a href="add-marketsms"><button type="button" id="btnNew" class="btn btn-primary" >新增</button></a>

                        <table class="table table-condensed table-hover table-striped" id="table2excel">
                            <thead>
                            <tr>
                                <th>发送时间</th>
                                <th>短信ID</th>
                                <th>用户类型</th>
                                <th>发送数量(条)</th>
                                <th>失败数量(条)</th>
                                <th>成功率(%)</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${page.list}" var="item">
                                <tr>
                                    <td><timestampTag:timestamp patten="yyyy-MM-dd HH:mm:ss" t="${item.addTime * 1000}" /></td>
                                    <td>${ item.smsTempId }</td>
                                    <td>${ item.userGroupType }</td>
                                    <td>${ item.totalSend }</td>
                                    <td>${ item.totalFail }</td>
                                    <td>${ item.totalSended/item.totalSend*100 }%</td>
                                    <td><a href="add-marketsms?marketSmsId=${item.marketSmsId }">编辑</a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </section>
                    <%@ include file="../shared/importJs.jsp"%>
                    <c:import url="../shared/paging.jsp">
                        <c:param name="pageModelName" value="page" />
                        <c:param name="urlAddress" value="/market/get-list" />
                    </c:import>
                </div>
            </div>
            <!-- page end-->
        </section>
    </section>
    <!--main content end-->
    <!--footer start-->
    <%@ include file="../shared/pageFooter.jsp"%>
    <!--footer end-->
</section>

<script src="<c:url value='/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'/>"></script>
<script src="<c:url value='/assets/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'/>"></script>

<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
</body>
</html>
