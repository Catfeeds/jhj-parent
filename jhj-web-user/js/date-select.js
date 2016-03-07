	 
/*********** 符合 下单 时间选择逻辑 的插件 (start) ***********/
	/*
	 *  使用 说明：
	 *     页面需要 提供   
	 *     
	 * 		<input type="text" id="serviceDate" name="serviceDate" >
	 *  
	 */

function dateSelect () {

		var serviceDateValues = [];
		
		var start =0;
		//当前整点小时数
		var nowHour = moment().hour();
		
//		var nowHour = 11;
		if (nowHour >= 16) start = 1;
		
		for (var i = start; i < 14; i++) {
			var tempDay = moment().add(i,'days').format('YYYY-MM-DD');
			serviceDateValues.push(tempDay);
		}
		
		var serviceHoursValues = [];
		// 可选 小时 的  最大范围 （考虑 时长）， 为  6 ~ 19
		for (var i =6; i < 20; i++) {
			var tempHour = moment({ hour:i}).format('HH');
			serviceHoursValues.push(tempHour);
		}
		
		/*
		 * 2. 今天下单，判断当前 的时间点  
		 * 
		 */

		// 当天  0~6点 （6点多）， 时间 起止范围    10~19 （不考虑服务时长）
		var dis1 = [10,11,12,13,14,15,16,17,18,19]; 
		
		// 当天  7 点 ~ 12点（12点多） ，时间起止范围  11~ 16 
		var dis2 = [11,12,13,14,15,16];
		
		//当天 13 ~ 15点（！！！不能是 15点多 ） ，时间 起止范围  17~19
		var dis3 = [17,18,19];
		
		// 当天 16~ 19点， 起止范围 第二天  6~ 19
		var dis4 = [6,7,8,9,10,11,12,13,14,15,16,17,18,19];
		
		//当天 20 ~23点 ，时间起止范围 为 第二天  10~ 19
		var dis5 = [10,11,12,13,14,15,16,17,18,19]; 
		
		if(nowHour < 7){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis1,
					serviceMins : ['00', '30']
			};
		}
		
		if(nowHour > 6 && nowHour <= 12){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis2,
					serviceMins : ['00', '30']
			};
		}
		
		if(nowHour >12 && nowHour <= 15){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis3,
					serviceMins : ['00', '30']
			}
		}
		
		if(nowHour >= 16 && nowHour <= 19){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis4,
					serviceMins : ['00', '30']
			}  
		}
		
		if(nowHour > 19 && nowHour <=23){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis5,
					serviceMins : ['00', '30']
			}  
		}
		
		//页面加载时。默认提供 一个 时间值
		$$("#serviceDate").val(serviceDateVendors.serviceDates[0] + "  " +serviceDateVendors.serviceHours[0] + ":"  + serviceDateVendors.serviceMins[0])

		
		
		var b =true;
		var a = true;
		
		var c = true;
		var d = true;
		
		var e = true;
		var f = true; 
		
	   var pickerInline = myApp.picker({
			input: '#serviceDate',		
			toolbarCloseText:'确认',
			onChange: function(p, values, displayValues){
				
				//时间为 19:00，则不能 选  19:30
				if(values[1] == 19){
					p.cols[3].setValue('00');
				}
				
				// 在今天  选  今天
				if(moment(values[0]).fromNow().indexOf("前")>0){
					//第一次加载，根据当前时间 加载 不同 的 时间段
//					console.log("e="+e);
//					console.log("f="+f);
					if(!f) return;
					
					if(e && f){
						// 0~6 点
						if(nowHour < 7){
							p.cols[1].replaceValues(dis1);
						}
						
						if(nowHour >=7 && nowHour <=12){
							
							if(nowHour < dis2[0]){
								var index = dis2.indexOf(nowHour+4);
								p.cols[1].replaceValues(dis2.slice(index));
							}else{
								var index=dis2.indexOf(nowHour);
								p.cols[1].replaceValues(dis2.slice(index+4));
							}
							
						}
						
						if(nowHour >12 && nowHour <= 15){
							
							var index=dis3.indexOf(nowHour+4);
							p.cols[1].replaceValues(dis3.slice(index));
						}
						
						if(nowHour >15 && nowHour <= 19){
							p.cols[1].replaceValues(dis4);
						}
						if(nowHour > 19 && nowHour <= 23){
							p.cols[1].replaceValues(dis5);
						}
							
						e = false;
						return;
					}
					
					// 经过 了    “今天 选明天 之后”，回滚时，需要重新加载  当前时间 对应 的 时间段
					while(f && !e && !d){
						if(nowHour < 7){
							p.cols[1].replaceValues(dis1);
						}
						
						if(nowHour >=7 && nowHour <=12){
							if(nowHour < dis2[0]){
								var index = dis2.indexOf(nowHour+4);
								p.cols[1].replaceValues(dis2.slice(index));
							}else{
								var index=dis2.indexOf(nowHour);
								p.cols[1].replaceValues(dis2.slice(index+4));
							}
						}
						
						if(nowHour >12 && nowHour <= 15){
							var index=dis3.indexOf(nowHour+4);
							p.cols[1].replaceValues(dis3.slice(index));
						}
						
						if(nowHour >15 && nowHour <= 19){
							p.cols[1].replaceValues(dis4);
						}
						
						if(nowHour > 19 && nowHour <= 23){
							p.cols[1].replaceValues(dis5);
						}
						
						d = true;
					}
					
				}
				
				// 今天 选 明天  
				var selectDay = moment(moment(values[0])).format("YYYY-MM-DD");
				var  inDay = moment(selectDay).toNow();
				
				if(inDay.indexOf("前")>0 && nowHour < 16){
					
//					console.log("c="+c);
//					console.log("d="+d);
					if(c && d){
						p.cols[1].replaceValues(dis4);
						c = true;
						d = false;
					}
					while(!c && !d){
						p.cols[1].replaceValues(dis4);
						c = false;
						d = true;
					}
				}
				
				//今天 20点  之后 选  明天 和 后天 	
				if(moment(values[0]).toNow().indexOf("天")>0){
					while(b && a){
						p.cols[1].replaceValues(dis4);
						b =false;
					}
					while(!b && !a){
						p.cols[1].replaceValues(dis4);
						b = true;
						a = false;
					}
					
				}else{
					if(nowHour < 7){
						while(a && !b && d){
							p.cols[1].replaceValues(dis1);
							a = false;
						}
						while(!a && b && d){
							p.cols[1].replaceValues(dis1);
							a = true;
						}
					}
					
					if(nowHour > 6 && nowHour <= 12){
						while(a && !b && d){
							if(nowHour < dis2[0]){
								var index = dis2.indexOf(nowHour+4);
								p.cols[1].replaceValues(dis2.slice(index));
							}else{
								var index=dis2.indexOf(nowHour);
								p.cols[1].replaceValues(dis2.slice(index+4));
							}
							a = false;
						}
						while(!a && b && d){
							if(nowHour < dis2[0]){
								var index = dis2.indexOf(nowHour+4);
								p.cols[1].replaceValues(dis2.slice(index));
							}else{
								var index=dis2.indexOf(nowHour);
								p.cols[1].replaceValues(dis2.slice(index+4));
							}
							a = true;
						}
					}
					
					if(nowHour >12 && nowHour <= 15){
						while(a && !b && d){
							var index=dis3.indexOf(nowHour+4);
							p.cols[1].replaceValues(dis3.slice(index));
							a = false;
						}
						while(!a && b && d){
							var index=dis3.indexOf(nowHour+4);
							p.cols[1].replaceValues(dis3.slice(index));
							a = true;
						}
					}
					
					if(nowHour >= 16 && nowHour <= 19){
						while(a && !b && d){
							p.cols[1].replaceValues(dis4);
							a = false;
						}
						while(!a && b && d){
							p.cols[1].replaceValues(dis4);
							a = true;
						}
					}
					
					if(nowHour > 19 && nowHour <=23){
						while(a && !b && d){
							p.cols[1].replaceValues(dis5);
							a = false;
						}
						while(!a && b && d){
							p.cols[1].replaceValues(dis5);
							a = true;
						}
					}
				}
			},
			formatValue: function (p, values, displayValues) {
				
				return values[0] + "  " + values[1] +':' +values[2] ;
		    },
			cols: [

			        {
			            values: serviceDateVendors.serviceDates,
			            width: 160,
			        },
			        
			        {
			            values: serviceDateVendors.serviceHours,
			        },			 
			        {
			            divider: true,
			            content: ':'
			        },
			        {
			            values: serviceDateVendors.serviceMins,
			        },			        
			 ]
		});
};		
/*********** 符合 下单 时间选择逻辑 的插件 (end) ***********/	

//当 用户 不在当前页面 时，关闭 时间 选择插件 。(解决：返回上一页面，时间选择不消失的问题)
window.onhashchange = function(){
	
	 $$(".close-picker").click();
};	


