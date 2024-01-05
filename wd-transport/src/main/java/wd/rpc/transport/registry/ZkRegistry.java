package wd.rpc.transport.registry;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import wd.rpc.transport.Common.RpcContext;
import wd.rpc.transport.invoker.Invoker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ZkRegistry implements Registry{

    private static final String SEPARATOR = ",";

    private static ZooKeeper zooKeeper;
    //TODO port 设置
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
            zooKeeper = new ZooKeeper("localhost:2181", 3000, watcher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void regist(Invoker invoker, RpcContext context) {
        //管理service

        try {
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
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String filterDump(String addressesStr, String address){
        String[] addressArr = addressesStr.split(SEPARATOR);
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
