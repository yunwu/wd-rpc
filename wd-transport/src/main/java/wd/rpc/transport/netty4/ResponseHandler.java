package wd.rpc.transport.netty4;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import io.netty.channel.Channel;
import wd.rpc.transport.Common.ResponseDTO;
import wd.rpc.transport.Common.Result;

import java.util.Map;
import java.util.concurrent.*;

public class ResponseHandler {


    public static final Map<String, Result> futureCache = new ConcurrentHashMap<>();

    //TODO 代码删掉
//    public static Object getResponse(Channel channel, long timeout, TimeUnit timeUnit){
//        Result result = futureCache.get(channel);
//        if (result == null){
//            return null;
//        }
//        try {
//            if (result.getException() instanceof Exception){
//                futureCache.remove(channel);
//                throw new RpcException(ErrorCode.SERVICE_RUN_ERROR, "get response error", result.getException());
//            }
//            Object data = result.get(timeout, timeUnit);
//            futureCache.remove(channel);
//            return data;
//        } catch (Exception e) {
//            futureCache.remove(channel);
//            throw new RpcException(ErrorCode.SERVICE_RUN_ERROR, "get response error", e);
//        }
//    };


    public static void resolveResult(Channel channel, final Object data){
        ResponseDTO responseDTO = JSON.parseObject((String)data, ResponseDTO.class, JSONReader.Feature.SupportClassForName);
        String requestId = responseDTO.getRequestId();
        if (requestId == null || "".equals(requestId)){
           return;
        }
        Result result =  futureCache.get(requestId);
        Object responseData = responseDTO.getData();
        if (responseDTO.getException() != null){
           result.setException(responseDTO.getException());
        }
        result.getFuture().complete(responseData);
    }

    public static Result setResult(String requestId){
        Result result = new Result(requestId);
        CompletableFuture<Object> future = new CompletableFuture();
        result.set(future);
        futureCache.put(requestId, result);
        return result;
    }

    public static void removeFutureByRequestId(String requestId){
        futureCache.remove(requestId);
    }
}
