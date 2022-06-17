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
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.interactions.Action;
//import org.openqa.selenium.interactions.Actions;
//
//public class EditChannelDescription {
//	private WebDriver driver;
//	private String baseUrl;
//	private StringBuffer verificationErrors = new StringBuffer();
//
//	@Before
//	public void setUp() throws Exception {
//		driver = new FirefoxDriver();
//		baseUrl = "http://localhost:8080/AuthenticationService/login.jsp?Type=AD&ReturnUrl=http://localhost:8080/RSS/signin?";
//		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//	}
//
//	@Test
//	public void testUntitled3() throws Exception {
//
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
//		// Having Issues populating the channel description
//
//		// driver.get(baseUrl + "/RSS/secure/welcome");
//		// assertEquals("RSS", driver.getTitle());
//		driver.findElement(By.id("navChannelReview")).click();
//		assertEquals("RSS", driver.getTitle());
//		driver.findElement(By.cssSelector("button[type=\"button\"]")).click();
//		assertEquals("RSS", driver.getTitle());
//		// WebElement ChannelDescription =
//		// driver.findElement(By.xpath("//*[@id='tinymce']"));
//		// Action InputDescription =
//		// builder.moveToElement(ChannelDescription).click().sendKeys(ChannelDescription,
//		// "Edited Channel Description 2").build();
//		// InputDescription.perform();
//		// driver.findElement(By.id("tinyeditor")).sendKeys("Edited Channel
//		// Descrption 2");
//		driver.findElement(By.id("tinyeditor_ifr")).click();
//		WebElement inputDescription = driver.findElement(By.id("tinyeditor_ifr"));
//		String selectAll = Keys.chord(Keys.CONTROL, "a");
//		inputDescription.sendKeys(selectAll);
//		inputDescription.sendKeys("Test Description");
//		driver.findElement(By.id("submit")).click();
//		assertEquals("RSS", driver.getTitle());
//	}
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
