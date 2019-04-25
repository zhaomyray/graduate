package edu.zut.pt.pojo;


/**
 * 系统公告实体类
 */
public class SystemNotice {

    //公告id
    private int id;
    //公告标题
    private String title;
    //公告内容
    private String content;
    //公告查看次数
    private int clickhit;
    //公告创建时间
    private String createdate;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getClickhit() {
        return clickhit;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setClickhit(int clickhit) {
        this.clickhit = clickhit;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
