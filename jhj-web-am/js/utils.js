/**
 * 判断是否是数字
 * @param value
 * @returns {Boolean}
 */
function isNum(value){
    if( value != null && value.length>0 && isNaN(value) == false){
        return true;
    }else{
        return false;
    }
}

/*
 * 判断 输入的 金额 值  是否 合法
 */
function isMoneyNum(value){
	var re = /^(([1-9][0-9]*\.[0-9][0-9]*)|([0]\.[0-9][0-9]*)|([1-9][0-9]*)|([0]{1}))$/; 
	
	if( value != null && value.length>0 && isNaN(value) == false && re.test(value)){
        return true;
    }else{
        return false;
    }
	
}

/*
 *  针对助理调整金额：   限制 输入框只能 输入 数字 和 小数
 */
function clearNoNum(obj){   

	obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符  

	obj.value = obj.value.replace(/^\./g,"");  //验证第一个字符是数字而不是. 

	obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.   
	
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	
}