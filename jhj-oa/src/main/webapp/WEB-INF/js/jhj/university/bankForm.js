function qNumCheck(){
	
	//及格达标 题目数
	var needNum = $("#totalNeed").val();
	
	//考试时，从该题库中随机取出的题目数量
	var randomQNum = $("#randomQNum").val();
	if(needNum > randomQNum){
		
		alert("达标题目数必须不大于题目总数");
		
		return false;
	}
}