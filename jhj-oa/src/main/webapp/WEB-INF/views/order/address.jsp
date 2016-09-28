<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="exampleModalLabel">添加地址</h4>
      </div>
      <div class="modal-body">
        <form>
          <input type="hidden" name="poiLatitude" id="poiLatitude"/>
          <input type="hidden" name="poiLongitude" id="poiLongitude"/>
          <input type="hidden" name="poiAddress" id="poiAddress"/>
		  <input type="hidden" name="poiCity" id="poiCity"/>
		  <input type="hidden" name="userId"/>
          
          <div class="form-group required">
			<label class="col-md-2 control-label">服务地址：</label>
				
			<input type="text" id="suggestId" size="20" value="请输入位置" style="width:150px;" name="name"/>
			<div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
			<br/><br/>
			<div style="margin-left:30px;width:520px;height:340px;border:1px solid gray" 
				id="containers"></div>
		   </div>
		
           <div class="form-group">
            <label for="message-text" class="control-label">门牌号：</label>
            <input type="text" class="form-control" id="recipient-addr" name="addr"/>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" onclick="saveAddress()">保存</button>
      </div>
    </div>
  </div>
</div>
