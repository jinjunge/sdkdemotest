package com.zhexin.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 自增主键主键插入值后获取自增ID
 * @param sql
 * @return
 */
public class GetInformationFromDB {
	/**
	 * 自增主键主键插入值后获取自增ID
	 * @param sql
	 * @return
	 */
	public int insertIntoDB(String sql){
		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		int key = -1;
		try{
			conn = DriverManager.getConnection(Config.databaseIP+"/qy_pay",Config.userName, Config.password);
			state = conn.createStatement();
			state.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			rs = state.getGeneratedKeys();
			if(rs.next()){
				key = rs.getInt(1);
			}
			return key;
		}catch (SQLException e) {
			e.printStackTrace();
			return key;
		}finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(state != null){
					state.close();
					state = null;
				}
				if(conn != null){
					conn.close();
					conn = null;
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
