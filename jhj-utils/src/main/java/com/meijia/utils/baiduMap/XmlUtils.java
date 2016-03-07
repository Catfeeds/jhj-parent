package com.meijia.utils.baiduMap;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 *
 * @author :hulj
 * @Date : 2015年6月24日下午2:00:51
 * @Description: 解析查询请求的结果，得到所有可能的目的地
 *
 */
public class XmlUtils {
	
	/*
	 * 传入参数：xml格式的字符串结果，需要得到的节点名称
	 *  返回所有查询到的 location
	 *  
	 *  解析第一次请求（天安门->饭店）的请求结果
	 *  
	 */
	public static List<String> getAllDestination(String xmlResult) throws DocumentException{
		
//		SAXReader saxReader = new SAXReader();
//
//        Document document = saxReader.read(new File("d:\\test.xml"));
		
		Document document = DocumentHelper.parseText(xmlResult);
		Element root = document.getRootElement();
		
		Element element = root.element("results");
		//得到 results节点下所有元素
		List<Element> list = element.elements();
		
		//存放查询出来的所有 目的地名称
		List<String> desList = new ArrayList<String>();
		if (list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				Element element2 = list.get(i);
				//将result节点下 ，元素名称为 name的 值 取出。
				desList.add(element2.elementText("name"));
			}
		}
		System.out.println(desList.toString());
		
		return desList;
	}
	/*
	 * 解析 （天安门->每个目的地）的请求结果报文，
	 * 
	 *   分析报文：若有距离字段，则必然有时间字段
	 *   	返回结果 为 "距离,时间"
	 *      若不存在，则返回一个特殊字符串 "#QQQ#"
	 */
	public static List<String> getShortDestination(String xmlDestination,List<String> orgNameList) throws DocumentException{
		 
//		SAXReader saxReader = new SAXReader();
//        Document document = saxReader.read(new File("d:\\test2.xml"));
			Document document = DocumentHelper.parseText(xmlDestination);
			Element root = document.getRootElement();
			
			Element element = root.element("result");
			
			Element a = element.element("elements");
			
			String b = null;//距离节点的内容，如 900米，1.3公里
			String c = null;
			String d = null;
			String e = null;
			List<Element> list = a.elements();
			List<String> lists = new ArrayList<String>();
			/*
			 * 返回报文中 距离 和时间都是成对出现，遍历得到 每组 距离和时间
			 */
			for (int i = 0; i < list.size()-1; i++) {
				//外层循环得到的是 distance节点，报文中 distance、duration是成对出现的,因而是 list.size()-1
				/*
				 * <elements>
						<distance>
							<text>17.7公里</text>
							<value>17725</value>
						</distance>
						<duration>
							<text>33分钟</text>
							<value>1952</value>
						</duration>
						<distance>
							<text>1213.2公里</text>
							<value>1213164</value>
						</distance>
						<duration>
							<text>13.7小时</text>
							<value>49267</value>
						</duration>
						...
					</elements>	
				 */
				
				if(list.get(i).getName().equals("distance")){
					
					/*
					 * 这里 的 目的地地址名称（orgName） 和 报文中 distance、duration 为 一对一
					 * 
					 *  即  一组 distance、duration，对应一个 门店地址。。
					 *    
					 *    结点数 则 为 门店 list的 2倍。
					 * 
					 */
					e = orgNameList.get(i/2);
					
					b= list.get(i).elementText("text");  //diatance（距离）节点的属性  text, 如 1.3公里
					//duration的 位置 是 在 distance后面，成对出现
					if (list.get(i+1).getName().equals("duration")) {
//					c = a.element("duration").elementText("text");
						c = list.get(i+1).elementText("text"); //duration（时间） 节点的属性 text，如 3小时
					}
					d= b+","+c+","+e;
					lists.add(d);
				}
			}
		return lists;
        
	}
	
	/*
	 * 
	 * 取到 返回结果中符合条件的   距离<10公里   的集合/ 时间<60分钟 的集合
	 */
	public static List<String> getMin(List<String> list){
		
		String time = "分钟";//选择以分钟为单位的时间值
		String distM = "米";//选择以米为单位的距离值
		String distG = "公里";
		
		//存放  符合条件的   距离，时间，门店地址名称
		List<String> orgList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			//将 结果 拆分 为  "距离"，"时间"
			String[] strings = list.get(i).split(",");
			//时间 以 分钟 为单位，必然小于 60分钟,所以直接取以分钟为单位的时间即可
			if (strings[1].endsWith(time)) {
				//时间符合条件后，判断 距离 
				if(strings[0].endsWith(distM)){
					
					//距离的单位为 米，必然符合条件
					orgList.add(list.get(i));
				}else if (strings[0].endsWith(distG)) {
					//距离的单位为公里，则判断是否小于10公里
					String dis = MyStringUtils.delChinese(strings[0]);
					
					if(Double.valueOf(dis)<10){
						//如果 是小于 10公里，则取出
						orgList.add(list.get(i));
					}
				}
			}
		}
		return orgList;
	}
}
