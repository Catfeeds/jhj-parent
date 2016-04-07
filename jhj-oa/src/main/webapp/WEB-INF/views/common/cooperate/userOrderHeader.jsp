<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="row">
	<div class="col-md-12">

		<div class="portlet box light-grey">
			<div class="portlet-title">
				<div class="caption">
					<i class="icon-search"></i> 
					<strong>合作商户引流用户列表</strong>
				</div>
			</div>
			<div class="portlet-body form">
					<div class="form-body">

						<div class="row">

							<div class="col-md-5">
								<div class="form-group">
									<label class="control-label col-md-3">总用户数</label>
									<div class="col-md-5">
										${sumUser }
									</div>
								</div>
							</div>
							<div class="col-md-5">
								<div class="form-group">
									<label class="control-label col-md-3">总订单数</label>
									<div class="col-md-5">
										${sumOrder }
									</div>
								</div>
							</div>
						</div>
					</div>
			</div>
		</div>
	</div>
</div>