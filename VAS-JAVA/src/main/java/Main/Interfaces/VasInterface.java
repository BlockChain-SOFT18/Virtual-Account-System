package Main.Interfaces;
import java.util.Date;
import java.util.List;
import java.util.Map;
public interface VasInterface {
    int agencyLogin(String agency_name,String agency_passwd);//密码已SHA256
    int userLogin(String user_name,String user_passwd);
    int userRegister(String user_name,String user_passwd);
    boolean userPasswdChanged(int user_id,String old_passwd,String new_passwd);
    List<Map<String,String>> agencyInformation(int agency_id);
    List<Map<Integer,Integer>> agencyAllUser(int agency_id);
    List<Map<String,String>> userInformation(int user_id);
    int freezeUnfreeze(int user_id,boolean if_unfreeze);
    boolean foundPasswd(String user_name,String user_identity,String new_passwd);
    List<Map<Integer,String>> agencyTradeInformation(int agency_id, String start_date, String end_date, int trade_type);
    List<Map<Integer,String>> userTradeInformation(int user_id,String start_date,String end_date,int trade_type);
    boolean transferConsume(int pay_user_id,int get_user_id,double amount,boolean trade_type);
    boolean reCharge(int user_id,double amount,boolean recharge_platform);
    boolean drawMoney(int user_id,double amount,boolean draw_platform);
}
