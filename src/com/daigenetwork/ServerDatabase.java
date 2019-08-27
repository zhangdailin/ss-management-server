package com.daigenetwork;

import java.sql.*;


public class ServerDatabase {
	private Connection conn = null;
	private PreparedStatement statement = null;

	public void connSQL() {
		String urle = "jdbc:mysql://localhost:3306/sspanel";
		String username = "root";
		String password = "87112215";
		try { 
			Class.forName("com.mysql.jdbc.Driver" );
			conn = DriverManager.getConnection(urle,username, password ); 
			}
		 catch ( ClassNotFoundException cnfex ) {
			 System.err.println(
			 "装载 JDBC/ODBC 驱动程序失败。" );
			 cnfex.printStackTrace(); 
		 } 
		 catch ( SQLException sqlex ) {
			 System.err.println( "无法连接数据库" );
			 sqlex.printStackTrace();
		 }
	}

	public void deconnSQL() {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			System.out.println("关闭数据库问题 ：");
			e.printStackTrace();
		}
	}

	public ResultSet selectSQL(String sql) {
		ResultSet rs = null;
		try {
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public boolean insertSQL(String sql) {
		try {
			statement = conn.prepareStatement(sql);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println("插入数据库时出错：");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("插入时出错：");
			e.printStackTrace();
		}
		return false;
	}
	public boolean deleteSQL(String sql) {
		try {
			statement = conn.prepareStatement(sql);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println("插入数据库时出错：");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("插入时出错：");
			e.printStackTrace();
		}
		return false;
	}
	public boolean updateSQL(String sql) {
		try {
			statement = conn.prepareStatement(sql);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println("插入数据库时出错：");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("插入时出错：");
			e.printStackTrace();
		}
		return false;
	}
	
	public void layoutStyle2(ResultSet rs) {
		System.out.println("-----------------");
		System.out.println("_id" + "\t" + "password");
		System.out.println("-----------------");
		try {
			while (rs.next()) {
				System.out.println(rs.getString("email") + "\t"
						+ rs.getString("passwd") + "\n");
				//"\t" + rs.getInt("age") + "\t"+ rs.getString("work") + "\t" + rs.getString("others") +"\n");
			}
		} catch (SQLException e) {
			System.out.println("显示时数据库出错。");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("显示出错。");
			e.printStackTrace();
		}
	}

	/*public static void main(String args[]) {

		ServerDatabase h = new ServerDatabase();
		h.connSQL();
		String select = "select * from userdata where _id="
				+ "'w'" + " and password="
				+ "'w'" + ";";
		ResultSet resultSet = h.selectSQL(select);
		h.layoutStyle2(resultSet);//调试信息
		String s = "select * from userdata";

		String insert = "insert into userdata(_id,password) " +
				"values('aaron','102938475610')";
		String update = "update userdata set password ='123456789' where _id= 'aaron'";
		String delete = "delete from userdata where _id= 'aaron'";

		if (h.insertSQL(insert) == true) {
			System.out.println("insert successfully");
			ResultSet resultSet = h.selectSQL(s);
			h.layoutStyle2(resultSet);
		}
		if (h.updateSQL(update) == true) {
			System.out.println("update successfully");
			ResultSet resultSet = h.selectSQL(s);	
			h.layoutStyle2(resultSet);
		}
		if (h.insertSQL(delete) == true) {
			System.out.println("delete successfully");
			ResultSet resultSet = h.selectSQL(s);
			h.layoutStyle2(resultSet);
		}
		h.deconnSQL();
	}*/
}

