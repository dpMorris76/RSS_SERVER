//package com.centurylink.rss.legacy;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//
//public class HackDriver {
//	private static WebDriver d;
//	
//	public static WebDriver getWebDriver()
//	{
//		if(d != null)
//		{
//			// do nothing.
//		}
//		else
//		{
//			d = new FirefoxDriver();
//		}
//		d.manage().deleteAllCookies();
//		d.get("");
//		return d;
//	}
//}
