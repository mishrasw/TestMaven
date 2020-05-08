package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class readConfig {

	public String getPropertyValue(String propertyName) {
		Properties prop = new Properties();
		String fileName = "config.properties";
		InputStream inpStrm = null;
		try {
			inpStrm = new FileInputStream(fileName);
			prop.load(inpStrm);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		return prop.getProperty(propertyName);
		
	}
	
}
