myApp.onPageBeforeInit('user-am-detail-page', function(page) {
	
	//新用户引导其添加地址，否则不让查看助理页
	var nowAmId = localStorage['am_id'];
	
	if(nowAmId == 'null'){
		myApp.alert('您还没有添加地址,点击确定前往添加地址立刻获得家庭助理', "", function () {
			mainView.router.loadPage("user/mine-add-addr.html?addr_id=0&return_url=user/user-am-detail.html");
	    });
		
		return;
	}
	
	
	var userId = localStorage['user_id'];
	var postData = {};
		
	postData.user_id = userId;
	
	$$.ajax({
		type:"get",
		url  : siteAPIPath+"user/user_get_am.json",
        data : postData,
        cache: true,
		success: function(datas,status,xhr){
			
			
			
			
			var result = JSON.parse(datas);
			
			var formResult = result.data;
			
			if (formResult != "") {
				localStorage.setItem('am_id', formResult.staff_id);
				localStorage.setItem('am_mobile', formResult.mobile);
			}
			
			
			if(formResult.head_img != ""){
				$$("#staffImg").attr("src",formResult.head_img);
			}else{
				$$("#staffImg").attr("src","./img/i-f7.png");
			}
			
			$$("#staffName").text("姓名:  "+formResult.name);
			$$("#cardNo").text("身份证号:  "+formResult.card_id);
			
			//根据出生日期，得出年龄
			var reg =/[\u4E00-\u9FA5]/g;	//去除中文
			
			var bbb =   moment(formResult.birth);
			var aaa =  moment(bbb).fromNow();
			
			$$("#age").text("年龄:  "+aaa.replace(reg,'')+"岁");
			
			//呼叫助理
			$$("#callStaff").attr("href","tel:"+formResult.mobile);
			
			
//			$$("#astro").text(formResult.astro);
			
			var astroName = getAstroName(formResult.astro);
			
			$$("#astro").text(astroName);
			
			$$("#bloodType").text(formResult.blood_type);
			
			for(var i = 0; i<formResult.tag_list.length;i++){
				$$("#tagName").append("<a href='#' >"+formResult.tag_list[i].tag_name+"</a>&nbsp&nbsp&nbsp");
			}
			
			$$("#orderNum").text("助理"+formResult.name+"已经为您服务"+formResult.order_num+"次啦");
			
			$$("#intro").text(formResult.intro);
			
		}
	});

	
});
