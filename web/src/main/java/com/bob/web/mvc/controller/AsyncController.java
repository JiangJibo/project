package com.bob.web.mvc.controller;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年6月9日 上午10:23:43
 */
@RestController
@RequestMapping("/async")
public class AsyncController {

    private static final String TIME_OUT_RESULT = "TIME_OUT";
    private static final String ASYNC_RESULT = "SUCCESS";

    final static Logger LOGGER = LoggerFactory.getLogger(AsyncController.class);

    @RequestMapping(value = "/callable", method = RequestMethod.GET)
    public Callable<String> getCallableMessage() {
        LOGGER.debug("请求{}方法,线程id:{}", "getAsyncMessage", Thread.currentThread().getId());
        Callable<String> callable = () -> {
            Thread.sleep(10000);
            return "异步信息";
        };
        // Future<String> future = exes.submit(callable);
        LOGGER.debug("释放线程id:{}", Thread.currentThread().getId());
        return callable;
    }

    /**
     * DeferredResult延迟结果使用案例
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "/deferred", method = RequestMethod.GET)
    public DeferredResult<String> getDeferredResult() throws InterruptedException {
        LOGGER.debug("请求{}方法,线程id:{}", "getDeferredResult", Thread.currentThread().getId());
        final DeferredResult<String> def = new DeferredResult<String>(6000L, TIME_OUT_RESULT);

        def.onTimeout(() -> {
            LOGGER.error("请求处理超时");
            def.setErrorResult(TIME_OUT_RESULT);
        });

        def.onCompletion(() -> LOGGER.debug("请求结束"));

        new Thread() {

            @Override
            public void run() {
                def.setResult(processAsyncResult());
                def.setResultHandler((result) -> LOGGER.debug("DeferredResultHandler.handleResult[{}]", def.getResult()));
            }

        }.start();

        LOGGER.debug("释放线程id:{}", Thread.currentThread().getId());
        return def;
    }

    private String processAsyncResult() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return ASYNC_RESULT;
    }

    /**
     * WebAsyncTask使用实例
     *
     * @return
     */
    @RequestMapping(value = "/asyncTask", method = RequestMethod.GET)
    public WebAsyncTask<String> getAsyncTask() {
        Callable<String> callable = new Callable<String>() {

            @Override
            public String call() throws Exception {
                return processAsyncResult();
            }
        };
        WebAsyncTask<String> asyncTask = new WebAsyncTask<>(6000L, callable);
        asyncTask.onCompletion(() -> LOGGER.debug("异步任务结束"));
        asyncTask.onTimeout(() -> {
            LOGGER.debug("异步任务结束");
            return TIME_OUT_RESULT;
        });
        return asyncTask;
    }

}
