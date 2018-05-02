package Main.Classes;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

import org.apache.commons.collections.map.CaseInsensitiveMap;
public class VirtualAccount implements Main.Interfaces.VasInterface{
    private DruidPooledConnection connectdb(){
        ApplicationContext context=new ClassPathXmlApplicationContext("springconfig.xml");
        DruidDataSource source=(DruidDataSource) context.getBean("dataSource");
        try {
            System.out.println("Connected");
            return source.getConnection();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private boolean closedb(DruidPooledConnection dbconnection){
        try{
            dbconnection.close();
            System.out.println("Closed");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private int getResult(ResultSet result,String column,DruidPooledConnection connection)throws Exception{
        if(result.next()){
            result.first();
            int returnint=result.getInt(column);
            closedb(connection);
            return returnint;
        }else{
            closedb(connection);
            return -1;
        }
    }
    private int checkAgencyExists(int agency_id) throws Exception{
        String sql="select * from agencyinformation where agencyid=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,agency_id);
        ResultSet result=statement.executeQuery();
        return getResult(result,"agencyID",connection);
    }
    private int checkAgencyExists(String agency_name) throws Exception{
        String sql="select * from agencyinformation where agencyname=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,agency_name);
        ResultSet result=statement.executeQuery();
        return getResult(result,"agencyID",connection);
    }
    private int checkUserExists(int user_id) throws Exception{
        String sql="select * from userinformation where userid=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,user_id);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    private int checkUserExists(String user_name) throws Exception{
        String sql="select * from userinformation where username=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,user_name);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    private int checkUserAgencyDuplicate(String user_identity,int agency)throws Exception{
        //只检查是否重复，不判断agency的存在性（即外键检查）
        String sql="select * from userinformation where useridentity=? and agency=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,user_identity);
        statement.setInt(2,agency);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    private int checkUserPasswd(String user_name,String passwd) throws Exception{
        String sql="select * from userinformation where username=? and userpasswd=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,user_name);
        statement.setString(2,passwd);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    private int checkUserPasswd(int user_id,String passwd) throws Exception{
        String sql="select * from userinformation where userid=? and userpasswd=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setInt(1,user_id);
        statement.setString(2,passwd);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    private int checkAgencyPasswd(String agency_name,String passwd) throws Exception{
        String sql="select * from agencyinformation where agencyname=? and agencypasswd=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,agency_name);
        statement.setString(2,passwd);
        ResultSet result=statement.executeQuery();
        return getResult(result,"agencyID",connection);
    }
    private int checkAgencyPasswd(int agency_id,String passwd) throws Exception{
        String sql="select * from agencyinformation where agencyid=? and agencypasswd=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setInt(1,agency_id);
        statement.setString(2,passwd);
        ResultSet result=statement.executeQuery();
        return getResult(result,"agencyID",connection);
    }
    private double getUserBalance(int user_id) throws Exception{
        if(checkUserExists(user_id)==-1){
            return -1;
        }else{
            String sql="select availablebalance from userinformation where userid=?";
            DruidPooledConnection connection=connectdb();
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,user_id);
            ResultSet result=statement.executeQuery();
            result.first();
            double returndata=result.getDouble("availableBalance");
            closedb(connection);
            return returndata;
        }
    }
    public int userLogin(String user_name,String user_passwd)
    {
        try {
            return checkUserPasswd(user_name, user_passwd);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    public int agencyLogin(String agency_name,String agency_passwd)
    {
        try{
            return checkAgencyPasswd(agency_name,agency_passwd);
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    public int userRegister(String user_name,String user_passwd,String user_realname,String user_tel,String user_email,String user_identity,int under_agency_id) {
        try {
            if (checkUserExists(user_name)!=-1){
                //check username private key
                return -1;
            }
            if(checkAgencyExists(under_agency_id)==-1){
                //check agency foreign key
                return -1;
            }
            if(checkUserAgencyDuplicate(user_identity,under_agency_id)!=-1){
                //check Account unique key
                return -1;
            }
            //Nothing Wrong
            String sql="insert into userinformation(username,userpasswd,userrealname,usertel,useremail,useridentity,agency) values(?,?,?,?,?,?,?);";
            DruidPooledConnection connection=connectdb();
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setString(1,user_name);
            statement.setString(2,user_passwd);
            statement.setString(3,user_realname);
            statement.setString(4,user_tel);
            statement.setString(5,user_email);
            statement.setString(6,user_identity);
            statement.setInt(7,under_agency_id);
            statement.executeUpdate();
            closedb(connection);
            return checkUserExists(user_name);
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    public int userRegister(String user_name,String user_passwd,String user_realname,String user_tel,String user_email,String user_identity,String under_agency_name)
    {
        try {
            int agency_id = checkAgencyExists(under_agency_name);
            if(agency_id==-1){
                //notExists
                return -1;
            }else{
                return userRegister(user_name,user_passwd,user_realname,user_tel,user_email,user_identity,agency_id);
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    public boolean userPasswdChanging(int user_id,String old_passwd,String new_passwd)
    {
        try {
            if (checkUserPasswd(user_id, old_passwd) == -1) {
                //not Exists
                return false;
            }
            String sql="update userinformation set userpasswd=? where userid=?";
            DruidPooledConnection connection=connectdb();
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setString(1,new_passwd);
            statement.setInt(2,user_id);
            statement.executeUpdate();
            closedb(connection);
            if(checkUserPasswd(user_id,new_passwd)!=-1){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Map<String,String>> agencyInformation(int agency_id)
    {
        try{
            if(checkAgencyExists(agency_id)==-1){
                return null;
            }else{
                String sql="select * from agencyinformation where agencyid=?";
                DruidPooledConnection connection= connectdb();
                PreparedStatement statement=connection.prepareStatement(sql);
                statement.setInt(1,agency_id);
                ResultSet result=statement.executeQuery();
                //
                assert(result.next());
                assert(!result.next());
                result.first();
                ResultSetMetaData metadata=result.getMetaData();
                Map<String,String> temp_map=new CaseInsensitiveMap();
                List<Map<String,String>> return_list=new ArrayList<Map<String, String>>();
                return_list.clear();
                temp_map.clear();
                for(int i=1;i<=metadata.getColumnCount();i++){
                    //从1开始是不是很SB
                    if(!metadata.getColumnName(i).toString().equalsIgnoreCase("agencyPasswd")) {
                        temp_map.put(metadata.getColumnName(i).toString(), result.getObject(i).toString());
                    }
                }
                return_list.add(temp_map);
                closedb(connection);
                return return_list;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public List<Map<Integer,Integer>> agencyAllUser(int agency_id)
    {
        int i=0;
        List<Map<Integer,Integer>> list=new ArrayList<Map<Integer, Integer>>();
        list.clear();
        try{
            if(checkAgencyExists(agency_id)==-1){
                return null;
            }
            String sql="select userid from userinformation where agency=?";
            DruidPooledConnection connection=connectdb();
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,agency_id);
            ResultSet result=statement.executeQuery();
            if(!result.next()) return null;
            result.beforeFirst();
            while(result.next()){
                Map<Integer,Integer> map=new CaseInsensitiveMap();
                map.clear();
                map.put(i,Integer.parseInt(result.getObject(1).toString()));
                list.add(map);
                i++;
            }
            closedb(connection);
            return list;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public List<Map<String,String>> userInformation(int user_id)
    {
        Map<String,String> map=new CaseInsensitiveMap();
        map.clear();
        List<Map<String,String>> list=new ArrayList<Map<String, String>>();
        list.clear();
        try{
            if(checkUserExists(user_id)==-1) return null;
            String sql="select * from userinformation where userid=?";
            DruidPooledConnection connection=connectdb();
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,user_id);
            ResultSet result=statement.executeQuery();
            //
            assert(result.next());
            assert(!result.next());
            result.first();
            //
            ResultSetMetaData metaData=result.getMetaData();
            for(int i=1;i<=metaData.getColumnCount();i++){
                if(metaData.getColumnName(i).equalsIgnoreCase("userpasswd")) continue;
                map.put(metaData.getColumnName(i),result.getObject(i).toString());
            }
            list.add(map);
            closedb(connection);
            return list;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public int freezeUnfreeze(int user_id,boolean if_freeze)
    {
        try {
            if (checkUserExists(user_id) == -1) {
                return 0;
            }
            String sql="update userinformation set ifFrozen=? where userid=?";
            DruidPooledConnection connection=connectdb();
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setBoolean(1,if_freeze);
            statement.setInt(2,user_id);
            statement.executeUpdate();
            closedb(connection);
            if(Boolean.parseBoolean(userInformation(user_id).get(0).get("iffrozen"))){
                return 1;
            }else{
                return 2;
            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    public boolean foundPasswd(String user_name,String user_identity,String new_passwd)
    {
        try {
            String sql = "select * from userinformation where username=? and useridentity=?";
            DruidPooledConnection connection=connectdb();
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setString(1,user_name);
            statement.setString(2,user_identity);
            if(statement.executeQuery().next()){
                statement.close();
                sql="update userinformation set userpasswd=? where username=?";
                statement=connection.prepareStatement(sql);
                statement.setString(1,new_passwd);
                statement.setString(2,user_name);
                statement.executeUpdate();
                if(checkUserPasswd(user_name,new_passwd)!=-1)
                    return true;
                else
                    return false;
            }else{
                closedb(connection);
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Map<Integer,String>> agencyTradeInformation(int agency_id, String start_date, String end_date, int trade_type)
    {
        //等区块链
        return null;
    }
    public List<Map<Integer,String>> userTradeInformation(int user_id,String start_date,String end_date,int trade_type)
    {
        //等区块链
        return null;
    }
    public boolean transferConsume(int pay_user_id,int get_user_id,double amount,boolean trade_type)
    {
        //等一大堆接口
        //并且没太看懂
        return false;
    }
    public boolean reCharge(int user_id,double amount,boolean recharge_platform)
    {
        if(amount<0) return false;
        try {
            if (checkUserExists(user_id) == -1) return false;
            String sql="update userinformation set availablebalance=availablebalance+? where userid=?";
            DruidPooledConnection connection=connectdb();
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setDouble(1,amount);
            statement.setInt(2,user_id);
            statement.executeUpdate();
            //等接口
            closedb(connection);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean drawMoney(int user_id,double amount,boolean draw_platform)
    {
        if(amount<0) return false;
        try{
            if(checkUserExists(user_id)==-1) return false;
            if(getUserBalance(user_id)<amount) return false;
            String sql="update userinformation set availablebalance=availablebalance-? where userid=?";
            DruidPooledConnection connection=connectdb();
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setDouble(1,amount);
            statement.setInt(2,user_id);
            statement.executeUpdate();
            //等接口
            closedb(connection);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Test
    public void test_agencyLogin(){
        System.out.println(agencyLogin("a","961b6dd3ede3cb8ecbaacbd68de040cd78eb2ed5889130cceb4c49268ea4d506"));
    }
    @Test
    public void test_userLogin(){
        System.out.println(userLogin("a","961b6dd3ede3cb8ecbaacbd68de040cd78eb2ed5889130cceb4c49268ea4d506"));
    }
    @Test
    public void test_userRegister(){
        System.out.println(userRegister("bb","961b6dd3ede3cb8ecbaacbd68de040cd78eb2ed5889130cceb4c49268ea4d506","LiHang","1111111","example@example.example","jafsahfkjshf",1111));
    }
    @Test
    public void test_userRegister_agencyName(){
        System.out.println(userRegister("cc","961b6dd3ede3cb8ecbaacbd68de040cd78eb2ed5889130cceb4c49268ea4d506","LiHang","1111111","example@example.example","gsfdg",1111));
    }
    @Test
    public void test_getAgencyInformation(){
        System.out.println(agencyInformation(1111).get(0).toString());
    }
    @Test
    public void test_allUserAgency(){
        System.out.println(agencyAllUser(1111).toString());
    }
    @Test
    public void test_userInformation(){
        System.out.println(userInformation(1245).toString());
    }
    @Test
    public void test_freeze(){
        System.out.println(freezeUnfreeze(1246,true));
    }
    @Test
    public void test_foundPasswd(){
        System.out.println(foundPasswd("a","dsa","e4fa7ccc32184b2ac0f1b3cf1f46aa7b34f90f61ae425a7d6507038eaf1ab38f"));
    }
    @Test
    public void test_DrawMoney(){
        System.out.println(drawMoney(1245,45,true));
    }
}
