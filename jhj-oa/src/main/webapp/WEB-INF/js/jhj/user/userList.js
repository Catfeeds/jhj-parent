function doReset(){
		$("#mobile1").attr("value","");
		$("#name1").attr("value","");
	}

 function download(){
    var url="/"+appName+"/user/download_project";
    window.open(url);
}