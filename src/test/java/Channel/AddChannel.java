package Channel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

public class AddChannel {
	
	
	public static void main(String[] args) {
		// Declaration and instantiation of objects/variables
		WebDriver driver = new FirefoxDriver();
		String baseUrl = "http://localhost:8080/AuthenticationService/login.jsp?Type=AD&ReturnUrl=http://localhost:8080/RSS/signin?";

		// Launch Firefox and direct it to the Base URL
		driver.get(baseUrl);

		// Get the text box element to type into & click submit
		WebElement user_id = driver.findElement(By.id("username"));
		WebElement submit = driver.findElement(By.xpath("//*[@value='Submit']"));

		// Input into text box
		Actions builder = new Actions(driver);
		Action InputId = builder.moveToElement(user_id).click().sendKeys(user_id, "aa06863").moveToElement(submit)
				.click().build();
		InputId.perform();

		// Waits 3 seconds for page to load
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// add Channel

		driver.findElement(By.id("navChannelForm")).click();

		driver.findElement(By.id("pupDatePicker")).click();
		driver.findElement(By.xpath("//td[contains(@class, \"today\")]/div")).click();
		driver.findElement(By.id("channelTitle")).click();
		WebElement inputChannelTitle = driver.findElement(By.id("channelTitle"));
		inputChannelTitle.sendKeys("Scripted Selenium Channel");

		driver.findElement(By.id("tinyeditor_ifr")).click();
		WebElement inputChannelDescription = driver.findElement(By.id("tinyeditor_ifr"));
		inputChannelDescription.sendKeys("test Channel description");

		driver.findElement(By.id("submit")).click();

	}
}