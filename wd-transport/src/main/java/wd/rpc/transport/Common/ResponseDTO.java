package wd.rpc.transport.Common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 返回结果
     */
    private Object data;

    /**
     * 执行异常
     */
    private Exception exception;
}
