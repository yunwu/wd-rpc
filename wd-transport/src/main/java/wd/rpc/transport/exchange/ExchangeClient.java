package wd.rpc.transport.exchange;

import wd.rpc.transport.Common.RequestDTO;
import wd.rpc.transport.Common.Result;
import wd.rpc.transport.Common.servicestrategy.ServiceSchedulerStrategy;
import wd.rpc.transport.netty4.NettyClient;
import wd.rpc.transport.netty4.NettyClientFactory;
import wd.rpc.transport.netty4.ResponseHandler;


public class ExchangeClient {


    private static final NettyClientFactory nettyClientFactory = new NettyClientFactory();

    public Result sendRequest(String serviceName, RequestDTO requestDTO){
        String url = ServiceSchedulerStrategy.getUrlByStrategy(serviceName, null);
        NettyClient client = nettyClientFactory.getClient(url);
        client.connect();
        client.sendMsg(requestDTO);
        return ResponseHandler.setResult(requestDTO.getRequestId());
    }
}
