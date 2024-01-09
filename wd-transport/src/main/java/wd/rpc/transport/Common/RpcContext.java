package wd.rpc.transport.Common;


import lombok.Data;

import java.util.Map;

@Data
public class RpcContext {

    //请求参数

    private String targetMethod;

    private Class<?>[] paramsTypes;

    private Object[] params;

    //接口参数
    private int timeout = 1000;

}
