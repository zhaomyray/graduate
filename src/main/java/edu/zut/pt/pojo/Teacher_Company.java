package edu.zut.pt.pojo;

/**
 * 校外指导老师登录信息实体类
 */
public class Teacher_Company {

    //登录id
    private String tno;
    //密码
    private String password;
    //姓名
    private String name;

    public String getTno() {
        return tno;
    }

    public void setTno(String tno) {
        this.tno = tno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
