package wd.rpc.transport.Common.servicestrategy;

import wd.rpc.transport.consumer.ServiceConsumer;

import java.util.List;

public class ServiceSchedulerStrategy {


    public static String getUrlByStrategy(String serviceName, ServiceStrategyEnum strategyEnum){
        if (strategyEnum == null){
            strategyEnum = ServiceStrategyEnum.DEDAULT;
        }
        ServiceConsumer serviceConsumer = new ServiceConsumer();
        List<String> urls = serviceConsumer.getServiceAddresses(serviceName);
        if (urls == null || urls.size() == 0){
            return null;
        }
        return strategyEnum.serviceStrategy.getUrl(urls);
    }

    public enum ServiceStrategyEnum {

        RANDOM("random",new RandomStrategy()),
        DEDAULT("default", new RandomStrategy());

        private String strategy;

        private ServiceStrategy serviceStrategy;

        ServiceStrategyEnum(String strategy, ServiceStrategy serviceStrategy){
            this.strategy = strategy;
            this.serviceStrategy = serviceStrategy;
        }
    }
}
