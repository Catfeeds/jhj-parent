package com.jhj.action.app;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.meijia.utils.vo.AppResultData;
import com.meijia.utils.HttpClientUtil;
import com.meijia.wx.utils.JsonUtil;

@Controller
@RequestMapping(value="/app/map")
public class MapController {

	@RequestMapping(value = "autocomplete", method = RequestMethod.GET)
    public AppResultData<Object> autoComplete(
    		@RequestParam("query") String query,
    		@RequestParam(value = "region", required = false, defaultValue = "全国") String region
    		) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Map<String, String> params = new HashMap<String, String>();
//		query = URLDecoder.decode(query);
		try {
			query = new String(query.getBytes("iso-8859-1"),"utf-8");
			region = new String(region.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("query", query);
		params.put("region", region);
		params.put("output", "json");
		params.put("ak", "2sshjv8D4AOoOzozoutVb6WT");
		String url = "http://api.map.baidu.com/place/v2/suggestion";
		String getResult = HttpClientUtil.get(url, params);
		
		Map<String, Object> m = JsonUtil.stringToMap(getResult);
		if ( m.get("message").toString().equals("ok")) {			
			result.setData(m.get("result"));
			System.out.println(m.get("result"));
		}
		
    	return result;
    }

}
