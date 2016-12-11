$(function(){
	




	var num1=0;
	var bannerindex=$(".banner1")[0];
	var picindex=$("li",bannerindex);
	setInterval(function(){

		num1++;
		if(num1==picindex.length){
			num1=0;
		}
		for(var k=0;k<picindex.length;k++){
			picindex[k].style.zIndex="5";
			
		}
		picindex[num1].style.zIndex="10";
	},3000)
})