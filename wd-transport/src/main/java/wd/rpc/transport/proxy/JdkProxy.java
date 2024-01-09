package wd.rpc.transport.proxy;

import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.invoker.Invoker;
import wd.rpc.transport.invoker.SkeletonInvoker;
import wd.rpc.transport.invoker.StubInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkProxy implements InvocationHandler {

    private Invoker target;

    public JdkProxy(Class proxyInterface){

        if (proxyInterface.isInterface()){
            this.target = new StubInvoker(proxyInterface);
        }else {
            this.target = new SkeletonInvoker(proxyInterface);
        }

    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcContext rpcContext = new RpcContext();
        rpcContext.setTargetMethod(method.getName());
        rpcContext.setParamsTypes(method.getParameterTypes());
        rpcContext.setParams(args);
        return target.invoke(rpcContext);
    }
}
