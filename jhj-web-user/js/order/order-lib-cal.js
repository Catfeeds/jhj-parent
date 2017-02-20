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
    
    var serviceTypeId=sessionStorage.getItem("service_type_id");

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
    var dayNum=1;

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
            var s=getServiceDate();
            if(getServiceDate()==date){
                if(nowHour>=16){
                    $$("#rilikongjian3-day li").removeClass("beijingse");
                    $$(selectDay).addClass("beijingse");
                    $$("#rilikongjian3-dateTime li").addClass("hour-beijingse");
                }
            }
            noSelectHour();
            filterServiceDate();
            filterWeek(serviceTypeId);
            
        });
        $$("#rilikongjian3-dateTime li").removeClass("hour-beijingse");
        filterServiceDate();
        noSelectHour();
        $$("#rilikongjian3-day").find(":first-child").addClass("beijingse");
       
        if(cmp==date){
            tomm(cmp);
        }
        filterWeek(serviceTypeId);
        
    }
    getDay(date);

    //日期变化
    function getPreDay(selectDate,c){
        var preDay;
        if(selectDate==undefined || selectDate==null || selectDate==''){
        	preDay = moment(date).add(c, 'days');
    	}else{
    		preDay = moment(selectDate).add(c, 'days');
    	}
        nowDate=preDay;
        getDay(preDay);
    }

    //日历减1天
    $$("#rilikongjian1-left").click(function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        dayTime="";
        var selectDate = getServiceDate();
        var comp_day = moment(selectDate).add(-1,'days').format("YYYY-MM-DD");
        if(comp_day>=date){
        	getPreDay(selectDate,-dayNum);
        }
        if(comp_day<=date && selectDate>date){
        	var days = moment(selectDate).diff(moment(date),'days');
        	getPreDay(selectDate,-days);
        }
    });

    //日历加1天
    $$("#rilikongjian1-right").click(function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        var selectDate = getServiceDate();
        getPreDay(selectDate,dayNum);
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
         var day = $$("#rilikongjian3-day li[class='beijingse']").text();
         if(day==undefined || day==null || day==''){
        	 day = $$(selectDay).text();
         }
         var pre_li = $$(selectDay).prevAll("li");
         var after_li = $$(selectDay).nextAll("li");
	 	 var flag=0;
	 	 var flag1=0;
         var flag2=0;
         if(pre_li.length>0 && after_li.length>0){
             for(var i=0;i<pre_li.length;i++){
                 var val = $$(pre_li[i]).text();
                 if(val<day){
                	 flag++;
                 }else{
                     flag1++;
                 }
             }
             for(var j=0;j<after_li.length;j++){
                 var val=$$(after_li[j]).text();
                 if(val>day ||val<day){
                      flag2++;
                 }
             }
             if(flag==pre_li.length && flag2==after_li.length){
                 serviceDate=year+"-"+month+"-"+day;
             }else{
             	serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
             }
         }

         if(pre_li.length==0){
             var nextVal=$$(after_li[0]).text();
             var next5Val=$$(after_li[5]).text();
             if(nextVal>day || (nextVal<day && next5Val<day)){
                 serviceDate=year+"-"+month+"-"+day;
             }else{
             	serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
             }
         }
         if(after_li.length==0){
             var preVal=$$(pre_li[0]).text();
             var pre5Val=$$(pre_li[5]).text();
             if(preVal<day && pre5Val<day){
                 serviceDate=year+"-"+month+"-"+day;
             }else{
             	serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
             }
         }
         if(after_li.length==0 && pre_li.length==0){
             serviceDate=year+"-"+month+"-"+day;
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
    function tomm(val){
    	var nyr
    	if(val==undefined ||val==null || val==''){
    		var nyr=getServiceDate();
    	}else{
    		nyr=val;
    	}
        if(nyr==moment().format("YYYY-MM-DD")){
            var lis = $$("#rilikongjian3-dateTime").find("li");

            if(nowHour>=0 && nowHour<=4){
                filterPreCurrentTime(lis,2);
            }
            if(nowHour>=4 && nowHour<=6){
            	filterPreCurrentTime(lis,4);
            }
            if(nowHour==7){
            	filterPreCurrentTime(lis,6);
            }
            if(nowHour>=8 && nowHour<=9){
            	filterPreCurrentTime(lis,10);
            }
            if(nowHour==10){
            	filterPreCurrentTime(lis,12);
            }
            if(nowHour==11){
            	filterPreCurrentTime(lis,14);
            }
            if(nowHour==12){
            	filterPreCurrentTime(lis,16);
            }
            if(nowHour==13){
            	filterPreCurrentTime(lis,18);
            }
            if(nowHour>=14 && nowHour<=15){
            	filterPreCurrentTime(lis,20);
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
            	filterPreCurrentTime(lis,2);
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
    
    //当前时间之前的时间不可选择
    function filterPreCurrentTime(arrays,timeNum){
    	for(var i=0;i<=arrays.length;i++){
            if(i<timeNum){
                $$(arrays[i]).addClass("hour-beijingse");
            }
        }
    }
    
    
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
    
    //过滤非周一到周三的时间
    function filterWeek(serviceTypeId){
    	if(serviceTypeId==undefined ||serviceTypeId=='' ||serviceTypeId==null) return false;
    	if(serviceTypeId=='69' || serviceTypeId=='70'){
    		var serviceTime = getServiceDate();
    		var week = moment(serviceTime).format("d");
    		
    		if(week=='4' || week=='5' || week=='6' || week=='0'){
    			var ss=$$("#rilikongjian3-dateTime");
    			$$("#rilikongjian3-dateTime li").addClass("hour-beijingse");
    		}
    	}
    }
    filterWeek(serviceTypeId);
    
    function isFull(serviceDateStr){
    	var param = {};
    	param.service_type_id = sessionStorage.getItem("service_type_id");
    	param.addr_id = localStorage.getItem("default_addr_id");
    	if(serviceDateStr==undefined || serviceDateStr==null || serviceDateStr==''){
    		serviceDateStr = moment().format("YYYY-MM-DD");
    	}
    	param.service_date_str = serviceDateStr;
    	$$.ajax({
    		type:"POST",
    		url:siteAPIPath+"order/check_dispatch.json",
    		data:param,
    		success:function(data){
    			console.log(data)
    			if(data.status=='0' && data.msg=='ok'){
    				
    			}
    		}
    	});
    }
    
    isFull();
    

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

