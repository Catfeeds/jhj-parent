package com.jhj.action.app.dict;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.po.model.dict.DictAd;
import com.jhj.po.model.dict.DictServiceTypes;
import com.jhj.service.dict.AdService;
import com.jhj.service.dict.ServiceTypeService;
import com.meijia.utils.vo.AppResultData;


@Controller
@RequestMapping(value="/app/dict")
public class BaseDataController<T> {

	@Autowired
	private ServiceTypeService serviceTypeService;

	@Autowired
	private AdService adService;

    @SuppressWarnings({"unchecked", "rawtypes" })
	@RequestMapping(value = "get_base_datas", method = RequestMethod.GET)
    public AppResultData<HashMap<String, Object>> getBaseDatas() {

    	//获得广告配置定义列表项
    	List<DictAd> listAd = adService.selectByAdType((short) 0);

    	//获得服务类型配置定义列表项
    	List<DictServiceTypes> listServiceType = serviceTypeService.getServiceTypes();

    	//组装成基础数据返回格式
    	HashMap<String, Object> resultDatas = new HashMap<String, Object>();

    	resultDatas.put("banner_ad", listAd);

    	resultDatas.put("service_types", listServiceType);

    	AppResultData<HashMap<String, Object>> result = null;
    	result = new AppResultData<HashMap<String, Object>>(0, "ok", resultDatas);

    	return result;
    }

}
