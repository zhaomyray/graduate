package edu.zut.pt.pojo;

/**
 * 学生登录信息实体类
 */
public class Student {

    //学号
    private String sno;
    //密码
    private String password;
//    private int stuRoleId;

    public String getSno() {
        return sno;
    }

    public String getPassword() {
        return password;
    }

//    public int getStuRoleId() {
//        return stuRoleId;
//    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public void setStuRoleId(int stuRoleId) {
//        this.stuRoleId = stuRoleId;
//    }
}
