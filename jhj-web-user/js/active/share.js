myApp.onPageInit('share', function (page) {
	
	var url = "http://jia-he-jia.com/u/index.html?share_user_id="+localStorage.getItem("user_id");
	
	$$("#share-btn").on('click',function(){
		
		$$.post(siteAPIPath+"wxShare.json",{"url":url},function(data){
			var result = JSON.parse(data).data;
			console.log(data);
			wx.config({
			  "appId": result.appId, // 必填，公众号的唯一标识
			  "timestamp": result.timestamp, // 必填，生成签名的时间戳
			  "nonceStr": result.nonceStr, // 必填，生成签名的随机串
			  "signature": result.signature,// 必填，签名，见附录1
			  "jsApiList": [
			     'onMenuShareTimeline',
			     'onMenuShareAppMessage',
			     'onMenuShareQQ',
			     'onMenuShareWeibo',
			     'onMenuShareQZone'
			  ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
			
			wx.onMenuShareTimeline({
				  title: '叮当到家', // 分享标题
				  link: result.url, // 分享链接
				  imgUrl: 'http://imgsrc.baidu.com/baike/pic/item/509b9fcb7bf335ab52664fdb.jpg', // 分享图标
				  success: function () { 
				      // 用户确认分享后执行的回调函数
					  alert();
				  },
				  cancel: function () { 
				      // 用户取消分享后执行的回调函数
				  }
				});
		});
		
	});
	
});

