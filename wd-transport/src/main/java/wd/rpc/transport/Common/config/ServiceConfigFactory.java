package wd.rpc.transport.Common.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class ServiceConfigFactory {

    private static final Map<Class, ServiceConfig> serviceConfigCache = new ConcurrentHashMap<>();

    //TODO serviceConfig Refresh
    public static ServiceConfig getServiceConfig(Class<?> clazz){
        Class<?> interfaceClazz = clazz;
        if (!interfaceClazz.isInterface()){
            interfaceClazz = clazz.getInterfaces()[0];
        }
        ServiceConfig serviceConfig = serviceConfigCache.get(interfaceClazz);
        if (serviceConfig == null){
            synchronized (interfaceClazz.getClass()){
                serviceConfig = serviceConfigCache.get(interfaceClazz);
                if (serviceConfig == null){
                    serviceConfig = new ServiceConfig(clazz);
                    serviceConfigCache.put(interfaceClazz, serviceConfig);
                }
            }
        }
        return serviceConfig;
    }
}
