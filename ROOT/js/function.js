/*
兼容问题   2015-4-20 18:20
*/
/*   
$函数   $(selector,obj)
参数1：string    选择器
       function  window.onload
参数2：obj   范围
功能：1. $(function(){   })    ===window.onload=function(){}   
      2. 获取页面元素 
        返回值： 类名 标签 返回元素集合
                 ID   返回的是元素
        $(选择器,范围)
        $(".box")  通过类名方式获取
        $("#box")  通过ID方式获取
        $("div")   通过标签名方式获取
        $("<div>") 创建一个元素节点
*/
function $(selector,obj){
    var obj=obj||document;
    if(typeof selector=="string"){
        selector=trim(selector);
    	if(selector.charAt(0)=="."){
    	   return getClass(selector.substring(1),obj);
    	}else if(selector.charAt(0)=="#"){
            //#box    box
    	   return document.getElementById(selector.substring(1));
           //div h2
    	}else if(/^[a-z|1-9]{1,10}$/.test(selector)){
    	    return obj.getElementsByTagName(selector)
    	}else if(/^<[a-z|1-9]{1,10}>$/g.test(selector)){// $("<div>")
            return document.createElement(selector.slice(1,-1))
        }
    }
    if(typeof selector=="function"){
    	window.onload=function (){
    		selector();
    	}
    }

}
/*
去除 字符串 前后空格
*/
function trim(str){
    return str.replace(/^\s+|\s+$/g,"")
}
/*
getClass(val,obj)
参数1： val   那个类名
参数2： obj   范围 
*/
function getClass(val,obj){
	var obj=obj||document;
	if(document.getElementsByClassName){
		//IE 9-11  chrome FF
	    return obj.getElementsByClassName(val);
	}else{
		//IE6-8
	var all=obj.getElementsByTagName("*");
	var ele=[];
	for(var i=0;i<all.length;i++){
		//ml-3 innerbox == innerbox
		if(check(all[i].className,val)){
			ele.push(all[i]);
		}
	}
	return ele;    
	}
}
//检测是多个类名
//check("1 2 3",2)
function check(classname,val){
	var arr=classname.split(" ");// arr=[1,2,3]
	for(var i=0;i<arr.length;i++){
	      if(arr[i]==val){// 1==2
		return true;
	      }  
	}
	return false;
}
/*
getText(obj,val)
参数1: obj  需要获取的对象
参数2: val  可选  如果传递val 则为设置
*/
function getText(obj,val){
    if(obj.innerText){
        if(val){
            obj.innerText=val;
        }else{
            return obj.innerText;
        }
    }else{
        if(val){
            obj.textContent=val;
        }else{
            return obj.textContent;
        }
    }
}
/*
获取行内、样式表中的样式
getStyle(box,"width")
 */
function getStyle(obj,attr){
  return  obj.currentStyle?obj.currentStyle[attr]:getComputedStyle(obj,null)[attr];
}
/*
循环
*/
function each(obj,callback){
    for(var i=0;i<obj.length;i++){
        callback(i);
    }
}


/*
获取所有子节点
*/
function getChilds(obj){
    var alls=obj.childNodes;
    var arr=[];
    for(var i=0;i<alls.length;i++){
     //alls[i].nodeType=3 || alls[i].nodeType=8
     //不要注释  不要空白
        if( !((alls[i].nodeType==3 && alls[i].nodeValue.replace(/^\s+|\s+$/g,"")=="")|| alls[i].nodeType==8)){
            arr.push(alls[i]);
        }
    }
    return arr;
}
/*
获取第一个子节点的引用
*/
function getFirst(obj){
    var all=getChilds(obj);
    return all[0];
}
/*
获取最后一个子节点的引用
*/
function getLast(obj){
    var all=getChilds(obj);
    return all[all.length-1];
}
/*
获取下一个兄弟节点的引用
 (next.nodeType==3 && next.nodeValue.replace(/^\s+|\s+$/g,"")=="")|| next.nodeType==8
*/
function getNext(obj){
    var next=obj.nextSibling; 
    if(next==null){
        return false;
    }
    while((next.nodeType==3&&next.nodeValue.replace(/^\s+|\s+$/g,"")=="") || next.nodeType==8){   
            next=next.nextSibling;
            if(next==null){
                return false;
            }
    }     
    return next;
}
/*
getUp(obj)  获取上一个兄弟节点的引用
*/

function getUp(obj){
    var up=obj.previousSibling;
    if(up==null){
        return false;
    } 
    while((up.nodeType==3&&up.nodeValue.replace(/^\s+|\s+$/g,"")=="") || up.nodeType==8){
            up=up.previousSibling;
            if(up==null){
                return false;
            }  
    }     
    return up;
}


/*
    insertAter(aobj,bobj)  插入到谁的后面
    aobj  要插入的对象
    bobj  插入到谁的后面
*/
// function insertAfter(aobj,bobj){
//     var parent=bobj.parentNode;
//     var next=getNext(bobj);
//     if(next){
//         parent.insertBefore(aobj,next);
//     }else{
//         parent.appendChild(aobj);   
//     }
    
// }
Node.prototype.insertAfter=function (aobj,bobj){
    var next=getNext(bobj);
        if(next){
            this.insertBefore(aobj,next);
        }else{
            this.appendChild(aobj);   
        }
        return this;
}
/*
鼠标移上去  width   height 
*/
function toggle(box,link){
    var bw;
    var bh;
    link.onmouseover=function(){
        if(getStyle(box,"display")=="none"){
            box.style.display="block";
            bw=parseInt(getStyle(box,"width"));
            bh=parseInt(getStyle(box,"height"))
        }
        box.style.width=0+"px";
        box.style.height=0+"px";
        animate(box,{"width":bw,"height":bh},500)
    }
    link.onmouseout=function(){
        animate(box,{"width":0,"height":0},500)
    }
}

