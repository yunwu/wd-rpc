package wd.rpc.transport.invoker;

import wd.rpc.transport.Common.RequestDTO;
import wd.rpc.transport.Common.Result;
import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.Common.config.ServiceConfig;
import wd.rpc.transport.Common.config.ServiceConfigFactory;
import wd.rpc.transport.Common.exceptions.ErrorCode;
import wd.rpc.transport.Common.exceptions.RpcException;
import wd.rpc.transport.exchange.ExchangeClient;
import wd.rpc.transport.netty4.ResponseHandler;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class StubInvoker<T> implements Invoker{

    private ServiceConfig serviceConfig;

    private ExchangeClient exchangeClient;


    public StubInvoker(Class<T> service){
        this.serviceConfig = ServiceConfigFactory.getServiceConfig(service);
        exchangeClient = new ExchangeClient();
    }

    //TODO 返回result实体，这样可以进行再处理， 不要直接返回data,对后续处理逻辑不友好
    @Override
    public Object invoke(RpcContext context) {
        //构造request
        RequestDTO requestDTO = new RequestDTO();
        String requestId = UUID.randomUUID().toString();
        requestDTO.setRequestId(requestId);
        requestDTO.setTargetService(getTargetService());
        requestDTO.setTargetMethod(context.getTargetMethod());
        requestDTO.setParams(context.getParams());
        requestDTO.setParamsTypes(context.getParamsTypes());
        Result result = exchangeClient.sendRequest(getServiceName(), requestDTO);
        Object data = null;

        try {
            CompletableFuture<Object> future = result.getFuture();
            data = future.get(context.getTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RpcException(ErrorCode.SERVICE_RUN_ERROR, "get response error", e.getCause());
        }finally {
            ResponseHandler.removeFutureByRequestId(requestId);
        }
        return data;
    }

    @Override
    public Class getTargetService() {
        return this.serviceConfig.getTargetService();
    }

    @Override
    public String getServiceName() {
        return this.serviceConfig.getPath();
    }
}
