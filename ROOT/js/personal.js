$(function(){
	var mainList=$(".main-banner-top-list2")[0];
	var xialaList=$(".main-xiala-list")[0];
	var mainList1=$(".main-banner-top-list3")[0];
	var xialaList1=$(".main-xiala-list1")[0];
	mainList.onmouseover=function(){
		xialaList.style.display="block";
	}
	xialaList.onmouseover=function(){
		xialaList.style.display="block";
	}
	mainList.onmouseout=function(){
		xialaList.style.display="none";
	}
	xialaList.onmouseout=function(){
		xialaList.style.display="none";
	}
	mainList1.onmouseover=function(){
		xialaList1.style.display="block";
	}
	xialaList1.onmouseover=function(){
		xialaList1.style.display="block";
	}
	mainList1.onmouseout=function(){
		xialaList1.style.display="none";
	}
	xialaList1.onmouseout=function(){
		xialaList1.style.display="none";
	}

	




	var inbox=$(".innerbox")[0];
	var imgs=$("img",inbox);
	var btnl=$(".btn-left")[0];
	var btnr=$(".btn-right")[0];
	var btns=$("li",$(".btn")[0]);
	var iw=parseInt(getStyle(imgs[0],"width"));//获取一个图片的宽度
	inbox.style.width=iw*imgs.length+"px";
	var num=0;
	var tt=setInterval(moveLeft,3000) //左边动画
	function moveLeft(){//左边 动画函数 
		animate(inbox,{"marginLeft":-iw},500,function(){
			var first=getFirst(inbox);
			this.appendChild(first);
			this.style.marginLeft=0;
			num++;
			if(num==btns.length){
				num=0;
			}
			for(var i=0;i<btns.length;i++){
				btns[i].id="";
			}
			btns[num].id="hot";
		})
	}

	btnr.onclick=function(){//左边按钮点击
		moveLeft();
	}
	btnl.onclick=function(){//右边边按钮点击
		num--;
		if(num<0){
			num=btns.length-1;
		}
		for(var i=0;i<btns.length;i++){
			btns[i].id="";
		}
		btns[num].id="hot";
		var last=getLast(inbox);
		var first=getFirst(inbox);
		inbox.insertBefore(last,first);
		inbox.style.marginLeft=-iw+"px";
		animate(inbox,{"marginLeft":0},500)
	}
	inbox.onmouseover=function(){
		clearInterval(tt);

	}
	inbox.onmouseout=function(){
		tt=setInterval(moveLeft,3000)
	}


	
})