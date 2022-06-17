//package com.centurylink.rss.selenium;
//
//import org.junit.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.interactions.Action;
//import org.openqa.selenium.interactions.Actions;
//
//public class AutoSelectedChannelsRemove {
//
//	@Test
//	public void removeAutoSelectedChannels() {
//		// Declaration and instantiation of objects/variables
//		WebDriver driver = new FirefoxDriver();
//		String baseUrl = "http://localhost:8080/AuthenticationService/login.jsp?Type=AD&ReturnUrl=http://localhost:8080/RSS/signin?";
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
//		// Open User Single Edit
//		driver.findElement(By.id("navAutoSelectChannels")).click();
//		driver.findElement(By.id("checkboxTest Channel")).click();
//		driver.findElement(By.cssSelector("input[value=\"Remove\"]")).click();
//		driver.switchTo().alert().accept();
//		driver.close();
//	}
//}