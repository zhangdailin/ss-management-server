package com.daigenetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyServer {
	public static void main(String[] args){
		try{
			@SuppressWarnings("resource")
			ServerSocket server=new ServerSocket(6666);
			while(true){
				System.out.println("服务器回归原点");//调试信息
				Socket s1 = server.accept();
				System.out.println("接到客户端socket请求");//调试信息
				InputStream is1 = s1.getInputStream();
				DataInputStream dis1=new DataInputStream(is1);
				String []getStr = dis1.readUTF().split(" ");;//用空格把账号和密码分开存储
				
				if(getStr[2].equals("Register")){
					System.out.println("等待下一步指令");
					Socket s2 = server.accept();
					InputStream is2 = s2.getInputStream();
					DataInputStream dis2 = new DataInputStream(is2);
					String []getStrSecond = dis2.readUTF().split(" ");
					System.out.println(getStrSecond[0] + " " + getStrSecond[1]
							+ " " + getStrSecond[2]);// 调试信息
					if(getStrSecond[2].equals("Login")){
						login(s2,getStrSecond);
					}
					else if(getStrSecond[2].equals("Register")){

					}
					else if(getStrSecond[2].equals("Registered")){
						insertMasterDB(getStrSecond);
					}
					is2.close();
					s2.close();
					dis2.close();
				}
				else if(getStr[2].equals("Login")){
					System.out.println("进入登陆判断");//调试信息
					login(s1,getStr);
				}
				else if(getStr[2].equals("Line")){
					Line(s1,getStr);
				}
				else if(getStr[2].equals("Registered")){
					insertMasterDB(getStr);
				}
				dis1.close();
				s1.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}	
	}
	
	public static void Line(Socket s1,String []getStr) throws IOException{
		ServerDatabase masterDB = new ServerDatabase();
		masterDB.connSQL();
		String select = "select port,passwd,method,protocol,obfs,protocol_param,obfs_param from user where email = '" + getStr[0]
				+ "' and passwd = '" + getStr[1] + "';";
		ResultSet r=masterDB.selectSQL(select);
		try {
			while (r.next()){
			    int i=r.getInt(1);
			    String a=Integer.toString(i);
			    String b=r.getString(2);
			    String c=r.getString(3);
			    String d=r.getString(4);
			    String e=r.getString(5);
			    String f=r.getString(6);
			    String g=r.getString(7);
			    System.out.println(a+" "+b+" "+c+" "+d+" "+e+" "+f+" "+g);
			    OutputStream os=s1.getOutputStream();
				DataOutputStream dos=new DataOutputStream(os);
				dos.writeUTF(a+" "+b+" "+c+" "+d+" "+e+" "+f+" "+g+" "+"Login");
				dos.close();
				System.out.println("数据已传至客户端");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		masterDB.deconnSQL();//关闭连接
	}
	public static void login(Socket s1,String []getStr){
		ServerDatabase masterDB = new ServerDatabase();
		masterDB.connSQL();
		String select = "select * from user where email = '" + getStr[0]
				+ "' and passwd = '" + getStr[1] + "';";
		ResultSet resultSet = masterDB.selectSQL(select);
		try {
			if (resultSet.next() == false) {
				OutputStream os=s1.getOutputStream();
				DataOutputStream dos=new DataOutputStream(os);
				dos.writeUTF("NO");
				dos.close();
				System.out.println("用户密码错误");//调试信息
			}
			else {
				OutputStream os=s1.getOutputStream();
				DataOutputStream dos=new DataOutputStream(os);
				dos.writeUTF("YES");
				dos.close();
				System.out.println("用户密码正确");//调试信息
			}
		} catch (Exception e) {
			System.out.println("显示出错。");
			e.printStackTrace();
		}
		masterDB.deconnSQL();// 关闭连接
	}
	
	public static void insertMasterDB(String []getStr){
		ServerDatabase masterDB = new ServerDatabase();
		masterDB.connSQL();
		String s = "select * from user";//调试信息
		String insert = "insert into user(email,passwd) " +
				"values('"+getStr[0]+"','"+getStr[1]+"')";
		if (masterDB.insertSQL(insert) == true) {
			System.out.println("insert successfully");
			ResultSet resultSet = masterDB.selectSQL(s);//调试信息
			masterDB.layoutStyle2(resultSet);//调试信息
		}
		masterDB.deconnSQL();//关闭连接
	}
}


