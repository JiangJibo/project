package com.bob.common.utils.task;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * 异步任务调度器配置
 *
 * @author wb-jjb318191
 * @create 2018-07-04 9:24
 */
@Aspect
@Configuration
public class AsyncTaskDispatcher {

    @Autowired
    private AsyncTaskManager taskManager;

    @Autowired
    private BeanFactory beanFactory;

    /**
     * 异步任务处理切面
     */
    @Around("@annotation(com.bob.common.utils.task.Async)")
    public void processAsyncTask(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        Async async = method.getAnnotation(Async.class);
        TaskRecord record = new TaskRecord(async.taskType(), async.taskName(), async.timeout());
        AsyncTaskProcesses processes = null;
        if (StringUtils.hasText(async.processBeanName())) {
            processes = beanFactory.getBean(async.processBeanName(), AsyncTaskProcesses.class);
        }
        taskManager.submit(new AsyncTask(record, processes, joinPoint), true);
    }

}
