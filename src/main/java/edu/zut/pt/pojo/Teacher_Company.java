package edu.zut.pt.pojo;

/**
 * 校外指导老师登录信息实体类
 */
public class Teacher_Company {

    //登录id
    private String id;
    //密码
    private String password;
    //姓名
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
