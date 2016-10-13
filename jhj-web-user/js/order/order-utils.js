/**
 * 清除临时会话数据
 */
function removeSessionData() {
	
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
		{"name":"油烟机清洗","url":"","serviceTypeId":""},
		{"name":"擦玻璃","url":"","serviceTypeId":""},
		{"name":"洗衣机清洗","url":"","serviceTypeId":""}
	];

	//冰箱清洗
	var bingxiang=[
		{"name":"油烟机清洗","url":"","serviceTypeId":""},
		{"name":"金牌保洁","url":"","serviceTypeId":""},
		{"name":"洗衣机清洗","url":"","serviceTypeId":""},
		{"name":"空调清洗","url":"","serviceTypeId":""}
	];

	//擦玻璃
	var caboli=[
		{"name":"油烟机清洗","url":"","serviceTypeId":""},
		{"name":"金牌保洁","url":"","serviceTypeId":""},
		{"name":"洗衣机清洗","url":"","serviceTypeId":""},
		{"name":"地板包养打蜡","url":"","serviceTypeId":""}
	];

	//厨卫消毒清洁杀菌
	var chuwei=[
		{"name":"擦玻璃","url":"","serviceTypeId":""},
		{"name":"金牌保洁","url":"","serviceTypeId":""},
		{"name":"床铺除螨杀菌","url":"","serviceTypeId":""},
		{"name":"地板包养打蜡","url":"","serviceTypeId":""}
	];

	//床铺除螨杀菌
	var chuangpu=[
		{"name":"擦玻璃","url":"","serviceTypeId":""},
		{"name":"地板包养打蜡","url":"","serviceTypeId":""},
		{"name":"洗衣机清洗","url":"","serviceTypeId":""},
		{"name":"冰箱清洗","url":"","serviceTypeId":""}
	];

	//地板包养打蜡
	var diban=[
		{"name":"擦玻璃","url":"","serviceTypeId":""},
		{"name":"金牌保洁","url":"","serviceTypeId":""},
		{"name":"床铺除螨杀菌","url":"","serviceTypeId":""},
		{"name":"房屋大扫除","url":"","serviceTypeId":""}
	];

	//房屋大扫除
	var fangwu=[
		{"name":"擦玻璃","url":"","serviceTypeId":""},
		{"name":"金牌保洁","url":"","serviceTypeId":""},
		{"name":"床铺除螨杀菌","url":"","serviceTypeId":""},
		{"name":"房屋大扫除","url":"","serviceTypeId":""}
	];

	//空调清洗
	var kongtiao=[
		{"name":"油烟机清洗","url":"","serviceTypeId":""},
		{"name":"金牌保洁","url":"","serviceTypeId":""},
		{"name":"洗衣机清洗","url":"","serviceTypeId":""},
		{"name":"冰箱清洗","url":"","serviceTypeId":""}
	];

	//洗衣机清洗
	var xiyiji=[
		{"name":"油烟机清洗","url":"","serviceTypeId":""},
		{"name":"金牌保洁","url":"","serviceTypeId":""},
		{"name":"空调清洗","url":"","serviceTypeId":""},
		{"name":"冰箱清洗","url":"","serviceTypeId":""}
	];

	//新居开荒
	var xinju=[
		{"name":"家务包月","url":"","serviceTypeId":""},
		{"name":"地板包养打蜡","url":"","serviceTypeId":""},
		{"name":"洗衣机清洗","url":"","serviceTypeId":""},
		{"name":"金牌保洁","url":"","serviceTypeId":""}
	];

	//油烟机清洗
	var youyanji=[
		{"name":"冰箱清洗","url":"","serviceTypeId":""},
		{"name":"洗衣机清洗","url":"","serviceTypeId":""},
		{"name":"金牌保洁","url":"","serviceTypeId":""},
		{"name":"家务包月","url":"","serviceTypeId":""}
	];

	//家务包月
	var jiawu=[
		{"name":"油烟机清洗","url":"","serviceTypeId":""},
		{"name":"洗衣机清洗","url":"","serviceTypeId":""},
		{"name":"空调清洗","url":"","serviceTypeId":""},
		{"name":"厨卫深度清洁杀菌","url":"","serviceTypeId":""}
	];

	//孕家洁
	var yunjiajie=[
		{"name":"月子房","url":"","serviceTypeId":""},
		{"name":"家务包月","url":"","serviceTypeId":""},
		{"name":"油烟机清洗","url":"","serviceTypeId":""},
		{"name":"洁宝宝","url":"","serviceTypeId":""}
	];

	//月子房
	var yuezifang=[
		{"name":"洁宝宝","url":"","serviceTypeId":""},
		{"name":"家务包月","url":"","serviceTypeId":""},
		{"name":"油烟机清洗","url":"","serviceTypeId":""},
		{"name":"洗衣机清洗","url":"","serviceTypeId":""}
	];

	//安居宝
	var anjubao=[
		{"name":"洁宝宝","url":"","serviceTypeId":""},
		{"name":"家务包月","url":"","serviceTypeId":""},
		{"name":"油烟机清洗","url":"","serviceTypeId":""},
		{"name":"厨卫深度清洁杀菌","url":"","serviceTypeId":""}
	];

	recoArr=[
		{"serviceTypeId":"28","list":jinpai},
		{"serviceTypeId":"","list":bingxiang},
		{"serviceTypeId":"","list":caboli},
		{"serviceTypeId":"","list":chuwei},
		{"serviceTypeId":"","list":chuangpu},
		{"serviceTypeId":"","list":diban},
		{"serviceTypeId":"","list":fangwu},
		{"serviceTypeId":"","list":kongtiao},
		{"serviceTypeId":"","list":xiyiji},
		{"serviceTypeId":"","list":xinju},
		{"serviceTypeId":"","list":youyanji},
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