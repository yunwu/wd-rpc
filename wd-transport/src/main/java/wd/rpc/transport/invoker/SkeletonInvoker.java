package wd.rpc.transport.invoker;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import wd.rpc.transport.Common.GlobalContext;
import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.Common.config.ServiceConfig;
import wd.rpc.transport.Common.config.ServiceConfigFactory;
import wd.rpc.transport.Common.exceptions.ErrorCode;
import wd.rpc.transport.Common.exceptions.RpcException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SkeletonInvoker<T> implements Invoker{

    private ServiceConfig serviceConfig;

    private static final Map<Class, Object> serviceCache = new ConcurrentHashMap<>();

    public SkeletonInvoker(Class service){
        this.serviceConfig = ServiceConfigFactory.getServiceConfig(service);
        T ref = (T) serviceCache.get(service);
        if (ref == null){
            synchronized (SkeletonInvoker.class){
                ref = (T) serviceCache.get(service);
                if (ref == null){
                    try {
                        ref = (T) serviceConfig.getTargetService().newInstance();
                        serviceCache.put(service, ref);
                        serviceConfig.setRef(ref);
                    } catch (Exception e) {
                        throw new RpcException(ErrorCode.SERVICE_CONSTRUCT_ERROR, "service construct error, " + service.getName(),e);
                    }
                }
            }
        }
    }
    @Override
    public Object invoke(RpcContext context) {
        T ref = (T)serviceConfig.getRef();
        try {
            Method method = ref.getClass().getMethod(context.getTargetMethod(), context.getParamsTypes());
            Object result = method.invoke(ref, context.getParams());
            return result;
        } catch (Exception e) {
            throw new RpcException(ErrorCode.SERVICE_RUN_ERROR, "provider invoke error,"+getServiceName()+"," + JSON.toJSONString(context), e);
        }
    }

    @Override
    public Class getTargetService() {
        return this.serviceConfig.getTargetService();
    }

    @Override
    public String getServiceName() {
        return this.serviceConfig.getPath();
    }
}
