package com.bob.common.utils.task;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 异步任务
 *
 * @author wb-jjb318191
 * @create 2018-06-27 8:30
 */
public class AsyncTask {

    /**
     * 任务记录
     */
    private TaskRecord taskRecord;
    /**
     * 任务流程处理器
     */
    private AsyncTaskProcesses taskProcesses;

    /**
     * 异步方法
     */
    private ProceedingJoinPoint joinPoint;

    public AsyncTask() {
    }

    public AsyncTask(TaskRecord taskRecord, AsyncTaskProcesses taskProcesses, ProceedingJoinPoint joinPoint) {
        this.taskRecord = taskRecord;
        this.taskProcesses = taskProcesses;
        this.joinPoint = joinPoint;
    }

    public int getTimeout() {
        return taskRecord.getTimeout();
    }

    public String getTaskType() {
        return taskRecord.getTaskType();
    }

    public String getTaskName() {
        return taskRecord.getTaskName();
    }

    public Long getTaskId() {
        return taskRecord.getId();
    }

    public void setTaskProcesses(AsyncTaskProcesses taskProcesses) {
        this.taskProcesses = taskProcesses;
    }

    public TaskRecord getTaskRecord() {
        return taskRecord;
    }

    public void setTaskRecord(TaskRecord taskRecord) {
        this.taskRecord = taskRecord;
    }

    public AsyncTaskProcesses getTaskProcesses() {
        return taskProcesses;
    }

    public ProceedingJoinPoint getJoinPoint() {
        return joinPoint;
    }

    public void setJoinPoint(ProceedingJoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    public Object process() throws Throwable {
        return joinPoint.proceed();
    }

    /**
     * 在启动任务时
     */
    void onStart() {
        if (taskProcesses != null) {
            taskProcesses.onStart();
        }
    }

    /**
     * 在结束任务
     *
     * @param success 是否运行成功,可能因为存在执行中的同种任务而放弃执行
     */
    void onFinished(boolean success) {
        if (taskProcesses != null) {
            taskProcesses.onFinished(success);
        }
    }

    /**
     * 在任务发生异常时, 可能是运行时发生异常,可能是超时而被强制结束
     *
     * @param ex
     */
    void onError(Throwable ex) {
        if (taskProcesses != null) {
            taskProcesses.onError(ex);
        }
    }

}
