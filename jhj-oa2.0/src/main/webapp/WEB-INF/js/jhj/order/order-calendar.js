var orgStaffId = 0;
$(function(){
	orgStaffId = $("#org_staff_id").val();
	$('#calendar').fullCalendar({
	    lang: 'zh-cn',
	    theme: false,
	    header: {
	        left: 'prev,next today',
	        center: 'title',
	        right: 'month,agendaDay'
	    },
	    height: 550,
	    firstHour:6,
	    slotMinutes:30,
	 /*   events: {
	    	allDay:true,
	        url: '/jhj-oa/interface-order/get-dispatch-by-month.json?org_staff_id='+orgStaffId,
	        error: function() {
	            $('#script-warning').show();
	        }
	    },
	   */
	    axisFormat:'H:mm',
	    slotMinutes:30,
	    allDaySlot:false,
	
	});
	var view=$('#calendar').fullCalendar('getView');
	var startStr = new Date(view.start).toLocaleDateString();
	var endStr = new Date(view.end).toLocaleDateString();
	var start = startStr.replace(new RegExp('/','gm'),'-');
	var end = endStr.replace(new RegExp('/','gm'),'-');
    $.getJSON('/jhj-oa/interface-order/get-dispatch-by-month.json?',{org_staff_id:orgStaffId,start:'2015-07-27',end:'2015-09-27'},function(data) {    
        for(var i=0;i<data.length;i++) {    
            var obj = new Object();    
            obj.id = data[i].id;    
            obj.title = data[i].title;                   
            //obj.description = data[i].description;            
            obj.color = data[i].color;  
            //obj.remindertime = $.fullCalendar.parseDate(data[i].remindertime);  
            //obj.messagenotice = data[i].messagenotice;  
            obj.description = "的水立方接口";  //data[i].description
            obj.start = data[i].start;                   
            obj.end =data[i].end; 
            obj.url = data[i].url;
            $("#calendar").fullCalendar('renderEvent',obj,true);                     
        }    
      });
});
