package wd.rpc.transport.registry;

import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.invoker.Invoker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Registry {

    /**
     * 注册服务
     * @param invoker
     * @param context
     */
    void regist(Invoker invoker, RpcContext context);

    String getUrl(String serviceName);
}
