/**
 * 清除临时会话数据
 */
function removeSessionData() {
	
	//服务类别临时会话数据
	sessionStorage.removeItem("service_type_id");
	
	//时间临时会话数据
	sessionStorage.removeItem("service_date");
	sessionStorage.removeItem("service_date_str");

	//地址临时会话数据
	sessionStorage.removeItem("addr_id");
	sessionStorage.removeItem("addr_name");

	//订单临时会话数据
	sessionStorage.removeItem("order_no");
	sessionStorage.removeItem("order_id");
	sessionStorage.removeItem("order_pay");
	sessionStorage.removeItem("order_money");

	//优惠劵临时会话数据
	sessionStorage.removeItem("user_coupon_id");
	sessionStorage.removeItem("user_coupon_value");
	sessionStorage.removeItem("user_coupon_name");
}


/**
 * 推荐服务
 *
 * */

function orderRecomment(serviceTypeId){
	var recoArr=[];

	if(serviceTypeId==undefined || serviceTypeId=='' || serviceTypeId==null) return ;

	//金牌保洁
	var jinpai=[
		{"name":"家务包月","url":"","serviceTypeId":""},
		{"name":"油烟机清洗","url":"","serviceTypeId":"36"},
		{"name":"擦玻璃","url":"","serviceTypeId":"54"},
		{"name":"洗衣机清洗","url":"","serviceTypeId":"60"}
	];

	//冰箱清洗
	var bingxiang=[
		{"name":"油烟机清洗","url":"","serviceTypeId":"36"},
		{"name":"金牌保洁","url":"","serviceTypeId":"28"},
		{"name":"洗衣机清洗","url":"","serviceTypeId":"60"},
		{"name":"空调清洗","url":"","serviceTypeId":"51"}
	];

	//擦玻璃
	var caboli=[
		{"name":"油烟机清洗","url":"","serviceTypeId":"36"},
		{"name":"金牌保洁","url":"","serviceTypeId":"28"},
		{"name":"洗衣机清洗","url":"","serviceTypeId":"60"},
		{"name":"地板保养打蜡","url":"","serviceTypeId":"52"}
	];

	//厨卫消毒清洁杀菌
	var chuwei=[
		{"name":"擦玻璃","url":"","serviceTypeId":"54"},
		{"name":"金牌保洁","url":"","serviceTypeId":"28"},
		{"name":"床铺除螨杀菌","url":"","serviceTypeId":"34"},
		{"name":"地板保养打蜡","url":"","serviceTypeId":"52"}
	];

	//床铺除螨杀菌
	var chuangpu=[
		{"name":"擦玻璃","url":"","serviceTypeId":"54"},
		{"name":"地板保养打蜡","url":"","serviceTypeId":"52"},
		{"name":"洗衣机清洗","url":"","serviceTypeId":"60"},
		{"name":"冰箱清洗","url":"","serviceTypeId":"50"}
	];

	//地板保养打蜡
	var diban=[
		{"name":"擦玻璃","url":"","serviceTypeId":"54"},
		{"name":"金牌保洁","url":"","serviceTypeId":"28"},
		{"name":"床铺除螨杀菌","url":"","serviceTypeId":"34"},
		{"name":"房屋大扫除","url":"","serviceTypeId":"53"}
	];

	//房屋大扫除
	var fangwu=[
		{"name":"擦玻璃","url":"","serviceTypeId":"54"},
		{"name":"金牌保洁","url":"","serviceTypeId":"28"},
		{"name":"床铺除螨杀菌","url":"","serviceTypeId":"34"},
		{"name":"房屋大扫除","url":"","serviceTypeId":"53"}
	];

	//空调清洗
	var kongtiao=[
		{"name":"油烟机清洗","url":"","serviceTypeId":"36"},
		{"name":"金牌保洁","url":"","serviceTypeId":"28"},
		{"name":"洗衣机清洗","url":"","serviceTypeId":"60"},
		{"name":"冰箱清洗","url":"","serviceTypeId":"50"}
	];

	//洗衣机清洗
	var xiyiji=[
		{"name":"油烟机清洗","url":"","serviceTypeId":"36"},
		{"name":"金牌保洁","url":"","serviceTypeId":"28"},
		{"name":"空调清洗","url":"","serviceTypeId":"51"},
		{"name":"冰箱清洗","url":"","serviceTypeId":"50"}
	];

	//新居开荒
	var xinju=[
		{"name":"家务包月","url":"","serviceTypeId":""},
		{"name":"地板保养打蜡","url":"","serviceTypeId":"52"},
		{"name":"洗衣机清洗","url":"","serviceTypeId":"60"},
		{"name":"金牌保洁","url":"","serviceTypeId":"28"}
	];

	//油烟机清洗
	var youyanji=[
		{"name":"冰箱清洗","url":"","serviceTypeId":"50"},
		{"name":"洗衣机清洗","url":"","serviceTypeId":"60"},
		{"name":"金牌保洁","url":"","serviceTypeId":"28"},
		{"name":"家务包月","url":"","serviceTypeId":""}
	];

	//家务包月
	var jiawu=[
		{"name":"油烟机清洗","url":"","serviceTypeId":"36"},
		{"name":"洗衣机清洗","url":"","serviceTypeId":"60"},
		{"name":"空调清洗","url":"","serviceTypeId":"51"},
		{"name":"厨卫深度清洁杀菌","url":"","serviceTypeId":"35"}
	];

	//孕家洁
	var yunjiajie=[
		{"name":"月子房","url":"","serviceTypeId":""},
		{"name":"家务包月","url":"","serviceTypeId":""},
		{"name":"油烟机清洗","url":"","serviceTypeId":"36"},
		{"name":"洁宝宝","url":"","serviceTypeId":""}
	];

	//月子房
	var yuezifang=[
		{"name":"洁宝宝","url":"","serviceTypeId":""},
		{"name":"家务包月","url":"","serviceTypeId":""},
		{"name":"油烟机清洗","url":"","serviceTypeId":"36"},
		{"name":"洗衣机清洗","url":"","serviceTypeId":"60"}
	];

	//安居宝
	var anjubao=[
		{"name":"洁宝宝","url":"","serviceTypeId":""},
		{"name":"家务包月","url":"","serviceTypeId":""},
		{"name":"油烟机清洗","url":"","serviceTypeId":"36"},
		{"name":"厨卫深度清洁杀菌","url":"","serviceTypeId":"35"}
	];

	recoArr=[
		{"serviceTypeId":"28","list":jinpai},
		{"serviceTypeId":"50","list":bingxiang},
		{"serviceTypeId":"54","list":caboli},
		{"serviceTypeId":"35","list":chuwei},
		{"serviceTypeId":"34","list":chuangpu},
		{"serviceTypeId":"52","list":diban},
		{"serviceTypeId":"53","list":fangwu},
		{"serviceTypeId":"51","list":kongtiao},
		{"serviceTypeId":"60","list":xiyiji},
		{"serviceTypeId":"56","list":xinju},
		{"serviceTypeId":"36","list":youyanji},
		{"serviceTypeId":"","list":jiawu},
		{"serviceTypeId":"","list":yunjiajie},
		{"serviceTypeId":"","list":yuezifang},
		{"serviceTypeId":"","list":anjubao}
	]

	for(var i=0;i<recoArr.length;i++){
		var reco=recoArr[i];
		var _serviceTypeId = reco.serviceTypeId;
		if(_serviceTypeId==serviceTypeId){
			return reco.list;
		}
	}
}