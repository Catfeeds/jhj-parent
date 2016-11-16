/**
 * Created by hulj on 2016/10/20.
 */
myApp.onPageInit('order-lib-cal',function(page) {

    var nextUrl = page.query.next_url;
    console.log("nextUrl = " + nextUrl);

    var url=page.url;
    var staffId=url.split("staff_id=")[1];
    if(staffId==undefined || staffId==null || staffId==''){
    	staffId=0;
    }

    //获取当前日期
    var date=moment().format("YYYY-MM-DD");
    var nowDate=date;

    var weekDay=['周日','周一','周二','周三','周四','周五','周六'];
    var tempWeek=['周日','周一','周二','周三','周四','周五','周六'];
    var time=['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
        '14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30','18:00'];
    var nowHour=moment().hour();
    var count=0;
    var dayTime="";
    var selectDay="#rilikongjian3-day li[class='beijingse']";

    //日历天数显示
    function getDay(cal){
        var contentDay="";
        var contentWeek="";
        if(cal==undefined || cal == null || cal =="") return ;
        var cmp=moment(cal).format("YYYY-MM-DD");
        for(var i=0;i<7;i++){
            var d = moment(cal).add(i,'days');
            var week=d.format('d');
            contentDay+="<li>"+d.format('DD')+"</li>";
            //显示今天明天
            if(cmp==date){
                if(i==0){
                    tempWeek[week]="今天";
                    if(parseInt(week)+1>6){
                        tempWeek[0]="明天";
                    }else{
                        tempWeek[parseInt(week)+1]="明天";
                    }
                }
                contentWeek+="<li>"+tempWeek[week]+"</li>"
            }else{
                contentWeek+="<li>"+weekDay[week]+"</li>"
            }
        }
        $$("#rilikongjian2-week").html(contentWeek);
        $$("#rilikongjian3-day").html(contentDay);
        $$(".rilikongjian p").text(moment(nowDate).format("YYYY"));
        $$("#rilikongjian1-month").text(moment(nowDate).format("MM"));

        $$("#rilikongjian3-day li").on("click",function(){
            selectDay = $$(this);
            $$("#rilikongjian3-day").find("li").removeClass("beijingse");
            $$("#rilikongjian3-dateTime").find("li").removeClass("hour-beijingse beijingse");
            $$("#all-button2").removeClass("all-button2").addClass("all-button11");
            $$(this).addClass("beijingse");
            tomm();
            if(moment(getServiceDate()+" "+nowHour).format("YYYY-MM-DD HH")<=moment().format("YYYY-MM-DD HH")){
                if(nowHour>=16){
                    $$("#rilikongjian3-day li").removeClass("beijingse");
                    $$(selectDay).addClass("beijingse");
                    $$("#rilikongjian3-dateTime li").addClass("hour-beijingse");
                }
            }
            noSelectHour();
            filterServiceDate();
        });
        $$("#rilikongjian3-dateTime li").removeClass("hour-beijingse");
        noSelectHour();
        if(cmp==date){
            $$("#rilikongjian3-day").find(":first-child").addClass("beijingse");
            tomm();
        }
    }
    getDay(date);

    //前一天
    function getPreDay(c){
        var preDay = moment(date).add(c, 'days');
        nowDate=preDay;
        getDay(preDay);
    }

    //后一天
    function getNextDay(c){
        var afterDay = moment(date).add(c, 'days');
        nowDate=afterDay;
        getDay(afterDay);
    }

    //日历减1天
    $$("#rilikongjian1-left").click(function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        var dy=$$(selectDay).parent().find(":first-child").text();
        if(dy==null ||dy==""){
            dy=moment(getServiceDate()).add(count, 'days').format("DD");
        }
        var cmp = moment().format("YYYY-MM")+"-"+dy;
        if(cmp<=date) return ;
        count--;
        getPreDay(count);
    });

    //日历加1天
    $$("#rilikongjian1-right").click(function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        count++;
        getNextDay(count);
    });

    //遍历时间
    function getTime(){
        var dateTime='';
        for(var i=0;i<time.length;i++){
            var notSelectTime=['11:30','12:00','12:30'];
            if(time[i]==notSelectTime[0] || time[i]==notSelectTime[1] || time[i]==notSelectTime[2]){
                dateTime+="<li class='rilikongjian3-1 hour-beijingse'>"+time[i]+"</li>";
            }else{
                dateTime+="<li class='rilikongjian3-1'>"+time[i]+"</li>";
            }
        }
        $$("#rilikongjian3-dateTime").html(dateTime);
    }
    getTime();

    function noSelectHour(){
        var li=$$("#rilikongjian3-dateTime").find("li");
        for(var i=7;i<=9;i++){
            $$(li[i]).addClass("hour-beijingse");
            console.log(li[i]);
            dayTime=$$(li[i]).text();
            dayTime="";
        }
    }
    //获取当前选择的时间，如何没有选择时间默认是当前时间
    function getServiceDate(){
        var serviceDate='';
        var year = $$(".rilikongjian p").text();
        var month = $$("#rilikongjian1-month").text();
        var day = $$(selectDay).text();
        if($$(selectDay).text()==undefined ||$$(selectDay).text()=="" || $$(selectDay).text()==null){
            day=moment().format('DD');
        }
        var pre_li = $$(selectDay).prevAll("li");
        var after_li = $$(selectDay).nextAll("li");
        var flag1=false;
        var flag2=false;
        if(pre_li.length>0 && after_li.length>0){
            for(var i=0;i<pre_li.length;i++){
                var val = pre_li[i].innerHTML;
                if(val<day){
                    flag1=true;
                }else{
                    flag1=false;
                }
            }
            for(var j=0;j<after_li.length;j++){
                var val=after_li[j].innerHTML;
                if(val>day ||val<day){
                    flag2=true;
                }
            }
            if(flag1 && flag2){
                serviceDate=year+"-"+month+"-"+day;
            }else{
                serviceDate=year+"-"+(parseInt(month)+1)+"-"+day;
            }
        }

        if(pre_li.length==0){
            var nextVal=$$(after_li[0]).text();
            var next5Val=$$(after_li[5]).text();
            if(nextVal>day || (nextVal<day && next5Val<day)){
                serviceDate=year+"-"+month+"-"+day;
            }else{
                serviceDate=year+"-"+(parseInt(month)+1)+"-"+day;
            }
        }
        if(after_li.length==0){
            var preVal=$$(pre_li[0]).text();
            var pre5Val=$$(pre_li[5]).text();
            if(preVal<day && pre5Val<day){
                serviceDate=year+"-"+month+"-"+day;
            }else{
                serviceDate=year+"-"+(parseInt(month)+1)+"-"+day;
            }
        }
        if(after_li.length==0 && pre_li.length==0){
            serviceDate=date;
        }
        return serviceDate;
    }

    $$("#rilikongjian3-dateTime li").on("click",function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        var dy=$$(selectDay).text();
        $$(this).addClass("beijingse");
        if($$(this).hasClass("hour-beijingse")){
            dayTime="";
            $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        }else{
            dayTime=$$(this).text();
        }
        if(''!=dayTime && dy!=null){
            $$("#all-button2").removeClass("all-button11").addClass("all-button2");
        }
    });

    /**
     *根据当前时间，判断下单可以选择的时间
     *
     * */
    function tomm(){
        var nyr=getServiceDate();
        if(nyr==moment().format("YYYY-MM-DD")){
            var lis = $$("#rilikongjian3-dateTime").find("li");

            if(nowHour>=0 && nowHour<=4){
                for(var i=0;i<=lis.length;i++){
                    if(i<2){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=4 && nowHour<=6){
                for(var i=0;i<=lis.length;i++){
                    if(i<4){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==7){
                for(var i=0;i<=lis.length;i++){
                    if(i<6){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=8 && nowHour<=9){
                for(var i=0;i<=lis.length;i++){
                    if(i<10){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==10){
                for(var i=0;i<=lis.length;i++){
                    if(i<12){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==11){
                for(var i=0;i<=lis.length;i++){
                    if(i<14){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==12){
                for(var i=0;i<=lis.length;i++){
                    if(i<16){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==13){
                for(var i=0;i<=lis.length;i++){
                    if(i<18){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=14 && nowHour<=15){
                for(var i=0;i<=lis.length;i++){
                    if(i<20){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=16 && nowHour<=19){
                var d=moment().add(1,"days").format("DD");
                var lisd = $$("#rilikongjian3-day").find("li");
                for(var i=0;i<=lisd.length;i++){
                    var val = $$(lisd[i]).text();
                    if(d==val){
                        $$("#rilikongjian3-day li").removeClass("beijingse");
                        $$(lisd[i]).addClass("beijingse");

                    }
                }
            }
            if(nowHour>=20 && nowHour<=23){
//                $$(lis[0]).addClass("hour-beijingse");
                for(var i=0;i<=lis.length;i++){
                    if(i<2){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
                $$("#rilikongjian3-day li").removeClass("beijingse");
                $$("#rilikongjian3-day").find("li:nth-child(2)").addClass("beijingse");
            }
        }
        if(nyr>moment().format("YYYY-MM-DD")){
            $$("#rilikongjian3-dateTime").find("li").removeClass("hour-beijingse");
        }
        noSelectHour();
    }
    tomm();

    //查询服务人员的已有派工的服务时间
    if(staffId!=0 && staffId!=null && staffId!=''){
    	$$.ajax({
    		type : "GET",
    		url:siteAPIPath+"/staff/get_dispatch_dates.json?staff_id="+staffId,
    		dataType:"json",
    		success:function(data){
    			if(data.data.length>0){
    				sessionStorage.setItem("serDate",data.data);
    			}
    		}
    	});
    }

    //过滤服务人有已占有的服务时间
    function filterServiceDate(){
        var serviceDateArr = sessionStorage.getItem("serDate");
        var serviceHour = sessionStorage.getItem("total_service_hour");
        if(serviceDateArr=="" ||serviceDateArr==null ||serviceDateArr==undefined) return ;
        var d = serviceDateArr.split(",");
        for(var i=0;i<d.length;i++){
        	var sd=d[i];
        	var serviceDateAdd = moment(sd).add(serviceHour,"hours").add(2,"hours");
        	var serviceDateSub = moment(sd).subtract(serviceHour,"hours").subtract(2,"hours");
        	var date = moment(serviceDateAdd).format("YYYY-MM-DD");
        	var hour1 = moment(sd).format("HH:mm");
            var hour2 = serviceDateAdd.format("HH:mm");
            var hoursub = serviceDateSub.format("HH:mm");
            var s = d[i].split(" ");
            if(date==getServiceDate()){
                for(var i=0;i<time.length;i++){
                	var index=i;
                	if(hoursub>=time[0] && time[i]>hoursub && time[i]<=hour1){
                		 var tli = $$("#rilikongjian3-dateTime li");
                         $$(tli[index]).addClass("hour-beijingse");
                	}
                	if(hoursub<time[0] && time[i]<=hour1){
                		 var tli = $$("#rilikongjian3-dateTime li");
                         $$(tli[index]).addClass("hour-beijingse");
                	}
                    if(time[i]>=hour1 && time[i]<=hour2){
                       var tli = $$("#rilikongjian3-dateTime li");
                       $$(tli[index]).addClass("hour-beijingse");
                    }
                }
            }

        }
    }

    //获取选择的服务时间
    $$("#all-button2").click(function(){
        var st = getServiceDate()+" "+dayTime+":00";
        if(dayTime!=""){
            console.log("serviceTime = " + st)
            sessionStorage.setItem('service_date_str',getServiceDate()+"("+ weekDay[moment(getServiceDate()).format("d")]+")"+dayTime);
            var serviceDate = moment(st).unix();
            sessionStorage.setItem('service_date', serviceDate);
            mainView.router.loadPage(nextUrl);
        }else{
            return;
        }
    });

});
