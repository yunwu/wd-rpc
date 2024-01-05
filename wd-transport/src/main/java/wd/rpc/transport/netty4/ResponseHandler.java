package wd.rpc.transport.netty4;

import io.netty.channel.Channel;
import wd.rpc.transport.Common.Result;

import java.util.Map;
import java.util.concurrent.*;

public class ResponseHandler {


    public static final Map<Channel, Result> futureCache = new ConcurrentHashMap<>();

    public static Object getResponse(Channel channel, long timeout, TimeUnit timeUnit){
        Result result = futureCache.get(channel);
        if (result == null){
            return null;
        }
        try {
            Object data = result.get(timeout, timeUnit);
            futureCache.remove(channel);
            return data;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    };


    public static void resloveResult(Channel channel, final Object data){
        Result result =  futureCache.get(channel);
        result.getFuture().complete(data);
    }

    public static void setResult(Channel channel){
        Result result = new Result(channel);
        CompletableFuture<Object> future = new CompletableFuture();
        result.set(future);
        futureCache.put(channel, result);
    }
}
