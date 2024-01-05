package wd.rpc.transport.registry;

import redis.clients.jedis.Jedis;
import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.invoker.Invoker;

public class RedisRegistry implements Registry{

    private static final Jedis jedis = new Jedis("localhost");
    @Override
    public void regist(Invoker invoker, RpcContext context) {

    }

    @Override
    public String getUrl(String serviceName) {
        return null;
    }
}
