package edu.zut.pt.pojo;

/**
 * 学生信息实体类
 */

public class StudentInfo {

    //学生学号
    private String sno;
    //学生姓名
    private String sname;
    //性别
    private String gender;
    //班级id
    private int classId;
    //实训类型名字
    private String typeName;
    //校内指导老师id
    private String schTeaId;
    //校外指导老师姓名
    private String comTeaName;
    //项目名字
    private String projectName;
    //联系方式
    private String phone;
    //qq邮箱
    private String qqemail;
    //父母联系方式
    private String parPhone;
    //校外指导老师联系方式
    private String comTeaPhone;
    //实训公司名字
    private String comName;
    //实训所在城市
    private String city;

    public String getSno() {
        return sno;
    }

    public String getSname() {
        return sname;
    }

    public String getGender() {
        return gender;
    }

    public int getClassId() {
        return classId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getSchTeaId() {
        return schTeaId;
    }

    public String getComTeaName() {
        return comTeaName;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getPhone() {
        return phone;
    }

    public String getQqemail() {
        return qqemail;
    }

    public String getParPhone() {
        return parPhone;
    }

    public String getComTeaPhone() {
        return comTeaPhone;
    }

    public String getComName() {
        return comName;
    }

    public String getCity() {
        return city;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setSchTeaId(String schTeaId) {
        this.schTeaId = schTeaId;
    }

    public void setComTeaName(String comTeaName) {
        this.comTeaName = comTeaName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setQqemail(String qqemail) {
        this.qqemail = qqemail;
    }

    public void setParPhone(String parPhone) {
        this.parPhone = parPhone;
    }

    public void setComTeaPhone(String comTeaPhone) {
        this.comTeaPhone = comTeaPhone;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
