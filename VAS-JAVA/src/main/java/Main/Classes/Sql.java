package Main.Classes;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Sql {
    protected DruidPooledConnection connectdb(){
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
    protected boolean closedb(DruidPooledConnection dbconnection){
        try{
            dbconnection.close();
            System.out.println("Closed");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    protected int getResult(ResultSet result, String column, DruidPooledConnection connection)throws Exception{
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
    protected int checkAgencyExists(int agency_id) throws Exception{
        String sql="select * from agencyinformation where agencyid=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,agency_id);
        ResultSet result=statement.executeQuery();
        return getResult(result,"agencyID",connection);
    }
    protected int checkAgencyExists(String agency_name) throws Exception{
        String sql="select * from agencyinformation where agencyname=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,agency_name);
        ResultSet result=statement.executeQuery();
        return getResult(result,"agencyID",connection);
    }
    protected int checkUserExists(int user_id) throws Exception{
        String sql="select * from userinformation where userid=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,user_id);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    protected int checkUserExists(String user_name) throws Exception{
        String sql="select * from userinformation where username=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,user_name);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    protected int checkUserAgencyDuplicate(String user_identity,int agency)throws Exception{
        //只检查是否重复，不判断agency的存在性（即外键检查）
        String sql="select * from userinformation where useridentity=? and agency=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,user_identity);
        statement.setInt(2,agency);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    protected int checkUserPasswd(String user_name,String passwd) throws Exception{
        String sql="select * from userinformation where username=? and userpasswd=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,user_name);
        statement.setString(2,passwd);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    protected int checkUserPasswd(int user_id,String passwd) throws Exception{
        String sql="select * from userinformation where userid=? and userpasswd=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setInt(1,user_id);
        statement.setString(2,passwd);
        ResultSet result=statement.executeQuery();
        return getResult(result,"userID",connection);
    }
    protected int checkAgencyPasswd(String agency_name,String passwd) throws Exception{
        String sql="select * from agencyinformation where agencyname=? and agencypasswd=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,agency_name);
        statement.setString(2,passwd);
        ResultSet result=statement.executeQuery();
        return getResult(result,"agencyID",connection);
    }
    protected int checkAgencyPasswd(int agency_id,String passwd) throws Exception{
        String sql="select * from agencyinformation where agencyid=? and agencypasswd=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setInt(1,agency_id);
        statement.setString(2,passwd);
        ResultSet result=statement.executeQuery();
        return getResult(result,"agencyID",connection);
    }
    protected double getUserBalance(int user_id) throws Exception{
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
    protected void userInsert(String user_name,String user_passwd,String user_realname,String user_tel,String user_email,String user_identity,int under_agency_id) throws Exception{
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
    }
    protected void updatePasswd(int user_id,String new_passwd) throws Exception{
        String sql="update userinformation set userpasswd=? where userid=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,new_passwd);
        statement.setInt(2,user_id);
        statement.executeUpdate();
        closedb(connection);
    }
    protected ResultSet getAgencyInformation(int agency_id)throws Exception{
        String sql="select * from agencyinformation where agencyid=?";
        DruidPooledConnection connection= connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setInt(1,agency_id);
        ResultSet result=statement.executeQuery();
        return result;
    }
    protected ResultSet getAgencyUsers(int agency_id)throws Exception{
        String sql="select userid from userinformation where agency=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setInt(1,agency_id);
        ResultSet result=statement.executeQuery();
        return result;
    }
    protected ResultSet getUserInformation(int user_id)throws Exception{
        String sql="select * from userinformation where userid=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setInt(1,user_id);
        ResultSet result=statement.executeQuery();
        return result;
    }
    protected void updateFrozen(int user_id,boolean if_freeze)throws Exception{
        String sql="update userinformation set ifFrozen=? where userid=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setBoolean(1,if_freeze);
        statement.setInt(2,user_id);
        statement.executeUpdate();
        closedb(connection);
    }
    protected ResultSet getUserIdentity(String user_name,String user_identity)throws Exception{
        String sql = "select * from userinformation where username=? and useridentity=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,user_name);
        statement.setString(2,user_identity);
        return statement.executeQuery();
    }
    protected void addBalance(int user_id,double amount)throws Exception{
        String sql="update userinformation set availablebalance=availablebalance+? where userid=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setDouble(1,amount);
        statement.setInt(2,user_id);
        statement.executeUpdate();
        closedb(connection);
    }
    protected void minusBalance(int user_id,double amount)throws Exception{
        String sql="update userinformation set availablebalance=availablebalance-? where userid=?";
        DruidPooledConnection connection=connectdb();
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setDouble(1,amount);
        statement.setInt(2,user_id);
        statement.executeUpdate();
        closedb(connection);
    }
}
