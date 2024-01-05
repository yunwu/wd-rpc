package wd.rpc.transport.consumer;


import lombok.extern.slf4j.Slf4j;
import wd.rpc.transport.registry.Registry;
import wd.rpc.transport.registry.RegistryFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceConsumer implements Consumer{

    private static final String registryName = "zookeeper";

    private static final String URL_SEPARATOR = ",";

    //服务与地址的映射关系
    private static final Map<String, List<String>> serviceUrlCache = new ConcurrentHashMap<>();

    //TODO 线程安全问题需要考虑
    private Set<String> serviceCache = new HashSet<>();

    @Override
    public void loadAllNeedRemoteAddress() {

    }

    @Override
    public void refreshAllServiceRemoteAddress() {
        for (String service : serviceUrlCache.keySet()) {
            refreshSignalServiceRemoteAddress(service);
        }
    }

    @Override
    public void refreshSignalServiceRemoteAddress(String serviceName){
        Registry registry = null;
        try {
            registry = RegistryFactory.getRegistry(registryName);
        } catch (Exception e) {
            log.error("获取registry error");
            return;
        }
        String url = registry.getUrl(serviceName);
        //如果拿不到URL 暂不处理，交给定时人物强制刷新数据
        if (url != null && url.trim().length() >0){
            List<String> addresses = Arrays.asList(url.split(URL_SEPARATOR));
            serviceUrlCache.put(serviceName, addresses);
            serviceCache.add(serviceName);
        }
    }


    //TODO 场景需要再仔细考虑一下
    public List<String> getServiceAddresses(String serviceName) {
        List<String> urls = serviceUrlCache.get(serviceName);
        if (urls == null || urls.size() == 0){
            refreshSignalServiceRemoteAddress(serviceName);
        }
        return serviceUrlCache.get(serviceName);
    }
}
