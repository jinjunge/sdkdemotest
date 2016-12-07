package com.zhexin.test;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Config {
	private Config(){		
	}
	static XSSFWorkbook xwb = GetDataFromExcel.getFile("config.xlsx");
	

	final static String databaseIP = GetDataFromExcel.getData(xwb, 0, "databaseIP").get(0);
// 已经在MySQL数据库中创建好的数据库。
    final static String databaseName = GetDataFromExcel.getData(xwb, 0, "databaseName").get(0);
//  final static String databaseName1 = "qy_pay";
    final static String databaseName1 = GetDataFromExcel.getData(xwb, 0, "databaseName1").get(0);
//  final static String userName = "root";// MySQL默认的root账户名  
    final static String userName = GetDataFromExcel.getData(xwb, 0, "userName").get(0);
//  final static String password = "";// 默认的root账户密码
    final static String password = GetDataFromExcel.getData(xwb, 0, "password").get(0);

//  final static String phoneNubmer = "+8615008675217"; //拦截号码
    final static String phoneNubmer = GetDataFromExcel.getData(xwb, 0, "phoneNubmer").get(0);
//  final static String interceptContext = "mytest_200";//拦截内容
    final static String interceptContext = GetDataFromExcel.getData(xwb, 0, "interceptContext").get(0);
    final static String interceptionStrategy = "InterceptInfo [message="+interceptContext+", phoneNum="+phoneNubmer; //拦截策略   

    final static String confirmStrategy = "VerifyInfo [code=, length=2, match=mytest_200";//二次确认策略
    
    static String[] strings = new String[ GetDataFromExcel.getData(xwb, 0, "up_sdk_ver_code").size()];
    final static String up_sdk_ver_code[] = GetDataFromExcel.getData(xwb, 0, "up_sdk_ver_code").toArray(strings);  //自更新原包版本
    static String[] strings1 = new String[ GetDataFromExcel.getData(xwb, 0, "new_sdk_ver_code").size()];
    final static String new_sdk_ver_code[] = GetDataFromExcel.getData(xwb, 0, "new_sdk_ver_code").toArray(strings1); //要更新到的版本
    
    final static String test_imsi=GetDataFromExcel.getData(xwb, 0, "test_imsi").get(0);   //被测手机imsi
    
    final static String linuxIP=GetDataFromExcel.getData(xwb, 0, "linuxIP").get(0);
    final static String linuxUN=GetDataFromExcel.getData(xwb, 0, "linuxUN").get(0);
    final static String linuxPWD=GetDataFromExcel.getData(xwb, 0, "linuxPWD").get(0);
    
}
