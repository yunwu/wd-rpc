package wd.rpc.transport.invoker;

import io.netty.channel.ChannelFuture;
import wd.rpc.transport.Common.RpcContext;

import java.util.concurrent.Future;

public interface Invoker<T> {

    Object invoke(RpcContext context);

    Class<T> getTargetService();

    String getServiceName();
}
