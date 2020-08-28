package stepDefinition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.cucumber.listener.Reporter;

import TestRunner.TestRun;
import base.BaseUtil;
import base.Constant;

public class test extends BaseUtil {

	public static String screenshotfolder;

	public ScreenShotTaken(BaseUtil base) {
	}

	public String getscreenshot(WebDriver base) throws Exception {
		String dest = null;
		String st = null;
		screenshotfolder=TestRun.folderPath;
		DateTimeFormatter dtf;
		File dir = new File(System.getProperty("user.dir") + "\\target");
	    File[] files = dir.listFiles();
	    File lastModified = Arrays.stream(files).filter(File::isDirectory).max(Comparator.comparing(File::lastModified)).orElse(null);
		try {
			dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
			final LocalDateTime now = LocalDateTime.now();
			st = dtf.format(now);
			File scrFile = ((TakesScreenshot) base).getScreenshotAs(OutputType.FILE);
			dest = lastModified+"\\";
			FileUtils.copyFile(scrFile, new File(dest+screenshotfolder+ st + ".png"));
			com.cucumber.listener.Reporter.addScreenCaptureFromPath(st + ".png");
			return dest+screenshotfolder+ st + ".png";
		} catch (Exception e) {
			Reporter.addStepLog("Error while taking screenshot");
			System.out.println(e.getMessage());
		}
		return dest + st + ".png";
	}
	
	public void writeInFile(WebDriver base,String role,int i) throws IOException, InterruptedException {
		Properties prop = new Properties();
		String pass;
		String Path = System.getProperty("user.dir") + "\\Resources\\RegisteredUserOrganisation.txt";
		FileReader Classnamefileread = new FileReader(Path);
		BufferedReader classnmeread = new BufferedReader(Classnamefileread);
		String registeredUser = classnmeread.readLine();
		FileWriter Institutionfile = new FileWriter(System.getProperty("user.dir") + "\\Resources\\Credentials.txt",true);
		BufferedWriter Institutionwriter = new BufferedWriter(Institutionfile);
		Institutionwriter.write(registeredUser + "-" + role+i+ "\n");
		if((i>3) && (role.contains("org Admin"))) {
			Institutionfile = new FileWriter(System.getProperty("user.dir") + "\\Resources\\EditAccountOrgadmincredentials.txt");
			Institutionwriter = new BufferedWriter(Institutionfile);
			Institutionwriter.write(registeredUser);
			FileWriter passwordInFile = new FileWriter(System.getProperty("user.dir") + "\\Resources\\Password.txt");
			BufferedWriter Institutionwriter1 = new BufferedWriter(passwordInFile);
			String propertiesPath = System.getProperty("user.dir") + "\\Resources\\Browser.properties";
			InputStream input = new FileInputStream(propertiesPath);
			prop.load(input);
			if(role.contains("OUP"))
			{
				pass = prop.getProperty("OUPPassword");
			}
			else {
				pass = prop.getProperty("NonOUPPassword");
			}
			Institutionwriter1.write(pass);
		}
		else if((i>2) && (role.contains("Teacher Admin")))
		{
			Institutionfile = new FileWriter(System.getProperty("user.dir") + "\\Resources\\EditAccountTeacherAdmincredentials.txt");
			Institutionwriter = new BufferedWriter(Institutionfile);
			Institutionwriter.write(registeredUser);
			FileWriter passwordInFile = new FileWriter(System.getProperty("user.dir") + "\\Resources\\Password.txt");
			BufferedWriter Institutionwriter1 = new BufferedWriter(passwordInFile);
			String propertiesPath = System.getProperty("user.dir") + "\\Resources\\Browser.properties";
			InputStream input = new FileInputStream(propertiesPath);
			prop.load(input);
			if(role.contains("OUP"))
			{
				pass = prop.getProperty("OUPPassword");
			}
			else {
				pass = prop.getProperty("NonOUPPassword");
			}
			Institutionwriter1.write(pass);
			
		}
		else if((i>2) && (role.contains("Teacher")))
		{
			Institutionfile = new FileWriter(System.getProperty("user.dir") + "\\Resources\\EditAccountTeachercredentials.txt");
			Institutionwriter = new BufferedWriter(Institutionfile);
			Institutionwriter.write(registeredUser);
			FileWriter passwordInFile = new FileWriter(System.getProperty("user.dir") + "\\Resources\\Password.txt");
			BufferedWriter Institutionwriter1 = new BufferedWriter(passwordInFile);
			String propertiesPath = System.getProperty("user.dir") + "\\Resources\\Browser.properties";
			InputStream input = new FileInputStream(propertiesPath);
			prop.load(input);
			if(role.contains("OUP"))
			{
				pass = prop.getProperty("OUPPassword");
			}
			else {
				pass = prop.getProperty("NonOUPPassword");
			}
			Institutionwriter1.write(pass);
			
		}
		else if((i>2) && (role.contains("learner")))
		{
			Institutionfile = new FileWriter(System.getProperty("user.dir") + "\\Resources\\EditAccountStudentcredentials.txt");
			Institutionwriter = new BufferedWriter(Institutionfile);
			Institutionwriter.write(registeredUser);
			FileWriter passwordInFile = new FileWriter(System.getProperty("user.dir") + "\\Resources\\Password.txt");
			BufferedWriter Institutionwriter1 = new BufferedWriter(passwordInFile);
			String propertiesPath = System.getProperty("user.dir") + "\\Resources\\Browser.properties";
			InputStream input = new FileInputStream(propertiesPath);
			prop.load(input);
			if(role.contains("OUP"))
			{
				pass = prop.getProperty("OUPPassword");
			}
			else {
				pass = prop.getProperty("NonOUPPassword");
			}
			Institutionwriter1.write(pass);
			
		}
		
		Institutionwriter.flush();
		Institutionwriter.close();
	}

