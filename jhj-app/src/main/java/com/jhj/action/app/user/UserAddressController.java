package com.jhj.action.app.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;
import com.jhj.service.async.UsersAsyncService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 * @description：
 * @author： kerryg
 * @date:2015年7月20日
 */
@Controller
@RequestMapping(value = "/app/user")
public class UserAddressController extends BaseController {

	@Autowired
	private UserAddrsService userAddrsService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private UserRefAmService userRefAmService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private UsersAsyncService usersAsyncService;
	
	@Autowired
	private OrdersService ordersService;

	/**
	 * 根据userId查询用户的地址
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "get_user_addrs", method = RequestMethod.GET)
	public AppResultData<Object> getAddress(@RequestParam("user_id") String userId) {

		List<UserAddrs> list = userAddrsService.selectByUserId(Long.valueOf(userId));

		UserAddrs userAddrs = userAddrsService.selectByDefaultAddr(Long.valueOf(userId));
		// 如果没有默认地址，则设置第一个为默认地址
		if (userAddrs == null) {
			if (!list.isEmpty()) {
				userAddrs = list.get(0);
				userAddrs.setIsDefault((short) 1);
				userAddrsService.updateByPrimaryKeySelective(userAddrs);
				usersAsyncService.allotOrg(userAddrs.getId());
			}
		}

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, list);
		return result;
	}

	/**
	 * 地址删除接口
	 * 根据addr_id删除对应用户的地址
	 */
	@RequestMapping(value = "post_set_addr_default", method = RequestMethod.POST)
	public AppResultData<String> setAddrDefault(@RequestParam("user_id") Long userId, @RequestParam("addr_id") Long addrId) {

		UserAddrs userAddrs = userAddrsService.selectByPrimaryKey(addrId);

		AppResultData<String> result = new AppResultData<String>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		if (userAddrs == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_ADDR_NOT_EXIST_MG);
			return result;
		}

		userAddrsService.updataDefaultByUserId(userId);
		userAddrsService.updataDefaultById(addrId);
		usersAsyncService.allotOrg(userAddrs.getId());

		return result;
	}

	/**
	 * 用户地址提交接口
	 * addr_id0表示新增，>0表示修改 mobile手机号 city_id城市ID cell_id小区ID address门牌号
	 * is_default 是否默认0=不默认1=默认
	 */
	@RequestMapping(value = "post_user_addrs", method = RequestMethod.POST)
	public AppResultData<Object> saveAddress(

			@RequestParam("user_id") Long userId,
			@RequestParam("addr_id") Long addrId,
			@RequestParam("is_default") Short isDefault,
			@RequestParam("name") String name,
			@RequestParam("addr") String addr, // 详细门牌号
			@RequestParam("longitude") String longitude,
			@RequestParam("latitude") String latitude,
			@RequestParam("city") String city,

			@RequestParam(value = "address", required = false, defaultValue = "") String address, // 百度地图详细地址
			@RequestParam(value = "uid", required = false, defaultValue = "") String uid, // 百度地图uid

			@RequestParam(value = "poi_type", required = false, defaultValue = "0") Short poiType,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(value = "post_code", required = false, defaultValue = "") String postCode,
			@RequestParam(value = "city_id", required = false, defaultValue = "0") String cityId,
			@RequestParam(value = "org_id", required = false, defaultValue = "0") Long orgId) {

		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		// 添加地址之前，判断是否在北京市 范围内。不在则不让添加

		/*
		 * 未做 编码转换, 该接口下没有 编码问题。。mapcontroller 接口下有编码问题
		 */
		if (!city.equals("北京市")) {
			// 如果不在 北京范围内
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ADDRESS_OUT_OF_BJ);
			return result;
		}
		// boolean input = BaiduMapSearchUtil.isInBeijingForUserInput(name);
		//
		// if(!input){
		//
		// //如果不在 北京范围内
		// result.setStatus(Constants.ERROR_999);
		// result.setMsg(ConstantMsg.ADDRESS_OUT_OF_BJ);
		//
		// return result;
		// }

		UserAddrs userAddrs = userAddrsService.initUserAddrs();
		if (addrId > 0L) {
			// addrId > 0 表明改地址已经存在
			userAddrs = userAddrsService.selectByPrimaryKey(addrId);
		}

		Users u = usersService.selectByPrimaryKey(userId);

		userAddrs.setId(addrId);
		if (u != null) {
			userAddrs.setUserId(u.getId());
			userAddrs.setMobile(u.getMobile());
		}
		userAddrs.setAddr(addr);
		userAddrs.setLongitude(longitude);
		userAddrs.setLatitude(latitude);
		userAddrs.setPoiType(poiType);
		userAddrs.setName(name);
		userAddrs.setAddress(address);
		userAddrs.setCity(city);
		userAddrs.setUid(uid);
		userAddrs.setPhone(phone);
		userAddrs.setPostCode(postCode);
		userAddrs.setUpdateTime(TimeStampUtil.getNow() / 1000);
		userAddrs.setIsDefault(isDefault);
		userAddrs.setOrgId(orgId);

		if (addrId.equals(0L)) {
			userAddrs.setAddTime(TimeStampUtil.getNow() / 1000);
		}

		// 如果当前的地址设置为默认，则取消其他默认，并设置当前为默认
		if (isDefault.equals((short) 1)) {
			// todo 把当前用户的所有地址 is_default设置为0
			// update user_addr set is_default =0 where mobile = ?
			userAddrsService.updataDefaultByUserId(userId);
		}

		if (addrId > 0L) {
			userAddrsService.updateByPrimaryKey(userAddrs);
		} else {
			// 当添加地址name+addr相同，则只保存一条记录
			UserAddrs temp = userAddrsService.selectByNameAndAddr(userId, name, addr);
			if (temp == null) {

				// 处理只有一个地址，则设置为默认地址
				List<UserAddrs> list = userAddrsService.selectByUserId(Long.valueOf(userId));
				if (list.isEmpty()) {
					userAddrs.setIsDefault((short) 1);
				}

				userAddrsService.insertSelective(userAddrs);

				if (userAddrs.getIsDefault().equals((short) 1)) {
					usersAsyncService.allotOrg(userAddrs.getId());
				}

			} else {
				BeanUtilsExp.copyPropertiesIgnoreNull(temp, userAddrs);
			}
		}

		result.setData(userAddrs);

		return result;
	}
	
	/**
	 * 地址删除接口
	 * 根据addr_id删除对应用户的地址
	 */
	@RequestMapping(value = "delete_addr.json", method = RequestMethod.GET)
	public AppResultData<String> deleteAddr(@RequestParam("addr_id") Long addrId) {

		AppResultData<String> result = new AppResultData<String>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		UserAddrs userAddrs = userAddrsService.selectByPrimaryKey(addrId);
		if(userAddrs!=null){
			Long userId = userAddrs.getUserId();
			Orders order = new Orders();
			order.setUserId(userId);
			List<Orders> newestOrder = ordersService.getNewestOrder(order);
			if(newestOrder!=null && newestOrder.size()>0){
				Orders orders = newestOrder.get(0);
				Short orderStatus = orders.getOrderStatus();
				if(orderStatus>1 && orderStatus<7){
					result.setStatus(Constants.ERROR_999);
					result.setMsg("您还有未完成的订单，不能删除该地址");
					return result;
				}
			}
		}
		
		userAddrsService.deleteByPrimaryKey(addrId);

		return result;
	}

}
