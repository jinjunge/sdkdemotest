package com.zhexin.test;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class util{
	private Uri SMS_INBOX = Uri.parse("content://sms/");
	private Context context;
	public util(Context context){
		this.context=context;
	}
    public String getSmsFromPhone() {  
        ContentResolver cr = context.getContentResolver();  
        String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type  
        String where = " address = '+8615008675217' ";                             //要修改的2个地方
//                + (System.currentTimeMillis() - 10 * 60 * 1000);  
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        Log.i("gejinjun", "到这里了");
        if (cur == null){
            return null;
        }
        else if (cur.moveToNext()) { 
        	Log.i("gejinjun", "到这里了1");
            //String number = cur.getString(cur.getColumnIndex("address"));//手机号  
            //String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表  
            String body = cur.getString(cur.getColumnIndex("body"));
            Log.i("gejinjun", "到这里了2");
            Log.i("gejinjun",body);
            //这里我是要获取自己短信服务号码中的验证码~~  
            //Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");  
            //Matcher matcher = pattern.matcher(body);  
            //if (matcher.find()) {  
                //String res = matcher.group().substring(1, 11);  
                //mobileText.setText(res);  
            //}
            cur.close();
            return body;
        }else{
        	cur.close();
        	return null;
        }
        
    }
    
    public int getSmsCountFromPhone(){
        ContentResolver cr = context.getContentResolver();  
        String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type  
        String where = " address = '+8615008675217' ";                                     //要修改的在这里
//                + (System.currentTimeMillis() - 10 * 60 * 1000);  
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        Log.i("gejinjun", "到这里了");
        if (cur == null){
            return 0;
        }else{
        	int a=cur.getCount();
        	cur.close();
        	Log.i("gejinjun","短信条数为"+a);
        	return a;
        }
    	
    }
    
	public boolean existSmsServer(String tag, String text) {
		boolean flag = false;
		Process mLogcatProc = null;
		BufferedReader reader = null;
		try {
			// 获取logcat日志信息
			mLogcatProc = Runtime.getRuntime().exec(
					new String[] { "logcat","-v", "time", tag + ":D *:S" });  //"-v time"+ 
			reader = new BufferedReader(new InputStreamReader(
					mLogcatProc.getInputStream()));
			String line;
			int total = reader.read();
			for (int i = 0; i < total && reader.readLine() != null; i++) {
				line = reader.readLine();
				//Log.i("gejinjun","循环第"+i+"次 "+line);
				if (line.indexOf(text) > 0) {
					flag = true;
					// logcat打印信息在这里可以监听到
					// 使用looper 把给界面一个显示
					// Looper.prepare();
					// // Toast.makeText(this.getActivity(), "监听到log信息",
					// // Toast.LENGTH_SHORT).show();
					// Looper.loop();
					break;
				}
				if (i == total - 1) {
					break;
				}
				Log.i("gejinjun",line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

}
