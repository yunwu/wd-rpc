package wd.rpc.transport.provider;

import lombok.extern.slf4j.Slf4j;
import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.invoker.Invoker;
import wd.rpc.transport.registry.Registry;
import wd.rpc.transport.registry.RegistryFactory;
import wd.rpc.transport.registry.ZkRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceProvider implements Provider{

    private static Map<String, Class<?>> cache = new ConcurrentHashMap<>();
    @Override
    public void regist(Invoker invoker, RpcContext context) {
        Registry registry = null;
        try {
            registry = RegistryFactory.getRegistry(RegistryFactory.registryName);
        } catch (Exception e) {
            log.error("获取registry error");
            return;
        }
        try{
            registry.regist(invoker, context);
        }catch (Exception e){
            e.printStackTrace();
        }
        cache.put(invoker.getServiceName(), invoker.getTargetService());
    }

    public static Class<?> getTarget(String service) {
        return cache.get(service);
    }
}
