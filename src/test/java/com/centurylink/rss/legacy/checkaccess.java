//package com.centurylink.rss.legacy;
//
//import static org.junit.Assert.assertTrue;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
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
//public class checkaccess {
//
//	private static final Logger logger = Logger.getLogger("AccessTest");
//	private static FileHandler fh;
//
//	public static void main(String[] args) {
//		try {
//			// This block configure the logger with handler and
//			// formatter
//			fh = new FileHandler("C:/SeleniumLogFiles/AccessTest.log", true);
//			logger.addHandler(fh);
//			SimpleFormatter formatter = new SimpleFormatter();
//			fh.setFormatter(formatter);
//
//			// The following statement is used to log any
//			// messages
//			logger.info("Button Test");
//
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		// Declaration and instantiation of objects/variables
//		String actualTitle = "";
//		List<WebElement> actualButtonlist;
//		Set<String> buttons = new HashSet<>();
//		Map<String[], Set<String>> map = new HashMap<String[], Set<String>>();
//		
//		String[] u1 = {"Mark Morris", "aa06863"};
//		String[] u2 = {"Scott Lindsay", "aa20975"};
//		String[] u3 = {"Martin Santora", "aa20990"};
//		String[] u4 = {"Josh Fuller", "aa21015"};
//		String[] u5 = {"David Byrne", "dmb5212"};
//		String[] u6 = {"Kerry Kern", "kxk7341"};
//		String[] u7 = {"Stan Neidecker", "skn073375"};
//		String[] u8 = {"Steven Wagner", "aa60206"};
//		String[] u9 = {"Corey Gunter", "aa60207"};
//		String[] u10 = {"Ryan Borst", "aa48031"};
//		String[] u11 = {"Kyle Tolliver", "aa48035"};
//		String[] u12 = {"Joseph Mertens", "aa95351"};
//		String[] u13 = {"James Sell", "ab03590"};
//		String[] u14 = {"Jacob Burt", "aa95354"};
//		
//		
//		buttons.add("Home");
//		buttons.add("Subscriptions");
//		buttons.add("Content");
//		buttons.add("Add Content");
//		buttons.add("Review");
//		buttons.add("Channels");
//		buttons.add("Add Channel");
//		buttons.add("Review");
//		buttons.add("Groups");
//		buttons.add("Add Group");
//		buttons.add("Review");
//		buttons.add("Users");
//		buttons.add("Tools");
//		buttons.add("ENS Call Guide");
//		buttons.add("CRIS Call Guide");
//		buttons.add("BUS Call Guide");
//		buttons.add("techMart");
//		buttons.add("Library");
//		buttons.add("Techpoint");
//		buttons.add("Tracking Report");
//		
//		map.put(u1 , buttons);
//		for(String[] d: map.keySet())
//		{
//			map.get(d);
//		}
//		String[] accessnames = new String[14];
//		accessnames[0] = "Mark Morris";
//		accessnames[1] = "Scott Lindsay";
//		accessnames[2] = "Martin Santora";
//		accessnames[3] = "Josh Fuller";
//		accessnames[4] = "David Byrne";
//		accessnames[5] = "Kerry Kern";
//		accessnames[6] = "Stan Neidecker";
//		accessnames[7] = "Steven Wagner";
//		accessnames[8] = "Corey Gunter";
//		accessnames[9] = "Ryan Borst";
//		accessnames[10] = "Kyle Tolliver";
//		accessnames[11] = "Joseph Mertens";
//		accessnames[12] = "James Sell";
//		accessnames[13] = "Jacob Burt";
//
//		String[] values = new String[14];
//		values[0] = "aa06863";
//		values[1] = "aa20975";
//		values[2] = "aa20990";
//		values[3] = "aa21015";
//		values[4] = "dmb5212";
//		values[5] = "kxk7341";
//		values[6] = "skn073375";
//		values[7] = "aa60206";
//		values[8] = "aa60207";
//		values[9] = "aa48031";
//		values[10] = "aa48035";
//		values[11] = "aa95351";
//		values[12] = "ab03590";
//		values[13] = "aa95354";
//		WebDriver driver = new FirefoxDriver();
//		// Initializes Loop
//		int i = values.length;
//		for (int j = 0; j < i; j++) {
//
//			// Declaration and instantiation of objects/variables
//			
//			String baseUrl = "http://localhost:8080/AuthenticationService/login.jsp?Type=AD&ReturnUrl=http://localhost:8080/RSS/signin?";
//			String expectedTitle = "Welcome " + accessnames[j];
//			
//			
//			
//			// Launch Firefox and direct it to the Base URL
//			driver.get(baseUrl);
//
//			// Get the text box element to type into & click submit
//			WebElement userid = driver.findElement(By.id("username"));
//			WebElement submit = driver.findElement(By.xpath("//*[@value='Submit']"));
//
//			// Input into text box
//			Actions builder = new Actions(driver);
//			Action InputId = builder.moveToElement(userid).click().sendKeys(userid, values[j]).moveToElement(submit)
//					.click().build();
//			InputId.perform();
//
//			// Waits 3 seconds for page to load
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			// Get the actual buttons of the title & For Each Loop
//			// int x = Buttons.length;
//			actualButtonlist = driver.findElements(By.xpath("//div[@id='site']/input"));
//			int x = actualButtonlist.size();
//
//			// for (WebElement actualButton : actualButtonlist) {
//			Set<String> thingsFound = checkFor(actualButtonlist, buttons);
//			for(String s: buttons)
//			{
//				if(thingsFound.contains(s))
//				{
//					logger.info(s + " found");
//				}
//				else
//				{
//					logger.info(s + " not found");
//				}
//			}
//			
//
//			// Get the actual value of the title
//			actualTitle = driver.findElement(By.id("userInfoTitle")).getText();
//
//			// Log access pass or fail
//			if (actualTitle.contentEquals(expectedTitle)) {
//
//				logger.info(accessnames[j] + " Access Test Pass");
//			} else {
//
//				logger.info(accessnames[j] + " Access Test Failed");
//
//			}
//			// Prints pass or fail to the console
//			// if (actualTitle.contentEquals(expectedTitle))
//			// {
//			// System.out.println("Login Test Passed!");
//			// } else {
//			// System.out.println("Login Test Failed");
//			// }
//			driver.manage().deleteAllCookies();
//		}
//		// }
//		try {
//			fileopener();
//		} catch (Exception e) {
//		}
//		driver.close();
//	}
//
//	public static Set<String> checkFor(List<WebElement> web, Set<String> things) {
//		String temp = null;
//		HashSet<String> s = new HashSet<String>();
//		for (WebElement w : web) {
//			temp = w.getAttribute("value");
//			if (things.contains(temp)) {
//				s.add(temp);
//			}
//		}
//		return s;
//	}
//
//	public static void fileopener() throws IOException {
//		WebDriver driver = new FirefoxDriver();
//		driver.get("file:C:\\SeleniumLogFiles\\AccessTest.log");
//		// text file, should be opening in default text editor
//		File file = new File("C:/SeleniumLogFiles/AccessTest.log");
//
//		// first check if Desktop is supported by Platform or not
//		if (!Desktop.isDesktopSupported()) {
//			System.out.println("Desktop is not supported");
//			return;
//		}
//
//		Desktop desktop = Desktop.getDesktop();
//		if (file.exists()) {
//			desktop.open(file);
//		}
//	}
//}
