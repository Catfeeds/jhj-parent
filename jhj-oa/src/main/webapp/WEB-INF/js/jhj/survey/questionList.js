

//参数 : 选项主键 id
function optionRelatQuestion(flag,qId){
	
	window.location.href = "/jhj-oa/survey/option_relate?id="+flag+"&qId="+qId;
	
};

//关联内容
function optionRelatContent(flag,optionNo,qId){
	
	window.location.href = "/jhj-oa/survey/ref_content_form?id="+flag+"&qId="+qId+"&optionNo="+optionNo;
	
}