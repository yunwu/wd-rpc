package wd.rpc.transport.netty4;

public class NettyClientFactory {

    public NettyClient createClient(String url){
        NettyClient client = new NettyClient(url);
        return client;
    }
}
