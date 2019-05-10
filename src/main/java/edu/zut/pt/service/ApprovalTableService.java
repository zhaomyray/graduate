package edu.zut.pt.service;

import edu.zut.pt.pojo.ApprovalTable;

public interface ApprovalTableService {

    /**
     * 添加审批表信息
     * @param sno
     * @param time_submit
     * @param tableFilePath
     * @return
     */
    public int insertApprovalTableInfo(String sno, String time_submit, String tableFilePath);


    /**
     * 更新审批表信息
     * @param sno
     * @param time_submit
     * @param tableFilePath
     * @return
     */
    public int updateApprovalTableInfo(String sno,String time_submit,String tableFilePath);


    /**
     * 查找某学生的审批表信息
     * @param sno
     * @return
     */
    public ApprovalTable selectApprovalTableInfoBySno(String sno);






}
