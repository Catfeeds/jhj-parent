package com.jhj.action.job;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jhj.po.model.user.Users;
import com.jhj.service.users.UsersService;

@Controller
@RequestMapping("/job/excel")
public class ReadExcelMobileController {
	
	@Autowired
	private UsersService userService;
	
	@RequestMapping(value="/readExcelMobile",method=RequestMethod.GET)
	public String readExcelMobile(){
		
		return "job/addExcel";
	}
	
	
	
	@RequestMapping(value="/readExcelMobile",method=RequestMethod.POST)
	public String readExcelMobile(HttpServletRequest request,@RequestParam("excelFile") MultipartFile file){
		if(file.isEmpty()) return null;
		
		try {
			InputStream inputStream = file.getInputStream();
			String filename = file.getOriginalFilename();
			Workbook workbook=null;
			if(filename.endsWith(".xls")){
				workbook = new HSSFWorkbook(inputStream);
			}
			if(filename.endsWith(".xlsx")){
				workbook = new XSSFWorkbook(inputStream);
			}
			//获取excel中sheet的数量
			int numberOfSheets = workbook.getNumberOfSheets();
			Set<String> set = new HashSet<String>();
			List<Users> userList = new ArrayList<Users>();
			DecimalFormat df = new DecimalFormat("0");
			
			for(int i=0;i<numberOfSheets;i++){
				Sheet sheet = workbook.getSheetAt(i);
				//获取最后一行
				int lastRowNum = sheet.getLastRowNum();
				for(int j=0;j<lastRowNum;j++){
					Row row = sheet.getRow(j);
					if(row==null) continue;
					short lastCellNum = row.getLastCellNum();
					for(int cellNum=0;cellNum<lastCellNum;cellNum++){
						
						Cell cell = row.getCell(cellNum);
						
						if(cell!=null && !cell.equals("")){
							int cellType = cell.getCellType();
							String mobile="";
							if(cellType==cell.CELL_TYPE_STRING){
								mobile = String.valueOf(cell.getStringCellValue());
							}
							if(cellType==cell.CELL_TYPE_NUMERIC){
								mobile = String.valueOf(df.format(cell.getNumericCellValue()));
							}
							String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
							boolean contains = mobile.contains("/");
							if(contains){
								String[] mobiles = mobile.split("/");
								for(int num=0;num<mobiles.length;num++){
									mobile = mobiles[num];
									Users user = userService.selectByMobile(mobile);
									if(user==null && Pattern.matches(regExp, mobile) && !set.contains(mobile)){
										set.add(mobile);
										Users u = userService.initUsers(mobile, (short)2);
										u.setMobile(mobile);
										userList.add(u);
										
									}else{
										continue;
									}
								}
							}else{
								Users user = userService.selectByMobile(mobile);
								if(user==null && Pattern.matches(regExp, mobile) && !set.contains(mobile)){
									set.add(mobile);
									Users u = userService.initUsers(mobile, (short)2);
									u.setMobile(mobile);
									userList.add(u);
									
								}else{
									continue;
								}
							}
							
						}
					}
				}
			}
			if(userList.size()>0)
				userService.insertBatch(userList);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
