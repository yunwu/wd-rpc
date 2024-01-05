package wd.rpc.transport.Common.exceptions;

public class RpcException extends RuntimeException{

    private int code;


    public RpcException(int code){
        super();
        this.code = code;
    }

    public RpcException(int code, String message){
        super(message);
        this.code = code;
    }

    public RpcException(int code, String message, Throwable cause){
        super(message, cause);
        this.code = code;
    }
}
