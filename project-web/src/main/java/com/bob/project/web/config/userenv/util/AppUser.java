/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.project.web.config.userenv.util;

import java.lang.reflect.Field;

import com.bob.project.web.config.exception.CustomizedException;
import com.bob.project.web.config.userenv.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @since 2017年4月3日 下午7:26:28
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class AppUser implements BeanFactoryAware {

    final static Logger LOGGER = LoggerFactory.getLogger(AppUser.class);

    private static BeanFactory beanFactory;

    public static final String USER_ENV_BEAN_NAME = ClassUtils.getShortNameAsProperty(LoginUser.class);

    /**
     * 获取当前Bean实例
     *
     * @return
     */
    public static AppUser getAppUser() {
        return beanFactory.getBean("appUser", AppUser.class);
    }

    /**
     * 根据用户属性名称获取登录用户信息
     *
     * @param name
     * @return
     */
    public static Object getUserEnv(String name) {
        LoginUser loginUser = beanFactory.getBean(USER_ENV_BEAN_NAME, LoginUser.class);
        try {
            Field field = ReflectionUtils.findField(LoginUser.class, name);
            field.setAccessible(true);
            return field.get(loginUser);
        } catch (Exception e) {
            LOGGER.error("尝试注入属性:[{}]时出现异常", name, e);
            throw new CustomizedException(e);
        }
    }

    /**
     * 将用户的登录信息寄存在应用(session)中
     *
     * @param user
     */
    public static void resideUserEnv(Object user) {
        beanFactory.getBean(LoginUser.class, user);
    }

    /**
     * 清除用户登录信息
     */
    public static void clearUserEnv() {
        ((DefaultListableBeanFactory)beanFactory).destroyScopedBean(USER_ENV_BEAN_NAME);
    }

    /**
     * 验证是否有用户登录
     *
     * @return
     */
    public static boolean isLogin() {
        try {
            beanFactory.getBean(USER_ENV_BEAN_NAME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        AppUser.beanFactory = beanFactory;
    }

}
