package wd.rpc.transport.Common.exceptions;

public class ErrorCode {

    /**
     * 逻辑处理参数错误
     */
    public static final int PARAM_ERROR = 1;

    /**
     * 文件读取IO异常
     */
    public static final int IO_ERROR = 2;

    /**
     * 类异常
     */
    public static final int CLASS_ERROR = 3;

    public static final int CONFIG_ITEM_ERROR = 4;

    public static final int ZOOKEEPER_ERROR = 5;

    public static final int SERVICE_RUN_ERROR = 6;

}
