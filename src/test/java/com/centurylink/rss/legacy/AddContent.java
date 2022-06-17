package com.centurylink.rss.legacy;
// THIS IS NOW AN INACTIVE TEST CASE.


//package ContentSubmission;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.interactions.Action;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.Select;
//import java.util.List;
//import org.hibernate.SessionFactory;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//public class AddContent {
//	public static void main(String[] args) {
//		// Declaration and instantiation of objects/variables
//		WebDriver driver = new FirefoxDriver();
//		String baseUrl = "http://localhost:8080/AuthenticationService/login.jsp?Type=AD&ReturnUrl=http://localhost:8080/RSS/signin?";
//		String expectedTitle = "Welcome Mark Morris";
//		String actualTitle = "";
//		String expectedAddContentTitle = "Content Submission Form";
//		String actualAddContentTitle = "";
//
//		// Launch Firefox and direct it to the Base URL
//		driver.get(baseUrl);
//
//		// Get the text box element to type into & click submit
//		WebElement user_id = driver.findElement(By.id("username"));
//		WebElement submit = driver.findElement(By.xpath("//*[@value='Submit']"));
//
//		// Input into text box
//		Actions builder = new Actions(driver);
//		Action InputId = builder.moveToElement(user_id).click().sendKeys(user_id, "aa06863").moveToElement(submit)
//				.click().build();
//		InputId.perform();
//
//		// Waits 3 seconds for page to load
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		driver.findElement(By.id("navContentForm")).click();
//
//		// Add Content Page
//
//		driver.findElement(By.id("navContentForm")).click();
//		
//		
//
//		Select dropdown = new Select(driver.findElement(By.id("channelId")));
//		dropdown.selectByVisibleText("Test Channel");
//
//		driver.findElement(By.id("storyTitle")).click();
//		WebElement inputStoryTitle = driver.findElement(By.id("storyTitle"));
//		inputStoryTitle.sendKeys("Script Selenium Test1");
//
//		driver.findElement(By.id("storyLink")).click();
//		WebElement inputStoryLink = driver.findElement(By.id("storyLink"));
//		inputStoryLink.sendKeys("test URL Link");
//
//		driver.findElement(By.id("tinyeditor_ifr")).click();
//		WebElement inputDescription = driver.findElement(By.id("tinyeditor_ifr"));
//		inputDescription.sendKeys("test description");
//
//		driver.findElement(By.id("submitAndApprove")).click();
//		
//		
//		
//		
//
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//
//}
