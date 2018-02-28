package com.svnnotification.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * read/write/modify/delete config
 * @author Natsume
 * @date 2018-01-05
 * @e-mail cug_zg@163.com
 */
public class ConfigUtil {
    // file path
    static String profilepath = "config.properties";
    private static Properties properties = new Properties();
    private static final ClassLoader CLASS_LOADER = ConfigUtil.class.getClassLoader();
    private static final String PATH = CLASS_LOADER.getResource("").getPath() + profilepath;
    static {
        try {
            properties.load(CLASS_LOADER.getResourceAsStream(URLDecoder.decode(PATH, "utf-8")));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * get property
     * @param key
     * @return
     */
    public static String getStringValue(String key) {
        return properties.getProperty(key);
    }

    /**
     * convert String 2 Integer
     * @param key
     * @return
     */
    public static Integer getIntValue(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    /**
     * convert String 2 Long
     * @param key
     * @return
     */
    public static long getLongValue(String key) {
        return Long.parseLong(properties.getProperty(key));
    }

    /**
     * write properties
     * 
     * @param keyname
     *            
     * @param keyvalue
     *           
     */
    public static int writeProperties(String keyname, String keyvalue) {
        int w = 1;
        try {
            OutputStream fos = new FileOutputStream(URLDecoder.decode(PATH, "utf-8"));
            properties.setProperty(keyname, keyvalue);
            properties.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            w = 0;
            System.err.println("properties error");
        }
        return w;
    }

    /**
     * update properties
     * 
     * @param keyname
     *            
     * @param keyvalue
     *           
     */
    public static int updateProperties(String keyname, String keyvalue) {
        int u = 1;
        try {
            OutputStream fos = new FileOutputStream(URLDecoder.decode(PATH, "utf-8"));
            properties.setProperty(keyname, keyvalue);
            properties.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            u = 0;
            System.err.println("properties error");
        }
        return u;
    }

    /**
     * delete properties
     * 
     * @param keyname
     */
    public static int deleteProperties(String keyname) {
        int d = 1;
        try {
            OutputStream fos = new FileOutputStream(URLDecoder.decode(PATH, "utf-8"));
            properties.remove(keyname);
            properties.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            d = 0;
            System.err.println("properties error");
        }
        return d;
    }

}
