package com.zhexin.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

public class SelfUpdateTest extends ActivityInstrumentationTestCase2 {
		private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME="com.astep.pay2.MainActivity";
		private static Class<?> launcherActivityClass;
		static{
			try {
				launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		public SelfUpdateTest()
		{
			super(launcherActivityClass);			
		}
		private Solo solo;
		protected void setUp() throws Exception {
			solo = new Solo(getInstrumentation(), getActivity());
		}
		public void tearDown() throws Exception{
	    	solo.finishOpenedActivities();
	    }
		public void testSelfUpdate(){
			//执行插入语句，返回主键 version1.2.8.1
			String oldName = String.format("%s.%s.%s.%s",Config.up_sdk_ver_code[0],Config.up_sdk_ver_code[1],Config.up_sdk_ver_code[2],Config.up_sdk_ver_code[3]);
			String oldCode = String.format("%s0%s0%s00%s",Config.up_sdk_ver_code[0],Config.up_sdk_ver_code[1],Config.up_sdk_ver_code[2],Config.up_sdk_ver_code[3]);
			String newName = String.format("%s.%s.%s.%s",Config.new_sdk_ver_code[0],Config.new_sdk_ver_code[1],Config.new_sdk_ver_code[2],Config.new_sdk_ver_code[3]);
			String newCode = String.format("%s0%s0%s00%s",Config.new_sdk_ver_code[0],Config.new_sdk_ver_code[1],Config.new_sdk_ver_code[2],Config.new_sdk_ver_code[3]);
			String packName = String.format("com.qy.%s", newName);
			String jarName = String.format("PaySDK-%s-UD_yx.jar", newName);
//		    GetInformationFromDB gd=new GetInformationFromDB();
		    String sql= "insert into qy_plugin_self_update(up_sdk_ver_code,new_sdk_ver_code,ver_name_old,ver_name_new,pack_age_name,down_load_url,activity_name,activity_dir,md5,stat) values ('"+oldCode+"','"+newCode+"','"+oldName+"','"+newName+"','"+packName+"',"+"'http://test-qiyi-hangzhou.oss-cn-hangzhou.aliyuncs.com/qiyi/test/sdk/PaySDK-1.2.8.1-UD_yx.jar?Expires=1480748503&OSSAccessKeyId=0i9k6tmj73887j2lpd2t14pd&Signature=tNfV3qAl74Xs4zah107cAJe%2FyO4%3D','"+jarName+"','/data/data/','cd9fc6c850e456e69d5f449c1dc530c4','1')";
//		    int id = gd.insertIntoDB(sql);
	//	    Log.i("gejinjun","id is"+id);
			
			try {
	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	            String databaseName = Config.databaseName1;// 已经在MySQL数据库中创建好的数据库。  
	            String userName = Config.userName;// MySQL默认的root账户名  
	            String password = Config.password;// 默认的root账户密码为空  
	            Connection conn = DriverManager.getConnection(Config.databaseIP + databaseName, userName, password);
	            Statement state = conn.createStatement();
//	            state.executeUpdate(sql);
	            Log.i("gejinjun", "插入数据数为"+state.executeUpdate(sql));
	            conn.close();
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
		    //执行服务端flush脚本
			RemoteExecuteCommand rec=new RemoteExecuteCommand(Config.linuxIP, Config.linuxUN,Config.linuxPWD);
			Log.i("gejinjun",rec.execute("/root/flush"));
			
		    solo.sleep(6000);
		    solo.clickOnButton("初始化");
		    
		    //自更新成功日志
		    String sdkVersion = "sdkversion---" + newName;
			Boolean sign = false;
			String tag = "paylog";
		    //solo.sleep(1000);
			util ut=new util(getActivity());
		    for(int i = 0  ;i < 10 ;i++){
		    	solo.sleep(500);
		    	Log.i("gejinjun", "循环第"+i+"次");
		    	if (ut.existSmsServer(tag, sdkVersion)==true){
		    		sign=true;
		    		break;
		    	}
		    }
			assertTrue("没有自更新成功", sign);
		    solo.sleep(2000);
		}

}
