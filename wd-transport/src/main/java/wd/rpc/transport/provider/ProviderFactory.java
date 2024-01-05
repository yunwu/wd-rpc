package wd.rpc.transport.provider;

import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.invoker.Invoker;

public class ProviderFactory {

    public Provider createProvider(){
        return new ServiceProvider();
    }
}
