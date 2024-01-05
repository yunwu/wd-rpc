package wd.rpc.transport.Common;

import io.netty.channel.Channel;

import java.util.concurrent.*;

public class Result  {

    private CompletableFuture<Object> future;

    private Channel channel;

    public Result(Channel channel){
        this.channel = channel;
    }
    public Object get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }

    public void set(CompletableFuture<Object> future){
        this.future = future;
    }

    public CompletableFuture<Object> getFuture(){
        return future;
    }
}
