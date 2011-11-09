package com.teama.pds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * User: Ryan Brown
 * Date: 10/19/11
 * Time: 12:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigManager {
	private String filename;
	private TimeReader tr;
	private StoreConfiguration storeConfiguration;

	public ConfigManager(TimeReader tr) {
		this.tr = tr;
		storeConfiguration = new StoreConfiguration(tr);
	}

	public void writeConfig(String filename, StoreConfiguration configuration) {
		configuration.build();
		Gson gson = new GsonBuilder().create();
		String mahconf = gson.toJson(configuration);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filename));
			out.write(mahconf);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public StoreConfiguration readConfig(String filename) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		StringBuffer fileData = new StringBuffer(1000);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		char[] buf = new char[1024];
		int numRead;

		try {
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		storeConfiguration = gson.fromJson(fileData.toString(), StoreConfiguration.class);
		storeConfiguration.rebuild(tr);
		return storeConfiguration;
	}

	public StoreConfiguration getStoreConfiguration() {
		return storeConfiguration;
	}

	public void setStoreConfiguration(StoreConfiguration storeConfiguration) {
		this.storeConfiguration = storeConfiguration;
	}
}
