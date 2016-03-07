
function preview(htmlUrl){
   		window.open(appRootUrl+htmlUrl); 
	}
function sendAgain(msgId){
		$.ajax({
	        type:"post",
	        url:"/"+appName+"/interface-promotion/send_msg_again.json",
	        dataType:"json",
	        cache:false,
	        data:"msgId="+msgId,
	        success :function(data){
	        	var data = data.data;
	        	BootstrapDialog.show({
	        	    title: '提示语',
	        	    message: data,
	        	    buttons: [{
	        	        id: 'btn-ok',   
	        	        icon: 'glyphicon glyphicon-check',       
	        	        label: 'OK',
	        	        cssClass: 'btn-primary', 
	        	        autospin: false,
	        	        action: function(dialogRef){    
	        	            dialogRef.close();
	        	        }
	        	    }]
	        	});
	        },
		    error : function(){}
	    });
	}