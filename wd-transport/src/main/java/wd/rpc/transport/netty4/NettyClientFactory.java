package wd.rpc.transport.netty4;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClientFactory {

    private static final Map<String, NettyClient> nettyClientMap = new ConcurrentHashMap<>();
    public NettyClient createClient(String url){
        NettyClient client = new NettyClient(url);
        return client;
    }

    public NettyClient getClient(String url){
        NettyClient client = nettyClientMap.get(url);
        if (client == null){
            synchronized (NettyClientFactory.class){
                client = nettyClientMap.get(url);
                if (client == null){
                    client = createClient(url);
                }
            }
        }
        return client;
    }
}
