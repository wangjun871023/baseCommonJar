package com.macrosoft.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtils {
	private String path = "";

	public static String getCelValue(HSSFCell cell, Class objClass) {
		if ((cell == null) || (cell.getCellType() == 3)) {
			return "";
		}
		if (cell.getCellType() == 1)
			return cell.getStringCellValue();
		if (cell.getCellType() == 0) {
			if ("java.util.Date".equals(objClass.getName())) {
				return "" + cell.getDateCellValue().getTime();
			}
			return "" + cell.getNumericCellValue();
		}
		if (cell.getCellType() == 4) {
			return Boolean.toString(cell.getBooleanCellValue());
		}
		return "";
	}

	public static Object getCelValue(HSSFCell cell, Object obj, String keyName)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		return ConvertUtils.convert(
				getCelValue(cell, PropertyUtils.getPropertyType(obj, keyName)),
				PropertyUtils.getPropertyType(obj, keyName));
	}

	public static HSSFWorkbook object2Excel(List list, String[] propertyKeys,
			String[] propertyShowKeys) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet sheet = wb.createSheet("new result");
		HSSFRow row = null;
		HSSFCell cell = null;
		Object cellVal = null;

		row = sheet.createRow(0);

		for (int i = 0; i < propertyKeys.length; i++) {
			cell = row.createCell(i);

			cell.setCellValue(propertyShowKeys[i]);
		}

		int rowId = 1;
		for (Iterator iter = list.iterator(); iter.hasNext(); rowId++) {
			row = sheet.createRow(rowId);
			Object obj = iter.next();

			for (int i = 0; i < propertyKeys.length; i++) {
				cell = row.createCell(i);
				cellVal = PropertyUtils.getProperty(obj, propertyKeys[i]);

				cell.setCellValue(cellVal == null ? "" : cellVal.toString());
			}
		}
		return wb;
	}

	public ExcelUtils() {
	}

	public ExcelUtils(String path) {
		this.path = path;
	}

	public void makeExcel(String sheetName, String[] fieldName,
			List<String[]> data) throws IOException {
		HSSFWorkbook workbook = makeWorkBook(sheetName, fieldName, data);

		String filePath = this.path.substring(0, this.path.lastIndexOf("\\"));

		File file = new File(filePath);

		if (!file.exists())
			file.mkdirs();
		FileOutputStream fileOut = new FileOutputStream(this.path);
		workbook.write(fileOut);
		fileOut.close();
	}

	public void makeStreamExcel(String excelName, String sheetName,
			String[] fieldName, List<String[]> data,
			HttpServletResponse response) {
		OutputStream os = null;
		try {
			response.reset();
			os = response.getOutputStream();
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(excelName.getBytes(), "ISO-8859-1"));

			response.setContentType("application/msexcel");
		} catch (IOException ex) {
			System.out.println("流操作错误:" + ex.getMessage());
		}

		HSSFWorkbook workbook = makeWorkBook(sheetName, fieldName, data);
		try {
			os.flush();
			workbook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Output is closed");
		}
	}

	private HSSFWorkbook makeWorkBook(String sheetName, String[] fieldName,
			List<String[]> data) {
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet();

		workbook.setSheetName(0, sheetName);

		HSSFRow row = sheet.createRow(0);

		for (int i = 0; i < fieldName.length; i++) {
			HSSFCell cell = row.createCell((short) i);

			cell.setCellType(1);

			cell.setCellValue(new HSSFRichTextString(fieldName[i]));
		}

		for (int i = 0; i < data.size(); i++) {
			String[] tmp = (String[]) data.get(i);

			row = sheet.createRow(i + 1);
			for (int j = 0; j < tmp.length; j++) {
				HSSFCell cell = row.createCell((short) j);

				cell.setCellType(1);
				cell.setCellValue(new HSSFRichTextString(tmp[j] == null ? ""
						: tmp[j]));
			}
		}

		return workbook;
	}

	public void write(int sheetOrder, int colum, int row, String content)
			throws Exception {
		Workbook workbook = new HSSFWorkbook(new POIFSFileSystem(
				new FileInputStream(this.path)));

		Sheet sheet = workbook.getSheetAt(sheetOrder);
		Row rows = sheet.createRow(row);
		Cell cell = rows.createCell(colum);
		cell.setCellValue(content);
		FileOutputStream fileOut = new FileOutputStream(this.path);
		workbook.write(fileOut);
		fileOut.close();
	}

	public int getSheetLastRowNum(int sheetOrder) throws IOException {
		Workbook workbook = new HSSFWorkbook(new POIFSFileSystem(
				new FileInputStream(this.path)));

		Sheet sheet = workbook.getSheetAt(sheetOrder);
		return sheet.getLastRowNum();
	}

	public String read(int sheetOrder, int colum, int row) throws Exception {
		Workbook workbook = new HSSFWorkbook(new POIFSFileSystem(
				new FileInputStream(this.path)));

		Sheet sheet = workbook.getSheetAt(sheetOrder);
		Row rows = sheet.getRow(row);
		Cell cell = rows.getCell(colum);
		String content = cell.getStringCellValue();
		return content;
	}

	public void makeEmptyExcel() throws IOException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");

		String filePath = this.path.substring(0, this.path.lastIndexOf("\\"));

		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
		FileOutputStream fileOut = new FileOutputStream(filePath + "\\"
				+ this.path.substring(this.path.lastIndexOf("\\") + 1));

		wb.write(fileOut);
		fileOut.close();
	}

	public List<String[]> getDataFromSheet(int sheetOrder) throws IOException {
		Workbook workbook = new HSSFWorkbook(new POIFSFileSystem(
				new FileInputStream(this.path)));

		Sheet sheet = workbook.getSheetAt(sheetOrder);
		List strs = new ArrayList();

		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row rows = sheet.getRow(i);
			String[] str = new String[rows.getLastCellNum()];

			for (int k = 0; k < rows.getLastCellNum(); k++) {
				Cell cell = rows.getCell(k);

				if (0 == cell.getCellType()) {
					DecimalFormat df = new DecimalFormat("########");
					str[k] = df.format(cell.getNumericCellValue());
				} else {
					str[k] = cell.getStringCellValue();
				}
			}
			strs.add(str);
		}
		return strs;
	}
}