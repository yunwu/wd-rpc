package wd.rpc.transport.Common;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Data
@Getter
@Setter
public class RequestDTO implements Serializable {

    private static final Long serialVersionUID = -1L;

    private String url;

    private Class<?> targetService;

    private String targetMethod;

    private Class<?>[] paramTypes;

    private Map<String, Object> params;
}
