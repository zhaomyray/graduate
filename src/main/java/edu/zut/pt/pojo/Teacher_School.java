package edu.zut.pt.pojo;

/**
 * 校内指导老师登录信息
 */
public class Teacher_School {

    //登录id
    private String id;
    //密码
    private String password;

    public String setId(){
        return id;
    }

    public String setPassword(){
        return password;
    }

    public void getId(String id){
        this.id = id;
    }

    public void getPassword(String password){
        this.password = password;
    }
}
