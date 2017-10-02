package com.example.asuspc.whosout;


import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void db_add_image() throws Exception {
        final String DB_URL = "jdbc:mysql://35.195.221.163/whosoutdb";

        final String username = "root";
        final String password = "123456";

        Connection connect = null;
        Statement state = null;

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error in JDBC creation");
        }

        try {
            connect = DriverManager.getConnection(DB_URL, username, password);
            state = connect.createStatement();
            File imgfile = new File("C:\\Users\\AsusPc\\Desktop\\yonca\\ora1.jpg");
            FileInputStream fin = new FileInputStream(imgfile);
            PreparedStatement pre = connect.prepareStatement("insert into Photo(name, image, date, flag, description) values(?,?,?,?,?)");
            //pre.setString(1, "img");
            pre.setString(1, "test_name");
            pre.setBinaryStream(2, fin, (int) imgfile.length());
            pre.setString(3, "01-10-2017");
            pre.setInt(4, 1);
            pre.setString(5, "Hello world");
            pre.executeUpdate();
            pre.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}