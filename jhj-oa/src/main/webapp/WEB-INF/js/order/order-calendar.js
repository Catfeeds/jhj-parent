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
    //获取当前日期
    var date=moment().format("YYYY-MM-DD");
    var nowDate=date;
    var nowHour=moment().hour();
    
    var dayTime=""; 
    var selectDay="#show-day li p[class='rili-day']";
    
    //展示时间 
    function showTime(selectDate,val){
        var dateTime='';
        var html = $("#show-dateTime").html();
        for(var i=0;i<time.length;i++){
            var notSelectTime=['11:30','12:00','12:30'];
            if(time[i]==notSelectTime[0] || time[i]==notSelectTime[1] || time[i]==notSelectTime[2]){
                dateTime+="<li class='rili-time-no'><p>"+time[i]+"</p><p>约满</p></li>";
            }else{
            	if(val==undefined || val==null || val==''){
    				dateTime+="<li>"+time[i]+"</li>";
            	}else{
            		for(var j = 0 ; j < val.length;j++){
            			if(time[i]==val[j].service_hour){
            				if(val[j].is_full==0){
            					dateTime+="<li>"+time[i]+"</li>";
            				}
            				if(val[j].is_full==1){
            					dateTime+="<li class='rili-time-no'><p>"+time[i]+"</p><p>约满</p></li>";
            				}
            			}
            		}
            	}
            }
        }
        $("#show-dateTime").html(dateTime);
        $("#show-dateTime li").on("click",function(obj){
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
	  });
        tomm(selectDate);
    }
    showTime();
    
  //显示年月
	function showYearMonth(date){
		var calendar = date;
		$("#show-year").text(moment(calendar).format("YYYY"));
    	$("#show-month").text(moment(calendar).format("MM"));
	}
	showYearMonth(date);
	
	 //获取当前选择的时间，如何没有选择时间默认是当前时间
    function getServiceDate(){
        var serviceDate='';
        var year = $("#show-year").text();
        var month = $("#show-month").text();
        var day = $("#show-day li p[class='rili-day']").text();
        if(day==undefined || day==null || day==''){
       	 	day = $(selectDay).text();
        }
        var pre_li = $(selectDay).prevAll("li");
        var after_li = $(selectDay).nextAll("li");
        var flag=0;
        var flag1=0;
        var flag2=0;
        if(pre_li.length>0 && after_li.length>0){
            for(var i=0;i<pre_li.length;i++){
                var val = $(pre_li[i]).text();
                if(val<day){
                    flag++;
                }else{
                    flag1++;
                }
            }
            for(var j=0;j<after_li.length;j++){
                var val=$(after_li[j]).text();
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
        return  serviceDate;
    }
    
    //是否约满
    function isFull(serviceDateStr){
    	var param = {};
    	param.service_type_id = sessionStorage.getItem("service_type_id");
    	param.addr_id = localStorage.getItem("default_addr_id");
    	if(serviceDateStr==undefined || serviceDateStr==null || serviceDateStr==''){
    		serviceDateStr = moment().format("YYYY-MM-DD");
    	}
    	param.service_date_str = serviceDateStr;
    	$.ajax({
    		type:"POST",
    		url:"http://localhost:8080/jhj-app/app/order/check_dispatch.json",
    		data:param,
    		success:function(data){
    			if(data.status=='0' && data.msg=='ok'){
    				var result = data.data;
    				showTime(serviceDateStr,result);
    			}
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
    	$("#show-week").html(contentWeek);
    	$("#show-day").html(contentDay);
    	showYearMonth(cal);
    	
    	$("#show-day li").on("click",function(){
    		selectDay = $(this);
    		$("#show-day").find("li p").removeClass("rili-day");
    		$("#show-dateTime li").removeClass("rili-time");
    		$("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
    		$(this).find("p").addClass("rili-day");
//    		tomm();
    		if(cal==date){
    			if(nowHour>=16){
    				$("#show-day li").removeClass("rili-time");
    				$(selectDay).addClass("rili-time");
    				$("#show-dateTime li").addClass("rili-time");
    			}
    		}
    		isFull(getServiceDate());
    		
    	});
    	$("#show-dateTime li").removeClass("rili-time-no");
    	$("#show-day").find(":first-child p").addClass("rili-day");
    	var compareDate = getServiceDate();
    	isFull(compareDate);
//    	if(cmp==date){
//    		tomm(cmp);
//    	}
    }
    getDay(date);

    //日历增加或减少日期
    function get7Day(selectDay,countDay){
    	var afterDay
    	if(selectDay==undefined || selectDay==null || selectDay==''){
    		afterDay = moment(date).add(countDay, 'days');
    	}else{
    		afterDay = moment(selectDay).add(countDay, 'days');
    	}
        nowDate=afterDay;
        getDay(afterDay);
    }

    //日历减1天
    $("#substranc-day").click(function(){
    	$("#show-dateTime").find("li").removeClass("rili-time");
    	$("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
        dayTime="";
        var service_date = getServiceDate();
        var comp_day = moment(service_date).add(-7,'days').format("YYYY-MM-DD");
        if(comp_day>=date){
	         get7Day(service_date,-dayNum);
        }
        if(comp_day<=date && service_date>date){
        	var days = moment(service_date).diff(moment(date),'days');
        	get7Day(service_date,-days);
        }
    });

    //日历加1天
    $("#add-day").click(function(){
        $("#show-dateTime").find("li").removeClass("rili-time");
        $("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
        dayTime="";
        var service_date = getServiceDate();
        get7Day(service_date,dayNum);
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
        if(nyr==date){
            var lis = $("#show-dateTime").find("li");

            if(nowHour>=0 && nowHour<=4){
            	filterBackDate(lis,2);
            }
            if(nowHour>4 && nowHour<=6){
            	filterBackDate(lis,4);
            }
            if(nowHour==7){
            	filterBackDate(lis,6);
            }
            if(nowHour>=8 && nowHour<=9){
            	filterBackDate(lis,10);
            }
            if(nowHour==10){
            	filterBackDate(lis,12);
            }
            if(nowHour==11){
            	filterBackDate(lis,14);
            }
            if(nowHour==12){
            	filterBackDate(lis,16);
            }
            if(nowHour==13){
            	filterBackDate(lis,18);
            }
            if(nowHour>=14 && nowHour<=15){
            	filterBackDate(lis,20);
            }
            if(nowHour>=16 && nowHour<=19){
                filterBackDate(lis,21);
            }
            if(nowHour>=20 && nowHour<=23){
            	filterBackDate(lis,2);
                $("#show-day li").removeClass("rili-time");
                $("#show-day").find("li:nth-child(2)").addClass("rili-time");
            }
        }
        if(nyr>date){
            $("#show-dateTime").find("li").removeClass("rili-time-no");
        }
    }
    
    function filterBackDate(arrys,compNum){
    	for(var i=0;i<=arrys.length;i++){
            if(i<compNum){
                $(arrys[i]).addClass("rili-time-no");
                var listext = $(arrys[i]).text();
                $(arrys[i]).html("<P>"+listext+"</p><p>约满</P>");
            }
        }
    }
    
  //选择时间
	 $("#show-dateTime li").on("click",function(obj){
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
	  });

    //获取选择的服务时间
    $("#checkDate").click(function(){
    	var year = $("#show-year").text();
        var month = $("#show-month").text();
        var day = $("#show-day li p[class='rili-day']").text();
        var st =year +"-"+month+"-"+day+" "+dayTime+":00";
        if(dayTime!=""){
            $("#serviceDate").val(st);
            $(this).attr("data-dismiss","modal");
        }else{
        	 $(this).attr("data-dismiss","");
            return false;
        }
    });
});
