package Main;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Selenium {
	public static void main(String[] args) throws InterruptedException {
		
		//set up
		System.setProperty("webdriver.chrome.driver", ".\\driver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		openWebsite(driver);
		Thread.sleep(2000);
		
		acceptCookies(driver);

		//print title
		String title = driver.getTitle();
		System.out.println("Title is: " + title);
		
		//click 'location' bar
		performClick(driver,"//*[@id=\"ss\"]");
		Thread.sleep(2000);
		
		//enter value - Limerick
		driver.findElement(By.xpath("//*[@id=\"ss\"]")).sendKeys("Limerick");
		Thread.sleep(2000);
		
		//click first option from the 'location' suggestions
		performClick(driver,"//*[@id=\"frm\"]/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]");
		Thread.sleep(2000);
		
		//click on arrow to change month
		for(int i = 0; i < 3; i++) {
			performClick(driver,"//*[@id=\"frm\"]/div[1]/div[2]/div[2]/div/div/div[2]");
			Thread.sleep(2000);
		}
		
		//get todays day of the month
	    String todayInString = String.valueOf(getToday());
	    String nextDayinString = String.valueOf(getToday()+1);
		
		//get datepicker table element
		WebElement date = driver.findElement(By.xpath("//*[@id=\"frm\"]/div[1]/div[2]/div[2]/div/div/div[3]/div[1]/table/tbody"));

	    //get columns from the table
	    List<WebElement> columns = date.findElements(By.tagName("td"));
	    
	    //find required day
	    for (WebElement requiredDay: columns) {	
	        if (requiredDay.getText().equals(todayInString) || requiredDay.getText().equals(nextDayinString)){
	        	JavascriptExecutor js = (JavascriptExecutor) driver;
	        	js.executeScript("arguments[0].click();", requiredDay);
	        }
	    }
	    Thread.sleep(2000);
	    
		//click on Search button
	    performClick(driver,"//*[@id=\"frm\"]/div[1]/div[4]/div[2]/button");
		Thread.sleep(4000);
		
		//scroll down to Star Rating search filter
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 1300)");
		Thread.sleep(2000);
			
		//click 3-star filter checkbox
		performClick(driver,"//*[@id=\"filter_class\"]/div[2]/a[1]/label/div");
		Thread.sleep(6000);
        
		//@test: check if Savoy Hotel or George Hotel exist in the 3-star item list
		assertIfExist(driver,"[data-hotelid='260219']",false,"Savoy hotel does not exist with 3star filter applied");
		assertIfExist(driver,"[data-hotelid='40243']",false,"George hotel does not exist with 3star filter applied");

		//scroll down to Star Rating search filter
		js.executeScript("window.scrollTo(0, 1350)");
		Thread.sleep(2000);
		
		//uncheck 3-star checkbox
		performClick(driver,"//*[@id=\"filter_class\"]/div[2]/a[1]/label/div");
		Thread.sleep(6000);
		
		//scroll down to Star Rating search filter
		js.executeScript("window.scrollTo(0, 1350)");
		Thread.sleep(2000);
		
		//click 4-star checkbox
		try {
			performClick(driver,"//*[@id=\"filter_class\"]/div[2]/a[2]/label/div");
			Thread.sleep(6000);
		}catch(Exception e) {
			System.out.println("Star 4 rating checkbox did not appear, please clear cache");
		}
		
		//@test check if Savoy Hotel or George Hotel exist in the 4-star item list
		assertIfExist(driver,"[data-hotelid='260219']",false,"Savoy hotel does not exist with 4star filter applied");
		assertIfExist(driver,"[data-hotelid='40243']",true,"George hotel does appear with 4star filter applied");
		
		//scroll down to Star Rating search filter
		js.executeScript("window.scrollTo(0, 1400)");
		Thread.sleep(2000);
		
		//uncheck 4-star checkbox
		try {
			performClick(driver,"//*[@id=\"filter_class\"]/div[2]/a[2]/label/div");
			Thread.sleep(6000);
		}catch(Exception e) {
			System.out.println("Star 4 rating checkbox did not appear, please clear cache");
		}
		
		//scroll down to Star Rating filter
		js.executeScript("window.scrollTo(0, 1400)");
		Thread.sleep(2000);
		
		//click 5-star checkbox
		try {
			performClick(driver,"//*[@id=\"filter_class\"]/div[2]/a[3]/label/div");
			Thread.sleep(6000);
		}catch(Exception e) {
			System.out.println("Star 5 rating checkbox did not appear due to cache, please clear cache");
		}
		
		//@test check if Savoy Hotel or George Hotel exist in the 5-star item list
		assertIfExist(driver,"[data-hotelid='260219']",true,"Savoy hotel does appear with 5star filter applied");
		assertIfExist(driver,"[data-hotelid='40243']",false,"George hotel does not exist with 5star filter applied");

		//scroll down to Star Rating search filter
		js.executeScript("window.scrollTo(0, 1400)");
		Thread.sleep(2000);
		
		//uncheck 5-star filter
		performClick(driver,"//*[@id=\"filter_class\"]/div[2]/a[3]/label/div");
		Thread.sleep(4000);
	
		//scroll down to spa and wellness centre search filter
		js.executeScript("window.scrollTo(0, 3300)");
		Thread.sleep(4000);
		
		//click 'show more' to expand facilities filter list
		performClick(driver,"//*[@id=\"filter_facilities\"]/div[2]/button[1]");
		Thread.sleep(4000);
		
		//click spa and wellness centre checkbox
		driver.findElement(By.cssSelector("[data-value='54']")).click();
		Thread.sleep(6000);
		
		//@test check if Savoy Hotel, George Hotel or Strand Hotel exist in spa and wellness centre item list
		assertIfExist(driver,"[data-hotelid='260219']",true,"Savoy hotel does appear with spa and wellness centre filter applied");
		assertIfExist(driver,"[data-hotelid='40243']",false,"George hotel does not exist with spa and wellness centre filter applied");
		assertIfExist(driver,"[data-hotelid='40345']",false,"Strand hotel does not exist with spa and wellness centre filter applied");
		
		//scroll down to sauna search filter
		js.executeScript("window.scrollTo(0, 1700)");
		Thread.sleep(6000);
		
		//click sauna checkbox
		driver.findElement(By.cssSelector("[data-value='10']")).click();
		Thread.sleep(6000);
		
		//@test check if Savoy Hotel, George Hotel or Strand Hotel exist in spa wellness centre + sauna item list
		assertIfExist(driver,"[data-hotelid='260219']",false,"Savoy hotel does not exist with spa and wellness centre and sauna filters applied");
		assertIfExist(driver,"[data-hotelid='40243']",false,"George hotel does not exist with spa and wellness centre and sauna filters applied");
		assertIfExist(driver,"[data-hotelid='40345']",false,"Strand hotel does not exist with spa and wellness centre and sauna filters applied");
		
		System.out.println("\nAll tests have passed");
	}
	
	public static int getToday() {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	    int today = calendar.get(Calendar.DATE);
	    return today;
	}
	public static void openWebsite(WebDriver driver) {
		driver.get("https://booking.com");
		driver.manage().window().maximize();
	}
	public static void acceptCookies(WebDriver driver) {
		try {
			driver.findElement(By.xpath("//*[@id=\"cookie_warning\"]/div/div/div[2]/button/span")).click();
			Thread.sleep(2000);
		}catch(Exception e) {
			System.out.println("Couldn't close 'Accept Cookies' pop-up");
		}
	}
	public static void performClick(WebDriver driver, String xPath) {
		driver.findElement(By.xpath(xPath)).click();
	}
	public static void assertIfExist(WebDriver driver, String cssLocator, Boolean expected, String info){
		List<WebElement> elementList = driver.findElements(By.cssSelector(cssLocator));
		boolean exists = elementList.size() > 0;
		assertEquals(expected,exists);
		System.out.println(info);
	}
}
