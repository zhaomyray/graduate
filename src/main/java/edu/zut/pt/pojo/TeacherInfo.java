package edu.zut.pt.pojo;

/**
 * 校内指导老师信息实体类
 */
public class TeacherInfo {

    //教工号
    private String tno;
    //姓名
    private String tname;
    //学生人数
    private int stuNum;
    //性别
    private String gender;
    //联系方式
    private String phone;
    //qq邮箱
    private String qqemail;

    public String getTno() {
        return tno;
    }

    public String getTname() {
        return tname;
    }

    public int getStuNum() {
        return stuNum;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getQqemail() {
        return qqemail;
    }

    public void setTno(String tno) {
        this.tno = tno;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public void setStuNum(int stuNum) {
        this.stuNum = stuNum;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setQqemail(String qqemail) {
        this.qqemail = qqemail;
    }
}
