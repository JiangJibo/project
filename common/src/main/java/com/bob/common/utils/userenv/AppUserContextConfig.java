package com.bob.common.utils.userenv;

import com.bob.common.utils.userenv.process.UserEnvAnnotationProcessor;
import com.bob.common.utils.userenv.entity.LoginUser;
import com.bob.common.utils.userenv.process.AppUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 用户环境变量配置,可在任意处获取用户登录信息
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2017年7月7日 上午9:21:48
 */
@Configuration
public class AppUserContextConfig {

    @Bean
    public UserEnvAnnotationProcessor userEnvAnnotationProcessor() {
        return new UserEnvAnnotationProcessor();
    }

    @Bean
    @Scope("session")
    public LoginUser loginUser(Object userEnv) {
        return new LoginUser(userEnv);
    }

    @Bean
    public AppUser appUser() {
        return new AppUser();
    }

}
