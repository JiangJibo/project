package com.bob.common.utils.task;

/**
 * 异步任务执行流程
 *
 * @author wb-jjb318191
 * @create 2018-06-26 12:15
 */
public interface AsyncTaskProcesses {

    /**
     * 在启动任务时
     */
    void onStart();

    /**
     * 在结束任务
     *
     * @param success 是否运行成功,可能因为存在执行中的同种任务而放弃执行
     */
    void onFinished(boolean success);

    /**
     * 在任务发生异常时, 可能是运行时发生异常,可能是超时而被强制结束
     *
     * @param ex
     */
    void onError(Throwable ex);

}