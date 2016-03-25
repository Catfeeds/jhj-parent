$('#org-form').validate({
	errorElement: 'span', //default input error message container
	errorClass: 'help-block', // default input error message class
	focusInvalid: false, // do not focus the last invalid input
	rules: {
		orgName: {
			required: true,
		},
		orgTel:{
			required: true,
			number: true
		},
		orgOwner:{
			required: true
		},
		orgOwnerTel:{
			required: true,
//			number:true
		},
		orgAddr:{
			required: true
		},
		location:{
			required: true
		}


	},

	messages: {
		orgName: {
			required: "请输入门店名称"
		},
		orgTel: {
			required: "请输入门店电话",
			number:"请输入合法数字"
		},
		orgOwner: {
			required: "请输入店长姓名"
		},
		orgOwnerTel: {
			required: "请输入店长电话",
			number:"请输入合法数字"
		},
		orgAddr:{
			required: "请输入门牌号"
		}
	
	},

	invalidHandler: function (event, validator) { //display error alert on form submit
		$('.alert-error', $('#org-form')).show();
	},

	highlight: function (element) { // hightlight error inputs
		$(element)
			.closest('.form-group').addClass('has-error'); // set error class to the control group
	},

	success: function (label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},

	errorPlacement: function (error, element) {
		error.insertAfter(element.parents(".col-md-5"));
	}

});

//校验门店名称是否重复
function valid(){
	var inputName = encodeURIComponent(document.getElementById("orgName").value);

	var option ={
		url:"/jhj-oa/bs/validOrg",	
		type:"get",
		dataType:"text",
		data:{
			name:inputName
		},
		success:function(responseText){
			if(responseText == "no"){
				 $("#showResult").html("门店名称已存在");
                 $("#showResult").css("color","red");
			}else{
				$("#showResult").html("");
				return true;
			}
		},
		error:function(){
			return false;
		}
	} ;
	$.ajax(option);
}

$("#orgForm_btn").click(function(form) {
	if($('#poiAddress').val() == ''){
		alert("请在地图上标注位置");
		return false;
	}
	if (confirm("确认要保存吗?")) {
		if ($('#org-form').validate().form()) {
			form.submit();
		}
	}else{
		return false;
	}
});

//baiduMap 相关
var map = new BMap.Map("containers");//初始化地图       
map.addControl(new BMap.NavigationControl());  //初始化地图控件              
map.addControl(new BMap.ScaleControl());                   
map.addControl(new BMap.OverviewMapControl());     
map.enableScrollWheelZoom(); //滚轮放大缩小控件
var point=new BMap.Point(116.404, 39.915);
map.centerAndZoom(point, 15);//初始化地图中心点
var marker = '';


var gc = new BMap.Geocoder();//地址解析类
 map.addEventListener("click", function(e){
        marker = new BMap.Marker(new BMap.Point(e.point.lng,e.point.lat));
	   map.clearOverlays(); 
        map.addOverlay(marker); 
        marker.enableDragging(); 
           $("#points").val(e.point.lng+'|'+e.point.lat);//获取拖动后的坐标
         //  alert("当前坐标:"+e.point.lng+'/'+e.point.lat);//当前坐标
           marker.addEventListener("dragstart", function(e){
        	    document.getElementById("poiLongitude").value = e.point.lng; 
       			document.getElementById("poiLatitude").value = e.point.lat; 
        	   
        	   gc.getLocation(e.point, function(rs){
        		   
        	        showLocationInfo(e.point, rs);
        	    });
        	});
    });

 
//信息窗口
function showLocationInfo(pt, rs){
    var opts = {
      width : 250,     //信息窗口宽度
      height: 100,     //信息窗口高度
      title : "当前位置"  //信息窗口标题
    }
     
    var addComp = rs.addressComponents;
    var addr = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber + "<br/>";
    addr += "纬度: " + pt.lat + ", " + "经度：" + pt.lng;
    //alert(addr);
    
    //var searchTxt = document.getElementById("suggestId").value; 
    document.getElementById("poiAddress").value = addComp.district 
				+ addComp.street  + addComp.streetNumber; 
		document.getElementById("poiCity").value =  addComp.city;
     
    var infoWindow = new BMap.InfoWindow(addr, opts);  //创建信息窗口对象
    marker.openInfoWindow(infoWindow);
}
 
//搜索提示
	function G(id) {
		return document.getElementById(id);
	}
	
	var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
			{"input" : "suggestId"
			,"location" : map
		});

		ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
		var str = "";
			var _value = e.fromitem.value;
			var value = "";
			if (e.fromitem.index > -1) {
				value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			}    
			str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
			
			value = "";
			if (e.toitem.index > -1) {
				_value = e.toitem.value;
				value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
				
				
				// jhj2.1 地址 名称
				$("#poiAddress").val(_value.district +  _value.street +  _value.business);
				$("#poiCity").val(_value.city);
				
				
			}     
			str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
			G("searchResultPanel").innerHTML = str;
		});

		var myValue;
		ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
		var _value = e.item.value;
			myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
			
			setPlace();
			
		});

		function setPlace(){
			map.clearOverlays();    //清除地图上所有覆盖物
			function myFun(){
				var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
				map.centerAndZoom(pp, 18);
				
				
				//jhj2.1 使用搜索后,设置 选中 目标地点的 坐标
				$("#poiLongitude").val(pp.lng);
				$("#poiLatitude").val(pp.lat);
				
				
				var marker = new BMap.Marker(pp);
				map.addOverlay(new BMap.Marker(pp));    //添加标注
				marker.enableDragging();
				
			}
			var local = new BMap.LocalSearch(map, { //智能搜索
			  onSearchComplete: myFun
			});
			local.search(myValue);
		}



 //页面加载时，判断是否回显地图信息
    function hhh(){
    	var b = document.getElementById("poiLatitude").value;
    	var c =  document.getElementById("poiLongitude").value;
    	
        var pt = new BMap.Point(c,b);
        var marker = new BMap.Marker(pt); //初始化地图标记，通过标记窗口，回显地图信息
        
        var myGeo = new BMap.Geocoder(); 
        //解析记录中  经纬度 确定的 标记点，在信息窗中回显该地址信息
        myGeo.getLocation(pt, function(rs){
        	var opts = {
      	          width : 250,     //信息窗口宽度
      	          height: 50,     //信息窗口高度
      	          title : "当前位置:"  //信息窗口标题
      	        }
  	       var addComp = rs.addressComponents;
        	
  	       var addr =  document.getElementById("poiAddress").value;
        	if(addr == "" ){
        		//若是新增页面。则定位在天安门，不显示信息窗
        		map.centerAndZoom(point, 15);
        	}else{
        		//若是修改页面。则定位在指定位置，并显示信息窗
	        	var infoWindow = new BMap.InfoWindow(addr, opts);  //创建信息窗口对象
	 	        map.addOverlay(marker);
	 	        marker.openInfoWindow(infoWindow);
        	}
        });
    }
    
//修改页面一加载就回显地图位置
window.onload = hhh;

