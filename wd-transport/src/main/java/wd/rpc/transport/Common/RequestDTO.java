package wd.rpc.transport.Common;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class RequestDTO implements Serializable {

    private static final Long serialVersionUID = -1L;

    private String requestId;

    private String url;

    private Class<?> targetService;

    private Class<?>[] paramsTypes;

    private String targetMethod;

    private Object[] params;
}
