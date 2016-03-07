package com.jhj.action.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.meijia.utils.vo.AppResultData;
import com.jhj.vo.DemoVo;

@Controller
@RequestMapping(value="/app/")
public class IndexController {

	@RequestMapping(value = "index", method = RequestMethod.GET)
    public AppResultData<List> index() {

    	List<DemoVo> list = new ArrayList<DemoVo>();

    	DemoVo vo1 = new DemoVo();
    	vo1.setName("demo1");
    	vo1.setEmail("demo1@zrj.com");

    	list.add(vo1);
    	DemoVo vo2 = new DemoVo();
    	vo2.setName("demo2");
    	vo2.setEmail("demo2@zrj.com");
    	list.add(vo2);

    	AppResultData<List> result = new AppResultData<List>(0, "ok", list);

    	return result;
    }

}
