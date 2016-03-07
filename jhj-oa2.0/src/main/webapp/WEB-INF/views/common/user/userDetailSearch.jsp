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
				<form:form modelAttribute="searchModel" class="form-horizontal"
					method="Get">
					<div class="form-body">

						<div class="row">

							<!--span-->
							<div class="col-md-5">
								<div class="form-group">
									<label class="control-label col-md-5">会员手机号</label>
									<div class="col-md-5">
										<form:input path="mobile" id="mobile1"
											class="form-control " autocomplete="off"
											placeholder="会员手机号" />

									</div>
								</div>
							</div>
							<!--/span-->
							<!--span-->
							<div class="col-md-5">
								<div class="form-group">
									<label class="control-label col-md-5">订单号</label>
									<div class="col-md-5">
										<form:input path="orderNo" id="orderNo1"
											class="form-control " autocomplete="off"
											placeholder="订单号" />

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
									<button type="submit" class="btn btn-success">搜索</button>
								</div>
							</div>
							<div class="col-md-6">
								<div class="col-md-offset-1">
									<button type="reset" onclick="doReset();"  class="btn btn-success">重置</button>
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