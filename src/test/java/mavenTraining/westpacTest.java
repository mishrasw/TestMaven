package mavenTraining;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
import utils.readConfig;

public class westpacTest {

	public WebDriver driver;
	public Logger log;
	public readConfig envVar = new readConfig();
	
	@BeforeTest
	public void beforeTest() {
		PropertyConfigurator.configure("log4j.properties");
		log = Logger.getLogger(westpacTest.class);
		
		System.setProperty("webdriver.chrome.driver", "resources\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get(envVar.getPropertyValue("url"));
		log.info("Launching URL");
		/*try {
			assert.assertEquals("actual", "expected");
			log.info("actual equals expected");
		}catch(Exception ex) {
			log.error("incorrect");
		}*/
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		log.info("Application launched successfully");
		
		
	}
	
	@AfterTest
	public void AfterTest() {
		driver.close();
	}
	
	@Test(dataProvider="dataSheet")
	public void westpacTest(String sourceCurr, String Amount, String destCurrency) throws InterruptedException
	{
		WebElement FXElement = driver.findElement(By.xpath("//a[text()='FX, travel & migrant']"));
		Actions mouseMove = new Actions(driver);
		mouseMove.moveToElement(FXElement).build().perform();
		

		driver.findElement(By.xpath("//a[text()='Currency converter']")).click();
		log.info("click Currency converter button");
		Thread.sleep(5000);
		driver.switchTo().frame("westpac-iframe");
		WebElement baseCurrency = driver.findElement(By.id("ConvertFrom"));
		Select baseCurrencySel = new Select(baseCurrency);
		baseCurrencySel.selectByVisibleText(sourceCurr);

		driver.findElement(By.id("Amount")).sendKeys(Amount);
		
		WebElement DestCurrency = driver.findElement(By.id("ConvertTo"));
		Select DestCurrencySel = new Select(DestCurrency);
		DestCurrencySel.selectByVisibleText(destCurrency);
		
		driver.findElement(By.xpath("//input[@value='Convert']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//em[contains(., 'United States Dollar')]")));
		System.out.println("PASS");
		
		driver.switchTo().defaultContent();
		driver.findElement(By.xpath("//a[@id='logo']")).click();
			
	}

	@DataProvider(name="dataSheet")
	public Object[][] readExcel() throws IOException
	{
		File file=new File("resources\\DataSheet.xls");
		FileInputStream fis=new FileInputStream(file);
		HSSFWorkbook workbook= new HSSFWorkbook(fis);
		HSSFSheet sheet=workbook.getSheet("Sheet1");
		int totalRows=sheet.getLastRowNum();
		int totalColums=sheet.getRow(0).getPhysicalNumberOfCells();
		
		// Read data from excel and store the same in the Object Array.
		Object obj[][]=new Object[totalRows][totalColums];
		for(int i=0;i<totalRows;i++)
			for(int j=0;j<totalColums;j++)
			{
			obj[i][j]=sheet.getRow(i+1).getCell(j).toString();
			//obj[i][1]=sheet.getRow(i+1).getCell(1).toString();
			}
		
		return obj;
	}
	
}

