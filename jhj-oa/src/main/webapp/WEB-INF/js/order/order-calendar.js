/**
 * Created by hulj on 2016/10/20.
 */
$(function(){

    var weekDay=['周日','周一','周二','周三','周四','周五','周六'];
    var tempWeek=['周日','周一','周二','周三','周四','周五','周六'];
    var time=['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
        '14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30','18:00'];
    var count=0;
    var dayNum=7;
    
    //展示时间 
    function showTime(){
        var dateTime='';
        for(var i=0;i<time.length;i++){
            var notSelectTime=['11:30','12:00','12:30'];
            if(time[i]==notSelectTime[0] || time[i]==notSelectTime[1] || time[i]==notSelectTime[2]){
                dateTime+="<li class='rili-time-no'><p>"+time[i]+"</p><p>约满</p></li>";
            }else{
                dateTime+="<li>"+time[i]+"</li>";
            }
        }
        $("#show-dateTime").html(dateTime);
    }
    showTime();
    
    //获取当前日期
    var date=moment().format("YYYY-MM-DD");
    var nowDate=date;
    
    var nowHour=moment().hour();
    
    var dayTime="";
    var selectDay="#show-day li p[class='rili-day']";
    
  //显示年月
	function showYearMonth(date){
		var calendar = date;
		$("#show-year").text(moment(calendar).format("YYYY"));
    	$("#show-month").text(moment(calendar).format("MM"));
	}
	showYearMonth(date);
	
	
    
    
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
    	$("#show-week").html(contentWeek);
    	$("#show-day").html(contentDay);
    	showYearMonth(cal);
    	
    	
    	$("#show-day li").on("click",function(){
    		selectDay = $(this);
    		$("#show-day").find("li p").removeClass("rili-day");
    		$("#show-dateTime li").removeClass("rili-time");
    		$("#checkDate").removeClass("all-button2").addClass("all-button11");
    		$(this).find("p").addClass("rili-day");
    		tomm();
    		var s=getServiceDate();
    		if(getServiceDate()==date){
    			if(nowHour>=16){
    				$("#rilikongjian3-day li").removeClass("beijingse");
    				$(selectDay).addClass("beijingse");
    				$("#show-dateTime li").addClass("rili-time-no");
    			}
    		}
    		noSelectHour();
//    		filterServiceDate();
//            filterWeek(serviceTypeId);
    		
    		/*-----------暂时添加的代码需要删除------*/
    		if(s<'2017-01-30'){
    			$("#show-dateTime li").addClass("rili-time-no");
    			return false;
    		}
    		/*-----------暂时添加的代码需要删除------*/
    		
    	});
    	$("#show-dateTime li").removeClass("rili-time-no");
//    	filterServiceDate();
    	noSelectHour();
    	$("#show-day").find(":first-child p").addClass("rili-day");
    	
    	if(cmp==date){
    		tomm(cmp);
    	}
//        filterWeek(serviceTypeId);
    }
    getDay(date);
    
  //选择时间
	 $("#show-dateTime li").on("click",function(){
	        $("#show-dateTime").find("li").removeClass("rili-time");
	        $(this).addClass("rili-time");
	        if($(this).hasClass("rili-time-no")){
	            dayTime="";
	            $(this).removeClass("rili-time");
	            $("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
	        }else{
	            dayTime=$(this).text();
	            $("#checkDate").removeClass("rili1-6-2").addClass("rili1-6-1");
	        }
	        if(''!=dayTime){
	            $("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
	        }
	  });


    //前一天
   /* function getPreDay(c){
        var preDay = moment(date).add(c, 'days');
        nowDate=preDay;
        getDay(preDay);
        
        ---------暂时添加的代码------------
        if(preDay.format("YYYY-MM-DD")<'2017-01-30'){
        	$("#show-dateTime li").addClass("rili-time-no");
    		return false;
        }
        ---------暂时添加的代码------------
    }*/

    //日历增加或减少日期
    function get7Day(c){
        var afterDay = moment(date).add(c, 'days');
        nowDate=afterDay;
        getDay(afterDay);
        
        /*---------暂时添加的代码------------*/
        if(afterDay.format("YYYY-MM-DD")<'2017-01-30'){
        	$("#show-dateTime li").addClass("rili-time-no");
    		return false;
        }
        /*---------暂时添加的代码------------*/
    }

    //日历减1天
    $("#substranc-day").click(function(){
    	$("#show-dateTime").find("li").removeClass("rili-time");
    	$("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
        var dy=$(selectDay).text();
        if(dy==''){
        	dy = $("#show-day").find("li p[class='rili-time']").text();
        }
        var cmp='';
        var service_date = getServiceDate();
        var comp_day = moment(service_date).add(-7,'days').format("YYYY-MM-DD");
        if(comp_day>=date){
	    	 count--;
	         var days = count*dayNum;
	         get7Day(days);
        }
        if(comp_day<=date && service_date>date){
        	var days = moment(service_date).diff(moment(date),'days');
        	get7Day(-days);
        }
    });

    //日历加1天
    $("#add-day").click(function(){
        $("#show-dateTime").find("li").removeClass("rili-time");
        $("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
        count++;
        var days = count*dayNum;
        get7Day(days);
    });

    

    function noSelectHour(){
        var li=$("#show-dateTime").find("li");
        for(var i=7;i<=9;i++){
            $(li[i]).addClass("rili-time-no");
            console.log(li[i]);
            dayTime=$(li[i]).text();
            dayTime="";
        }
    }
    //获取当前选择的时间，如何没有选择时间默认是当前时间
    function getServiceDate(){
        var serviceDate='';
        var year = $("#show-year").text();
        var month = $("#show-month").text();
        var day = $(selectDay).text();
        if($(selectDay).text()==undefined ||$(selectDay).text()=="" || $(selectDay).text()==null){
            day=moment(date).add(count, 'days').format("DD");
        }
        var pre_li = $(selectDay).prevAll("li");
        var after_li = $(selectDay).nextAll("li");
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
            	serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
            }
        }

        if(pre_li.length==0){
            var nextVal=$(after_li[0]).text();
            var next5Val=$(after_li[5]).text();
            if(nextVal>day || (nextVal<day && next5Val<day)){
                serviceDate=year+"-"+month+"-"+day;
            }else{
            	serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
            }
        }
        if(after_li.length==0){
            var preVal=$(pre_li[0]).text();
            var pre5Val=$(pre_li[5]).text();
            if(preVal<day && pre5Val<day){
                serviceDate=year+"-"+month+"-"+day;
            }else{
            	serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
            }
        }
        if(after_li.length==0 && pre_li.length==0){
            serviceDate=year+"-"+month+"-"+day;
        }
        return  serviceDate;
    }

   

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
            var lis = $("#show-dateTime").find("li");

            if(nowHour>=0 && nowHour<=4){
                for(var i=0;i<=lis.length;i++){
                    if(i<2){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
            }
            if(nowHour>=4 && nowHour<=6){
                for(var i=0;i<=lis.length;i++){
                    if(i<4){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
            }
            if(nowHour==7){
                for(var i=0;i<=lis.length;i++){
                    if(i<6){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
            }
            if(nowHour>=8 && nowHour<=9){
                for(var i=0;i<=lis.length;i++){
                    if(i<10){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
            }
            if(nowHour==10){
                for(var i=0;i<=lis.length;i++){
                    if(i<12){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
            }
            if(nowHour==11){
                for(var i=0;i<=lis.length;i++){
                    if(i<14){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
            }
            if(nowHour==12){
                for(var i=0;i<=lis.length;i++){
                    if(i<16){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
            }
            if(nowHour==13){
                for(var i=0;i<=lis.length;i++){
                    if(i<18){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
            }
            if(nowHour>=14 && nowHour<=15){
                for(var i=0;i<=lis.length;i++){
                    if(i<20){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
            }
            if(nowHour>=16 && nowHour<=19){
                var d=moment().add(1,"days").format("DD");
                var lisd = $("#rilikongjian3-day").find("li");
                for(var i=0;i<=lisd.length;i++){
                    var val = $(lisd[i]).text();
                    if(d==val){
                        $("#rilikongjian3-day li").removeClass("beijingse");
                        $(lisd[i]).addClass("beijingse");

                    }
                }
            }
            if(nowHour>=20 && nowHour<=23){
                for(var i=0;i<=lis.length;i++){
                    if(i<2){
                        $(lis[i]).addClass("rili-time-no");
                    }
                }
                $("#rilikongjian3-day li").removeClass("beijingse");
                $("#rilikongjian3-day").find("li:nth-child(2)").addClass("beijingse");
            }
        }
        if(nyr>moment().format("YYYY-MM-DD")){
            $("#show-dateTime").find("li").removeClass("rili-time-no");
        }
        noSelectHour();
    }
    tomm();
    
    
    /*---------------暂时添加代码-------------------*/
    if(date<'2017-01-30'){
    	$("#show-dateTime li").addClass("rili-time-no");
    }
    /*---------------暂时添加代码-------------------*/
    
    

    //查询服务人员的已有派工的服务时间
//    if(staffId!=0 && staffId!=null && staffId!=''){
//    	$.ajax({
//    		type : "GET",
//    		url:siteAPIPath+"/staff/get_dispatch_dates.json?staff_id="+staffId,
//    		dataType:"json",
//    		success:function(data){
//    			if(data.data.length>0){
//    				sessionStorage.setItem("serDate",data.data);
//    			}
//    		}
//    	});
//    }

    //过滤服务人有已占有的服务时间
//    function filterServiceDate(){
//        var serviceDateArr = sessionStorage.getItem("serDate");
//        var serviceHour = sessionStorage.getItem("total_service_hour");
//        if(serviceDateArr=="" ||serviceDateArr==null ||serviceDateArr==undefined) return ;
//        var d = serviceDateArr.split(",");
//        for(var i=0;i<d.length;i++){
//        	var sd=d[i];
//        	var serviceDateAdd = moment(sd).add(serviceHour,"hours").add(2,"hours");
//        	var serviceDateSub = moment(sd).subtract(serviceHour,"hours").subtract(2,"hours");
//        	var date = moment(serviceDateAdd).format("YYYY-MM-DD");
//        	var hour1 = moment(sd).format("HH:mm");
//            var hour2 = serviceDateAdd.format("HH:mm");
//            var hoursub = serviceDateSub.format("HH:mm");
//            var s = d[i].split(" ");
//            if(date==getServiceDate()){
//                for(var i=0;i<time.length;i++){
//                	var index=i;
//                	if(hoursub>=time[0] && time[i]>hoursub && time[i]<=hour1){
//                		 var tli = $("#show-dateTime li");
//                         $(tli[index]).addClass("rili-time-no");
//                	}
//                	if(hoursub<time[0] && time[i]<=hour1){
//                		 var tli = $("#show-dateTime li");
//                         $(tli[index]).addClass("rili-time-no");
//                	}
//                    if(time[i]>=hour1 && time[i]<=hour2){
//                       var tli = $("#show-dateTime li");
//                       $(tli[index]).addClass("rili-time-no");
//                    }
//                }
//            }
//        }
//    }
    
    //过滤非周一到周三的时间
   /* function filterWeek(serviceTypeId){
    	if(serviceTypeId==undefined ||serviceTypeId=='' ||serviceTypeId==null) return false;
    	if(serviceTypeId=='69' || serviceTypeId=='70'){
    		var serviceTime = getServiceDate();
    		var week = moment(serviceTime).format("d");
    		
    		if(week=='4' || week=='5' || week=='6' || week=='0'){
    			var ss=$("#show-dateTime");
    			$("#show-dateTime li").addClass("rili-time-no");
    		}
    	}
    }*/
//    filterWeek(serviceTypeId);

    //获取选择的服务时间
    $("#checkDate").click(function(){
        var st = getServiceDate()+" "+dayTime+":00";
        if(dayTime!=""){
            $("#serviceDate").val(st);
            $(this).attr("data-dismiss","modal");
        }else{
            return;
        }
    });
});

    