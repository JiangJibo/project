package com.bob.root.config;

import java.io.File;

//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.joran.JoranConfigurator;
//import ch.qos.logback.core.joran.spi.JoranException;
import org.junit.runners.model.InitializationError;
//import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 主要目的是在单元测试中加在logback
 */
public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {

    public JUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        loadLogbackConfig();
    }

    private void loadLogbackConfig() {
        File logbackFile = new File("src/test/resources/logback-test.xml");
        if (logbackFile.exists()) {
            /*LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            try {
                configurator.doConfigure(logbackFile);
            } catch (JoranException e) {
                e.printStackTrace(System.err);
                System.exit(-1);
            }*/
        }
    }

}  