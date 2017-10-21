package com.example.asuspc.whosout;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by AsusPc on 1.10.2017.
 */

public class DatabaseHelper {
    private final String DB_URL = "jdbc:mysql://35.195.221.163/whosoutdb";

    private final String username = "root";
    private final String password = "123456";

    private Connection connect;
    private Statement state;

    public DatabaseHelper(){
        connect = null;
        state = null;
    }

    public void connectDatabase(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error in JDBC creation");
        }
        try{
            connect = DriverManager.getConnection(DB_URL, username, password);
            state = connect.createStatement();

            state.executeUpdate("USE whosoutdb");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getImageNotSeen(){
        File root = new File(Environment.getExternalStorageDirectory(), "Images_Whosout");

        try {
            String sql = "SELECT * FROM Photo WHERE flag='1';";
            PreparedStatement stmt = connect.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            int index=0;
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                String description = resultSet.getString(2);
                if (!root.exists()) {
                    root.mkdirs(); // this will create folder.
                }
                root.mkdirs();
                File image = new File(root,"image_test"+(++index)+".jpg");
                FileOutputStream fos = new FileOutputStream(image);

                byte[] buffer = new byte[1];
                InputStream is = resultSet.getBinaryStream(3);
                while (is.read(buffer) > 0) {
                    fos.write(buffer);
                }
                fos.close();
            }
        }
        catch (Exception e){}
        /*set the flag zero nd the next message old images not shown
        try{
            String sql = "UPDATE Photo SET flag='0' WHERE flag='1';";
            PreparedStatement stmt = connect.prepareStatement(sql);
            stmt.executeUpdate(sql);
        }
        catch (Exception e){}
        */
    }

    public void getImageAll(){
        File root = new File(Environment.getExternalStorageDirectory(), "Images_Whosout");

        try {
            String sql = "SELECT * FROM Photo WHERE flag='1';";
            PreparedStatement stmt = connect.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            int index=0;
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                String description = resultSet.getString(2);
                if (!root.exists()) {
                    root.mkdirs(); // this will create folder.
                }
                root.mkdirs();
                File image = new File(root,"image_test"+(++index)+".jpg");
                FileOutputStream fos = new FileOutputStream(image);

                byte[] buffer = new byte[1];
                InputStream is = resultSet.getBinaryStream(3);
                while (is.read(buffer) > 0) {
                    fos.write(buffer);
                }
                fos.close();
            }
        }
        catch (Exception e){}
    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                //    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }
}
