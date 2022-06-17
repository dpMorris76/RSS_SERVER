//package com.centurylink.rss.legacy;
//
//import static org.junit.Assert.assertTrue;
//
//import java.io.IOException;
//import java.util.logging.FileHandler;
//import java.util.logging.Logger;
//import java.util.logging.SimpleFormatter;
//
//import java.awt.Desktop;
//import java.io.File;
//import java.io.IOException;
//
//import org.junit.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.interactions.Action;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import com.google.common.base.Predicate;
//
//class checkbuttons extends checkaccess {
//
//	public static void main(String[] args) {
//		// Declaration and instantiation of objects/variables
//		WebDriver driver = new FirefoxDriver();
//		String baseUrl = "http://localhost:8080/AuthenticationService/login.jsp?Type=AD&ReturnUrl=http://localhost:8080/RSS/signin?";
//		String actualTitle = "";
//		String expectedTitle = "Welcome Mark Morris";
//		
//		// Launch Firefox and direct it to the Base URL
//		driver.get(baseUrl);
//		
//		// Get the text box element to type into & click submit
//					WebElement userid = driver.findElement(By.id("username"));
//					WebElement submit = driver.findElement(By.xpath("//*[@value='Submit']"));
//
//					// Input into text box
//					Actions builder = new Actions(driver);
//					Action InputId = builder
//							.moveToElement(userid)
//							.click()
//							.sendKeys(userid, "aa06863")
//							.moveToElement(submit)
//							.click()
//							.build();
//					InputId.perform();
//
//					// Waits 3 seconds for page to load
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//
//
//
//					// Get the actual value of the title
//					actualTitle = driver.findElement(By.id("userInfoTitle")).getText();
//
//	}
//}
