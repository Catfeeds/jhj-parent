/**
 * Created by hulj on 2016/10/20.
 */
myApp.onPageInit('order-lib-cal',function(page) {

    var nextUrl = page.query.next_url;
    console.log("nextUrl = " + nextUrl);

    //获取当前日期
    var date=moment().format("YYYY-MM-D");
    var nowDate=date;
    
    var weekDay=['周日','周一','周二','周三','周四','周五','周六'];
    var tempWeek=['周日','周一','周二','周三','周四','周五','周六'];
    var time=['8:00','8:30','9:00','9:30','10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
        '14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30','18:00'];
    var nowHour=moment().hour();
    var count=0;
    var dayTime="";
    var selectDay="#rilikongjian3-day li[class='beijingse']";
    
//    //小于当前时间的日期都不可以选择
//    function dateTimeNoSelect(){
//        var today=getServiceDate();
//        var current_time = moment().add(4,"hours");
//        var li=$$("#rilikongjian3-dateTime").find("li");
//        for(var i=0;i<time.length;i++){
//            if(moment(today+" "+time[i]).isBefore(current_time)){
//                $$(li[i]).addClass("hour-beijingse");
//                dayTime="";
//            }
//        }
//    }
//    dateTimeNoSelect();
    
    //日历天数显示
    function getDay(cal){
        var contentDay="";
        var contentWeek="";
        if(cal==undefined || cal == null || cal =="") return ;
        var cmp=moment(getServiceDate()).add(count, 'days').format("YYYY-MM-D");
        for(var i=0;i<7;i++){
            var d = moment(cal).add(i,'days');
            var week=d.format('d');
            contentDay+="<li>"+d.format('D')+"</li>";
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
            $$("#all-butto2").removeClass("all-button2").addClass("all-button11");
            $$(this).addClass("beijingse");
            tomm();
            if(moment(getServiceDate()+" "+nowHour).format("YYYY-MM-D HH")==moment().format("YYYY-MM-D HH")){
            	if(nowHour>=17){
            		$$("#rilikongjian3-day li").removeClass("beijingse");
            		$$(selectDay).addClass("beijingse");
            		$$("#rilikongjian3-dateTime li").addClass("hour-beijingse");
            	}
            }
            noSelectHour();
        });
        $$("#rilikongjian3-dateTime li").removeClass("hour-beijingse");
        noSelectHour();
        if(cmp==date){
        	$$("#rilikongjian3-day").find(":first-child").addClass("beijingse");
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
        $$("#all-butto2").removeClass("all-button2").addClass("all-button11");
        var cmp=moment(getServiceDate()).add(count, 'days').format("YYYY-MM-D");
        if(cmp<=date) return ;
        count--;
        getPreDay(count);
    });

    //日历加1天
    $$("#rilikongjian1-right").click(function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$("#all-butto2").removeClass("all-button2").addClass("all-button11");
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
        var day = parseInt($$(selectDay).text());
        if($$(selectDay).text()==undefined ||$$(selectDay).text()=="" || $$(selectDay).text()==null){
            day=parseInt(moment().format('D'));
        }
        var pre_li = $$(selectDay).prevAll("li");
        var after_li = $$(selectDay).nextAll("li");
        var flag1=false;
        var flag2=false;
        if(pre_li.length>0 && after_li.length>0){
            for(var i=0;i<pre_li.length;i++){
                var val = parseInt(pre_li[i].innerHTML);
                if(val<day){
                    flag1=true;
                }else{
                    flag1=false;
                }
            }
            for(var j=0;j<after_li.length;j++){
                var val=parseInt(after_li[j].innerHTML);
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
            var nextVal=parseInt($$(after_li[0]).text());
            var next5Val=parseInt($$(after_li[5]).text());
            if(nextVal>day || (nextVal<day && next5Val<day)){
                serviceDate=year+"-"+month+"-"+day;
            }else{
                serviceDate=year+"-"+(parseInt(month)+1)+"-"+day;
            }
        }
        if(after_li.length==0){
            var preVal=parseInt($$(pre_li[0]).text());
            var pre5Val=parseInt($$(pre_li[5]).text());
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
        $$(this).addClass("beijingse");
        if($$(this).hasClass("hour-beijingse")){
            dayTime="";
            $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        }else{
            dayTime=$$(this).text();
        }
        if(''!=dayTime){
            $$("#all-button2").removeClass("all-button11").addClass("all-button2");
        }
    });

    /**
     *根据当前时间，判断下单可以选择的时间
     *
     * */
    function tomm(){
        var nyr=getServiceDate();
        if(nyr==moment().format("YYYY-MM-D")){
        	var lis = $$("#rilikongjian3-dateTime").find("li");
            if(nowHour>=16 && nowHour<=19){
                var d=parseInt(moment().format("D"))+1
                var lisd = $$("#rilikongjian3-day").find("li");
                for(var i=0;i<=lisd.length;i++){
                    var val = $$(lisd[i]).text();
                    if(d==val){
                        $$("#rilikongjian3-day li").removeClass("beijingse");
                        $$(lisd[i]).addClass("beijingse");
                    }
                }
            }
            if((nowHour>=20 && nowHour<=23) ||(nowHour>=0 && nowHour<=4)){
                $$(lis[0]).addClass("hour-beijingse");
                for(var i=0;i<=lis.length;i++){
                    if(i<2){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
                $$("#rilikongjian3-day li").removeClass("beijingse");
                $$("#rilikongjian3-day").find("li:nth-child(2)").addClass("beijingse");
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
        }
        if(nyr>moment().format("YYYY-MM-D")){
            $$("#rilikongjian3-dateTime").find("li").removeClass("hour-beijingse");
        }
    }
    tomm();

    //获取选择的服务时间
    $$("#all-button2").click(function(){
    	var st = getServiceDate()+" "+dayTime+":00";
        if(dayTime!=""){
        	console.log("serviceTime = " + st)
        	sessionStorage.setItem('service_date_str',getServiceDate()+"("+ weekDay[moment(getServiceDate()).format("d")]+")"+dayTime);
        	myApp.alert(st);
        	var serviceDateStr = moment(st).format("YYYY-MM-DD HH:mm:ss");
        	myApp.alert(serviceDateStr);
        	var serviceDate = moment(serviceDateStr).unix();
        	myApp.alert(serviceDate);
        	sessionStorage.setItem('service_date', serviceDate);
            mainView.router.loadPage(nextUrl);
        }else{
            return;
        }
    });

});
