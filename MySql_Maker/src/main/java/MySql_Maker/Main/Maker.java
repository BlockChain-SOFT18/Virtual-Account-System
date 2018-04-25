package MySql_Maker.Main;
import java.sql.*;
import com.alibaba.druid.pool.DruidDataSource;
public class Maker {
    private DruidDataSource DB=new DruidDataSource();
    public Maker(String User, String Passwd){
        DB.setUrl("jdbc:mysql://localhost:3306/mysql?characterEncoding=utf8&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        DB.setUsername(User);
        DB.setPassword(Passwd);
        DB.setDriverClassName("com.mysql.jdbc.Driver");
        DB.setPoolPreparedStatements(false);
    }
    public Connection getConnection(){
        Connection connection;
        try{
            synchronized (DB){
                connection=DB.getConnection();
            }
        }catch(Exception e){
            connection=null;
            e.printStackTrace();
        }
        return connection;
    }
}
