myApp.onPageBeforeInit('order-rate', function(page) {

	var userId = localStorage['user_id'];

	var orderId = sessionStorage.getItem("order_id");

	var staffNames = sessionStorage.getItem("staff_names");

	var orderNo = sessionStorage.getItem("order_no");

	var orderType = sessionStorage.getItem("order_type");

	console.log("orderType = " + orderType);
	var postdata = {};
	postdata.order_no = orderNo;
	if (orderType == 0) {
		$$.ajax({
			type : "GET",
			url : siteAPIPath + "order/order_hour_detail.json",
			dataType : "json",
			data : postdata,
			cache : true,
			async : false, // 不能是异步
			success : function(data) {
				console.log(data);
				var staffNameStr = "";
				list = data.data.order_dispatchs;
				console.log(list);
				$$.each(list, function(i, item){
					console.log(i);
					staffNameStr+='<a href="order/order-rate-staff.html?staff_id='+item.staff_id+'">'+item.staff_name+'</a>&nbsp;';
				});
				console.log(staffNameStr);
				$$("#staffNameStr").html(staffNameStr);
			}
		});
	} else if (orderType == 1) {
		$$.ajax({
			type : "GET",
			url : siteAPIPath + "order/get_exp_clean_order_detail.json",
			dataType : "json",
			data : postdata,
			cache : true,
			async : false, // 不能是异步
			success : function(data) {
				var staffNameStr = "";
				list = data.data.order_dispatchs;
				$$.each(list, function(i, item){
					staffNameStr+='<a href="order/order-rate-staff.html?staff_id='+item.staff_id+'">'+item.staff_name+'</a>&nbsp;';
				});
				$$("#staffNameStr").html(staffNameStr);
			}
		})
	}


	$$("#orderId").val(orderId);
//	$$("#staffNameStr").html(staffNames);

	$$("#rateAttitude").val(0);
	$$("#rateSkill").val(0);

	//最多只能输入254个字
	$$("#rateContent").keydown(function() {
		var curLength = $$("#rateContent").val().length;
		if (curLength >= 253) {
			var content = $$("#rateContent").val().substr(0, 254);
			$$("#rateContent").val(content);
		}
	});

	$$("#rateSubmit").on("click",function(){

		var rateArrival = $$("#rateArrival").val();
		var rateAttitude = $$("#rateAttitude").val();
		var rateSkill = $$("#rateSkill").val();

		if (rateArrival == "") {
			myApp.alert("请评价到达时间.");
			return false;
		}

		if (rateAttitude == "" || rateAttitude == 0) {
			myApp.alert("请评价服务态度.");
			return false;
		}

		if (rateSkill == "" || rateSkill == 0) {
			myApp.alert("请评价服务技能.");
			return false;
		}



		var params = {};
		params.user_id = userId;
		params.order_id = orderId;
		params.rate_arrival = $$("#rateArrival").val();
		params.rate_attitude = $$("#rateAttitude").val();
		params.rate_skill = $$("#rateSkill").val();

		var rateContent = $$("#rateContent").val();
		if (rateContent == undefined) rateContent = "";
		params.rate_content = rateContent;
		
//		return false;

		$$.ajax({
			type : "POST",
			url : siteAPIPath + "order/post_rate.json",
			dataType : "json",
			cache : true,
			async : false,
			data : params,
			success : function(data) {

				var s = data.status;
				if (s == "999") {
					myApp.alert(data.msg);
					return false;
				}

				mainView.router.loadPage("order/order-rate-success.html");
			}
		})
	});

//	$$("#rateImgTrigger").on("click",function(){
//		$$("#rateImgFile").trigger("click");
//	});


});

function doRateArrival(v) {
	if (v == 0) {
		$$("#rateArrival_0").removeClass("waiter1-2").addClass("waiter1-1");
		$$("#rateArrival_1").removeClass("waiter1-1").addClass("waiter1-2");
	}

	if (v == 1) {
		$$("#rateArrival_0").removeClass("waiter1-1").addClass("waiter1-2");
		$$("#rateArrival_1").removeClass("waiter1-2").addClass("waiter1-1");
	}

	$$("#rateArrival").val(v);
}

function doRateAttitude(v) {

	for (var i = 1; i <= 5; i++) {
		if (v >= i) {
			$$("#rateAttitude_" + i).attr("src", "img/yudingayi/xx.png");
		} else {
			$$("#rateAttitude_" + i).attr("src", "img/yudingayi/xx1.png");
		}
	}
	$$("#rateAttitude").val(v);
}

function doRateSkill(v) {

	for (var i = 1; i <= 5; i++) {
		if (v >= i) {
			$$("#rateSkill_" + i).attr("src", "img/yudingayi/xx.png");
		} else {
			$$("#rateSkill_" + i).attr("src", "img/yudingayi/xx1.png");
		}
	}
	$$("#rateSkill").val(v);
}

//function rateUpload() {
//	$$("#rateImgFile").focus().trigger('click');
//}


//预览图片
function setImagePreviews(obj) {
	var files = obj.files;
	if(files.length>0){
	    var hml='';
	    var index=0;
	    for(var i= 0,len=files.length;i<len;i++){
	    	var reader = new FileReader();
			var f = files[i];
			if(!/image\/\w+/.test(f.type)){
				alert("文件必须为图片！"); 
				return false;
			}
			reader.readAsDataURL(f);
			reader.onload=function(e){
				hml+="<div class='waiter4-1'><img src='"+e.target.result+"' alt=''/></div>"
				+"<div class='waiter4-2' id='delImg' onclick='delImg(this)'>×</div>";
				index++;
				if(index<=4){
					$$("#img").html(hml);
				}
			}
	    }
    }
}

//删除图片
function delImg(obj){
	var ele=$$(obj).prev();
	ele.remove();
	$$(obj).remove();
}
