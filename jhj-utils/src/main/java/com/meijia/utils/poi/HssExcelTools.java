package com.meijia.utils.poi;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 
 * excel导出类
 * <p>处理.xls格式<p>
 * @since jdk1.6
 * @date 2016-6-2
 *  
 */

public class HssExcelTools extends ExcelTools{

	
	public HssExcelTools() {
		super();
	}
	/**
	 * 构造函数
	 * ExcelExp
	 * @param filePath 文件路径，如com/test/template/test.xls
	 * @param sheetNum 要操作的页签，0为第一个页签
	 * @throws IOException
	 */
	public HssExcelTools(String filePath, int sheetNum) throws IOException {
//		URL resource = this.getClass().getClassLoader().getResource(filePath);
		InputStream is = new FileInputStream(filePath);
		hssWb = new HSSFWorkbook(is);
		hssSheet = hssWb.getSheetAt(sheetNum);
	}

    public static HSSFCellStyle getContentStyle (HSSFWorkbook workbook) {  
        HSSFCellStyle contentStyle = workbook.createCellStyle(); //设置内容行格式  
        contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);    
        contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中     
        contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);    
        contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);    
        contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);    
        contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);   
        HSSFDataFormat df = workbook.createDataFormat();  //此处设置数据格式  
        contentStyle.setDataFormat(df.getFormat("#,#0")); //数据格式只显示整数，如果是小数点后保留两位，可以写contentStyle.setDataFormat(df.getFormat("#,#0.00"));  
        return contentStyle;  
   }  
	
	@Override
	public void createFooter() {
		Footer footer = hssSheet.getFooter();
		footer.setRight("第" + HSSFFooter.page() + "页，共" + HSSFFooter.numPages() + "页");
	}

	@Override
	public void downloadExcel(HttpServletResponse response, String filaName) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		this.getHssWb().write(os);
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

	@Override
	public void insertRows(int startRow, int rows) {
        int bottomRow = hssSheet.getLastRowNum();
		if(startRow > bottomRow){
			int n = startRow - bottomRow;
			for(int i = 1; i <= n; i++){
				hssSheet.createRow(bottomRow + i);
			}
		}
		hssSheet.shiftRows(startRow, hssSheet.getLastRowNum(), rows, true, false);
	}

	@Override
	public void replaceExcelData(Map<String, String> map) {
		int rowNum = hssSheet.getLastRowNum();
		for(int i = 0;i <= rowNum; i++){
			HSSFRow row = hssSheet.getRow(i);
			if(row == null) continue;
			for(int j = 0;j < row.getPhysicalNumberOfCells();j++){
				HSSFCell cell = row.getCell(j);
				if(cell == null) continue;
				String key = cell.getStringCellValue();
				if(map.containsKey(key)){
					cell.setCellValue(map.get(key));
				}
			}
		}
	}

}