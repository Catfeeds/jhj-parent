package com.jhj.action.app.period;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.period.PeriodOrderAddons;
import com.jhj.service.period.PeriodOrderAddonsService;
import com.jhj.vo.period.PeriodOrderAddonsVo;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping("/app/period")
public class PeriodOrderAddonsController {
	
	@Autowired
	private PeriodOrderAddonsService periodOrderAddonsService;
	
	@RequestMapping(value="/get_period_order_addons_list.json", method = RequestMethod.GET)
	public AppResultData<Object>  getPeriodOrderAddonsList(@RequestParam("periodOrderId") Integer periodOrderId){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		List<PeriodOrderAddons> periodOrderAddonsList = periodOrderAddonsService.selectByPeriodOrderId(periodOrderId);
		
		if(periodOrderAddonsList!=null && periodOrderAddonsList.size()>0){
			for(int i=0;i<periodOrderAddonsList.size();i++){
				PeriodOrderAddons periodOrderAddons = periodOrderAddonsList.get(i);
				PeriodOrderAddonsVo transVo = periodOrderAddonsService.transVo(periodOrderAddons);
				periodOrderAddonsList.set(i, transVo);
			}
		}
		
		result.setData(periodOrderAddonsList);
		return result;
	}

}
