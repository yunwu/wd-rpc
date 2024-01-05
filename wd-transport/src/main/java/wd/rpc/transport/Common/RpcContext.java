package wd.rpc.transport.Common;


import lombok.Data;

import java.util.Map;

@Data
public class RpcContext {

    private String targetMethod;

    private Class<?>[] paramsTypes;

    private Map<String, Object> params;

    private int timeout = 1000;
}
