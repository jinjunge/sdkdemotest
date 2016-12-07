package com.zhexin.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class GetDataFromExcel {
	
	
	private List<String> extractTextFromXLS2007(XSSFWorkbook xwb,
			int sheetNum,String parameter ) throws Exception {

		List<String> titleList = new ArrayList<String>();
		List<String> dataList = new ArrayList<String>();
		Map<String, Integer> titleMap = new HashMap<String, Integer>();

		XSSFSheet xSheet = xwb.getSheetAt(sheetNum);
        //将第一行的参数名称都加入到list中
		for (int i = 0; i < xSheet.getRow(0).getLastCellNum(); i++) {

			try {
				if (xSheet.getRow(0).getCell(i) == null) {
					titleList.add("");
					continue;
				}
			} catch (NullPointerException e) {
				titleList.add("");
				continue;
			}

			if (xSheet.getRow(0).getCell(i).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				titleList.add(""
						+ (int) xSheet.getRow(0).getCell(i)
								.getNumericCellValue());
			} else {
				titleList.add(xSheet.getRow(0).getCell(i).getStringCellValue());
			}

		}
        //遍历list，将值和列的号放到map中
		for (int i = 0; i < titleList.size(); i++) {
			titleMap.put(titleList.get(i), titleList.indexOf(titleList.get(i)));
		}
        //从第二行开始遍历所有的行
		for (int i = 1; i < xSheet.getLastRowNum() + 1; i++) {

			try {
				if (xSheet.getRow(i).getCell(titleMap.get(parameter)) == null) {
					break;
				}
			} catch (NullPointerException e) {
				break;
			}

			if (xSheet.getRow(i).getCell(titleMap.get(parameter)).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				dataList.add(""
						+ (int) xSheet.getRow(i)
								.getCell(titleMap.get(parameter))
								.getNumericCellValue());
			} else {
				dataList.add(xSheet.getRow(i).getCell(titleMap.get(parameter))              //得到的值如果是空，转成“”，不是则返回原值
						.getStringCellValue().trim().equals("空") ? "" : xSheet
						.getRow(i).getCell(titleMap.get(parameter))
						.getStringCellValue().trim());
			}

		}

		return dataList;
	}

	private String extractTextFromXLS2007Element(XSSFWorkbook xwb,
			String parameter) throws Exception {
		// StringBuffer content = new StringBuffer();
		String[] firstColumn = new String[xwb.getSheetAt(1).getRow(0)
				.getLastCellNum()];
		List<String> unitList = new ArrayList<String>();
		String value = "";
		XSSFSheet xSheet = xwb.getSheetAt(1);
		// 循环行Row
		for (int rowNum = 0; rowNum <= xSheet.getLastRowNum(); rowNum++) {
			XSSFRow xRow = xSheet.getRow(rowNum);
			if (xRow == null) {
				continue;
			}

			// 循环列Cell
			for (int cellNum = 0; cellNum <= xRow.getLastCellNum(); cellNum++) {
				XSSFCell xCell = xRow.getCell(cellNum);
				if (xCell == null) {
					continue;
				}
				if (xCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					// content.append(""+(int)xCell.getNumericCellValue());
					unitList.add("" + (int) xCell.getNumericCellValue());
					if (rowNum == 0) {
						firstColumn[cellNum] = ""
								+ (int) xCell.getNumericCellValue();
					}
				} else {
					// content.append(xCell.getStringCellValue());
					unitList.add(xCell.getStringCellValue());
					if (rowNum == 0) {
						firstColumn[cellNum] = xCell.getStringCellValue();
					}
				}
			}
		}

		for (int index = 0; index < firstColumn.length; index++) {
			if (firstColumn[index].equals(parameter)) {
				value = unitList.get(index + firstColumn.length);
			}
		}
		return value;
	}
	
	
	

	private List<String> getXLS2007Data(XSSFWorkbook xwb,int sheet,String parameter) {
		List<String> value = null;
		try {
			value = extractTextFromXLS2007(xwb, sheet, parameter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	private List<String> getXLS2007EXPC(XSSFWorkbook xwb, String parameter) {
		List<String> value = null;
		try {
			value = extractTextFromXLS2007(xwb, 2 ,parameter );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	private String getXLS2007Element(XSSFWorkbook xwb, String parameter) {
		String value = null;
		try {
			value = extractTextFromXLS2007Element(xwb, parameter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	

	public static List<String> getEXPC(XSSFWorkbook xwb, String parameterName) {

		return new GetDataFromExcel().getXLS2007EXPC(xwb, parameterName);
	}

	
	public static List<String> getData(XSSFWorkbook xwb,int sheet, String parameterName) {

		return new GetDataFromExcel().getXLS2007Data(xwb,sheet,parameterName);
	}

	public static String getElement(XSSFWorkbook xwb, String parameterName) {

		return new GetDataFromExcel().getXLS2007Element(xwb, parameterName);
	}

	public static XSSFWorkbook getFile(String fileName) {

		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		try {
			return new XSSFWorkbook(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
