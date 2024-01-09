package wd.rpc.transport.netty4;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import wd.rpc.transport.Common.RequestDTO;
import wd.rpc.transport.Common.ResponseDTO;
import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.invoker.Invoker;
import wd.rpc.transport.invoker.SkeletonInvoker;
import wd.rpc.transport.provider.ServiceProvider;
import wd.rpc.transport.proxy.JdkProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


@Slf4j
public class RequestHandler<T> {



    public void request(Channel channel, Object msg){

        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String msgStr = (String)msg;
            RequestDTO request = JSON.parseObject(msgStr,RequestDTO.class, JSONReader.Feature.SupportClassForName);

            responseDTO.setRequestId(request.getRequestId());
            Class targetClass = request.getTargetService();
            if (!targetClass.isInterface()){
                throw new RuntimeException("执行方法非接口");
            }
            Invoker target = new SkeletonInvoker(targetClass);
            RpcContext rpcContext = new RpcContext();
            rpcContext.setParamsTypes(request.getParamsTypes());
            rpcContext.setTargetMethod(request.getTargetMethod());
            rpcContext.setParams(request.getParams());
            Object result = target.invoke(rpcContext);
            responseDTO.setData(result);
        } catch (Exception e) {
            log.error("request run error,{},", msg, e);
            responseDTO.setException(e);
        }
        channel.writeAndFlush(JSON.toJSONString(responseDTO));
    }
}
