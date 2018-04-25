package MySql_Maker.Main;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    private static Statement stat;
    private static void createDB(){
        try {
            stat.executeUpdate("create database VAS");
            stat.executeUpdate("use VAS");
            stat.executeUpdate("create table Merchant_Information(" +
                    "Merchant_ID int not null," +
                    "Merchant_Name varchar(128) unique not null," +
                    "Merchant_Passwd char(64) not null," +
                    "LegalPerson_Name varchar(128) not null," +
                    "LegalPerson_Tel varchar(32) not null," +
                    "LegalPerson_Email varchar(128) not null," +
                    "Available_Balance decimal(19,2) not null," +
                    "Frozen_Balance decimal(19,2) not null," +
                    "primary key(Merchant_ID))");
            stat.executeUpdate("create table User_Information(" +
                    "User_ID int not null," +
                    "User_Name varchar(128) unique not null," +
                    "User_Passwd char(64) not null," +
                    "User_RealName varchar(128) not null," +
                    "User_Tel varchar(32) not null," +
                    "User_Email varchar(128) not null," +
                    "User_Identify varchar(64) not null," +
                    "User_Sex tinyint(1) not null," +
                    "User_Birthday date not null," +
                    "User_Merchant int not null," +
                    "If_Frozen boolean not null," +
                    "primary key(User_ID)," +
                    "foreign key (User_Merchant) references Merchant_Information(Merchant_ID))");
            stat.executeUpdate("create table Account(" +
                    "Account_ID int not null," +
                    "Account_Name varchar(128) not null," +
                    "If_Freeze boolean not null," +
                    "Available_Balance decimal(19,2) not null," +
                    "Frozen_Balance decimal(19,2) not null," +
                    "User int not null," +
                    "If_Main boolean not null," +
                    "primary key (Account_ID)," +
                    "foreign key (User) references User_Information(User_ID)," +
                    "Unique(Account_name,User))");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        String Username,Passwd;
        Scanner scan=new Scanner(System.in);
        System.out.print("UserName: ");
        Username=scan.next();
        System.out.print("PassWord: ");
        Passwd=scan.next();
        Connection conn=(new Maker(Username,Passwd)).getConnection();
        try {
            stat = conn.createStatement();
            System.out.print("是否自动删除以下数据库[y/n]：fsp vas");
            if(scan.next().equals("y")) {
                stat.executeUpdate("drop database if exists fsp");
                stat.executeUpdate("drop database if exists vas");
                System.out.println("Clean Finished");
            }else{
                System.out.println("Canceled");
            }
            createDB();
            stat.close();
            conn.close();
            System.out.println("All Finished");
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
    }
}
