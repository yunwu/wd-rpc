package wd.rpc.transport.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import wd.rpc.transport.Common.GlobalContents;
import wd.rpc.transport.Common.GlobalContext;
import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.Common.exceptions.ErrorCode;
import wd.rpc.transport.Common.exceptions.RpcException;
import wd.rpc.transport.invoker.Invoker;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Slf4j
public class ZkRegistry implements Registry{

    private static final String ZOOKEEPER_URL_KEY = "zookeeper.url";
    private static final String RPC_PORT_KEY = "rpc.port";

    private static ZooKeeper zooKeeper;
    private int port = 2884;

    private static Watcher watcher;
    static {
        watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("zk事件" + watchedEvent.toString());
            }
        };
        try {
            String value = GlobalContext.getByKeyOrDefault(ZOOKEEPER_URL_KEY,"");
            if (value == null || "".equals(value.trim())){
                throw new RpcException(ErrorCode.CONFIG_ITEM_ERROR, "zookeeper 链接地址配置异常");
            }
            zooKeeper = new ZooKeeper(value, 3000, watcher);
        } catch (Exception e) {
            log.error("链接zookeeper异常，", e);
            throw new RpcException(ErrorCode.CONFIG_ITEM_ERROR, "链接zookeeper异常",e);
        }
    }



    @Override
    public void regist(Invoker invoker, RpcContext context) {
        //管理service

        try {
            port = Integer.parseInt(GlobalContext.getByKeyOrDefault(RPC_PORT_KEY, port+""));
            String address = InetAddress.getLocalHost().getHostAddress() + ":" + port;
            String path = "/" + invoker.getServiceName();
            Stat stat =  zooKeeper.exists(path, false);

            if (stat == null){
                zooKeeper.create(path, address.getBytes(),  ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }else {
                byte[] addresses = zooKeeper.getData(path, false, null);
                String addressStr = filterDump(new String(addresses), address);
                zooKeeper.delete(path, stat.getVersion());
                zooKeeper.create(path, addressStr.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            log.error("service regist error, ",e);
            throw new RpcException(ErrorCode.ZOOKEEPER_ERROR, "service regist error",e);
        }
    }

    private String filterDump(String addressesStr, String address){
        String[] addressArr = addressesStr.split(GlobalContents.COMMA_SEPARATOR);
        if (addressArr == null || addressArr.length == 0){
            return address;
        }
        List<String> result = new ArrayList<>(Arrays.asList(addressArr));
        result.add(address);
        return result.stream().filter(Objects::nonNull).distinct().collect(Collectors.joining());
    }

    @Override
    public String getUrl(String serviceName) {
        try {
            return new String(zooKeeper.getData("/"+serviceName, false, null));
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
