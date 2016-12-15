<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="cloudOrgSelect" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<html>
<head>
    <!--common css for all pages-->
    <%@ include file="../shared/importCss.jsp"%>

    <!--css for this page-->
    <link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>" type="text/css" />
    <link href="<c:url value='/assets/bootstrap-datetimepicker/css/datetimepicker.css'/>" rel="stylesheet" type="text/css" />

    <style>
        .tangram-suggestion-main {
            z-index: 1060;
        }
    </style>
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
                        <header class="panel-heading">
                            <h4>会员营销短信</h4>
                        </header>
                        <hr style="width: 100%; color: black; height: 1px; background-color: black;" />
                        <div class="panel-body">
                            <form:form action="save-marketsms" class="form-horizontal" method="POST" name="form" id="marketSms">
                                <input type="hidden" id="marketSmsId" name="marketSmsId" value="${marketSms.marketSmsId }"/>
                                <div class="form-body">
                                    <div class="form-group">
                                        <label class="col-md-2 control-label"><font
                                                color="red">*</font>短信模板ID</label>
                                        <div class="col-md-5">
                                            <input id="smsTempId" name="smsTempId"  class="form-control" value="${marketSms.smsTempId }"/>
                                        </div>
                                    </div>
                                    <div class="form-group required">
                                        <label class="col-md-2 control-label"><font
                                                color="red">*</font>发送用户类型</label>
                                        <div class="col-md-5">
                                            <input type="checkbox" id="isAll" name="userGroupTypeList" value="0" <c:if test="${fn:contains(marketSms.userGroupTypeList,'0') }">checked</c:if> onclick="selectOne(this)"/>全部用户&nbsp;&nbsp;
                                            <input type="checkbox" class="isVip" name="userGroupTypeList" value="1" <c:if test="${fn:contains(marketSms.userGroupTypeList,'1') }">checked</c:if> />会员用户&nbsp;&nbsp;
                                            <input type="checkbox" class="isVip" name="userGroupTypeList" value="2" <c:if test="${fn:contains(marketSms.userGroupTypeList,'2') }">checked</c:if> />非会员用户&nbsp;&nbsp;
                                            <input type="checkbox" class="other" name="userGroupTypeList" value="3" <c:if test="${fn:contains(marketSms.userGroupTypeList,'3') }">checked</c:if> />1个月内未使用用户&nbsp;&nbsp;<br/>
                                            <input type="checkbox" class="other" name="userGroupTypeList" value="4" <c:if test="${fn:contains(marketSms.userGroupTypeList,'4') }">checked</c:if> />3个月内未使用用户&nbsp;&nbsp;
                                            <input type="checkbox" class="other" name="userGroupTypeList" value="5" <c:if test="${fn:contains(marketSms.userGroupTypeList,'5') }">checked</c:if> />6个月内未使用用户&nbsp;&nbsp;
                                            <input type="checkbox" class="other" name="userGroupTypeList" value="6" <c:if test="${fn:contains(marketSms.userGroupTypeList,'6') }">checked</c:if>  />注册未使用用户
                                        </div>
                                    </div>
                                    <c:if test="${marketSms.marketSmsId!= null }">
                                     <div class="form-group">
                                        <label class="col-md-2 control-label"><font
                                                color="red"></font>发送条数</label>
                                        <div class="col-md-5">
                                            <input id="smsNum" name="smsNum"  class="form-control" />
                                        </div>
                                    </div>
                                    </c:if>
                                    <div class="form-group">
                                    	<c:if test="${marketSms.marketSmsId==null }">
	                                        <input type="submit" class="btn btn-primary" value="保存"/>
                                        </c:if>
                                        <c:if test="${marketSms.marketSmsId!= null }">
	                                        <input type="button" id="sendSms" class="btn btn-primary" value="发送短信"/>
	                                        <input type="button" id="testsendSms" class="btn btn-primary" value="测试发送短信"/>
                                        </c:if>
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </section>
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
<!-- js placed at the end of the document so the pages load faster -->
<!--common script for all pages-->
<%@ include file="../shared/importJs.jsp"%>
<!--script for this page-->
<script type="text/javascript" src="<c:url value='/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/assets/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'/>"></script>
<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/market/addMarketSms.js'/>"></script>
</body>
</html>
