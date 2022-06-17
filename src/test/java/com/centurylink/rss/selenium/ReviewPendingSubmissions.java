//package com.centurylink.rss.selenium;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
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
//public class ReviewPendingSubmissions {
//
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
//	public void testUntitled2() throws Exception {
//		driver.get(baseUrl + "/RSS/secure/welcome");
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
//		assertEquals("RSS", driver.getTitle());
//		driver.findElement(By.id("navContentGrid")).click();
//		assertEquals("RSS", driver.getTitle());
//		new Select(driver.findElement(By.id("combobox"))).selectByVisibleText("All Content Submissions");
//		driver.findElement(By.name("submit")).click();
//		assertEquals("RSS", driver.getTitle());
//		assertNotEquals("Showing 0 to 0 of 0 entries", driver.findElement(By.className("dataTables_info")).getText());
//
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
