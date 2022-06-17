//package com.centurylink.rss.legacy;
//
//import static org.junit.Assert.assertTrue;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.logging.FileHandler;
//import java.util.logging.Logger;
//import java.util.logging.SimpleFormatter;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
////import org.apache.log4j.Logger;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.interactions.Action;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.Random;
//
//import com.centurylink.rss.domain.entity.Channel;
//import com.centurylink.rss.domain.entity.ChannelGroup;
//import com.centurylink.rss.domain.entity.Permission;
//import com.centurylink.rss.domain.entity.User;
//import com.centurylink.rss.domain.entity.util.HibernateUtil;
//import com.google.common.base.Predicate;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/applicationContext.xml" })
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
//public class AutoSelectChannelsTest {
//
//	@Autowired
//	SessionFactory sf;
//
//	public Long Subscription = 1L;
//	public Long UserEdit = 6L;
//	
//	private void save(Object arg0)
//	{
//		sf.getCurrentSession().saveOrUpdate(arg0);
//	}
//	// for generating random things.
//	Random rand = new Random();
//	public String rString(int len)
//	{
//		String s = "";
//		for(int i = 0; i < len; i++)
//		{
//			s += (char)(64 + rand.nextInt(52)); 
//		}
//		return s;
//	}
//	
//	public boolean textInsideOfElementNumber(WebDriver d, String text, String tagName, int number)
//	{
//		List<WebElement> things = d.findElements(By.tagName(tagName));
//		for(int i = 0; i < things.size(); i++)
//		{
//			System.out.println(things.get(i).getText());
//		}
//		return false;
//	}
//	
//	private User u;
//	String uname = "aa06863";
//	private Channel c1;
//	private Channel c2;
//	private ChannelGroup g;
//	public static HibernateUtil hib;
//	public void setUpHibernateUtil()
//	{
//		hib = new HibernateUtil();
//		hib.setSessionFactory(sf);
//	}
//	public void CreateTestObjects()
//	{
//		// this is really all that I need to do.
//		u = (User)hib.findByWhereClause(User.class, "where username = '" + uname + "'").get(0);
//		
//		g = new ChannelGroup();
//		
//		g.setTitle(rString(5));
//		c1 = new Channel();
//		c2 = new Channel();
//		ArrayList<Channel> cs = new ArrayList<Channel>();
//		cs.add(c1);
//		cs.add(c2);
//		for(int i = 0; i < cs.size(); i++)
//		{
//			String j = String.valueOf(i);
//			Channel c = cs.get(i); // will this be a soft or a hard copy? 
//			c.setTitle(rString(5)); // hopefully it will be a soft copy.
//			c.setDescription(rString(5));
//			c.setLanguage("English");
//			c.setGenerator("CTL RSS FEEDER");
//			c.setLink("http://");
//			c.setLastBuildDate(new Date());
//			c.setChannelGroup(g);
//			save(c);
//		}
//		save(g);
//		u.setAssocGroupId(g.getId());
//		save(u);
//	}
//	public void tearDown()
//	{
//		hib.delete(u);
//		hib.delete(g);
//		hib.delete(c1);
//		hib.delete(c2);
//	}
//	
//	public void waitForHibernate()
//	{
//		hib.getSessionFactory().getCurrentSession().flush();
//		try{
//			Thread.sleep(1000L);
//		} catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		// we do this because our spring needs to get the memo that things changed... there is probably 
//		// a better way to do this but it's not worth finding atm.
//	}
//	
//	@Test
//	public void leftAndRightMovementOfChannels()
//	{
//		setUpHibernateUtil();
//		CreateTestObjects();
//		HashSet<Permission> perms = new HashSet<Permission>();
//		for(Object o: hib.findAll(Permission.class))
//		{
//			perms.add((Permission)o);
//		}
//		
//		u.setPermissions(perms);
//		save(u);
//		waitForHibernate();
//		WebDriver d = HackDriver.getWebDriver();
//		d.get("http://localhost:8080/AuthenticationService/login.jsp?Type=AD&ReturnUrl=http://localhost:8080/RSS/signin?");
//		WebElement clickMe = d.findElement(By.id(uname)); // find aa20990 to click on it.
//		clickMe.click();
//		waitForNextPage(d);
//		clickMe = d.findElement(By.id("navAutoSelectChannels"));
//		clickMe.click();
//		waitForNextPage(d);
//		System.out.print(d.getPageSource());
//		System.err.println(c1.getTitle());
//		assertTrue("contains i", d.getPageSource().contains(c1.getTitle()));
//		assertTrue("contains i", d.getPageSource().contains(c2.getTitle()));
//		
//		clickMe = d.findElement(By.id("checkbox" + c1.getTitle()));
//		clickMe.click();
//		waitForNextPage(d);
//		clickMe = d.findElement(By.linkText("Add"));
//		clickMe.click();
//		waitForNextPage(d);
//		textInsideOfElementNumber(d, c1.getTitle(), "table", 3);
//	}
//	
//	
//	public void jay_test() {
//		WebDriver d = new FirefoxDriver();
//		
//		String baseUrl = "http://localhost:8080/AuthenticationService/login.jsp?Type=AD&ReturnUrl=http://localhost:8080/RSS/signin?";
//		d.get(baseUrl);
//		WebElement user_id = d.findElement(By.id("username"));
//		WebElement submit = d.findElement(By.xpath("//*[@value='Submit']"));
//		d.findElement(By.xpath(""));
//		Actions builder = new Actions(d);
//
//		Action InputId = builder.moveToElement(user_id).click().sendKeys(user_id, "aa06863").moveToElement(submit)
//				.click().build();
//		InputId.perform();
//		waitForNextPage(d);
//		String expectedTitle = "Welcome Mark Morris";
//		assertTrue(d.findElement(By.id("userInfoTitle")).getText().contentEquals(expectedTitle));
//	}
//
//	public void waitForNextPage(WebDriver d) {
//		WebDriverWait wait = new WebDriverWait(d, 30);
//		wait.until(new Predicate<WebDriver>() {
//			@Override
//			public boolean apply(WebDriver d) {
//				String s = String.valueOf(((JavascriptExecutor) d).executeScript("return document.readyState"));
//				return s.equals("complete");
//			}
//		});
//		// wait.until(new Function<WebDriver, Boolean>(){
//		// @Override
//		// public Boolean apply(WebDriver d) {
//		// String s =
//		// String.valueOf(((JavascriptExecutor)d).executeScript("return
//		// document.readyState"));
//		// return s.equals("complete");
//		// }
//		//
//		//
//		// } );
//	}
//}
