$(function(){
	var bjIndex=$(".index-shouye")[0];
	var mobile=$(".index-shouye3")[0];
	var daohangTop=$(".index-shouye4")[0];
	var daohangTop1=$(".index-shouye5")[0];
	var daohangTop2=$(".index-shouye6")[0];
	var bodyHeight= document.documentElement.clientHeight;
	bjIndex.style.height=bodyHeight+"px";
	mobile.style.top=(bodyHeight-542)/2+"px";
	daohangTop.style.top=(bodyHeight-542)+"px";
	daohangTop1.style.top=(bodyHeight-442)+"px";
	daohangTop2.style.top=(bodyHeight-202)+"px";
})