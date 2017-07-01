//获取定制信息
function getPeriodOrder(){
	var mobile = $("#mobile").val();
	$.ajax({
		type:"get",
		url:"getPeriodOrder?mobile="+mobile,
		dataType:"json",
		success:function(data){
			var periodOrderList = data;
			var selectid = document.getElementById("periodOrderId");
			for (var i = 0; i < periodOrderList.length; i++) {
				var periodName;
				switch (periodOrderList[i].packageType) {
					case 1: periodName="定制一"; break;
					case 2: periodName="定制二"; break;
					case 3: periodName="定制三"; break;
					case 4: periodName="定制四"; break;
					case 5: periodName="定制五"; break;
					case 6: periodName="定制六"; break;
					default:
						break;
				}
				
				selectid[i + 1] = new Option(periodName, periodOrderList[i].id, false, false);
			}
		}
	});
}