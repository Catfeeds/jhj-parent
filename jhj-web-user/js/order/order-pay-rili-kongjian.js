myApp.onPageInit('order-pay-rili-kongjian',function(page){
    var serviceTime="";
    var _day=""
    var _dayTime=""
    _day=moment().format("D");
    var nowHour=moment().hour();
    var year=moment().year();
    $$(".rilikongjian p").text(year);
    var month=moment().month()+1;
    $$("#rilikongjian1-month").text(month);

    var weekDay=['周日','周一','周二','周三','周四','周五','周六'];
    var tempWeek=['周日','周一','周二','周三','周四','周五','周六'];
    var _date=[];

    //遍历日期
    function getDay(year,month,day,sign){

        var today=moment().format('D');
        if(day!=undefined || day!=""){
            today=_day;
        }
        var week=moment(year+"-"+month+"-"+today).format('d');
        if(year==undefined && month==undefined){
            week=moment().format('d');
        }
        var contentDay="";
        var contentWeek="";
        var cmp_day=moment().format("YYYY-MM-D")
        for(var i=0;i<7;i++){
            today=parseInt(today);
            if(today>getMonthNum){
                today=1;
            }
            contentDay+="<li>"+today+"</li>";
            if(moment(year+"-"+month+"-"+today).format("YYYY-MM-D")==cmp_day){
                sign=true;
            }
            if(sign){
                if(moment(year+"-"+month+"-"+today).format("YYYY-MM-D")==cmp_day){
                    tempWeek[week]="今天";
                    if(week==6){
                        tempWeek[0]="明天";
                    }else{
                        tempWeek[parseInt(week)+1]="明天";
                    }
                }
                contentWeek+="<li>"+tempWeek[week]+"</li>"
            }else{
                contentWeek+="<li>"+weekDay[week]+"</li>"
            }
            today+=1;
            week=parseInt(week)+1;
            if(week>6){
                week=0;
            }
        }
        $$("#rilikongjian3-day").html(contentDay);
        $$("#rilikongjian2-week").html(contentWeek);
        $$("#rilikongjian3-day").find(":first-child").addClass("beijingse");
    }
    getDay(year,month,_day,true);

    var time=['8:00','8:30','9:00','9:30','10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
        '14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30','18:00'];
    //遍历时间
    var dateTime="";
    for(var i=0;i<time.length;i++){
        dateTime+="<li class='rilikongjian3-1'>"+time[i]+"</li>";
    }
    $$("#rilikongjian3-dateTime").html(dateTime);

    $$("#rilikongjian1-left").click(function(){
        var d_first=$$("#rilikongjian3-day").find("li:first-child").text();
        if(moment(year+"-"+month+"-"+_day).format("YYYY-MM-D")>moment().format("YYYY-MM-D")) {
            changDay(d_first, 'left')
        }else{
            return;
        }
    });
    $$("#rilikongjian1-right").click(function(){
        var d_first=$$("#rilikongjian3-day").find("li:first-child").text();
        changDay(d_first,'right')
    });
    var getMonthNum=moment().daysInMonth();

    //变更日期
    function changDay(val,flag){
        if(val==undefined || val=="") return ;
        getMonthNum = moment(year+"-"+month,"YYYY-MM").daysInMonth();
        val=parseInt(val);
        if(flag=='left'){
            _day=val-1;
            if(moment(year+"-"+month+"-"+_day).isBefore(moment().format("YYYY-MM-D"))) return;
            if(_day<1){
                month-=1;
                _day= moment(year+"-"+month,"YYYY-MM").daysInMonth()
                if(month<1){
                    month=12;
                    year-=1;
                }
            }
        }
        if(flag=='right'){
            _day=val+1;
            if(_day>getMonthNum){
                _day=1;
                month+=1;
                if(month>12){
                    month=1;
                    year+=1;
                }
            }
        }
        $$(".rilikongjian p").text(year);
        $$("#rilikongjian1-month").text(month);
        getDay(year,month,_day,false);
        tomm();
        noSelectHour();
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$("#rilikongjian3-day li").on("click",function(){
            _day = parseInt($$(this).text());
            var comp_day=$$(this).parent().find(":first-child").text();
            if(_day<parseInt(comp_day)){
                month+=1;
            }
            $$("#rilikongjian3-day").find("li").removeClass("beijingse");
            $$("#rilikongjian3-dateTime").find("li").removeClass("hour-beijingse");
            $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
            $$(this).toggleClass("beijingse");
            dateTimeNoSelect();
            tomm();
            noSelectHour();
        });
    }

    function dateTimeNoSelect(){
        var current_time = moment().format("YYYY-MM-DD HH:mm");
        var li=$$("#rilikongjian3-dateTime").find("li");
        for(var i=0;i<time.length;i++){
            if(moment(year+"-"+month+"-"+_day+" "+time[i]).isBefore(current_time)){
                $$(li[i]).addClass("hour-beijingse");
                _dayTime="";
            }
        }
    }
    dateTimeNoSelect();

    $$("#rilikongjian3-day li").on("click",function(){
        _day = parseInt($$(this).text());
        var comp_day=$$(this).parent().find(":first-child").text();
        if(_day<parseInt(comp_day)){
            month+=1;
        }
        $$("#rilikongjian3-day").find("li").removeClass("beijingse");
        $$("#rilikongjian3-dateTime").find("li").removeClass("hour-beijingse");
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$(this).toggleClass("beijingse");
        dateTimeNoSelect();
        tomm();
        noSelectHour();
    });

    function noSelectHour(){
        for(var i=7;i<=9;i++){
            var li=$$("#rilikongjian3-dateTime").find("li");
            $$(li[i]).addClass("hour-beijingse");
            _dayTime=$$(li[i]).text();
            _dayTime="";
        }
    }
    noSelectHour();

    $$("#rilikongjian3-dateTime li").on("click",function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$(this).addClass("beijingse");
        noSelectHour();
        dateTimeNoSelect();
        if($$(this).hasClass("hour-beijingse")){
            _dayTime="";
        }else{
            _dayTime=$$(this).text();
        }
    });

    /**
     *根据当前时间，判断下单可以选择的时间
     *
     * */
    function tomm(){
        var nyr=year+"-"+month+"-"+_day;
        if(nyr==moment().format("YYYY-MM-D")){
            if(nowHour>=16 && nowHour<=19){
                _day=parseInt(moment().format("D"))+1
                var lis = $$("#rilikongjian3-day").find("li");
                for(var i=0;i<=lis.length;i++){
                    var val = $$(lis[i]).text();
                    if(_day==val){
                        $$("#rilikongjian3-day").find("li").removeClass("beijingse");
                        $$(lis[i]).addClass("beijingse");
                        $$("#rilikongjian3-dateTime").find("li").removeClass("hour-beijingse");
                    }
                }
                noSelectHour();
            }
            if((nowHour>=20 && nowHour<=23) ||(nowHour>=0 && nowHour<=4)){
                var lis = $$("#rilikongjian3-dateTime").find("li");
                $$(lis[0]).addClass("hour-beijingse");
                for(var i=0;i<=lis.length;i++){
                    if(i<2){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=4 && nowHour<=6){
                var lis = $$("#rilikongjian3-dateTime").find("li");
                for(var i=0;i<=lis.length;i++){
                    if(i<4){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==7){
                var lis = $$("#rilikongjian3-dateTime").find("li");
                for(var i=0;i<=lis.length;i++){
                    if(i<6){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=8 && nowHour<=9){
                var lis = $$("#rilikongjian3-dateTime").find("li");
                for(var i=0;i<=lis.length;i++){
                    if(i<10){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==10){
                var lis = $$("#rilikongjian3-dateTime").find("li");
                for(var i=0;i<=lis.length;i++){
                    if(i<12){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==11){
                var lis = $$("#rilikongjian3-dateTime").find("li");
                for(var i=0;i<=lis.length;i++){
                    if(i<14){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==12){
                var lis = $$("#rilikongjian3-dateTime").find("li");
                for(var i=0;i<=lis.length;i++){
                    if(i<16){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==13){
                var lis = $$("#rilikongjian3-dateTime").find("li");
                for(var i=0;i<=lis.length;i++){
                    if(i<18){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=14 && nowHour<=15){
                var lis = $$("#rilikongjian3-dateTime").find("li");
                for(var i=0;i<=lis.length;i++){
                    if(i<20){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
        }
        if(nyr>moment().format("YYYY-MM-D")){
            $$("#rilikongjian3-dateTime").find("li").removeClass("hour-beijingse");
        }

    }
    tomm();

    //获取选择的服务时间
    $$("#all-button2").click(function(){
        if(_day==""){
            _day=moment().format("D");
        }
        serviceTime=year+"-"+month+"-"+_day+" "+_dayTime;
        if(_dayTime!=""){
            mainView.router.loadPage('html/order-pay-rili.html?service_time='+serviceTime);
        }else{
            alert("请选择服务时间");
            return;
        }
    });

});
