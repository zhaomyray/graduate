package edu.zut.pt.pojo;

/**
 * 校内指导老师登录信息
 */
public class Teacher_School {

    //登录id
    private String tno;
    //密码
    private String password;

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
}
