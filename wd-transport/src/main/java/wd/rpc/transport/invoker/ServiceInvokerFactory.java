package wd.rpc.transport.invoker;

public class ServiceInvokerFactory<T> {

    /**
     * 将服务包装成invoker
     * @param targetService
     * @return
     */
    public Invoker createInvoker(Class<T> targetService){
        ServiceInvoker invoker = new ServiceInvoker(targetService);
        return invoker;
    }
}
