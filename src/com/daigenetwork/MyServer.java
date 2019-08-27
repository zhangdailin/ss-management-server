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
				System.out.println("�������ع�ԭ��");//������Ϣ
				Socket s1 = server.accept();
				System.out.println("�ӵ��ͻ���socket����");//������Ϣ
				InputStream is1 = s1.getInputStream();
				DataInputStream dis1=new DataInputStream(is1);
				String []getStr = dis1.readUTF().split(" ");;//�ÿո���˺ź�����ֿ��洢
				
				if(getStr[2].equals("Register")){
					System.out.println("�ȴ���һ��ָ��");
					Socket s2 = server.accept();
					InputStream is2 = s2.getInputStream();
					DataInputStream dis2 = new DataInputStream(is2);
					String []getStrSecond = dis2.readUTF().split(" ");
					System.out.println(getStrSecond[0] + " " + getStrSecond[1]
							+ " " + getStrSecond[2]);// ������Ϣ
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
					System.out.println("�����½�ж�");//������Ϣ
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
				System.out.println("�����Ѵ����ͻ���");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		masterDB.deconnSQL();//�ر�����
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
				System.out.println("�û��������");//������Ϣ
			}
			else {
				OutputStream os=s1.getOutputStream();
				DataOutputStream dos=new DataOutputStream(os);
				dos.writeUTF("YES");
				dos.close();
				System.out.println("�û�������ȷ");//������Ϣ
			}
		} catch (Exception e) {
			System.out.println("��ʾ����");
			e.printStackTrace();
		}
		masterDB.deconnSQL();// �ر�����
	}
	
	public static void insertMasterDB(String []getStr){
		ServerDatabase masterDB = new ServerDatabase();
		masterDB.connSQL();
		String s = "select * from user";//������Ϣ
		String insert = "insert into user(email,passwd) " +
				"values('"+getStr[0]+"','"+getStr[1]+"')";
		if (masterDB.insertSQL(insert) == true) {
			System.out.println("insert successfully");
			ResultSet resultSet = masterDB.selectSQL(s);//������Ϣ
			masterDB.layoutStyle2(resultSet);//������Ϣ
		}
		masterDB.deconnSQL();//�ر�����
	}
}


