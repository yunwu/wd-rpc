package wd.rpc.transport.consumer;

import wd.rpc.transport.Common.RpcContext;

import java.util.List;
import java.util.concurrent.Future;

public interface Consumer {

    /**
     * 保存用到的remote服务的地址
     * 需要加载的所有的service
     */

    void loadAllNeedRemoteAddress();

    /**
     * 出现问题时刷新服务地址
     */
    void refreshAllServiceRemoteAddress();

    /**
     * 刷新单个服务的地址
     */
    void refreshSignalServiceRemoteAddress(String serviceName);
}
