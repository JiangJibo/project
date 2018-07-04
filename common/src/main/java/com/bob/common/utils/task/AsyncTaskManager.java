package com.bob.common.utils.task;

/**
 * 异步任务管理器
 *
 * @author wb-jjb318191
 * @create 2018-06-25 16:44
 */
public interface AsyncTaskManager {

    /**
     * 初始化调用
     */
    void init();

    /**
     * 提交任务
     *
     * @param task
     * @param abortIfExists 若存在对应任务,是否中止 true : 中止; false:延迟直到之前任务结束
     */
    void submit(AsyncTask task, boolean abortIfExists);

}