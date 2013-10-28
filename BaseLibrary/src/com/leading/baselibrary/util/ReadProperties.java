package com.leading.baselibrary.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

    private static ReadProperties READ_PROPEETIES;// 单例模式

    private static Properties PROPERTIES;

    public static String FILE_PATH = "";

    static synchronized public ReadProperties getInstance() {
        if (READ_PROPEETIES == null)
            READ_PROPEETIES = new ReadProperties();
        return READ_PROPEETIES;
    }

    // 私有化构造方法
    public ReadProperties() {
        try {
            init();
        } catch (FileNotFoundException e) {
            System.err.println("配置文件config.properties找不到！");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("读取配置文件config.properties错误！");
            e.printStackTrace();
        }

    }

    // 私有化实际启动方法
    private void init() throws FileNotFoundException, IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                FILE_PATH);
        PROPERTIES = new Properties();
        PROPERTIES.load(in);
        in.close();
    }

    public String getValueString(String key) {
        return PROPERTIES.getProperty(key);
    }

    public Boolean getValueBoolean(String key) {
        try {
            return Boolean.parseBoolean(PROPERTIES.getProperty(key));
        } catch (Exception e) {
            return null;
        }
    }

    public Double getValueDouble(String key) {
        try {
            return Double.parseDouble(PROPERTIES.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public Integer getValueInteger(String key) {
        try {
            return Integer.parseInt(PROPERTIES.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Long getValueLong(String key) {
        try {
            return Long.parseLong(PROPERTIES.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Byte getValueByte(String key) {
        try {
            return Byte.parseByte(PROPERTIES.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Float getValueFloat(String key) {
        try {
            return Float.parseFloat(PROPERTIES.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public char getValueChar(String key) {
        try {
            return PROPERTIES.getProperty(key).charAt(0);
        } catch (Exception e) {
            return '\u0000';
        }
    }

    public Short getValueShort(String key) {
        try {
            return Short.parseShort(PROPERTIES.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        // InputStream in
        // =Thread.currentThread().getContextClassLoader().getResource("");
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
    }
}
