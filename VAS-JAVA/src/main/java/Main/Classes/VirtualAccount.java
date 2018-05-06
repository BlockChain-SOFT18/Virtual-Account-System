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
public class VirtualAccount extends Sql implements Main.Interfaces.VasInterface{

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
            userInsert(user_name,user_passwd,user_realname,user_tel,user_email,user_identity,under_agency_id);
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
            updatePasswd(user_id,new_passwd);
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
                ResultSet result=getAgencyInformation(agency_id);
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

            ResultSet result=getAgencyUsers(agency_id);
            if(!result.next()) return null;
            result.beforeFirst();
            while(result.next()){
                Map<Integer,Integer> map=new CaseInsensitiveMap();
                map.clear();
                map.put(i,Integer.parseInt(result.getObject(1).toString()));
                list.add(map);
                i++;
            }
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
            ResultSet result=getUserInformation(user_id);
            result.first();
            //
            ResultSetMetaData metaData=result.getMetaData();
            for(int i=1;i<=metaData.getColumnCount();i++){
                if(metaData.getColumnName(i).equalsIgnoreCase("userpasswd")) continue;
                map.put(metaData.getColumnName(i),result.getObject(i).toString());
            }
            list.add(map);
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
            updateFrozen(user_id,if_freeze);
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
            if(getUserIdentity(user_name,user_identity).next()){
                updatePasswd(checkUserExists(user_name),new_passwd);
                if(checkUserPasswd(user_name,new_passwd)!=-1)
                    return true;
                else
                    return false;
            }else{
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
            addBalance(user_id,amount);
            //等接口
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
            minusBalance(user_id,amount);
            //等接口
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
