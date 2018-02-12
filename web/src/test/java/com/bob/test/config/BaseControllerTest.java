package com.bob.test.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

import com.bob.root.config.RootContextConfig;
import com.bob.web.config.WebContextConfig;
import com.bob.web.config.exception.CustomizedException;
import com.bob.web.config.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月8日 下午4:45:26
 */
//@Rollback()
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootContextConfig.class, WebContextConfig.class})
public abstract class BaseControllerTest {

    protected Gson gson;

    private MockMvc mockMvc;
    protected MockHttpSession session;

    protected Object mappedController;

    protected String userName;
    protected String password;

    protected boolean loginBefore;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Before()
    public void setup() {
        //使用GsonBuilder针对日期类型指定解析后的格式,当Date只有年月日时,会使用当前时间来凑够解析长度
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); // 初始化MockMvc对象
        init();
    }

    /**
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String getRequest(String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.GET, null, false, null, urlTemplate, urlVariables);
    }

    /**
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String getAsyncRequest(String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.GET, null, true, null, urlTemplate, urlVariables);
    }

    /**
     * @param contentType
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String postRequest(MediaType contentType, String content, String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.POST, contentType, false, content, urlTemplate, urlVariables);
    }

    /**
     * @param contentType
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String postAsyncRequest(MediaType contentType, String content, String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.POST, contentType, true, content, urlTemplate, urlVariables);
    }

    /**
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String postRequest(String content, String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.POST, MediaType.APPLICATION_JSON, false, content, urlTemplate, urlVariables);
    }

    /**
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String postAsyncRequest(String content, String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.POST, MediaType.APPLICATION_JSON, true, content, urlTemplate, urlVariables);
    }

    /**
     * @param contentType
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String putRequest(MediaType contentType, String content, String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.PUT, contentType, false, content, urlTemplate, urlVariables);
    }

    /**
     * @param contentType
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String putAsyncRequest(MediaType contentType, String content, String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.PUT, contentType, true, content, urlTemplate, urlVariables);
    }

    /**
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String putRequest(String content, String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.PUT, MediaType.APPLICATION_JSON, false, content, urlTemplate, urlVariables);
    }

    /**
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String putAsyncRequest(String content, String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.PUT, MediaType.APPLICATION_JSON, true, content, urlTemplate, urlVariables);
    }

    /**
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String deleteRequest(String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.DELETE, null, false, null, urlTemplate, urlVariables);
    }

    /**
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    public String deleteAsyncRequest(String urlTemplate, Object... urlVariables) {
        return doRequest(RequestMethod.DELETE, null, true, null, urlTemplate, urlVariables);
    }

    /**
     * @param filePath
     * @return
     */
    public String readJsonFile(String filePath) {
        BufferedReader bufferReader = null;
        try {
            File file = new File(filePath);
            long length = file.length();
            if (length > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("试图读取的文件大小超过5M");
            }
            return IOUtils.toString(new FileInputStream(file), "UTF-8");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(filePath, e);
        } finally {
            try {
                if (null != bufferReader) {
                    bufferReader.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * @param method
     * @param contentType
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    protected String doRequest(RequestMethod method, MediaType contentType, boolean async, String content, String urlTemplate, Object... urlVariables) {

        checkMappedController(urlTemplate);

        MockHttpServletRequestBuilder builder;
        switch (method) {
            case GET:
                builder = MockMvcRequestBuilders.get(urlTemplate, urlVariables);
                break;
            case POST:
                builder = MockMvcRequestBuilders.post(urlTemplate, urlVariables);
                break;
            case PUT:
                builder = MockMvcRequestBuilders.put(urlTemplate, urlVariables);
                break;
            case DELETE:
                builder = MockMvcRequestBuilders.delete(urlTemplate, urlVariables);
                break;
            default:
                throw new UnsupportedOperationException(method.name());
        }
        if (loginBefore && login(userName, password)) {
            builder.session(session);
        }
        if (content != null) {
            builder.contentType(contentType).content(content);
        }
        try {
            MvcResult mvcResult = mockMvc.perform(builder).andReturn();
            return async ? mvcResult.getAsyncResult().toString() : mvcResult.getResponse().getContentAsString();
        } catch (Exception e) {
            return hanldeRequestException(e, method, contentType, content, urlTemplate, urlVariables);
        }
    }

    /**
     * 上传文件
     *
     * @param file         文件域名称
     * @param path         路由
     * @param urlTemplate
     * @param async        是否是异步请求
     * @param urlVariables
     * @return
     */
    public String fileUpload(String file, String path, String urlTemplate, boolean async, Object... urlVariables) {
        checkMappedController(urlTemplate);
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload(urlTemplate, urlVariables);
        if (loginBefore && login(userName, password)) {
            builder.session(session);
        }
        try {
            builder.file(file, FileUtils.readFileToByteArray(new File(path)));
            MvcResult mvcResult = mockMvc.perform(builder).andReturn();
            return async ? mvcResult.getAsyncResult().toString() : mvcResult.getResponse().getContentAsString();
        } catch (Exception e) {
            return hanldeRequestException(e, RequestMethod.POST, null, null, urlTemplate, urlVariables);
        }
    }

    /**
     * @param e
     * @param method
     * @param contentType
     * @param content
     * @param urlTemplate
     * @param urlVariables
     * @return
     */
    protected String hanldeRequestException(Exception e, RequestMethod method, MediaType contentType, String content, String urlTemplate,
                                            Object... urlVariables) {
        return null;
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    private boolean login(String userName, String password) {
        User user = new User(userName, password);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/users/login");
        builder.contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user));
        String result = null;
        try {
            MvcResult mvc = mockMvc.perform(builder).andReturn();
            this.session = (MockHttpSession)mvc.getRequest().getSession();
            result = mvc.getResponse().getContentAsString();
        } catch (Exception e) {
            throw new IllegalStateException(String.format("用户名[%s],密码[%s]登录异常,请重新登录。", userName, password));
        }
        Assert.state(Boolean.parseBoolean(result), String.format("用户名[%s],密码[%s]登录失败,请验证用户名密码。", userName, password));
        this.loginBefore = false;
        return true;
    }

    /**
     * 校验请求的路由是否匹配指定的Controller,主要根据Controller上的@RequestMapping()来校验
     *
     * @param urlTemplate
     */
    private void checkMappedController(String urlTemplate) {
        Assert.notNull(mappedController, "必须指明当前ControllerTest映射到哪个Controller");
        Class<?> controllerClass = AopUtils.isAopProxy(mappedController) ? AopUtils.getTargetClass(mappedController) : mappedController.getClass();
        RequestMapping mapping = controllerClass.getAnnotation(RequestMapping.class);
        Assert.notNull(mapping, "mappedController必须含有[RequestMapping]注解");
        boolean mapped = false;
        for (String value : mapping.value()) {
            if (value.contains("{")) {
                value = value.substring(0, value.indexOf("{") + 1); // 当@RequestMapping("/user/{id}")形式时,截取"/user"
            }
            if (urlTemplate.startsWith(value)) {
                mapped = true;
                break;
            }
        }
        if (!mapped) {
            throw new CustomizedException(String.format("请求路由[%s]不匹配Controller：[%s]", urlTemplate, mappedController.getClass().getName()));
        }
    }

    /**
     * 模板方法,由子类重写以决定是否在运行之前登录
     */
    protected abstract void init();

}
