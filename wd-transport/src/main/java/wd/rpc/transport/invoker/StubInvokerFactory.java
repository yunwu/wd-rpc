package wd.rpc.transport.invoker;

public class StubInvokerFactory<T> {

    /**
     * 将服务包装成invoker
     * @param targetService
     * @return
     */
    public Invoker createInvoker(Class<T> targetService){
        StubInvoker invoker = new StubInvoker(targetService);
        return invoker;
    }
}
