package cochrane_package;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class cochrane_class {
     static WebDriver d;
     static Properties ob;
    //To Write in file
	public  void write(String url,String topic,String title,String author,String date,String fn)
	{
		File log = new File(fn);
		
		try {
			
			if(!log.exists())
			{
				log.createNewFile();
				PrintWriter out1 = new PrintWriter(new FileWriter(log, true)); 
				out1.append("URL"+"|"+"TOPIC"+"|"+"TITLE"+"|"+"AUTHOR"+"|"+"DATE");
				out1.println();
				out1.close();
			}
			PrintWriter out = new PrintWriter(new FileWriter(log, true));  
			    out.append(url+"|"+topic+"|"+title+"|"+author+"|"+date+"");
			    out.println();
			    //out.println();
			    out.close();
					 
	        }
	        catch(IOException ex) {
	            System.out.println( "Error writing to file '"+ fn + "'"+ex);
	           
	        }
		}
	
	//To Visit next page
	public void next()
	{
		try {
			d.findElement(By.xpath(ob.getProperty("next_xpath"))).click();
		    Thread.sleep(3000);
	        }
		
		catch(Exception e) {
			d.close();
		}
	}
	
	
	//To start web driver
	public void wd_setup() {
		WebDriverManager.chromedriver().setup();
		 d=new ChromeDriver();
	}
	
public static void main(String args[]) throws  IOException {
		cochrane_class c=new cochrane_class(); //object created
		c.wd_setup();
		//Accessing property file 
	     ob = new Properties();
		FileInputStream f= new  FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\ob.properties");
		ob.load(f);
		//Opening Input URL
		d.get(ob.getProperty("ip_url"));
		d.manage().window().maximize();
		//Selecting the topic as Allergy and intolerance
		d.findElement(By.xpath(ob.getProperty("allint_xpath"))).click();
		//Obtaining the number of reviews
		String s=d.findElement(By.xpath(ob.getProperty("result_xpath"))).getText();
		int result = Integer.parseInt(s);
		
  for(int i=0;i<result; result=result-25)
  {
	int j=0,z=0;
	List<WebElement> L_link=  d.findElements(By.xpath(ob.getProperty("link_xpath")));
	List<WebElement> L_author= d.findElements(By.xpath(ob.getProperty("author_xpath")));
	List<WebElement> L_date=  d.findElements(By.xpath(ob.getProperty("date_xpath")));
	for(WebElement e:L_link)
	{
			String URL=e.getAttribute("href");
		    String TITLE=e.getText();
		      while(j<L_author.size())
		       {
			     String AUTHOR=L_author.get(j).getText();
			     
			       while(z<L_date.size())
			        {
				     String DATE=L_date.get(z).getText();
				     z++;
				     c.write(URL,ob.getProperty("topic_st"), TITLE, AUTHOR, DATE,ob.getProperty("filename"));
				     break;
				     
				     }
			       j++;
			     break;
			   }
		   
		  }
    c.next();
   
  }
System.out.println("Sucessfully extracted the data and stored at desktop with file name 'Allergy_intolerance.txt'");		 
}
		
}


