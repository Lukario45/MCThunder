package net.mcthunder.src;

import java.io.File;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


public class Congif 
{
	private static PropertiesConfiguration conf;
	public static File cnf = new File("/config.yml");
	private int port;
	private String serverName;
	private String worldName;
	private boolean onlineMod;
	
}
