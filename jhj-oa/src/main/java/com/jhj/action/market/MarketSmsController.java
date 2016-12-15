package com.jhj.action.market;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.market.MarketSms;
import com.jhj.po.model.market.MarketSmsLog;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.Users;
import com.jhj.service.async.SendMarketSmsService;
import com.jhj.service.market.MarketSmsLogService;
import com.jhj.service.market.MarketSmsService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.market.MarketSmsSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.user.UserSearchVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;

@Controller
@RequestMapping(value="/market")
public class MarketSmsController extends BaseController{
	
	@Autowired
	private MarketSmsService marketSmsService;
	
	@Autowired
	private MarketSmsLogService marketSmsLogService;
	
	@Autowired
	private SendMarketSmsService sendMarketSmsService;
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@AuthPassport
	@RequestMapping(value="/get-list",method=RequestMethod.GET)
	public String list(HttpServletRequest request,Model model,MarketSmsSearchVo searchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		List<MarketSms> list = marketSmsService.selectByListPage(searchVo,pageNo,pageSize);
		PageInfo<MarketSms> page = new PageInfo<MarketSms>(list);
		
		model.addAttribute("page", page);
		model.addAttribute("marketSmsSearchVo", searchVo);
		return "market/marketSmsList";
	}
	
	@AuthPassport
	@RequestMapping(value="/add-marketsms",method=RequestMethod.GET)
	public String addMarketSms(HttpServletRequest request,Model model,@RequestParam(value="marketSmsId",required=false) Integer marketSmsId){
		if(marketSmsId!=null){
			MarketSms ms = marketSmsService.selectByPrimaryKey(marketSmsId);
			String userGroupType = ms.getUserGroupType();
			String[] userGroupTypeArry = userGroupType.split(",");
			List<String> list=new ArrayList<String>(Arrays.asList(userGroupTypeArry));
			ms.setUserGroupTypeList(list);
			model.addAttribute("marketSms", ms);
		}
		return "market/addMarketSms";
	}
	
	@AuthPassport
	@RequestMapping(value="/save-marketsms",method=RequestMethod.POST)
	public String saveMarketSms(HttpServletRequest request,Model model,@ModelAttribute("marketSms") MarketSms marketSms){
		
		marketSms.setTotalSend(0);
		marketSms.setTotalSended(0);
		marketSms.setTotalFail(0);
		marketSms.setAddTime(TimeStampUtil.getNow()/1000);
		marketSms.setUpdateTime(TimeStampUtil.getNow()/1000);
		String str = marketSms.getUserGroupTypeList().toString().replace("[", "").replace("]", "").replace(" ", "");
		marketSms.setUserGroupType(str);
		marketSmsService.insert(marketSms);
		return "redirect:get-list";
	}
	
