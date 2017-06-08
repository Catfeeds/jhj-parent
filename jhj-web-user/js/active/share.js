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
            alert("分享成功"+ 
                "好友下单成功会短信通知您");
            saveShare();
        },
        fail:function(res){
        	alert(JSON.stringify(res));	
        },
        cancel: function () {
            alert("分享失败");
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
                
        wx.ready(function(){
        	wx.onMenuShareAppMessage(shareParam);
        	wx.onMenuShareWeibo(shareParam);
        	wx.onMenuShareTimeline(shareParam);
        	wx.onMenuShareQQ(shareParam);
        	wx.onMenuShareQZone(shareParam);
        });

        $$("#share-delete").on("click", function () {
            $$(".share-operation").css("display", "none");
        });

    });

});

