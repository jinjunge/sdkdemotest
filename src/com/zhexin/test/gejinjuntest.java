package com.zhexin.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import android.util.Log;

public class gejinjuntest {
	public static void main(String[] args){
		String oldName = String.format("%s.%s.%s.%s",Config.up_sdk_ver_code[0],Config.up_sdk_ver_code[1],Config.up_sdk_ver_code[2],Config.up_sdk_ver_code[3]);
		String oldCode = String.format("%s0%s0%s00%s",Config.up_sdk_ver_code[0],Config.up_sdk_ver_code[1],Config.up_sdk_ver_code[2],Config.up_sdk_ver_code[3]);
		String newName = String.format("%s.%s.%s.%s",Config.new_sdk_ver_code[0],Config.new_sdk_ver_code[1],Config.new_sdk_ver_code[2],Config.new_sdk_ver_code[3]);
		String newCode = String.format("%s0%s0%s00%s",Config.new_sdk_ver_code[0],Config.new_sdk_ver_code[1],Config.new_sdk_ver_code[2],Config.new_sdk_ver_code[3]);
		String packName = String.format("com.qy.%s", newName);
		String jarName = String.format("PaySDK-%s-UD_yx.jar", newName);
//	    GetInformationFromDB gd=new GetInformationFromDB();
	    String sql= "insert into qy_plugin_self_update(up_sdk_ver_code,new_sdk_ver_code,ver_name_old,ver_name_new,pack_age_name,down_load_url,activity_name,activity_dir,md5,stat) values ('"+oldCode+"','"+newCode+"','"+oldName+"','"+newName+"','"+packName+"',"+"'http://test-qiyi-hangzhou.oss-cn-hangzhou.aliyuncs.com/qiyi/test/sdk/PaySDK-1.2.8.1-UD_yx.jar?Expires=1480748503&OSSAccessKeyId=0i9k6tmj73887j2lpd2t14pd&Signature=tNfV3qAl74Xs4zah107cAJe%2FyO4%3D','"+jarName+"','/data/data/','cd9fc6c850e456e69d5f449c1dc530c4','1')";
//	    int id =gd.insertIntoDB(sql);
//	    System.out.print("id is"+id);
	    
	    try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String databaseName = Config.databaseName1;// 已经在MySQL数据库中创建好的数据库。  
            String userName = Config.userName;// MySQL默认的root账户名  
            String password = Config.password;// 默认的root账户密码为空  
            Connection conn = DriverManager.getConnection(Config.databaseIP + databaseName, userName, password);
            Statement state = conn.createStatement();
//            state.executeUpdate(sql);
            System.out.print("gejinjun"+ "插入数据数为"+state.executeUpdate(sql));
            }catch(Exception e){
            	e.printStackTrace();
            }
	}

}
