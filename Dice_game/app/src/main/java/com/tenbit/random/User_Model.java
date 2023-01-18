package com.tenbit.random;
public class User_Model {

    private String user_Name;
    private String user_Email;
    private String user_id;
    private int user_Point;
    private String user_ReferCode;
    
    private  int refer_Count;
    private int codeUse_orNot;


    public User_Model() {
    }

    public User_Model(String user_Name, String user_Email, String user_id, int user_Point, String user_ReferCode, int refer_Count, int codeUse_orNot) {
        this.user_Name = user_Name;
        this.user_Email = user_Email;
        this.user_id = user_id;
        this.user_Point = user_Point;
        this.user_ReferCode = user_ReferCode;
        
        this.refer_Count = refer_Count;
        this.codeUse_orNot = codeUse_orNot;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public String getUser_Email() {
        return user_Email;
    }

    public void setUser_Email(String user_Email) {
        this.user_Email = user_Email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getUser_Point() {
        return user_Point;
    }

    public void setUser_Point(int user_Point) {
        this.user_Point = user_Point;
    }

    public String getUser_ReferCode() {
        return user_ReferCode;
    }

    public void setUser_ReferCode(String user_ReferCode) {
        this.user_ReferCode = user_ReferCode;
    }

    

    public int getRefer_Count() {
        return refer_Count;
    }

    public void setRefer_Count(int refer_Count) {
        this.refer_Count = refer_Count;
    }

    public int getCodeUse_orNot() {
        return codeUse_orNot;
    }

    public void setCodeUse_orNot(int codeUse_orNot) {
        this.codeUse_orNot = codeUse_orNot;
    }
}
