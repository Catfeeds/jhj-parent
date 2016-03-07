<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="row">
	<div class="col-md-12">

		<div class="portlet box light-grey">
			<div class="portlet-title">
				<div class="caption">
					<i class="icon-search"></i>数据检索
				</div>
			</div>
			<div class="portlet-body form">
			
				<!-- BEGIN FORM-->
				<form:form modelAttribute="searchModel" class="form-horizontal" id="convertForm"
					method="Get">
					<div class="form-body">

						<div class="row">
							<!--span-->
							<div class="col-md-5">
								<div class="form-group">
									<label class="control-label col-md-5">开始日期：</label>
									<div class="col-md-5">
										<form:input path="startDate" id="fromDate" class="form-control form_datetime" style="width:110px;" readonly="true"  />
									</div>
								</div>
							</div>
							<!--/span-->
							<!--span-->
							<div class="col-md-5">
								<div class="form-group">
									<label class="control-label col-md-5">结束日期</label>
									<div class="col-md-5">
										<form:input path="endDate"  id="toDate" class="form-control form_datetime" style="width:110px;" readonly="true" />
									</div>
								</div>
							</div>
							<!--/span-->
							
						</div>
					</div>
					<div class="form-actions">
						<div class="row">
							<div class="col-md-5">
								<div class="col-md-offset-10">
									<input type="button" onclick="doSearch();" value="搜索" class="btn btn-success">
								</div>
							</div>
							<div class="col-md-6">
								<div class="col-md-offset-1">
									<input  type="button" value="重置" onclick="doReset();"  class="btn btn-success">
								</div>
							</div>
						</div>
					</div>
				</form:form>
				<!-- END FORM-->
			</div>
		</div>
	</div>
</div>