package wd.rpc.transport.Common.config;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.Map;

@Data
public class ServiceConfig<T> {

    /**
     * 接口类
     */
    private Class<?> serviceInterface;

    /**
     * 该服务提供的接口数组
     */
    private Method[] interfaces;

    private Map<Method, MethodConfig> methodConfigMap;

    /**
     * 该服务的实现类
     */
    private Class<?> targetService;

    /**
     * 服务过期时间， 单位毫秒
     */
    private long timeout = 1000;

    /**
     * 服务全限定名称
     */
    private String path;

    /**
     * 实现类对象
     */
    private T ref;

    private long version;

    /**
     * 默认尝试次数
     */
    private int retries = 0;

    private boolean delay;

    private boolean export;


    public ServiceConfig(Class<T> service){
        if (service.isInterface()){
            this.serviceInterface = service;
            this.interfaces = service.getDeclaredMethods();
            this.path = service.getName();
        }else{
            this.serviceInterface = service.getInterfaces()[0];
            this.interfaces = this.serviceInterface.getDeclaredMethods();
            this.path = this.serviceInterface.getName();
        }
        this.targetService = service;

    }


}
