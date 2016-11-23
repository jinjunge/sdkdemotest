package com.zhexin.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;
public class SdkDemoTest extends ActivityInstrumentationTestCase2 {
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME="com.astep.pay2.MainActivity";
	private static Class<?> launcherActivityClass;
	static{
		try {
			launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public SdkDemoTest()
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
	public void testSdkDemoTest(){
		solo.sleep(3000);
		Date now=new Date();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    	String td=dateFormat.format(now);
    	Log.i("gejinjun",td);
    	try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String databaseName = "qy_pay_log";// 已经在MySQL数据库中创建好的数据库。  
            String userName = "root";// MySQL默认的root账户名  
            String password = "tryme";// 默认的root账户密码为空  
            int rowCount ;
            int rowCount1 ;
            int rowCount2;
            int rowCount3;
            boolean flag =false;
            Connection conn = DriverManager.getConnection("jdbc:mysql://120.26.3.132:3306/" + databaseName, userName, password);    
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE ,ResultSet.CONCUR_READ_ONLY);   
            String sql_bills = "SELECT result,type from qy_bills_info_rec_"+td+" order by create_time desc";
            String sql_network="SELECT result_code from qy_network_conn_pay_rec_"+td+" order by create_time desc";
            ResultSet rs=stmt.executeQuery(sql_bills);            
            rs.last();                                           //指针指向最后一行
            rowCount = rs.getRow();                              //获取行数
            Log.i("gejinjun","rowCount is"+rowCount);
            
            ResultSet rs2=stmt.executeQuery(sql_network);
            rs2.last();
            rowCount2 = rs2.getRow();
            Log.i("gejinjun","rowCount2 is"+rowCount2);
    	   

    	
		    solo.sleep(6000);
		    solo.clickOnButton("支付4元");
		    solo.sleep(2000);

		    //toast支付成功验证
		    String toast=null;
		    for(int t=1;t<11;t++){
		    	solo.sleep(1000);
		        try{
		    	TextView view =(TextView)solo.getView("message");                     //获取toast，强制转换成textview
		    	toast=view.getText().toString();
		    	Log.i("gejinjun","toast is"+view.getText());
		        }catch(junit.framework.AssertionFailedError e){
		        	e.printStackTrace();
		        	break;
		        }
		    }
		    assertTrue(toast.equals("支付成功"));                                //toast显示支付成功验证
		 
		    solo.sleep(15000);

	
		
		//调用sendUIAutomatorRequestWithParams方法获得“允许”按钮并点击
//		utils.sendUIAutomatorRequestWithParams("AlertAllow", "clickButton");
    	//solo.clickOnScreen(125, 643);
    	//solo.clickOnScreen(125, 643);
//		Spoon.screenshot(getActivity(), "hjb_snap_01");
//		Spoon.screenshot(getActivity(), "hjb_snap_02");
//		String tag = "paylog";
//		String text = "-------SmsReceiver--2-----sdkversion---1.2.4.6 ";
//		//调用existSmsServer获取到text,并和text比较
//		assertTrue(text, existSmsServer(tag, text));
//		if(existButton("GameOver")){
//		}
		   
		   
         //bills表新增2条数据验证和数据的result和type验证		   
		   ResultSet rs1=stmt.executeQuery(sql_bills);
		   rs1.last();
		   rowCount1 = rs1.getRow();
		   Log.i("gejinjun","rowCount1 is"+rowCount1);
		   
		   ResultSet rs3=stmt.executeQuery(sql_network);
		   rs3.last();
		   rowCount3 = rs3.getRow();
		   Log.i("gejinjun","rowCount1 is"+rowCount1);
		   
		   assertTrue("bills表中数据未新增2条",rowCount1-rowCount==2);
		   assertTrue("newtwork表中数据未新增1条",rowCount3-rowCount2==1);
		   rs1.first();
		   int r1 = Integer.valueOf(rs1.getString(1)).intValue();
		   int t1 = Integer.valueOf(rs1.getString(2)).intValue();
		   rs1.next();
		   int r2 = Integer.valueOf(rs1.getString(1)).intValue();
		   int t2 = Integer.valueOf(rs1.getString(2)).intValue();
		   assertTrue("数据库表中result不正确",r1==0);
		   assertTrue("数据库表中result不正确",r2==0);
		   if(t1==1&&t2==2){
			  flag = true;
		   }
	       if(t1==2&&t2==1){
			  flag = true;
		   }
		   assertTrue("数据库表中result或type状态不正确",flag);
		   
		   rs3.first();
		   int result_code= Integer.valueOf(rs3.getString(1)).intValue();
		   assertTrue("network表result_code不正确",result_code==0);
		   conn.close();
		 
		
    	}catch(Exception e){
    		e.printStackTrace();
			assertTrue("数据库连接异常或缺表"+e,false);
    	}
	}

}
