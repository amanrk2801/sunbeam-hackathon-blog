package org.example.test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbUtil {
    private static String DB_DRIVER;
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        try {
            Properties props = new Properties();
            InputStream is = DbUtil.class.getClassLoader().getResourceAsStream("application.properties");
            if (is == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            props.load(is);
            
            DB_DRIVER = props.getProperty("db.driver");
            DB_URL = props.getProperty("db.url");
            DB_USER = props.getProperty("db.user");
            DB_PASSWORD = props.getProperty("db.password");
            
            if (DB_DRIVER == null || DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
                throw new RuntimeException("Missing required database configuration properties");
            }
            
            Class.forName(DB_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Connection getConnection() throws Exception {
        Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        return con;
    }
}
