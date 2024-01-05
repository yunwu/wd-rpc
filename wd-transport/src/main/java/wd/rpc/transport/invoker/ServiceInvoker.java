package wd.rpc.transport.invoker;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import wd.rpc.transport.Common.RequestDTO;
import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.Common.servicestrategy.ServiceSchedulerStrategy;
import wd.rpc.transport.netty4.NettyClient;
import wd.rpc.transport.netty4.NettyClientFactory;
import wd.rpc.transport.netty4.ResponseHandler;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ServiceInvoker<T> implements Invoker<T>{

    //服务实现类
    private Class<T> targetService;

    //服务名称
    private String serviceName;

    private NettyClientFactory nettyClientFactory = new NettyClientFactory();

    public ServiceInvoker(Class<T> targetService){
        //如果是调用端，则targetService是接口，serviceName是接口名
        if(targetService.isInterface()){
            this.targetService = targetService;
            this.serviceName = targetService.getName();
        }else {
           //如果是实现类，则serviceName是接口名，targetService是实现类
           this.targetService = targetService;
           Class[] interfaces = targetService.getInterfaces();
           if (interfaces != null && interfaces.length ==1){
               serviceName = interfaces[0].getName();
           }
        }
    }

    @Override
    public Object invoke(RpcContext context) {
        String url = ServiceSchedulerStrategy.getUrlByStrategy(serviceName, null);
        NettyClient client = nettyClientFactory.createClient(url);
        //构造request
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setTargetService(targetService);
        requestDTO.setTargetMethod(context.getTargetMethod());
        requestDTO.setParamTypes(context.getParamsTypes());
        requestDTO.setParams(context.getParams());
        client.connect();
        ChannelFuture future = client.sendMsg(requestDTO);
        ResponseHandler.setResult(future.channel());
        Object result = ResponseHandler.getResponse(future.channel(),context.getTimeout(), TimeUnit.MILLISECONDS);
        return result;
    }

    @Override
    public Class<T> getTargetService() {
        return targetService;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }
}
