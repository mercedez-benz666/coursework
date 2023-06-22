package ru.sfedu.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigUtils {
    private static String defaultConfigPath = "src/main/resources/API.properties";
    private static final String str = System.getProperty("Nam");
    private static final Properties configuration = new Properties();

    public ConfigUtils(){
        if (str != null){
            defaultConfigPath=str;
        }
    }

    private static Properties getConfiguration() throws IOException {
        if (configuration.isEmpty()) {
            loadConfiguration();
        }
        return configuration;
    }

    private static void loadConfiguration() throws IOException {
        FileInputStream in = new FileInputStream(defaultConfigPath);
        try {
            configuration.load(in);
        } catch (IOException ex) {
            throw new IOException(ex);
        } finally {
            in.close();
        }
    }

    public static String getConfigurationEntry(String key) throws IOException {
        return getConfiguration().getProperty(key);
    }
}