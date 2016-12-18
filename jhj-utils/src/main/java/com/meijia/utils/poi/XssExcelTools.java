package com.meijia.utils.poi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 
 * excel导出类
 * <p>处理.xlsx格式<p>
 * @since jdk1.6
 * @date 2016-6-2
 *  
 */

public class XssExcelTools extends ExcelTools {
	
	public XssExcelTools() {
		super();
	}

	/**
	 * 构造函数
	 * ExcelExp
	 * @param filePath 文件路径，如com/test/template/test.xlsx
	 * @param sheetNum 要操作的页签，0为第一个页签
	 * @throws IOException
	 */
	public XssExcelTools(String filePath, int sheetNum) throws IOException {
//		URL resource = this.getClass().getClassLoader().getResource(filePath);
		InputStream is = new FileInputStream(filePath);
		xssWb = new XSSFWorkbook(is);
		xssSheet = xssWb.getSheetAt(sheetNum);
	}

    public static XSSFCellStyle getContentStyle (XSSFWorkbook workbook) {  
        XSSFCellStyle contentStyle = workbook.createCellStyle(); //设置内容行格式  
        contentStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);    
        contentStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中     
        contentStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);    
        contentStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);    
        contentStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);    
        contentStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);   
        XSSFDataFormat df = workbook.createDataFormat();  //此处设置数据格式  
        contentStyle.setDataFormat(df.getFormat("#,#0")); //数据格式只显示整数，如果是小数点后保留两位，可以写contentStyle.setDataFormat(df.getFormat("#,#0.00"));  
        return contentStyle;  
   }  
	
	/**
	 * 设置页脚 
	 */
	public void createFooter(){
		Footer footer = xssSheet.getFooter();
		footer.setRight("第" + HSSFFooter.page() + "页，共" + HSSFFooter.numPages() + "页");
	}
	
	/**
	 * 
	 * 插入行 
	 * @param startRow
	 * @param rows
	 */
	public void insertRows(int startRow, int rows){
        int bottomRow = xssSheet.getLastRowNum();
		if(startRow > bottomRow){
			int n = startRow - bottomRow;
			for(int i = 1; i <= n; i++){
				xssSheet.createRow(bottomRow + i);
			}
		}
		xssSheet.shiftRows(startRow, xssSheet.getLastRowNum(), rows, true, false);
	}
	
	/**
	 * 
	 * 替换模板中变量
	 * @param map
	 */
	public void replaceExcelData(Map<String, String> map){
		int rowNum = xssSheet.getLastRowNum();
		for(int i = 0;i <= rowNum; i++){
			XSSFRow row = xssSheet.getRow(i);
			if(row == null) continue;
			for(int j = 0;j < row.getPhysicalNumberOfCells();j++){
				XSSFCell cell = row.getCell(j);
				if(cell == null) continue;
				String key = cell.getStringCellValue();
				if(map.containsKey(key)){
					cell.setCellValue(map.get(key));
				}
			}
		}
	}
	
	@Override
	public void downloadExcel(HttpServletResponse response, String filaName) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		this.getXssWb().write(os);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String((filaName).getBytes(), "iso-8859-1"));
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
	
}