 public String clearUsernameandEnterExistinguser(WebDriver base,String role) throws IOException {
       
        String registeredUser = null ;
        
        
        if(role.contains("Org Admin")) {
        	FileReader Classnamefileread = new FileReader(System.getProperty("user.dir") + "\\Resources\\EditAccountOrgadmincredentials.txt");
    		BufferedReader classnmeread = new BufferedReader(Classnamefileread);
    		 registeredUser = classnmeread.readLine();
			
		}
		else if(role.contains("Teacher Admin") && role.contains("Admin"))
		{
			FileReader Classnamefileread = new FileReader(System.getProperty("user.dir") + "\\Resources\\EditAccountTeacherAdmincredentials.txt");
    		BufferedReader classnmeread = new BufferedReader(Classnamefileread);
    		 registeredUser = classnmeread.readLine();
			
			
		}
		else if(role.contains("Teacher") && !role.contains("Admin"))
		{
			FileReader Classnamefileread = new FileReader(System.getProperty("user.dir") + "\\Resources\\EditAccountTeachercredentials.txt");
    		BufferedReader classnmeread = new BufferedReader(Classnamefileread);
    		 registeredUser = classnmeread.readLine();
			
			
		}
		else if(role.contains("learner"))
		{
			FileReader Classnamefileread = new FileReader(System.getProperty("user.dir") + "\\Resources\\EditAccountTeachercredentials.txt");
    		BufferedReader classnmeread = new BufferedReader(Classnamefileread);
    		 registeredUser = classnmeread.readLine();
			
			
		}
		return registeredUser;
		
		
     

 }
 
 @SuppressWarnings("resource")
public String clearUsernameandEnterExistingEACuser(WebDriver base,String role) throws IOException {
     String cred = null;
     String[] split;
     String user = null;
     FileReader Classnamefileread = new FileReader(Constant.Credentials);
     BufferedReader classnmeread = new BufferedReader(Classnamefileread);
     String existedclassname = classnmeread.readLine();
     StringBuffer sb = new StringBuffer();
     while (existedclassname != null) {
         sb.append(existedclassname + "-");
         existedclassname = classnmeread.readLine();
     }
     cred = sb.toString();
     split = cred.split("[-]");
     for (int i = 0; i < split.length; i = i + 2) {
         if (split[i+1].equalsIgnoreCase(role)) {
            user = split[i];
           break;
         }
     }
     return user;

}
 
	public int getNumber(String role) {
		String num = role.replaceAll("[^0-9]", "");
		return Integer.parseInt(num);
	}
	
	
	
	@SuppressWarnings("resource")
	public String getAccessCode() throws IOException {
        String code = null;
        String[] split;
        FileReader Classnamefileread = new FileReader(Constant.AcessCode);
		BufferedReader classnmeread = new BufferedReader(Classnamefileread);
        String existedclassname = classnmeread.readLine();
        code = existedclassname.toString();
        split = code.split("[-]");
        return split[0];
 }
}
