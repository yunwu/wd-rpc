package wd.rpc.transport.netty4;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import wd.rpc.transport.Common.GlobalContents;
import wd.rpc.transport.Common.RequestDTO;
import wd.rpc.transport.Common.exceptions.ErrorCode;
import wd.rpc.transport.Common.exceptions.RpcException;
import wd.rpc.transport.provider.ServiceProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@Slf4j
public class RequestHandler {


    public void request(Channel channel, Object msg){
        //通过反射实现方法调用
         RequestDTO request = JSON.parseObject((String)msg,RequestDTO.class);
         Class targetClass = request.getTargetService();
        try {
            if (!targetClass.isInterface()){
                throw new RuntimeException("执行方法非接口");
            }
            targetClass = ServiceProvider.getTarget(targetClass.getName());
            Object target = targetClass.newInstance();
            Method method = targetClass.getMethod(request.getTargetMethod(),request.getParamTypes());
            Object result = method.invoke(target, request.getParams().values().toArray());
            channel.writeAndFlush(result);
        } catch (Exception e) {
            log.error("request run error,{},", msg, e);
            throw new RpcException(ErrorCode.SERVICE_RUN_ERROR, "request run error", e);
        }
    }
}
