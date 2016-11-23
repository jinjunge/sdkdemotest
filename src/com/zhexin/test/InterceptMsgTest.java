package com.zhexin.test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.robotium.solo.Solo;

import android.app.Instrumentation;

import android.content.Context;


import android.test.ActivityInstrumentationTestCase2;

public class InterceptMsgTest extends ActivityInstrumentationTestCase2 {
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME="com.astep.pay2.MainActivity";
		
	private static Class<?> launcherActivityClass;
	static{
		try {
			launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public InterceptMsgTest()
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
    	System.out.println(td);
    	try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String databaseName = "qy_pay_log";// 已经在MySQL数据库中创建好的数据库。  
            String userName = "root";// MySQL默认的root账户名  
            String password = "tryme";// 默认的root账户密码为空  
            int rowCount ;
            int rowCount1 ;
            boolean flag =false;
            Connection conn = DriverManager.getConnection("jdbc:mysql://120.26.3.132:3306/" + databaseName, userName, password);    
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE ,ResultSet.CONCUR_READ_ONLY);   
            String sql = "SELECT result,type from qy_bills_info_rec_"+td+" order by create_time desc";
            ResultSet rs=stmt.executeQuery(sql);
            rs.last();                                           //指针指向最后一行
            rowCount = rs.getRow();                              //获取行数
            System.out.println("rowCount is"+rowCount );
            
            //获取支付前短信条数
            util ut=new util(getActivity());   	    
    	    int smsCount1=ut.getSmsCountFromPhone();  	   
    	
		    solo.sleep(6000);
		    solo.clickOnButton("支付4元");
		    
		    String tag="paylog";
			String text = "InterceptInfo [message=mytest_200, phoneNum=+8613767714454";
			//调用existSmsServer获取到text,并和text比较
			assertTrue("没有找到下发的拦截策略", ut.existSmsServer(tag, text));
		    
		    solo.sleep(20000);
				   
         //bills表新增2条数据验证和数据的result和type验证		   
		   ResultSet rs1=stmt.executeQuery(sql);
		   rs1.last();
		   rowCount1 = rs1.getRow();
		   System.out.println("rowCount1 is"+rowCount1);
		   assertTrue("bill表中数据未新增2条",rowCount1-rowCount==2);       
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
		   conn.close();
		   solo.sleep(2000);
		   
		   //短信拦截验证模块
		   util ut1=new util(getActivity());
	       int smsCount2= ut1.getSmsCountFromPhone();           //支付后短信条数
	       String sms1 = ut1.getSmsFromPhone();                 //支付后最新短信
	       
	       assertFalse("短信未拦截",(smsCount1!=smsCount2)&&(sms1.equals("mytest_200")));     //要修改的在这里
	      
		
    	}catch(Exception e){
    		e.printStackTrace();
			assertTrue("数据库连接异常或缺表"+e,false);
    	}
	}

}

