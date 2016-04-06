myApp.onPageBeforeInit('mine', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	getUserInfos(userId);
	
	$$("#mine-order-lists").on("click",function(){
		mainView.router.loadPage("order/order-cal.html?user_id="+userId);
	});
	
	$$("#mine-addr-list").on("click",function(){
		mainView.router.loadPage("user/mine-addr-list.html?user_id="+userId);
	});
	
	$$("#mine-coupon-lists").on("click",function(){
		mainView.router.loadPage("user/coupon/mine-coupon-list.html?user_id="+userId);
	});
	$$("#mine-charge-list").on("click",function(){
		mainView.router.loadPage("user/charge/mine-charge-list.html?user_id="+userId);
	});
	$$("#mine-feedback-info").on("click",function(){
		mainView.router.loadPage("user/mine-feedback-info.html?user_id="+userId);
	});
	$$("#mine-more").on("click",function(){
		mainView.router.loadPage("user/more.html");
	});
	$$("#mine-info").on("click",function(){
		mainView.router.loadPage("user/user-wancheng.html");
	});

	// 点击 余额 --消费明细
   $$("#restMoneyDiv").on('click',function(){
	   mainView.router.loadPage("user/mine-rest-money-detail.html");
   });
	
	
	
	$$('.user-logout').on('click', function() {
		  localStorage.removeItem("mobile");
		  localStorage.removeItem('user_id');
		  localStorage.removeItem('im_username');
		  localStorage.removeItem('im_password');
		  
		 /*
		  *  此处是 用户退出登录操作, 斟酌之后, 结合 require-data.js的改动
		  *  
		  *  用户还在微网站内！！ 决定保留 这两个 “用户无关性” 数据 	
		  */
//		  localStorage.removeItem('service_type_addons_list');
//		  localStorage.removeItem('service_type_list');
		  
		  localStorage.removeItem("am_id");
		  localStorage.removeItem("am_mobile");
		  mainView.router.loadPage("index.html");
	});
	
});


var onUserInfoSuccess =function(data, textStatus, jqXHR) {
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var user = result.data;
	var headImg = user.head_img;
	if(headImg !='' && headImg != undefined){
		$$("#head_img-span").attr("src",user.head_img);
	}
	 $$("#mobile").text(user.mobile);
	 $$("#total_coupon").text(user.total_coupon+" 张");
	 $$("#user-id").text(user.id);
	 $$("#mine-rest-money").text(user.rest_money+" 元");
	 $$("#score").text(user.score);
	 var temp = Number(0);
     var restMoney = user.rest_money;
     temp = restMoney/100;
     if(temp>=100){
    	 temp = 100;
     }
	 $$("#am-time-span").text("约"+temp.toFixed(1)+"小时");
	 
	  // 路径配置
//	    require.config({
//	        paths: {
//	            	echarts: 'http://echarts.baidu.com/build/dist'
//	        }
//	    });
//	    // 使用
//	    require(
//	        [
//	            'echarts',
//	            'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
//	        ],
//	        function (ec) {
//	            // 基于准备好的dom，初始化echarts图表
//	            var myChart = ec.init(document.getElementById('main')); 
//	            option = {
//	            	    tooltip : {
//	            	        formatter: "{a} <br/>{b} : {c}%"
//	            	    },
//	            	    series : [
//	            	        {
//	            	           /* name:'个性化仪表盘',*/
//	            	            type:'gauge',
//	            	            center : ['50%', '50%'],    // 默认全局居中
//	            	            radius : [0, '75%'],
//	            	            startAngle: 140,
//	            	            endAngle : -140,
//	            	            min: 0,                     // 最小值
//	            	            max: 100,                   // 最大值
//	            	            precision: 0,               // 小数精度，默认为0，无小数点
//	            	            splitNumber: 10,             // 分割段数，默认为5
//	            	            axisLine: {            // 坐标轴线
//	            	                show: true,        // 默认显示，属性show控制显示与否
//	            	                lineStyle: {       // 属性lineStyle控制线条样式
//	            	                    color: [[0.2, 'lightgreen'],[0.4, 'orange'],[0.8, 'skyblue'],[1, '#ff4500']], 
//	            	                    width: 30
//	            	                }
//	            	            },
//	            	            axisTick: {            // 坐标轴小标记
//	            	                show: true,        // 属性show控制显示与否，默认不显示
//	            	                splitNumber: 5,    // 每份split细分多少段
//	            	                length :8,         // 属性length控制线长
//	            	                lineStyle: {       // 属性lineStyle控制线条样式
//	            	                    color: '#eee',
//	            	                    width: 1,
//	            	                    type: 'solid'
//	            	                }
//	            	            },
//	            	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
//	            	                show: true,
//	            	                rotate:-90,
//	            	                formatter: function(v){
//	            	                    switch (v+''){
//	            	                        case '0': return '100';
//	            	                        case '20': return '80';
//	            	                        case '40': return '60';
//	            	                        case '60': return '40';
//	            	                        case '80': return '20';
//	            	                        case '100': return '0';
//	            	                        default: return '';
//	            	                    }
//	            	                },
//	            	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
//	            	                    color: '#333'
//	            	                }
//	            	            },
//	            	            splitLine: {           // 分隔线
//	            	                show: true,        // 默认显示，属性show控制显示与否
//	            	                length :30,         // 属性length控制线长
//	            	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
//	            	                    color: '#eee',
//	            	                    width: 2,
//	            	                    type: 'solid'
//	            	                }
//	            	            },
//	            	            pointer : {
//	            	                length : '80%',
//	            	                width : 8,
//	            	                color : 'auto'
//	            	            },
//	            	            tooltip :{
//	            	            	show:false
//	            	            },
//	            	            title : {
//	            	                show : true,
//	            	                offsetCenter: ['-65%', -10],       // x, y，单位px
//	            	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
//	            	                    color: '#333',
//	            	                    fontSize : 15
//	            	                }
//	            	            },
//	            	            detail : {
//	            	                show : false,
//	            	                backgroundColor: 'rgba(0,0,0,0)',
//	            	                borderWidth: 0,
//	            	                borderColor: '#ccc',
//	            	                width: 100,
//	            	                height: 40,
//	            	                offsetCenter: ['-60%', 10],       // x, y，单位px
//	            	                formatter:'{value}%',
//	            	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
//	            	                    color: 'auto',
//	            	                    fontSize : 30
//	            	                }
//	            	            },
//	            	            data:[{value: 30}],
//	            	        }
//	            	    ]
//	            	};
//	    
//	            // 为echarts对象加载数据 
//	            var temp = Number(0);
//	           var restMoney = user.rest_money;
//	           temp = restMoney/100;
//	           if(temp>=100){
//	        	 temp = 100;
//	           }
//	       	 option.series[0].data[0].value = 100-temp.toFixed(1);
//	         myChart.setOption(option, true);
//	        }
//	    );
}

//获取用户信息接口
function getUserInfos(userId) {
	var postdata = {};
    postdata.user_id = userId;    
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json",
		dataType : "json",
		cache : true,
		data :postdata,
		statusCode: {
         	200: onUserInfoSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
}
/*function goToOrderForm() {
	var userId = $$.urlParam('user_id');
	location.href = "wx-order-form.html?user_id=" + userId;
}

function goToOrderList() {
	var userId = $$.urlParam('user_id');
	location.href = "wx-order-list.html?user_id=" + userId;
}*/


