package wd.rpc.transport.consumer;

public class ConsumerFactory {

    public Consumer createConsumer(){
        return new ServiceConsumer();
    }
}
