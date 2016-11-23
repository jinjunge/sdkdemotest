package com.zhexin.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.robotium.solo.Solo;

public class DoubleConfirmationTest extends  ActivityInstrumentationTestCase2{
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME="com.astep.pay2.MainActivity";
	
	private static Class<?> launcherActivityClass;
	static{
		try {
			launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public DoubleConfirmationTest()
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
	public void testDoubleConfirmation(){
		//VerifyInfo [code=, length=2, match=mytest_200, phoneNum=+8613767714454
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
            boolean flag =false;
            Connection conn = DriverManager.getConnection("jdbc:mysql://120.26.3.132:3306/" + databaseName, userName, password);    
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE ,ResultSet.CONCUR_READ_ONLY);   
            String sql = "SELECT result,type from qy_bills_info_rec_"+td+" order by create_time desc";
            ResultSet rs=stmt.executeQuery(sql);
            rs.last();                                           //指针指向最后一行
            rowCount = rs.getRow();                              //获取行数
            System.out.println("rowCount is"+rowCount );
            
    	   

    	
		    solo.sleep(6000);
		    solo.clickOnButton("支付4元");
		    
		  //二次确认策略下发验证 
		    String tag="paylog";
			String text = "VerifyInfo [code=, length=2, match=mytest_200";
			util ut=new util(getActivity());
			Boolean sign = false;
		    //solo.sleep(1000);
		    for(int i = 0  ;i < 20 ;i++){
		    	solo.sleep(500);
		    	if (ut.existSmsServer(tag, text)==true){
		    		sign=true;
		    		break;
		    	}
		    }
			assertTrue("没有找到下发的二次确认策略", sign);
		    
			

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

		    
         //bills表新增2条数据验证和数据的result和type验证		   
		   ResultSet rs1=stmt.executeQuery(sql);
		   rs1.last();
		   rowCount1 = rs1.getRow();
		   Log.i("gejinjun","rowCount1 is"+rowCount1);
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
		
    	}catch(Exception e){
    		e.printStackTrace();
			assertTrue("数据库连接异常或缺表"+e,false);
    	}
	}

}
