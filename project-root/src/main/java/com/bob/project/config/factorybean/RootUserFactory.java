package com.bob.project.config.factorybean;

import com.bob.project.utils.model.RootUser;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * FactoryBean的使用方法
 *
 * @since 2017年1月16日 上午10:20:11
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class RootUserFactory implements FactoryBean<RootUser> {

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    @Override
    public RootUser getObject() throws Exception {
        RootUser student = new RootUser();
        student.setId(001);
        student.setName("lanboal");
        student.setAge(28);
        student.setAdress("杭州");
        student.setTelephone("18758107760");
        return student;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return RootUser.class;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

}
