package Main.Classes;

import java.util.List;
import java.util.Map;
public class VirtualAccount implements Main.Interfaces.VasInterface {
    public int agencyLogin(String agency_name,String agency_passwd)//密码已SHA256
    {
        return 0;
    }
    public int userLogin(String user_name,String user_passwd)
    {
        return 0;
    }
    public int userRegister(String user_name,String user_passwd)
    {
        return 0;
    }
    public boolean userPasswdChanged(int user_id,String old_passwd,String new_passwd)
    {
        return false;
    }
    public List<Map<String,String>> agencyInformation(int agency_id)
    {
        return null;
    }
    public List<Map<Integer,Integer>> agencyAllUser(int agency_id)
    {
        return null;
    }
    public List<Map<String,String>> userInformation(int user_id)
    {
        return null;
    }
    public int freezeUnfreeze(int user_id,boolean if_unfreeze)
    {
        return 0;
    }
    public boolean foundPasswd(String user_name,String user_identity,String new_passwd)
    {
        return false;
    }
    public List<Map<Integer,String>> agencyTradeInformation(int agency_id, String start_date, String end_date, int trade_type)
    {
        return null;
    }
    public List<Map<Integer,String>> userTradeInformation(int user_id,String start_date,String end_date,int trade_type)
    {
        return null;
    }
    public boolean transferConsume(int pay_user_id,int get_user_id,double amount,boolean trade_type)
    {
        return false;
    }
    public boolean reCharge(int user_id,double amount,boolean recharge_platform)
    {
        return false;
    }
    public boolean drawMoney(int user_id,double amount,boolean draw_platform)
    {
        return false;
    }
}
