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
    var selectDay="#calendar-day li p[class='rilichange-day']";
    var dayNum=7;
    
  //获取当前选择的时间，如何没有选择时间默认是当前时间
    function getServiceDate(element){
    	 var serviceDate='';
         var year = $$("#calendar-year").text();
         var month = $$("#calendar-month").text();
         var day = $$("#calendar-day li p[class='rilichange-day']").text();
         if(day==undefined || day==null || day==''){
        	 day = $$(element).text();
         }
         serviceDate=year+"-"+month+"-"+day;
         var pre_li = $$(element).parent().prevAll("li");
         var after_li = $$(element).parent().nextAll("li");
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
                 serviceDate=serviceDate;
             }else{
             	serviceDate = moment(serviceDate).add(1,'M').format("YYYY-MM-DD");
             }
         }

         if(pre_li.length==0){
             var nextVal=$$(after_li[0]).text();
             var next5Val=$$(after_li[5]).text();
             if(nextVal>day || (nextVal<day && next5Val<day)){
                 serviceDate=serviceDate;
             }else{
             	serviceDate = moment(serviceDate).add(1,'M').format("YYYY-MM-DD");
             }
         }
         if(after_li.length==0){
             var preVal=$$(pre_li[0]).text();
             var pre5Val=$$(pre_li[5]).text();
             if(preVal<day && pre5Val<day){
                 serviceDate=serviceDate;
             }else{
             	serviceDate = moment(serviceDate).add(1,'M').format("YYYY-MM-DD");
             }
         }
         return serviceDate;
    }
    
    //遍历时间
    function getTime(selectDate,result){
        var dateTime='';
        for(var i=0;i<time.length;i++){
        	
        	var notSelectTime=['11:30','12:00','12:30'];
        	 if(result==undefined || result==null || result==''){
                 if(time[i]==notSelectTime[0] || time[i]==notSelectTime[1] || time[i]==notSelectTime[2]){
                     dateTime+="<li class='rilichange-no-time'><p>"+time[i]+"</p><p>约满</P></li>";
                 }else{
                	 dateTime+="<li>"+time[i]+"</li>";
                 }
        	 }else{
        		 for(var j=0;j<result.length;j++){
          			if(time[i]==result[j].service_hour){
          					
      					if(result[j].is_full==0){
      						if(time[i]==notSelectTime[0] || time[i]==notSelectTime[1] || time[i]==notSelectTime[2]){
      							dateTime+="<li class='rilichange-no-time'><p>"+time[i]+"</p><p>约满</P></li>";
      						}else{
      							dateTime+="<li>"+time[i]+"</li>";
      						}
      					}
      					if(result[j].is_full==1){
      						dateTime+="<li class='rilichange-no-time'><p>"+time[i]+"</p><p>约满</p></li>";
      					}
          			}
          		}
        	 }
        }
        $$("#calendar-time").html(dateTime);
        
        $$("#calendar-time li").on("click",function(){
            $$("#calendar-time").find("li").removeClass("rilichange-time");
            var day=$$(selectDay).text();
            if(day==undefined || day==null || day==''){
            	day = $$("#calendar-day li[class='rilichange-time']").text();
            }
            $$(this).addClass("rilichange-time");
            if($$(this).hasClass("rilichange-no-time")){
                dayTime="";
                $$(this).removeClass("rilichange-time");
                $$("#all-button2").removeClass("all-button2").addClass("all-button11");
            }else{
                dayTime=$$(this).text();
            }
            if(''!=dayTime && day!=null){
                $$("#all-button2").removeClass("all-button11").addClass("all-button2");
            }
        });
        
        tomm(selectDate);
    }
    getTime(date);
    
    //获取可派工人数接口
    function isFull(serviceDateStr){
    	var param = {};
    	param.service_type_id = sessionStorage.getItem("service_type_id");
    	param.addr_id = localStorage.getItem("default_addr_id");
    	if(serviceDateStr==undefined || serviceDateStr==null || serviceDateStr==''){
    		serviceDateStr = moment().format("YYYY-MM-DD");
    	}
    	param.service_date_str = serviceDateStr;
    	param.staff_id = staffId;
    	$$.ajax({
    		type:"POST",
    		url:siteAPIPath+"order/check_dispatch.json",
    		data:param,
    		success:function(data){
    			var result = JSON.parse(data);
    			result = result.data;
    			getTime(serviceDateStr,result);
    		}
    	});
    }
    

    //日历天数显示
    function getDay(cal){
        var contentDay="";
        var contentWeek="";
        if(cal==undefined || cal == null || cal =="") return ;
        var cmp=moment(cal).format("YYYY-MM-DD");
        for(var i=0;i<7;i++){
            var d = moment(cal).add(i,'days');
            var week=d.format('d');
            contentDay+="<li><p>"+d.format('DD')+"</p></li>";
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
        $$("#calendar-week").html(contentWeek);
        $$("#calendar-day").html(contentDay);
        $$("#calendar-year").text(moment(nowDate).format("YYYY"));
        $$("#calendar-month").text(moment(nowDate).format("MM"));

        $$("#calendar-day li").on("click",function(){
            $$("#calendar-day li p").removeClass("rilichange-day");
            $$("#calendar-time").find("li").removeClass("rilichange-time rilichange-no-time");
            $$("#all-button2").removeClass("all-button2").addClass("all-button11");
            $$(this).children().addClass("rilichange-day");
            dayTime="";
            var selectDate = getServiceDate($$(this).children());
            if(selectDate==date){
                if(nowHour>=16){
                    $$("#calendar-day li").removeClass("rilichange-day");
                    $$(this).children().addClass("rilichange-day");
                    $$("#calendar-time li").addClass("rilichange-time");
                }
            }
            filterServiceDate();
            isFull(selectDate);
        });
        $$("#calendar-time li").removeClass("rilichange-no-time");
        filterServiceDate();
        $$("#calendar-day li:first-child").find("p").addClass("rilichange-day");
        isFull(getServiceDate(selectDay));
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
        dayTime="";
        nowDate=preDay;
        getDay(preDay);
    }

    //日历减1天
    $$("#calendar-day-left").click(function(){
        $$("#calendar-time").find("li").removeClass("rilichange-time");
        $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        var selectDate = getServiceDate(selectDay);
        var comp_day = moment(selectDate).add(-dayNum,'days').format("YYYY-MM-DD");
        if(comp_day>=date){
        	getPreDay(selectDate,-dayNum);
        }
        if(comp_day<=date && selectDate>date){
        	var days = moment(selectDate).diff(moment(date),'days');
        	getPreDay(selectDate,-days);
        }
    });

    //日历加1天
    $$("#calendar-day-right").click(function(){
        $$("#calendar-time").find("li").removeClass("rilichange-time");
        $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        var selectDate = getServiceDate(selectDay);
        getPreDay(selectDate,dayNum);
    });

    /**
     *根据当前时间，判断下单可以选择的时间
     *
     * */
    function tomm(val){
    	var nyr
    	if(val==undefined ||val==null || val==''){
    		nyr=getServiceDate(selectDay);
    	}else{
    		nyr=val;
    	}
        if(nyr==date){
            var lis = $$("#calendar-time").find("li");

            if(nowHour>=0 && nowHour<=4){
                filterPreCurrentTime(lis,2);
            }
            if(nowHour>4 && nowHour<=6){
            	filterPreCurrentTime(lis,4);
            }
            if(nowHour>=7 && nowHour<=8){
            	filterPreCurrentTime(lis,5);
            }
            if(nowHour==9){
            	filterPreCurrentTime(lis,6);
            }
            if(nowHour==10){
            	filterPreCurrentTime(lis,10);
            }
            if(nowHour==11){
            	filterPreCurrentTime(lis,11);
            }
            if(nowHour==12){
            	filterPreCurrentTime(lis,13);
            }
            if(nowHour==13){
            	filterPreCurrentTime(lis,15);
            }
            if(nowHour==14){
            	filterPreCurrentTime(lis,17);
            }
            if(nowHour==15){
            	filterPreCurrentTime(lis,19);
            }
            if(nowHour>=16 && nowHour<=19){
            	filterPreCurrentTime(lis,21);
            }
            if(nowHour>=20 && nowHour<=23){
            	filterPreCurrentTime(lis,2);
                $$("#calendar-day li p").removeClass("rilichange-day");
                $$("#calendar-day").find("li:nth-child(2) p").addClass("rilichange-day");
            }
        }
    }
    
    //当前时间之前的时间不可选择
    function filterPreCurrentTime(arrays,timeNum){
    	for(var i=0;i<=arrays.length;i++){
            if(i<timeNum){
                if(!$$(arrays[i]).hasClass('rilichange-no-time')){
                	$$(arrays[i]).addClass("rilichange-no-time");
                	var listext = $$(arrays[i]).text();
                	$$(arrays[i]).html("<p>"+listext+"</p><p>约满</p>");
                }
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
            if(date==getServiceDate(selectDay)){
                for(var i=0;i<time.length;i++){
                	var index=i;
                	if(hoursub>=time[0] && time[i]>hoursub && time[i]<=hour1){
                		 var tli = $$("#calendar-time li");
                         $$(tli[index]).addClass("rilichange-time");
                	}
                	if(hoursub<time[0] && time[i]<=hour1){
                		 var tli = $$("#calendar-time li");
                         $$(tli[index]).addClass("rilichange-time");
                	}
                    if(time[i]>=hour1 && time[i]<=hour2){
                       var tli = $$("#calendar-time li");
                       $$(tli[index]).addClass("rilichange-time");
                    }
                }
            }
        }
    }
    
    //获取选择的服务时间
    $$("#all-button2").click(function(){
    	var selectDate = getServiceDate(selectDay);
        var st = selectDate+" "+dayTime+":00";
        if(dayTime!=""){
            console.log("serviceTime = " + st)
            sessionStorage.setItem('service_date_str',selectDate+"("+ weekDay[moment(selectDate).format("d")]+")"+dayTime);
            var serviceDate = moment(st).unix();
            sessionStorage.setItem('service_date', serviceDate);
            mainView.router.loadPage(nextUrl);
        }else{
            return;
        }
    });
    
});

