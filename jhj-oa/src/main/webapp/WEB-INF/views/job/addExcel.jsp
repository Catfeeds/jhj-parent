<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<html>
<head>
    <!--common css for all pages-->
    <%@ include file="../shared/importCss.jsp"%>

    <!--css for this page-->
    <link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>" type="text/css" />

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
                            <h4>读取excel中的手机号码</h4>
                        </header>
                        <hr style="width: 100%; color: black; height: 1px; background-color: black;" />
                        <div class="panel-body">
                            <form action="readExcelMobile" class="form-horizontal" method="POST" enctype="multipart/form-data">
                             	<input type="file" name="excelFile" />
                             	<input type="submit" value="提交" />
                            </form>
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
</body>
</html>
