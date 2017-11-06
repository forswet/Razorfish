package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
/*
 * This class will store all the locator and methods of Form page
 */

public class FormPage {
	
WebDriver driver;
	
	By firstName = By.id("fname");
	By lastName = By.id("lname");
	By phoneNumber = By.id("phone");
	By submitButton = By.id("submit");
	By clearButton = By.xpath("//button[@onclick='return clearForm();']");
	
	public FormPage(WebDriver driver){
		this.driver = driver;
	}
	
	public void submitForm(String button,String fname, String lname,String phoneno){
		
		driver.findElement(firstName).sendKeys(fname);
		driver.findElement(lastName).sendKeys(lname);
		driver.findElement(phoneNumber).sendKeys(phoneno);
		
			if(button.equalsIgnoreCase("TestSubmitButton")){
			
				driver.findElement(submitButton).click();
			
			}else if(button.equalsIgnoreCase("TestClearButton")){
			
				driver.findElement(clearButton).click();
			}
		}	
		

}
