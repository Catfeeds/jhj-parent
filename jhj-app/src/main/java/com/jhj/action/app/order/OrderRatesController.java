package com.jhj.action.app.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderRateImg;
import com.jhj.po.model.order.OrderRates;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.DictService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderRateImgService;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderRatesVo;
import com.jhj.vo.order.OrderStaffRateVo;
import com.meijia.utils.ImgServerUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/order")
public class OrderRatesController extends BaseController {

	@Autowired
	private OrderRatesService orderRatesService;
	
	@Autowired
	private OrderRateImgService orderRateImgService;

	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private DictService dictService;

	@RequestMapping(value = "post_rate.json", method = RequestMethod.POST)
	public AppResultData<Object> PostExpCleanOrder(
			@RequestParam("user_id") Long userId, 
			@RequestParam("order_id") Long orderId,
			@RequestParam("rate_arrival") int rateArrival, 
			@RequestParam("rate_attitude") int rateAttitude, 
			@RequestParam("rate_skill") int rateSkill,
			@RequestParam(value = "rate_content", required = false, defaultValue = "") String rateContent,
			@RequestParam(value = "imgs", required = false) MultipartFile[] imgs) throws JsonParseException, JsonMappingException, IOException {
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");


		Orders orders = ordersService.selectByPrimaryKey(orderId);
		if (orders == null) {
			return result;
		}
		
		Short orderStatus = orders.getOrderStatus();
		if (!orderStatus.equals(Constants.ORDER_HOUR_STATUS_7)) {
			result.setMsg("订单完成服务之后才能评价.");
			result.setStatus(Constants.ERROR_999);
			return result;
		}
		
		//找出是否已经评价过
		
		OrderDispatchSearchVo orderRateSearchVo = new OrderDispatchSearchVo();
		orderRateSearchVo.setOrderId(orderId);
		List<OrderRates> orderRates = orderRatesService.selectBySearchVo(orderRateSearchVo);
		
		if (!orderRates.isEmpty()) {
			result.setMsg("订单已经评价过");
			result.setStatus(Constants.ERROR_999);
			return result;
		}

		//找出订单派工人员.
		OrderDispatchSearchVo disPatchSearchVo = new OrderDispatchSearchVo();
		disPatchSearchVo.setOrderId(orderId);
		disPatchSearchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(disPatchSearchVo);
		
		//循环插入
		for (OrderDispatchs op : orderDispatchs) {
			OrderRates record = orderRatesService.initOrderRates();

			
			record.setOrderId(orderId);
			record.setOrderNo(orders.getOrderNo());
			record.setStaffId(op.getStaffId());
			record.setUserId(userId);
			record.setMobile(orders.getMobile());
			record.setRateArrival(rateArrival);
			record.setRateAttitude(rateAttitude);
			record.setRateSkill(rateSkill);
			record.setRateContent(rateContent);
			
			orderRatesService.insert(record);
		}
		
		//处理图片上传.
		if (imgs != null && imgs.length > 0) {

			for (int i = 0; i < imgs.length; i++) {
				String url = Constants.IMG_SERVER_HOST + "/upload/";
				String fileName = imgs[i].getOriginalFilename();
				String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
				fileType = fileType.toLowerCase();
				String sendResult = ImgServerUtil.sendPostBytes(url, imgs[i].getBytes(), fileType);

				ObjectMapper mapper = new ObjectMapper();

				HashMap<String, Object> o = mapper.readValue(sendResult, HashMap.class);

				String ret = o.get("ret").toString();

				HashMap<String, String> info = (HashMap<String, String>) o.get("info");

				String imgUrl = Constants.IMG_SERVER_HOST + "/" + info.get("md5").toString();

				OrderRateImg orderRateImg = orderRateImgService.initOrderRateImg();

				orderRateImg.setOrderId(orderId);
				orderRateImg.setOrderNo(orders.getOrderNo());
				orderRateImg.setUserId(userId);
				orderRateImg.setMobile(orders.getMobile());
				orderRateImg.setImgUrl(imgUrl);

				orderRateImgService.insert(orderRateImg);
			}
		}
		
		orders.setOrderStatus(Constants.ORDER_HOUR_STATUS_8);
		orders.setUpdateTime(TimeStampUtil.getNowSecond());
		ordersService.updateByPrimaryKeySelective(orders);

		return result;
	}

