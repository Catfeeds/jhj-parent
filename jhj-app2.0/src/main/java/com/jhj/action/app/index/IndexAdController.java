package com.jhj.action.app.index;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.dao.dict.DictAdMapper;
import com.jhj.po.model.dict.DictAd;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/indexAd")
public class IndexAdController extends BaseController {
	
	@Autowired
	private DictAdMapper adMapper;
	/**
	 * 获得广告位的图片的列表
	 * @return
	 */
	@RequestMapping(value = "/get_ad_list", method = RequestMethod.GET)
	public AppResultData<Object> adList(Model model) {
	 	
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, new String());
		
		List<DictAd> dictAdList = adMapper.selectByListPage();
		
		//AdUrlVo vo = new AdUrlVo();
		/*List<String> imgUrList = new ArrayList<String>();
		for (DictAd item : dictAdList) {
			imgUrList.add(item.getImgUrl());
			//vo.setImgUrl(imgUrList);
		}*/
		result.setData(dictAdList);
		/*model.addAttribute("contentModel",result);
*/
		return result;
	}
}
