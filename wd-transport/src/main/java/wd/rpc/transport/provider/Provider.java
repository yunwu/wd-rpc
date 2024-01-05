package wd.rpc.transport.provider;

import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.invoker.Invoker;

public interface Provider {


    void regist(Invoker invoker, RpcContext context);
}