	// 订单详情接口
	@RequestMapping(value = "get_order_rate", method = RequestMethod.GET)
	public AppResultData<Object> GetOrderRate(
			@RequestParam("user_id") Long userId, 
			@RequestParam("order_id") Long orderId,
			@RequestParam(value="staff_id",required=false) Long staffId) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Orders orders = ordersService.selectByPrimaryKey(orderId);
		if (orders == null) {
			return result;
		}
		
		Short orderStatus = orders.getOrderStatus();
		if (!orderStatus.equals(Constants.ORDER_HOUR_STATUS_8)) return result;
		
		OrderDispatchSearchVo orderRateSearchVo = new OrderDispatchSearchVo();
		orderRateSearchVo.setOrderId(orderId);
		if(staffId!=null && staffId>0L){
			orderRateSearchVo.setStaffId(staffId);
		}
		List<OrderRates> orderRates = orderRatesService.selectBySearchVo(orderRateSearchVo);
		
		if (orderRates.isEmpty()) return result;
		
		List<OrderStaffRateVo> vos = orderRatesService.changeToOrderStaffReteVo(orderRates);
		result.setData(vos);
		
		return result;
	}
	
	// 用户评价接口，返回用户已经评价的订单
	@RequestMapping(value = "get_user_rates", method = RequestMethod.GET)
	public AppResultData<Object> GetUserRate(
			@RequestParam("user_id") Long userId,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			HttpServletRequest request) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		OrderDispatchSearchVo orderRateSearchVo = new OrderDispatchSearchVo();
		orderRateSearchVo.setDispatchStatus((short)1);
		orderRateSearchVo.setUserId(userId);
		List<OrderRates> orderRates = orderRatesService.selectByListPage(orderRateSearchVo, page, Constants.PAGE_MAX_NUMBER,true).getList();
		
		for(int i=0,length=orderRates.size();i<length;i++){
			OrderRates orderRate = orderRates.get(i);
			OrderRatesVo transVo = orderRatesService.transVo(orderRate);
			orderRates.set(i, transVo);
		}
		
		result.setData(orderRates);
		return result;
	}
	
	// 订单评价接口
	@RequestMapping(value = "get_staff_rates", method = RequestMethod.GET)
	public AppResultData<Object> GetStaffRate(
			@RequestParam("staff_id") Long staffId,
			@RequestParam("order_id") Long orderId) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		Orders orders = ordersService.selectByPrimaryKey(orderId);
		if (orders == null) {
			return result;
		}
		
		Short orderStatus = orders.getOrderStatus();
		if (!orderStatus.equals(Constants.ORDER_HOUR_STATUS_8)) return result;
		
		OrderDispatchSearchVo orderRateSearchVo = new OrderDispatchSearchVo();
		orderRateSearchVo.setOrderId(orderId);
		
		if (staffId > 0L) orderRateSearchVo.setStaffId(staffId);
		
		List<OrderRates> orderRates = orderRatesService.selectBySearchVo(orderRateSearchVo);
		
		if (orderRates.isEmpty()) return result;
		
		List<OrderStaffRateVo> vos = orderRatesService.changeToOrderStaffReteVo(orderRates);
		
		result.setData(vos);
		return result;
	}
	
	// 订单评价接口
		@RequestMapping(value = "get_staff_total_rate", method = RequestMethod.GET)
		public AppResultData<Object> GetStaffTotalRate(
				@RequestParam("staff_id") Long staffId) {
			AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
			
			
			
			OrderDispatchSearchVo orderRateSearchVo = new OrderDispatchSearchVo();
			orderRateSearchVo.setStaffId(staffId);
			
			List<OrderRates> orderRates = orderRatesService.selectBySearchVo(orderRateSearchVo);
			
			
			List<OrderStaffRateVo> vos = new ArrayList<OrderStaffRateVo>();
			
			//如果没有评价，则默认为最低评价
			if (orderRates.isEmpty()) {
				
				OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
				
				OrderStaffRateVo vo = orgStaffsService.getOrderStaffRateVo(orgStaff);
				
				int totalRateStar = 5;
				String totalArrival = "100%"; 
				vo.setTotalRateStar(totalRateStar);
				vo.setTotalArrival(totalArrival);
				vos.add(vo);
				
			} else {
			
				vos = orderRatesService.changeToOrderStaffReteVo(orderRates);
			}
			result.setData(vos);
			return result;
		}
}
