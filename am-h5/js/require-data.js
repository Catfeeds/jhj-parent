//检测必备的数据是否正确。

//检测
if (localStorage.getItem('university_service_type_list') == null) {
	universityServiceTypeList();
}

if (localStorage.getItem('university_learning_list') == null) {
	universityLearningList();
}


/* --- 1 
 * 叮当大学,预加载所有 需考核的 service */
function universityServiceTypeList(){
	$.ajax({
		type : "GET",
		url : "http://"+window.location.host+"/jhj-app/app/"+ "university/load_all_service.json?",
		dataType : "json",
		async:false,
		cache : true,
		statusCode : {
				200 : ajaxUniversityServiceTypeListSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
	});
}


function ajaxUniversityServiceTypeListSuccess(data, textStatus, jqXHR){
	
	var result = data;
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var serviceTypeList = JSON.stringify(result.data);
	localStorage.setItem('university_service_type_list', serviceTypeList);
}

/*叮当大学，根据 服务Id得到  服务名称 */
function getUniversityServiceTypeName(serviceTypeId){
	
	var serviceTypeList = JSON.parse(localStorage.getItem("university_service_type_list"));
	var serviceName = "";
	
	$.each(serviceTypeList, function(i,item){
		if (item.service_type_id == serviceTypeId) {
			serviceName = item.name;
		}
	});
	
	return serviceName;
}	


/*	-- 2
 *  预加载所有 培训资料 */
function universityLearningList(){
	$.ajax({
		type : "GET",
		url : "http://"+window.location.host+"/jhj-app/app/"+ "university/load_study_learning.json?",
		dataType : "json",
		async:false,
		cache : true,
		statusCode : {
				200 : ajaxUniversityLearningListSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
	});
}

function ajaxUniversityLearningListSuccess(data, textStatus, jqXHR){
	var result = data;
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var serviceTypeList = JSON.stringify(result.data);
	localStorage.setItem('university_learning_list', serviceTypeList);
}
	
/*叮当大学，根据 服务Id得到 培训资料 */
function getUniversityLearningDetail(serviceTypeId){
	
	var learningList = JSON.parse(localStorage.getItem("university_learning_list"));
	var learningDetail = "";
	
	$.each(learningList, function(i,item){
		if (item.service_type_id == serviceTypeId) {
			learningDetail = item.content;
		}
	});
	
	return learningDetail;
}	
