package com.bob.project.config.registrar.scan;

/**
 * HSF通用属性配置
 * 定义此类的目的是为了获取HSF的基本配置,原先配置是定义在antx.properties文件中,
 * 这个文件由maven管理,在编译后不同环境下的value赋给相应的${...};
 * 在项目运行时获取不到antx中的属性信息,因此不能通过代码从antx.properties中提取指定key的value;
 * 至少需要定义一个Bean,由maven将当前环境的配置信息传入Spring容器中。
 *
 * @author wb-jjb318191
 * @create 2017-12-25 13:42
 */
public class HsfGenericConfig {

    private String serviceVersion;
    private String serviceGroup;
    private String clientTimeout;

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public String getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(String clientTimeout) {
        this.clientTimeout = clientTimeout;
    }
}
