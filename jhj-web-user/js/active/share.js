myApp.onPageInit('share', function (page) {

    var userId = localStorage.getItem("user_id");
    var mobile = localStorage.getItem("user_mobile");

    var url = "http://" + window.location.host + "/u/index.html?share_user_id=" + userId;
    var curUrl = location.href.split('#')[0];

    var shareParam = {
        title: '惊到了！有洁癖的家政服务商竟然这样做保洁！',
        desc: '', // 分享描述
        link: url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: '', // 分享图标
        success: function (res) {
            myApp.alert("好友下单成功会短信通知您","分享成功");
            saveShare();
        },
        fail:function(res){
        	myApp.alert(JSON.stringify(res));	
        },
        cancel: function () {
        	myApp.alert("分享失败");
        }
    }
    
    //保存分享
    function saveShare() {
        var param = {};
        param.share_user_id = userId;
        param.mobile = mobile;
        $$.ajax({
            type: "post",
            url: siteAPIPath + "saveOrderShare.json",
            data: param,
            dataType: 'json',
            success: function () {
            	
            }
        });
    }
    
    $$.post(siteAPIPath + "wx-share.json", {"url": curUrl}, function (data) {
        var result = JSON.parse(data).data;
        console.log(data);
        wx.config({
            "appId": result.appId, // 必填，公众号的唯一标识
            "timestamp": result.timestamp, // 必填，生成签名的时间戳
            "nonceStr": result.noncestr, // 必填，生成签名的随机串
            "signature": result.signature,// 必填，签名，见附录1
            "jsApiList": [
                'onMenuShareTimeline',
                'onMenuShareAppMessage',
                'onMenuShareQQ',
                'onMenuShareWeibo',
                'onMenuShareQZone'
            ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
                
       /* wx.ready(function(){
        	wx.onMenuShareAppMessage(shareParam);
        	wx.onMenuShareWeibo(shareParam);
        	wx.onMenuShareTimeline(shareParam);
        	wx.onMenuShareQQ(shareParam);
        	wx.onMenuShareQZone(shareParam);
        });*/

    });
    
    function shareFriend(){
    	WeixinJSBridge.invoke('shareAppMessage',shareParam);
    }
    
    function shareTimeline(){
    	WeixinJSBridge.invoke('shareTimeline',shareParam);
    }
    
    function shareWeibo(){
    	WeixinJSBridge.invoke('shareWeibo',shareParam);
    }
    
    $$(".share-btn").on("click",function(){
    	$$(".share-operation").css("display","block");
    	
    	$$(".share-opera-content").on("click",'.share-ope-btn',function(){
    		
    		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    			// 发送给好友
    			WeixinJSBridge.on('menu:share:appmessage', function(argv){
    				shareFriend();
    			});
    			// 分享到朋友圈
    			WeixinJSBridge.on('menu:share:timeline', function(argv){
    				shareTimeline();
    			});
    			// 分享到微博
    			WeixinJSBridge.on('menu:share:weibo', function(argv){
    				shareWeibo();
    			});
    		},false);
        	
    		/*var id = $$(this).attr("id");
    		if(id == "wechat_friend"){
    			wx.onMenuShareAppMessage(shareParam);
    		}
			if(id == "friends_circle"){
				wx.onMenuShareTimeline(shareParam);		
			}
			if(id == "weibo"){
				wx.onMenuShareWeibo(shareParam);
			}*/
        });
    });
    
    
    $$("#share-delete").on("click", function () {
        $$(".share-operation").css("display", "none");
    });

});

