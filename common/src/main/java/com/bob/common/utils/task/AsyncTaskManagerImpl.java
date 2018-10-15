package com.bob.common.utils.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.Assert;

/**
 * 异步任务管理器
 *
 * @author wb-jjb318191
 * @create 2018-06-25 17:31
 */
public class AsyncTaskManagerImpl implements AsyncTaskManager {

    //@Autowired
    //private TaskRecordDAO taskRecordDAO;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    private static final String TIMEOUT_ERROR_MESSAGE = "任务超时,被强制结束";

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskManagerImpl.class);

    /**
     * 启动后5S执行一次,最后每隔半小时扫描一次
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void scheduleTimeoutTaskScanning() {
        LOGGER.debug("*****************启动异步任务超时扫描*****************");
        init();
    }

    /**
     * 处理那些在运行过程中,应用异常导致未能结束的任务
     */
    @Override
    public void init() {
        //List<TaskRecord> unfinishedTasks = taskRecordDAO.selectUnfinishedTasks();
        List<TaskRecord> unfinishedTasks = new ArrayList<>();
        for (final TaskRecord task : unfinishedTasks) {
            long timeDiff = task.getGmtCreate().getTime() + task.getTimeout() - System.currentTimeMillis();
            // 如果任务已超时
            if (timeDiff <= 0) {
                terminateTimeoutTask(task);
            }
        }
    }

    @Override
    public void submit(final AsyncTask task, final boolean abortIfExists) {
        checkTaskData(task);
        if (!start(task)) {
            // 如果已存在同种任务
            processAborting(task, abortIfExists);
        } else {
            processStarting(task);
        }
    }

    /**
     * 校验异步任务数据
     *
     * @param task
     */
    private void checkTaskData(AsyncTask task) {
        Assert.notNull(task, "异步任务不能为空");
        TaskRecord record = task.getTaskRecord();
        Assert.notNull(record, "异步任务记录数据不能为空");
        Assert.hasText(record.getTaskType(), "异步任务类型不能为空");
        Assert.hasText(record.getTaskName(), "异步任务名称不能为空");
        Assert.state(record.getTimeout() != null && record.getTimeout() > 0, "异步任务必须正确设置超时时间");
    }

    /**
     * 处理中止的任务
     *
     * @param task
     * @param abortIfExists
     */
    private void processAborting(final AsyncTask task, final boolean abortIfExists) {
        // 当前任务放弃执行
        if (abortIfExists) {
            finish(task, false);
        } else {
            // 当前任务延迟执行,延迟时间为任务过期时间
            LOGGER.debug(String.format("异步任务Type:[%s],Name:[%s] 因存在执行中的同种任务而延迟执行", task.getTaskType(), task.getTaskName()));
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    submit(task, abortIfExists);
                }
            }, new Date(System.currentTimeMillis() + task.getTimeout()));
        }
    }

    /**
     * 处理任务正常开启
     *
     * @param asyncTask
     */
    private void processStarting(final AsyncTask asyncTask) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    asyncTask.process();
                    finish(asyncTask, true);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    error(asyncTask, new InterruptedException(TIMEOUT_ERROR_MESSAGE));
                } catch (Exception e) {
                    error(asyncTask, e);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        };
        // 提交异步任务
        final Future future = executor.submit(task);
        // 在超时时间之后检查, 如果任务还未结束, 则中断任务
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                if (!future.isDone()) {
                    future.cancel(true);
                }
            }
        }, new Date(System.currentTimeMillis() + asyncTask.getTimeout()));
    }

    public boolean start(AsyncTask asyncTask) {
        //boolean exists = taskRecordDAO.checkHavingUnfinishedTask(asyncTask.getTaskType(), asyncTask.getTaskName());
        // 如果存在同种任务,启动失败
        if (true) {
            return false;
        }
        TaskRecord record = asyncTask.getTaskRecord();
        record.setGmtCreate(new Date());
        record.setGmtModified(new Date());
        record.setStatus(0L);
        //taskRecordDAO.insertSelective(record);
        asyncTask.onStart();
        return true;
    }

    public void finish(AsyncTask asyncTask, boolean success) {
        if (success) {
            TaskRecord update = new TaskRecord();
            update.setId(asyncTask.getTaskId());
            update.setGmtModified(new Date());
            //taskRecordDAO.updateWithSuccess(update);
        } else {
            LOGGER.warn(String.format("异步任务Type:[%s],Name:[%s],ID:[%d] 因存在执行中的同种任务而放弃执行",
                asyncTask.getTaskType(), asyncTask.getTaskName(), asyncTask.getTaskId()));
        }
        asyncTask.onFinished(success);
    }

    public void error(AsyncTask asyncTask, Throwable ex) {
        LOGGER.error(
            String.format("异步任务Type:[%s],Name:[%s],ID:[%d] 执行失败", asyncTask.getTaskType(), asyncTask.getTaskName(), asyncTask.getTaskId()), ex);
        TaskRecord update = new TaskRecord();
        update.setId(asyncTask.getTaskId());
        update.setGmtModified(new Date());
        update.setExecuteInfo(ex.getMessage());
        //taskRecordDAO.updateWithError(update);
        asyncTask.onError(ex);
    }

    /**
     * 终止超时任务
     *
     * @param task
     */
    private void terminateTimeoutTask(TaskRecord task) {
        /*int num = taskRecordDAO.terminateTimeoutTaskIfApplicable(task.getId(), TIMEOUT_ERROR_MESSAGE);
        if (num == 1) {
            AdminMapLogUtils.error(String.format("ID:[%d]对应的数据同步任务超时,被强制结束", task.getId()));
        }*/
    }
}
