myApp.onPageBeforeInit('order-deep-choose', function(page) {

	var serviceTypeId = sessionStorage.getItem("service_type_id");
	var result="";
	$$.ajax({
		type:"GET",
		url:siteAPIPath+"dict/get_service_type_addons.json?service_type_id="+serviceTypeId,
		dataType:"json",
		async:false,
		success:function(data){
			var _data=data.data;
			for(var i=0;i<_data.length;i++){
				var name =_data[i].name;
				if(name!="金牌保洁"){
					result+="<ul class='order-rili'>" +
					"<li>"+_data[i].name+"</li>"+
					"<li><span>＋</span>"+
					"<input type='text' value='0'>"+
					" <span>－</span></li></ul>";
				}
			}
			$$("#order-deep-content").html(result);
		}
	});
	
	
	$$("#order-deep-content ul li").on("click",function(){
		var num=parseInt($$(this).find("span:first-child").next().val());
		num+=1;
		$$(this).find("span:first-child").next().val(num);
	});
	
	$$("#order-deep-content ul li").on("click",function(){
		var num=parseInt($$(this).find("span:last-child").prev().val());
		if(num==0){
			
		}
		num-=1;
		$$(this).find("span:first-child").next().val(num);
	});
	
});

