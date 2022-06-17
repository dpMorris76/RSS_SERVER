//package com.centurylink.rss.selenium;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;
//
//import java.util.concurrent.TimeUnit;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.interactions.Action;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.Select;
//
//public class HighPrioritySubmitAndApprove {
//
//	WebDriver driver;
//	String baseUrl;
//	boolean acceptNextAlert = true;
//	StringBuffer verificationErrors = new StringBuffer();
//
//	@Before
//	public void setUp() throws Exception {
//		driver = new FirefoxDriver();
//		baseUrl = "http://localhost:8080/AuthenticationService/login.jsp?Type=AD&ReturnUrl=http://localhost:8080/RSS/signin?";
//		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//	}
//
//	@Test
//	public void testUntitled() throws Exception {
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
//
//		// broken code produced by IDE, working on figuring out the issue with
//		// it so i can just fix the IDE code in the future rather than rewrite
//		// it from scratch
//		assertEquals("RSS", driver.getTitle());
//		driver.findElement(By.id("navContentForm")).click();
//		assertEquals("RSS", driver.getTitle());
//		// new
//		// Select(driver.findElement(By.id("channelId"))).selectByVisibleText("Test
//		// Channel");
//		// driver.findElement(By.id("pupDatePicker")).click();
//		// driver.findElement(By.cssSelector("div.xdsoft_timepicker.active >
//		// button.xdsoft_prev")).click();
//		// driver.findElement(By.xpath("//div[2]/div[2]/div/div/div[17]")).click();
//		// driver.findElement(By.id("pupDatePicker")).click();
//		// driver.findElement(By.id("expDatePicker")).click();
//		// driver.findElement(By.xpath("//div[3]/div[2]/div/div/div[18]")).click();
//		// driver.findElement(By.id("storyTitle")).click();
//		// WebElement inputStoryTitle = driver.findElement(By.id("storyTitle"));
//		// inputStoryTitle.sendKeys("IDE Script Selenium Test1");
//		// driver.findElement(By.id("storyLink")).click();
//		// WebElement inputStoryLink = driver.findElement(By.id("storyLink"));
//		// inputStoryLink.sendKeys("test URL Link");
//		// driver.findElement(By.id("tinyeditor_ifr")).click();
//		// WebElement inputDescription =
//		// driver.findElement(By.id("tinyeditor_ifr"));
//		// inputDescription.sendKeys("test description");
//		// driver.findElement(By.id("submitAndApprove")).click();
//
//		// Add Content Page
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
//		driver.findElement(By.id("isHighPriority1")).click();
//
//		driver.findElement(By.id("submitAndApprove")).click();
//		System.out.println(driver.getCurrentUrl());
//	}
//
////	public static void fileopener() throws IOException {
////		WebDriver driver = new FirefoxDriver();
////		driver.get("file:C:\\SeleniumLogFiles\\RSStests.log");
////		// text file, should be opening in default text editor
////		File file = new File("C:/SeleniumLogFiles/RSStests.log");
////
////		// first check if Desktop is supported by Platform or not
////		if (!Desktop.isDesktopSupported()) {
////			System.out.println("Desktop is not supported");
////			return;
////		}
////
////		Desktop desktop = Desktop.getDesktop();
////		if (file.exists()) {
////			desktop.open(file);
////		}
////	}
//
//	@After
//	public void tearDown() throws Exception {
//		driver.quit();
//		String verificationErrorString = verificationErrors.toString();
//		if (!"".equals(verificationErrorString)) {
//			fail(verificationErrorString);
//		}
//	}	
//}