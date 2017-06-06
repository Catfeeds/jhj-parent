myApp.onPageInit('share', function (page) {

    var userId = localStorage.getItem("user_id");
    var mobile = localStorage.getItem("user_mobile");

    var url = "http://jia-he-jia.com/u/index.html?share_user_id=" + userId;

    var shareParam = {
        title: '叮当到家',
        desc: '叮当到家', // 分享描述
        link: url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: '', // 分享图标
        success: function () {
            alert("分享成功");
        },
        cancel: function () {
            alert("分享失败");
        }
    }

    $$("#share-btn").on('click', function () {

        $$(".share-operation").css("display", "block");

        $$(".share-opera-content").on("click", '.share-ope-btn', function () {

            $$.post(siteAPIPath + "wxShare.json", {"url": url}, function (data) {
                var result = JSON.parse(data).data;
                console.log(data);
                wx.config({
                    debug: true,
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

                var id = $$(this).attr("id");
                if (id == "wechat_friend") {
                    wx.onMenuShareAppMessage(shareParam);
                }
                if (id == "friends_circle") {
                    wx.onMenuShareTimeline(shareParam);
                }
                if (id == "weibo") {
                    wx.onMenuShareWeibo(shareParam);
                }
                saveShare();

            });

        });

        $$("#share-delete").on("click", function () {
            $$(".share-operation").css("display", "none");
        });

        //保存分享
        function saveShare() {
            var param = {};
            param.user_id = userId;
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
    });

});

