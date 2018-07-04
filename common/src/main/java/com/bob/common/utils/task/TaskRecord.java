package com.bob.common.utils.task;

import java.util.Date;

/**
 * 数据库表：task_record
 *
 * @author wb-jjb318191
 * @create 2018-06-26
 */
public class TaskRecord {

    /**
     * null
     */
    private Long id;

    /**
     * null
     */
    private Date gmtCreate;

    /**
     * null
     */
    private Date gmtModified;

    /**
     * null
     */
    private String taskName;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务桩体-0运行中；1:正常结束；2：异常结束
     */
    private Long status;

    /**
     * 执行错误信息
     */
    private String executeInfo;

    /**
     * 任务超时时间，单位毫秒
     */
    private Integer timeout;

    public TaskRecord() {
    }

    public TaskRecord(String taskType, String taskName, Integer timeout) {
        this.taskType = taskType;
        this.taskName = taskName;
        this.timeout = timeout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType == null ? null : taskType.trim();
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getExecuteInfo() {
        return executeInfo;
    }

    public void setExecuteInfo(String executeInfo) {
        this.executeInfo = executeInfo == null ? null : executeInfo.trim();
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}