	@AuthPassport
	@RequestMapping(value="/send-marketsms",method=RequestMethod.GET)
	public String sendMarketSms(HttpServletRequest request,Model model,@RequestParam("marketSmsId") Integer marketSmsId) throws InterruptedException, ExecutionException{
		
		MarketSms marketSms = marketSmsService.selectByPrimaryKey(marketSmsId);
		
		String userGroupType = marketSms.getUserGroupType();
		
		marketSms.setUserGroupTypeList(Arrays.asList(userGroupType.split(",")));
		
		List<String> userGroupTypeList = marketSms.getUserGroupTypeList();
		
		String parameter = request.getParameter("testuserGroupType");
		
		Set<Users> set = new HashSet<Users>();
		
		Set<Long> sets = new HashSet<Long>();
		
		UserSearchVo userVo=new UserSearchVo();
		List<Users> users =null;
		if(parameter==null){
			if(userGroupTypeList.contains("0")){
				List<Users> userList = userService.selectBySearchVo(userVo);
				set.addAll(userList);
			}
			if(userGroupTypeList.contains("1")){
				userVo.setIsVip((short)1);
				List<Users> userList = userService.selectBySearchVo(userVo);
				set.addAll(userList);
			}
			if(userGroupTypeList.contains("2")){
				userVo.setIsVip((short)0);
				List<Users> userList = userService.selectBySearchVo(userVo);
				set.addAll(userList);
			}
			if(userGroupTypeList.contains("3")){
				OrderSearchVo searchVo = new OrderSearchVo();
	            searchVo.setStartServiceTime(DateUtil.curStartDate(0));
	            searchVo.setEndServiceTime(DateUtil.curLastDate(0));
	            List<Orders> orders = orderQueryService.selectBySearchVo(searchVo);
	            for (Orders o : orders) {
	            	sets.add(o.getUserId());
	            }
			}
			if(userGroupTypeList.contains("4")){
				 OrderSearchVo searchVo = new OrderSearchVo();
	             searchVo.setStartServiceTime(DateUtil.curStartDate(3));
	             searchVo.setEndServiceTime(DateUtil.curLastDate(1));
	             List<Orders> orders = orderQueryService.selectBySearchVo(searchVo);
	             for (Orders o : orders) {
	            	 sets.add(o.getUserId());
	             }
			}
			if(userGroupTypeList.contains("5")){
				 OrderSearchVo searchVo = new OrderSearchVo();
				 searchVo.setStartServiceTime(DateUtil.curStartDate(9));
	             searchVo.setEndServiceTime(DateUtil.curLastDate(3));
	             List<Orders> orders = orderQueryService.selectBySearchVo(searchVo);
	             for (Orders o : orders) {
	            	 sets.add(o.getUserId());
	             }
			}
			if(userGroupTypeList.contains("6")){
				 // 找出未下过单的用户.
	            users = userService.selectUsersByOrderMobile();
			}
		}
		
		//测试用户发短信
		if(parameter!=null && parameter.equals("99")){
			String[] mobile= {"18612514665","18600018345","18734187116","15201023689","15600913197","18811043684","13811855734","13466512812","13488723862","15011489008","18405449076","15010069127"};
			
			for(int i=0;i<mobile.length;i++){
				Users u=new Users();
				Long id=(long) i;
				u.setId(id);
				u.setMobile(mobile[i]);
				set.add(u);
			}
		}
		
		if(sets!=null && sets.size()>0){
			UserSearchVo vo=new UserSearchVo();
			vo.setUserIds(new ArrayList<Long>(sets));
			List<Users> userList = userService.selectBySearchVo(vo);
			set.addAll(userList);
		}
		if(users!=null && users.size()>0){
			set.addAll(users);
		}
		
		int totalNum = set.size();
		String[] content = new String[]{""};
		String smsTempId = String.valueOf(marketSms.getSmsTempId());
		String str = request.getParameter("smsNum");
		int smsNum=0;
		if(str!=null && !str.equals("")){
			smsNum = Integer.parseInt(str);
		}
		
		List<MarketSmsLog> marketSmsLogList = marketSmsLogService.selectByMarketSmsId(marketSmsId);
		
		List<Users> userList=new ArrayList<Users>(set);
		for(int i=0;i<totalNum;i++){
			if(totalNum>smsNum && i<=smsNum){
				Users u = userList.get(i);
				Long id = u.getId();
				if(marketSmsLogList!=null && marketSmsLogList.size()>0){
					for(int j=0,leng=marketSmsLogList.size();j<leng;j++){
						Long userId = marketSmsLogList.get(j).getUserId();
						if(id!= userId){
							sendMarketSmsService.allotSms(u,marketSms.getMarketSmsId() ,smsTempId,content);
						}
					}
				}else{
					sendMarketSmsService.allotSms(u,marketSms.getMarketSmsId() ,smsTempId,content);
				}
				
			}
		}
		
		marketSms.setTotalSend(totalNum);
		marketSms.setTotalSended(sendMarketSmsService.successNum());
		marketSms.setTotalFail(sendMarketSmsService.failNum());
		marketSmsService.updateByPrimaryKeySelective(marketSms);
		
		return "redirect:get-list";
	}
}
