/**
 * 检查字符串是否为合法手机号码
 * @param {String} 字符串
 * @return {bool} 是否为合法手机号码
 */
function isPhone(aPhone) {
	var bValidate = RegExp(
			/^(0|86|17951)?(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$/)
			.test(aPhone);
	if (bValidate) {
		return true;
	} else
		return false;
}

/**
 * 检查字符串是否为合法email地址
 * @param {String} 字符串
 * @return {bool} 是否为合法email地址
 */
function isEmail(aEmail) {
	var bValidate = RegExp(
			/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/)
			.test(aEmail);
	if (bValidate) {
		return true;
	} else
		return false;
}