<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="row">
	<div class="col-md-10">

		<div class="portlet box light-grey">
			<div class="portlet-body form">
				<!-- BEGIN FORM-->
				<form:form modelAttribute="searchModel" class="form-horizontal"	method="Get">
					<div class="form-body">

						<div class="row">
							<!--span-->
							<div class="col-md-5">
								<div class="form-group">
									<label class="control-label col-md-5">时间周期</label>
									<div class="col-md-5">
										<form:input path="timeCycle" id="timeCycle"
											class="form-control " autocomplete="off"
											placeholder="时间周期" />

									</div>
								</div>
							</div>
							<!--/span-->
							<div class="form-actions">
								<div class="row">
									<div class="col-md-5">
										<div class="col-md-offset-10">
											<button type="submit" class="btn btn-success">搜索</button>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">

							<!--span-->
							<div class="col-md-5">
								<div class="form-group">
									<label class="control-label col-md-5">开始时间</label>
									<div class="col-md-5">
										<form:input path="fromDate" id="fromDate"
											class="form-control " autocomplete="off"
											placeholder="开始时间" />

									</div>
								</div>
							</div>
							<!--/span-->
							<!--span-->
							<div class="col-md-5">
								<div class="form-group">
									<label class="control-label col-md-5">结束时间</label>
									<div class="col-md-5">
										<form:input path="fromDate" id="fromDate"
											class="form-control " autocomplete="off"
											placeholder="结束时间" />

									</div>
								</div>
							</div>
							<!--/span-->
							<div class="form-actions">
								<div class="row">
									<div class="col-md-5">
										<div class="col-md-offset-10">
											<button type="submit" class="btn btn-success">搜索</button>
										</div>
									</div>
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