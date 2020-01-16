package com.project.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TaskUrlEndpoint {
	
	private Properties properties;
	
	public TaskUrlEndpoint(String path) throws FileNotFoundException, IOException {
		properties = new Properties();
		properties.load(new FileInputStream(path));
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getValue(String key) {
		return (String) getProperties().get(key);
	}
	
	public String[] getParams(String key) {
		String obj = getValue(key) != null ? getValue(key) : "";
		return obj.split(";");
	}
}
