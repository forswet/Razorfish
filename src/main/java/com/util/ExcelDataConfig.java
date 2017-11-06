package com.util;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelDataConfig {
private static XSSFWorkbook xExcelWBook;
	
	public static void setExcelFile(String sPath) throws Exception{
		FileInputStream excelFile = new FileInputStream(sPath);
		xExcelWBook = new XSSFWorkbook(excelFile);
	}
	public int getRowCount(String sSheetName) throws Exception{
		XSSFSheet xExcelWSheet;
		int iNumber = 0;
		
		xExcelWSheet = xExcelWBook.getSheet(sSheetName);
		iNumber = xExcelWSheet.getLastRowNum();
		
		return iNumber;	
	}
	
	public String getCellData(int iRowNum, int iColNum, String sSheetName){
		Cell cCell;
		XSSFRow xRow;
		XSSFSheet xExcelWSheet;
		String sCellData = "";
		try{
			xExcelWSheet = xExcelWBook.getSheet(sSheetName);
			xRow = xExcelWSheet.getRow(iRowNum);
			cCell = xRow.getCell(iColNum);
			sCellData = cCell.getStringCellValue();
		}catch(Exception e){
			sCellData = "";
		}
		
		return sCellData;
	}
	
	public void saveResults(String sFileName) throws Exception{
		FileOutputStream fileOut = new FileOutputStream(sFileName);
		xExcelWBook.write(fileOut);
		fileOut.close();
	}
	
	public String compareResults(String sSheetName, int iRowNum, int iExpectedCell, int iActualCell, int iResultCell){
		String sCompareResult = "Pass";
		String sExpectedData = "";
		String sActualData = "";
		
		try{
			sExpectedData = getCellData(iRowNum, iExpectedCell, sSheetName);
			sActualData = getCellData(iRowNum, iActualCell, sSheetName);
			if(!sExpectedData.contentEquals("NoCompare")){
				if(sExpectedData.contentEquals(sActualData)){
					setCellData(sCompareResult, iRowNum, iResultCell, sSheetName);
				}else{
					sCompareResult = "Fail";
					setCellData(sCompareResult, iRowNum, iResultCell, sSheetName);
				}
			}
		}catch(Exception e){
			sCompareResult = "Error";
		}
		
		return sCompareResult;
	}
	
	public void setCellData(String sResult, int iRowNum, int iColNum, String sSheetName) throws Exception{
		Cell cCell;
		XSSFRow xRow;
		XSSFSheet xExcelSheet;	
		xExcelSheet = xExcelWBook.getSheet(sSheetName);
		xRow = xExcelSheet.getRow(iRowNum);
		cCell = xRow.getCell(iColNum, xRow.RETURN_BLANK_AS_NULL);
		if(cCell == null){
			cCell = xRow.createCell(iColNum);
			cCell.setCellValue(sResult);
		}else{
			cCell.setCellValue(sResult);
		}
	}
	
		
}


