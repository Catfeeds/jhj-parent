/**
 * 只允许输入数字
 */
function inputNum(obj) {
	var tmptxt=obj.val();
	tmptxt = tmptxt.replace(/[^\d\.]/g,'');
	obj.val(tmptxt);
}

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

/**
 * 替换url中的参数
 * @param value
 * @returns {Boolean}
 */
function replaceParamVal(url, paramName, replaceWith) {
    var re = eval('/(' + paramName + '=)([^&]*)/gi');
    var nUrl = url.replace(re, paramName + '=' + replaceWith);
    return nUrl;
}

/*
 * 去除字符串 中的 中文
 * 
*/

function delChinese(someString){
	var reg=/[\u4E00-\u9FA5]/g;
		var result = someString.replace(reg,'');
	
	return result;
	
}

/*
 * 处理 人民币，数字   ￥123.322  -->  123.322
 */
function delSomeString(someString){
	
	var result = someString.substr(1,someString.length);
	
	return result;
}



