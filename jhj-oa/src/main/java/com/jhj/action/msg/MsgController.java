package com.jhj.action.msg;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.jhj.po.model.msg.Msg;
import com.jhj.service.msg.MsgService;
import com.jhj.vo.MsgSearchVo;
import com.jhj.vo.msg.OaMsgVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
@Controller
@RequestMapping(value = "/msg")
public class MsgController extends BaseController {
	
	@Autowired
	private MsgService msgService;
	
	/**
	 * 消息列表
	 * @param request
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
    @AuthPassport
	@RequestMapping(value = "/msgList", method = { RequestMethod.GET })
	public String msgList(HttpServletRequest request, MsgSearchVo searchVo,Model model) throws UnsupportedEncodingException {
    	
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());

		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		//分页
		PageHelper.startPage(pageNo, pageSize);
		//若搜索条件为空，则展示全部
		if (searchVo == null) {
			searchVo = new MsgSearchVo();
		}
		model.addAttribute("searchVoModel",searchVo);
		//设置中文 参数 编码，解决 中文 乱码
	//	searchVo.setTitle(new String(searchVo.getTitle().getBytes("iso-8859-1"),"utf-8"));
	
		List<Msg> list = msgService.selectListPageBySearchVo(searchVo,pageNo,pageSize);
		
		PageInfo result = new PageInfo(list);
		
		model.addAttribute("contentModel", result);

		return "msg/msgList";
	}
//    @AuthPassport
	@RequestMapping(value = "/msgForm", method = { RequestMethod.GET })
	public String serviceTypeForm(Model model,
			@RequestParam(value = "msg_id") Long msgId,
			HttpServletRequest request) {

		if (msgId == null) {
			msgId = 0L;
		
		}
//		Msg msg = msgService.initMsg();
		
		OaMsgVo msgVo = msgService.initOaMsgVo();
		
		String dateStr = "";
		
		if (msgId != null && msgId > 0) {
			Msg msg = msgService.selectByPrimaryKey(msgId);
			
			BeanUtilsExp.copyPropertiesIgnoreNull(msg, msgVo);
			
			dateStr = TimeStampUtil.timeStampToDateStr(msg.getSendTime()*1000);
		}
			
		//回显发送时间
		model.addAttribute("sendTimeDateStr", dateStr);
		model.addAttribute("contentModel", msgVo);
		
		return "msg/msgForm";
	}

	
	/**
	 * 消息保存数据方法.
	 *
	 * @param request
	 * @param model
	 * @param serviceType
	 * @param result
	 * @return
	 * @throws ParseException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
//	@AuthPassport
	@RequestMapping(value = "/msgForm", method = { RequestMethod.POST })
	public String doServiceTypeForm(HttpServletRequest request, Model model,
			@ModelAttribute("contentModel") OaMsgVo msg,
			BindingResult result) throws ParseException, IllegalAccessException, InvocationTargetException {

		Long msgId = Long.valueOf(request.getParameter("msgId"));
		String sendTimeStr =request.getParameter("sendTime").toString();
		
		//如果选择的 定时发送
		if(!StringUtil.isEmpty(sendTimeStr) && msg.getSendWay() == 1){
			msg.setSendTime(Long.valueOf(sendTimeStr));
		}
		
		//如果选择的立即发送
		if(msg.getSendWay() == 0){
			msg.setSendTime(0L);
		}
		
		//更新或者新增
		msg.setAppType("jhj-u");
		
		
		//2016年1月28日17:10:51  设置一些默认属性
		msg.setContent("");
		msg.setGotoUrl("");
		msg.setUserType((short)1);	//用户类型，暂时都设置为 服务人员
		
		
		Msg initMsg = msgService.initMsg();
		
		
		if (msgId != null && msgId > 0) {
			
			initMsg = msgService.selectByPrimaryKey(msgId);
//			BeanUtils.copyProperties(initMsg, msg);
			
			BeanUtilsExp.copyPropertiesIgnoreNull(msg, initMsg);
			
			msgService.updateByPrimaryKeySelective(initMsg);
		} else {
//			BeanUtils.copyProperties(initMsg, msg);
			
			BeanUtilsExp.copyPropertiesIgnoreNull(msg, initMsg);
			
			initMsg.setMsgId(0L);
			initMsg.setAddTime(TimeStampUtil.getNow()/1000);
			
			
	        msgService.insertSelective(initMsg);
		}

		return "redirect:msgList";
	}
}
