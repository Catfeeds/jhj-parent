package com.meijia.utils;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.sf.jxls.util.Util;

public class ExcelUtil {
	/*
	 * //************************************XSSF********************************
	 * ************
	 */

	public static Workbook createWorkBook(List<Map<String, Object>> list, String[] keys, String columnNames[]) {
		// 创建excel工作簿
		Workbook wb = new HSSFWorkbook();
		// 创建第一个sheet（页），并命名
		Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < keys.length; i++) {
			sheet.setColumnWidth((short) i, (short) (35.7 * 150));
		}

		// 创建第一行
		org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);

		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();

		// 创建两种字体
		org.apache.poi.ss.usermodel.Font f = wb.createFont();
		org.apache.poi.ss.usermodel.Font f2 = wb.createFont();

		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 10);
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight((short) Font.BOLD);

		// 创建第二种字体样式（用于值）
		f2.setFontHeightInPoints((short) 10);
		f2.setColor(IndexedColors.BLACK.getIndex());

		// Font f3=wb.createFont();
		// f3.setFontHeightInPoints((short) 10);
		// f3.setColor(IndexedColors.RED.getIndex());

		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);

		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setBorderLeft(CellStyle.BORDER_THIN);
		cs2.setBorderRight(CellStyle.BORDER_THIN);
		cs2.setBorderTop(CellStyle.BORDER_THIN);
		cs2.setBorderBottom(CellStyle.BORDER_THIN);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);
		// 设置列名
		for (int i = 0; i < columnNames.length; i++) {
			org.apache.poi.ss.usermodel.Cell cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
			cell.setCellStyle(cs);
		}
		// 设置每行每列的值
		for (short i = 1; i < list.size(); i++) {
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			org.apache.poi.ss.usermodel.Row row1 = sheet.createRow(i);
			// 在row行上创建一个方格
			for (short j = 0; j < keys.length; j++) {
				org.apache.poi.ss.usermodel.Cell cell = row1.createCell(j);
				cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
				cell.setCellStyle(cs2);
			}
		}
		return wb;
	}

	/**
	 * Excel文件到List
	 * 
	 * @param fileName
	 * @param sheetIndex
	 *            // 工作表索引
	 * @param skipRows
	 *            // 跳过的表头
	 * @return
	 * @throws Exception
	 */
	public static List<Object> readToList(String fileName, InputStream in, int sheetIndex, int skipRows) throws Exception {
		List<Object> ls = new ArrayList<Object>();
		Workbook wb = loadWorkbook(fileName, in);
		if (null != wb) {
			Sheet sh = wb.getSheetAt(sheetIndex);
			int rows = sh.getPhysicalNumberOfRows();
			int cells = 0;
			cells = sh.getRow(0).getPhysicalNumberOfCells();
			for (int i = skipRows; i < rows; i++) {
				Row row = sh.getRow(i);
				if (null == row) {
					break;
				}

				if (row.getLastCellNum() > cells)
					cells = row.getPhysicalNumberOfCells();
				if (cells == 0) {
					continue;
				}
				List<String> r = new ArrayList<String>(cells);
				for (int c = 0; c < cells; c++) {

					String v = "";
					if (row.getCell(c) != null) {
						System.out.println(cells + "-----" + c + "----- " + row.getCell(c).getCellType());
						v = readCellValues(row.getCell(c));
					}

					r.add(v);
				}
				ls.add(r);
			}
		}

		return ls;
	}

	public static String readCellValues(Cell cell) throws Exception {
		// 用于返回结果
		String result = new String();

		try {
			// 如果单元格为空，返回null
			if (cell == null) {
				result = "null";
			} else {
				// 判断单元格类型
				switch (cell.getCellType()) {
				// 数字类型
				case Cell.CELL_TYPE_NUMERIC:
					// 处理日期格式、时间格式
					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat sdf = null;
						if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
							sdf = new SimpleDateFormat("HH:mm");
						} else {// 日期
							sdf = new SimpleDateFormat("yyyy-MM-dd");
						}
						Date date = cell.getDateCellValue();
						result = sdf.format(date);
					} else if (cell.getCellStyle().getDataFormat() == 58) {
						// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						double value = cell.getNumericCellValue();
						Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
						result = sdf.format(date);
					} else {
						double value = cell.getNumericCellValue();
						CellStyle style = cell.getCellStyle();
						DecimalFormat format = new DecimalFormat();
						String temp = style.getDataFormatString();
						// 单元格设置成常规
						if (temp.equals("General")) {
							format.applyPattern("#");
						}
						result = format.format(value);
					}
					break;
				case Cell.CELL_TYPE_STRING:// String类型
					result = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_BLANK:
					result = "";
				default:
					result = "";
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 读取Excel文件，支持2000与2007格式
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Workbook loadWorkbook(String fileName, InputStream in) throws Exception {
		if (null == fileName)
			return null;

		Workbook wb = null;
		if (fileName.toLowerCase().endsWith(".xls")) {
			try {
				POIFSFileSystem fs = new POIFSFileSystem(in);
				wb = new HSSFWorkbook(fs);
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (fileName.toLowerCase().endsWith(".xlsx")) {
			try {
				wb = new XSSFWorkbook(in);
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new Exception("不是一个有效的Excel文件");
		}
		return wb;
	}

	public static void writeToExcel(Workbook wb, OutputStream out) {
		try {
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static enum ExcelType {
		xls, xlsx;
	}

	public static Workbook listToWorkbook(List<?> rows, ExcelType type) {
		Workbook wb = null;
		if (ExcelType.xls.equals(type)) {
			wb = new HSSFWorkbook();
		} else if (ExcelType.xlsx.equals(type)) {
			wb = new XSSFWorkbook();
		} else {
			return null;
		}

		Sheet sh = wb.createSheet();
		if (null != rows) {
			for (int i = 0; i < rows.size(); i++) {
				Object obj = rows.get(i);
				Row row = sh.createRow(i);

				if (obj instanceof Collection) {
					Collection<?> r = (Collection<?>) obj;
					Iterator<?> it = r.iterator();
					int j = 0;
					while (it.hasNext()) {
						Cell cell = row.createCell(j++);
						cell.setCellValue(String.valueOf(it.next()));
					}
				} else if (obj instanceof Object[]) {
					Object[] r = (Object[]) obj;
					for (int j = 0; j < r.length; j++) {
						Cell cell = row.createCell(j);
						cell.setCellValue(String.valueOf(r[j]));
					}
				} else {
					Cell cell = row.createCell(0);
					cell.setCellValue(String.valueOf(obj));
				}
			}
		}

		return wb;
	}

	/**
	 * insert row into the target sheet, the style of cell is the same as
	 * startRow
	 * 
	 * @param wb
	 * @param sheet
	 * @param starRow
	 *            - the row to start shifting
	 * @param rows
	 */
	public static void insertRow(HSSFWorkbook wb, HSSFSheet sheet, int startRow, int rows) {

		sheet.shiftRows(startRow + 1, sheet.getLastRowNum(), rows, true, false);
		// Parameters:
		// startRow - the row to start shifting
		// endRow - the row to end shifting
		// n - the number of rows to shift
		// copyRowHeight - whether to copy the row height during the shift
		// resetOriginalRowHeight - whether to set the original row's height to
		// the default

		for (int i = 0; i < rows; i++) {

			HSSFRow sourceRow = null;
			HSSFRow targetRow = null;

			sourceRow = (HSSFRow) sheet.getRow(startRow);
			targetRow = (HSSFRow) sheet.createRow(++startRow);

			Util.copyRow((HSSFSheet) sheet, sourceRow, targetRow);
		}

	}

	/**
	 * 行复制功能
	 * 
	 * @param fromRow
	 * @param toRow
	 */
	public static void copyRow(HSSFWorkbook wb, HSSFRow fromRow, HSSFRow toRow, boolean copyValueFlag) {
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			HSSFCell tmpCell = (HSSFCell) cellIt.next();
			HSSFCell newCell = toRow.createCell(tmpCell.getCellNum());
			copyCell(wb, tmpCell, newCell, copyValueFlag);
		}
	}

	/**
	 * 复制单元格
	 * 
	 * @param srcCell
	 * @param distCell
	 * @param copyValueFlag
	 *            true则连同cell的内容一起复制
	 */
	public static void copyCell(HSSFWorkbook wb, HSSFCell srcCell, HSSFCell distCell, boolean copyValueFlag) {
		HSSFCellStyle newstyle = wb.createCellStyle();
		copyCellStyle(srcCell.getCellStyle(), newstyle);
		// distCell.setEncoding(srcCell.getEncoding());
		// 样式
		distCell.setCellStyle(newstyle);
		// 评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		if (copyValueFlag) {
			if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {
				if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
					distCell.setCellValue(srcCell.getDateCellValue());
				} else {
					distCell.setCellValue(srcCell.getNumericCellValue());
				}
			} else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {
				distCell.setCellValue(srcCell.getRichStringCellValue());
			} else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {
				// nothing21
			} else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {
				distCell.setCellValue(srcCell.getBooleanCellValue());
			} else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {
				distCell.setCellErrorValue(srcCell.getErrorCellValue());
			} else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());
			} else { // nothing29
			}
		}
	}

	/**
	 * 复制一个单元格样式到目的单元格样式
	 * 
	 * @param fromStyle
	 * @param toStyle
	 */
	public static void copyCellStyle(HSSFCellStyle fromStyle, HSSFCellStyle toStyle) {
		toStyle.setAlignment(fromStyle.getAlignment());
		// 边框和边框颜色
		toStyle.setBorderBottom(fromStyle.getBorderBottom());
		toStyle.setBorderLeft(fromStyle.getBorderLeft());
		toStyle.setBorderRight(fromStyle.getBorderRight());
		toStyle.setBorderTop(fromStyle.getBorderTop());
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

		// 背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setFillPattern(fromStyle.getFillPattern());
		// toStyle.setFont(fromStyle.getFont(null));
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());// 首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());// 旋转
		toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
		toStyle.setWrapText(fromStyle.getWrapText());

	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// String path = "/Users/lnczx/Desktop/tmp/staff-excel/";
		// String fileName = "批量导入员工模板文件(5).xlsx";
		//
		// InputStream in = new FileInputStream(path + fileName);
		// List<Object> excelDatas = ExcelUtil.readToList(fileName, in, 0, 0);
		// for (int i = 0; i < excelDatas.size(); i++) {
		// List<String> b = (List<String>) excelDatas.get(i);
		//
		// System.out.println(b.size() + "-----" + excelDatas.get(i));
		// }
		// String v = "1";
		// v = String.format("%04d", Integer.parseInt(v));
		// System.out.println(v);

		FileOutputStream out = new FileOutputStream("/Users/lnczx/Downloads/2.xls");

		String cpath = "/workspace/work/java/meijia-parent/xcloud/src/main/webapp/WEB-INF/attach/";
		String fileName = "考勤统计表.xls";

		InputStream in = new FileInputStream(cpath + fileName);

		HSSFWorkbook wb = (HSSFWorkbook) ExcelUtil.loadWorkbook(fileName, in);
		HSSFSheet sh = wb.getSheetAt(0);
		int rows = sh.getPhysicalNumberOfRows();
		System.out.println("rows = " + rows);
		int year = com.meijia.utils.DateUtil.getYear();
		int month = com.meijia.utils.DateUtil.getMonth();
		
		HSSFRow row0 = sh.getRow(0);

		HSSFCell cellHeader = row0.getCell(0);
		cellHeader.setCellValue(year + "年" + month + "月公司考勤统计表");


		int startRow = 4;
		int totalStaffs = 5;
		int endRow = totalStaffs;
		ExcelUtil.insertRow(wb, sh, startRow, endRow);
	
		int rowNum = startRow;
		for(int i = 0; i < 5; i++) {
			
			
			HSSFRow rowData = sh.getRow(rowNum);
			
			HSSFCell cell0 = rowData.getCell(0);
			cell0.setCellValue("0");
			
			HSSFCell cell1 = rowData.getCell(1);
			cell1.setCellValue("刘德华"+ i);
			
			HSSFCell cell2 = rowData.getCell(2);
			cell2.setCellValue("财务部");
			
			HSSFCell cell3 = rowData.getCell(3);
			cell3.setCellValue(1);
			
			HSSFCell cell4 = rowData.getCell(4);
			cell4.setCellValue(2);
			
			HSSFCell cell5 = rowData.getCell(5);
			cell5.setCellValue(3);
			
			HSSFCell cell6 = rowData.getCell(6);
			cell6.setCellValue(4);
			
			HSSFCell cell7 = rowData.getCell(7);
			cell7.setCellValue(5);
			
			HSSFCell cell8 = rowData.getCell(8);
			cell8.setCellValue(6);
			
			HSSFCell cell9 = rowData.getCell(9);
			cell9.setCellValue(7);
			
			HSSFCell cell10 = rowData.getCell(10);
			cell10.setCellValue(8);
			
			HSSFCell cell11 = rowData.getCell(11);
			cell11.setCellValue(9);
			
			HSSFCell cell12 = rowData.getCell(12);
			cell12.setCellValue(10);
			
			HSSFCell cell13 = rowData.getCell(13);
			cell13.setCellValue(11);
			
			HSSFCell cell14 = rowData.getCell(14);
			cell14.setCellValue(12);
			
			HSSFCell cell15 = rowData.getCell(15);
			cell15.setCellValue(13);
			
			HSSFCell cell16 = rowData.getCell(16);
			cell16.setCellValue(14);
			
			HSSFCell cell17 = rowData.getCell(17);
			cell17.setCellValue(15);
			
			rowNum++;
		}		
		
		wb.write(out);
	}
}
