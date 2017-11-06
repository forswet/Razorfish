package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.pages.FormPage;
import com.util.ExcelDataConfig;


public class FormTest {
	
	WebDriver driver;
	String chrome_exe;
	String url;
	
	@BeforeSuite
	public void setUp() throws Exception{
		File fSourceFile;
		File fDestination;
		fSourceFile = new File("./TestData/RazorfishTestData.xlsx");
		//create TestResult Directory
		fDestination = new File("./TestResult");
		//copy File to above Directory
		FileUtils.copyFileToDirectory(fSourceFile,fDestination );		
	}
	
	@BeforeMethod 
	public void readPropertyFile() throws Exception{
		// Reading configuration details from Property file
		File src = new File("./config.properties");
		FileInputStream fis = new FileInputStream(src);
		Properties prop = new Properties();
		prop.load(fis);
		chrome_exe = prop.getProperty("chrome_exe");
		url = prop.getProperty("url");
		
	}
	
	
	@Test(dataProvider="formData")
	public void submitFormTest(String button, String firstname, String lastname, String phoneno,String expected_error, int rownum ) throws Exception{
	
		launchDriver();
		String actual_error = "";
		FormPage obj = new FormPage(driver);
		
		ExcelDataConfig.setExcelFile("./TestResult/RazorfishTestData.xlsx");
		
		ExcelDataConfig config1 = new ExcelDataConfig();
		
		
		if(firstname.equalsIgnoreCase("NULL")) { firstname = "";}
		if(lastname.equalsIgnoreCase("NULL")) { lastname = "";}
		if(phoneno.equalsIgnoreCase("NULL")) { phoneno = "";}
		if(expected_error.equalsIgnoreCase("NULL")) { expected_error = "";}
		
		// Passing Input data to FormPage class
        obj.submitForm(button,firstname,lastname,phoneno);
		
        if(button.equalsIgnoreCase("TestSubmitButton")){
	 
        	actual_error = driver.findElement(By.tagName("p")).getText();
		
        	if (actual_error.equals("Form submitted!") ){ 
        		actual_error = driver.findElement(By.tagName("p")).getText();	
        	}
        	else {
        		if(driver.findElement(By.id("fname-error")).getText().length() != 0){
        			actual_error = driver.findElement(By.id("fname-error")).getText(); 
				 }
			else if (driver.findElement(By.id("lname-error")).getText().length() != 0){
				   actual_error = driver.findElement(By.id("lname-error")).getText();
				}
			else if (driver.findElement(By.id("phone-error")).getText().length() != 0){
				   actual_error = driver.findElement(By.id("phone-error")).getText();
				}
        	}	
		
        //Populate the actual data
        config1.setCellData(actual_error, rownum, 5, "InputDataSheet");
        
      //Compare the results
        config1.compareResults("InputDataSheet", rownum, 4, 5, 6);  
        
        config1.saveResults("./TestResult/RazorfishTestData.xlsx");
        
        Assert.assertEquals(actual_error, expected_error);		
        }
        else if(button.equalsIgnoreCase("TestClearButton")){
        	
        	if((driver.findElement(By.id("fname")).getText().length() != 0) || 
    				(driver.findElement(By.id("lname")).getText().length() != 0) ||
    				(driver.findElement(By.id("phone")).getText().length() != 0)){

        		//Populate the result of the test case
        		config1.setCellData("Fail", rownum, 6, "InputDataSheet");
        		config1.saveResults("./TestResult/RazorfishTestData.xlsx");
    			Assert.assertFalse(true);			
    		}
        	else{
        		config1.setCellData("Pass", rownum, 6, "InputDataSheet");	
        		config1.saveResults("./TestResult/RazorfishTestData.xlsx");
    			Assert.assertFalse(false);			
    		}
        }       
	}
	
	@DataProvider(name ="formData")
	public Object[][] formData() throws Exception{
		
		//Set the excel file object
		ExcelDataConfig.setExcelFile("./TestData/RazorfishTestData.xlsx");
		ExcelDataConfig config = new ExcelDataConfig();
		int rows = config.getRowCount("InputDataSheet");
		Object[][] data = new Object[rows][6];
		
		for(int i=0; i<rows; i++){
			data[i][0] = config.getCellData(i+1, 0,"InputDataSheet");
			data[i][1] = config.getCellData(i+1, 1,"InputDataSheet");
			data[i][2] = config.getCellData(i+1, 2,"InputDataSheet");	
		 	data[i][3] = config.getCellData(i+1, 3,"InputDataSheet");
		 	data[i][4] = config.getCellData(i+1, 4,"InputDataSheet");
		 	data[i][5] = i+1;
		}
		
		return data;
	}

	@AfterMethod
	public void closeBrowser() {
		driver.quit();
	}

	
	public void launchDriver(){
		System.setProperty("webdriver.chrome.driver",chrome_exe);
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		File file =new File(url);
		driver.get(file.getAbsolutePath());	
	}

	
}

