package wd.rpc.transport.Common;

import io.netty.channel.Channel;

import java.util.concurrent.*;

public class Result  {

    /**
     * 请求执行结果
     */
    private CompletableFuture<Object> future;

    /**
     * 请求通道
     */
    private String requestId;

    /**
     * 请求执行期间发生的异常
     */
    private Exception exception;

    public Result(String requestId){
        this.requestId = requestId;
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

    public void setException(Exception exception){
        this.exception = exception;
    }

    public Exception getException(){
        return exception;
    }

    public String getRequestId(){
        return requestId;
    }
}
