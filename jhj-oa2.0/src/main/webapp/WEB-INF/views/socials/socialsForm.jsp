<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<html>
<head>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<!--日期显示 -->
<link
	href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
	rel="stylesheet" type="text/css" />
	<!-- 图片上传 -->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />
</head>

<body>

	<section id="container"> <!--header start--> <%@ include
		file="../shared/pageHeader.jsp"%> <!--header end-->

	<!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%>
	<!--sidebar end--> <!--main content start--> <section id="main-content">
	<section class="wrapper"> <!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading">
			社区活动</header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="socialsVo" id="socials-form" commandName="socialsVo" action="socials-form"
					class="form-horizontal" method="POST" enctype="multipart/form-data">
					<form:hidden path="id"  id="id"/>
					<div class="form-body">
						<div class="form-group">
							<label class="col-md-2 control-label required" >标题*</label>
							<div class="col-md-5">
								<form:input path="title" class="form-control" 
									placeholder="标题" value="${socialsVo.title}" maxlength="100" />
								<span class="field-has-error" id="giftNameResult"></span>
								<form:errors path="title" class="field-has-error"></form:errors>
							</div>
						</div>
						<c:if test="${socialsVo.titleSmallImg != null && socialsVo.titleSmallImg != '' }">
						<div class="form-group ">
							<label class="col-md-2 control-label">题图</label>
							<div class="col-md-5">
								<img src="${ socialsVo.titleSmallImg }"/>
							</div>
						<%-- 	<a href="${path }/bs/deleteHeadImg">删除</a> --%>
						</div>
					</c:if>	
						
						<div class="form-group required">

							<label class="col-md-2 control-label required">题图*</label>
							<div class="col-md-5">
								<form:hidden path="titleImgs" />
								<input id="headImg" type="file"  onchange="getFileSize(this.value)"  name='titleImg' accept="image/*"	data-show-upload="false"/>
								<form:errors path="titleImg" class="field-has-error"></form:errors>
							</div>
							<span style="color:green">(推荐图片格式： 320px X 215px)</span>
						</div>
						<div class="form-group required">
							<label class="col-md-2 control-label required">内容
							</label>
							<div class="col-md-5">
								<form:textarea   class="textarea" id="some-textarea" path="content"
								 placeholder="请输入活动内容"></form:textarea>
								<form:errors path="content" class="field-has-error"></form:errors>
							</div>
							<span style="color:green">(图片可以粘贴入文本)</span>
							<span class="field-has-error" id="content_span"></span>
						</div>
					<div class="form-group required">
						<label class="col-md-2 control-label">是否发布*</label>
						<div class="col-md-5">
								<form:radiobutton path="isPublish" value="0" id="radio1" lable="未发布"/>暂不发布
								<form:radiobutton path="isPublish" value="1" id="radio2" lable="发布"/>发布活动
                         </div>
                    </div>
                    <div class="form-group">
							<label class="col-md-2 control-label required" >活&nbsp;动&nbsp;时&nbsp;间*</label>
							<div class="col-md-5">
							开始日期:
								<div class="input-group date">
									<fmt:formatDate var='formattedDate1' value='${socialsVo.beginDate}' type='both'
										pattern="yyyy-MM-dd" />
										<input type="text" value="${formattedDate1}" 
										id="fromDate" name="beginDate" readonly class="form-control">
									<span class="input-group-addon"><i class="glyphicon glyphicon-th"></i></span>
								</div>
							结束日期:
								<div class="input-group date">
									<fmt:formatDate var='formattedDate2' value='${socialsVo.endDate}' type='both'
										pattern="yyyy-MM-dd" />
										<input type="text" value="${formattedDate2}" id="toDate"
										name="endDate" readonly class="form-control">
									<span class="input-group-addon"><i class="glyphicon glyphicon-th"></i></span>
								</div>
							</div>
						</div>
					</div>
					<div class="form-actions">
						<div class="row">
							<div class="col-md-4" align="right">
								<button class="btn btn-success" id="addSocials_btn" type="button">
									保存</button>
							</div>
							<!-- Button -->
							<div class="col-md-8">
								<button class="btn btn-success" type="reset">重置</button>
							</div>

						</div>
					</div>
					<!-- </fieldset> -->
				</form:form>
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../shared/pageFooter.jsp"%> <!--footer end-->
	</section>
	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
	<!--script for this page-->
	<!-- 日期处理js -->
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<!-- 头像上传 -->
	<script type="text/javascript"	src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>
	
	<!--socials Form表单js  -->
	<script	src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"type="text/javascript"></script>
	<script src="<c:url value='/js/jhj/socials/socialsForm.js'/>"type="text/javascript"></script>
		
	<!--富编辑器js-->
	<script charset="utf-8" src="<c:url value='/assets/kindeditor-4.1.10/kindeditor.js'/>"></script>
	<script charset="utf-8" src="<c:url value='/assets/kindeditor-4.1.10/lang/zh_CN.js'/>"></script>
	
	<script>
       KindEditor.ready(function(K) {
       	 K.create("#some-textarea",{
              	width:'400px',
              	height:'300px',
              	resizeMode:'0',
              	minWidth:'400px',
              	minHeight:'300px',
              	allowUpload : true,
              	//imageUploadJson : 'action/blog/upload_img',
              	items:
              		[
                     'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'cut', 'copy', 'paste',
                  'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                     'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                     'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen'  , '|', 
                      'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                     'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|','insertfile', 'table', 'hr', 'emoticons',
                     'pagebreak','anchor', 'link', 'unlink' 
             ],
             afterBlur: function(){this.sync();}//帮助KindEditor获得textarea的值
       	 });
      });
	</script>
</body>
</html>
