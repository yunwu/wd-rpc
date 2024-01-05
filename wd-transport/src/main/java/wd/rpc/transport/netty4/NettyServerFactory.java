package wd.rpc.transport.netty4;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyServerFactory {


    public synchronized NettyServer getServer(){
        NettyServer server = new NettyServer();
        return server;
    }
}
