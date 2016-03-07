package com.jhj.action.bs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.dict.DictUtil;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.bs.CouponVo;
import com.jhj.vo.dict.CouponSearchVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.ExcelUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.RandomUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;


/**
 * @description：
 * @author： kerryg
 * @date:2015年7月16日 
 */
@Controller
@RequestMapping(value = "/bs")
public class DictCouponsController extends BaseController {

	@Autowired
	private DictCouponsService couponService;
	
	@Autowired
	private UserCouponsService userCouponsService;
	

	@Autowired
	private UsersService usersService;

	/**列表显示兑换码的优惠券
	 * @param request
	 * @param model
	 * @param searchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/convert-coupon-list", method = { RequestMethod.GET })
	public String convertCouponList(HttpServletRequest request, Model model,
			CouponSearchVo searchVo) {
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		
		model.addAttribute("searchModel", searchVo);
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		PageInfo result = couponService.searchVoListPage(searchVo,(short)0, pageNo,
				pageSize);
		
		model.addAttribute("contentModel", result);
		model.addAttribute("searchModel", searchVo);
		
		return "coupons/convertCouponList";
	}
	/**列表显示充值后赠送的优惠券
	 * @param request
	 * @param model
	 * @param searchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/recharge-coupon-list", method = { RequestMethod.GET })
	public String rechargeCouponList(HttpServletRequest request, Model model,
			CouponSearchVo searchVo) {
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		
		model.addAttribute("searchModel", searchVo);
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		PageInfo result = couponService.searchVoListPage(searchVo,(short)1, pageNo,
				pageSize);
		
		model.addAttribute("contentModel", result);
		return "coupons/rechargeCouponList";
	}
	/**
	 * toTo 兑换码优惠券表单
	 * @param dictCoupons
	 * @param model
	 * @param id
	 * @return
	 */
//	@AuthPassport
	@RequestMapping(value = "/toConvertCouponForm", method = { RequestMethod.GET })
	public String toAddConvertCoupons(
			@ModelAttribute("dictCoupons") DictCoupons dictCoupons,
			 Model model,
			@RequestParam(value="id" ,required = false)Long id) {
		
		if (id == null) {
			id = 0L;
			dictCoupons = couponService.initConvertCoupon();
		}else {
			dictCoupons = couponService.selectByPrimaryKey(id);
			}
		CouponVo couponVo = new CouponVo();
		try {
			BeanUtils.copyProperties(couponVo,dictCoupons);
		}catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("dictCoupons", couponVo);  
		model.addAttribute("serviceTypeMap",couponService.getSelectServiceTypeSource());

		return "coupons/convertCouponForm";
	}
	 /**
	  * toTo 充值后赠送优惠券 表单
	  * @param dictCoupons
	  * @param model
	  * @param id
	  * @return
	  */
	@AuthPassport
	@RequestMapping(value = "/toRechargeCouponForm", method = { RequestMethod.GET })
	public String toAddRechargeCoupons(
			@ModelAttribute("dictCoupons") DictCoupons dictCoupons,
			 Model model,
			@RequestParam(value="id" ,required = false)Long id) {
		
		if (id == null) {
			id = 0L;
			dictCoupons = couponService.initRechargeCoupon();
		}else {
			dictCoupons = couponService.selectByPrimaryKey(id);
			}
		
		CouponVo couponVo = new CouponVo();
		try {
			BeanUtils.copyProperties(couponVo,dictCoupons);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("dictCoupons", couponVo);
		model.addAttribute("selectDataSource",couponService.getSelectRangMonthSource());
		model.addAttribute("serviceTypeMap",couponService.getSelectServiceTypeSource());
		return "coupons/rechargeCouponForm";
	}
	/**
	 * 优惠券对应的用户列表
	 * @param id
	 * @param request
	 * @param model
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/toRechargeCouponUserList",method = { RequestMethod.GET})
	public String toRechargeCouponUserList(
			@RequestParam(value="id" ,required = false)Long id,
			HttpServletRequest request, Model model) {
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		PageHelper.startPage(pageNo, pageSize);
		
		List<UserCoupons> userCouponsList = userCouponsService.selectByCouponId(id);
		List<Users> userList = new ArrayList<Users>();
		if (userCouponsList != null) {
			List<Long> userIdList = new ArrayList<Long>();
			for (UserCoupons item :userCouponsList ) {
				userIdList.add(item.getUserId());
				userList = usersService.selectByListPage(userIdList, pageNo, pageSize);
			}
		}
		PageInfo result = new PageInfo(userList);
		model.addAttribute("contentModel", result);
		return "coupons/rechargeCouponUserList";
	}
	/**
	 * 根据id 删除充值后赠送优惠券
	 * @param dictCoupons
	 * @param id
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/deleteByRechargeCouponId", method = { RequestMethod.GET })
	public String deleteByRechargeCouponId(
			@ModelAttribute("dictCoupons") DictCoupons dictCoupons,
			@RequestParam(value="id")Long id) {
		String path = "redirect:recharge-coupon-list";
		if(id!=null){
			int result = couponService.deleteByPrimaryKey(id);
			if(result > 0){
				path = "redirect:recharge-coupon-list";
			}else{
				path = "error";
			}
		}
		return path;
	}
	/**
	 * 根据Id 删除兑换码优惠券
	 * @param dictCoupons
	 * @param id
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/deleteByConvertCouponId", method = { RequestMethod.GET })
	public String deleteByConvertCouponId(
			@ModelAttribute("dictCoupons") DictCoupons dictCoupons,
			@RequestParam(value="id")Long id) {
			String path = "redirect:convert-coupon-list";
			if(id!=null){
				int result = couponService.deleteByPrimaryKey(id);
				if(result > 0){
					path = "redirect:convert-coupon-list";
				}else{
					path = "error";
				}
			}
		return path;
	}
	
	/**
	 * 新增充值后赠送优惠券
	 */
	@AuthPassport
	@RequestMapping(value = "/rechargeCouponForm", method = { RequestMethod.POST })
	public String addRechargeCoupons(HttpServletRequest request, Model model,
			@ModelAttribute("dictCoupons") DictCoupons dictCoupons,
			BindingResult result) {
		
		
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		
		Long flag = Long.valueOf(request.getParameter("id"));
		//更新或者新增
		if (flag != null && flag > 0) {
			//更新充值后赠送优惠券
			DictCoupons dictCoupon = couponService.selectByPrimaryKey(flag);
			dictCoupon.setServiceType(dictCoupons.getServiceType());
			dictCoupon.setValue(dictCoupons.getValue());
			dictCoupon.setMaxValue(dictCoupons.getMaxValue());
			dictCoupon.setDescription(dictCoupons.getDescription());
			dictCoupon.setIntroduction(dictCoupons.getIntroduction());
			dictCoupon.setRangMonth(dictCoupons.getRangMonth());
			couponService.updateByPrimaryKeySelective(dictCoupon);
		} else {
			//新增充值后赠送优惠券
			DictCoupons dictCoupon = couponService.initRechargeCoupon();
			dictCoupon.setCardNo(RandomUtil.randomNumber(6));
			dictCoupon.setCardPasswd(RandomUtil.randomCode(8));
			dictCoupon.setServiceType(dictCoupons.getServiceType());
			dictCoupon.setValue(dictCoupons.getValue());
			dictCoupon.setMaxValue(dictCoupons.getMaxValue());
			dictCoupon.setDescription(dictCoupons.getDescription());
			dictCoupon.setIntroduction(dictCoupons.getIntroduction());
			dictCoupon.setRangMonth(dictCoupons.getRangMonth());
			couponService.insertSelective(dictCoupon);
		}
		return "redirect:recharge-coupon-list";
	}
	/**
	 * 添加优惠券
	 * @param request
	 * @param model
	 * @param dictCoupons
	 * @param result
	 * @return
	 */
//	@AuthPassport
	@RequestMapping(value = "/convertCouponForm", method = { RequestMethod.POST })
	public String addConvertCoupons(HttpServletRequest request, Model model,
		@ModelAttribute("dictCoupons") DictCoupons dictCoupons,
		BindingResult result) {
		
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		
		String  passwordNum = request.getParameter("password_num"); //优惠券兑换码位数
		String fromDate = request.getParameter("fromDate");			//有效开始时间
		String toDate = request.getParameter("toDate");				//结束时间
		Long count = Long.valueOf(request.getParameter("count"));	//生成卡数量
	
		List<DictCoupons> list = couponService.selectAll();
		Long cardNo = 0L;
		//count >0 执行插入操作
		if (count != null && count >= 0) {
			for (int i = 1; i <= count; i++) {
				dictCoupons.setId(null);
				if (list != null && list.size() > 0) {
					cardNo = Long.valueOf(couponService.selectAllByCardNo()
						.get(0).getCardNo());
					dictCoupons.setCardNo("" + (cardNo + 1));
				} else {
					cardNo = (long) 300000000;
					dictCoupons.setCardNo("130" + (cardNo + i));
				}
				dictCoupons.setAddTime(TimeStampUtil.getNow() / 1000);
				if(passwordNum !=null){
					dictCoupons.setCardPasswd(RandomUtil.randomCode(Integer.valueOf(passwordNum)));
				}else {
					dictCoupons.setCardPasswd(RandomUtil.randomCode(4));
				}
				if (!StringUtil.isEmpty(fromDate) && !StringUtil.isEmpty(toDate)) {
					dictCoupons.setFromDate(DateUtil.parse(fromDate));
					dictCoupons.setToDate(DateUtil.parse(toDate));
				}
				dictCoupons.setUpdateTime(TimeStampUtil.getNow() / 1000);
				//如果兑换码相同，重新生成
				DictCoupons temp = couponService.selectByCardPasswd(dictCoupons.getCardPasswd());
				if(temp !=null){
					i--;
					continue;
				}else {
					couponService.insertSelective(dictCoupons);
				}
			}
		}
		return "redirect:convert-coupon-list";
	}
	/**
	 * 列表显示用户优惠券
	 * @param request
	 * @param model
	 * @param searchVo
	 * @return
	 */
	// @AuthPassport
	/*@RequestMapping(value = "/used", method = { RequestMethod.GET })
	public String userCouponsList(HttpServletRequest request, Model model,
			UserCouponSearchVo searchVo) {
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		model.addAttribute("searchModel", searchVo);

		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		PageInfo result = userCouponService.searchVoListPage(searchVo, pageNo,
				pageSize);
		model.addAttribute("contentModel", result);

		return "coupons/userCouponList";
	}*/

	/**
	 * 导出优惠券
	 */
	@RequestMapping(value = "/download_project", method = { RequestMethod.GET })
	public void download(HttpServletRequest request,HttpServletResponse response,CouponSearchVo couponSearchVo) throws IOException{
        String fileName="excel文件";    

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //填充projects数据
        couponSearchVo.setCoupontType((short)0);
        List<DictCoupons> userses= couponService.selectBySearchVo(couponSearchVo);

        List<Map<String,Object>> list= createExcelRecord(userses);

        String columnNames[]={"描述","优惠券卡号","优惠券密码","优惠券金额","通用类型","服务类型","开始日期","结束日期","添加时间"};//列名
        String keys[]   =    {"introdution","cardNo","cardPassword","value","couponType","serviceType","startDate","endDate","addTime"};//map中的key
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ExcelUtil.createWorkBook(list,keys,columnNames).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
	/**
	 * 创建Excle模板
	 */
    private List<Map<String, Object>> createExcelRecord(List<DictCoupons> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        DictCoupons dictCoupons=couponService.initConvertCoupon();
        for (int j = 0; j < list.size(); j++) {
        	dictCoupons=list.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("introdution",dictCoupons.getIntroduction());
            mapValue.put("cardNo",dictCoupons.getCardNo());
            mapValue.put("cardPassword",dictCoupons.getCardPasswd());
            mapValue.put("value",dictCoupons.getValue());
            mapValue.put("couponType",OneCareUtil.getRangTypeName(dictCoupons.getRangType()));
            mapValue.put("serviceType",DictUtil.getServiceTypeItemName(dictCoupons.getServiceType()));
            mapValue.put("startDate",DateUtil.formatDate(dictCoupons.getFromDate()));
            mapValue.put("endDate",DateUtil.formatDate(dictCoupons.getToDate()));
            Date addDate =  TimeStampUtil.timeStampToDate(dictCoupons.getAddTime()*1000);
            mapValue.put("addTime",DateUtil.formatDate(addDate));
            listmap.add(mapValue);
        }
        return listmap;
    }
}
