package wd.rpc.transport.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    private RequestHandler requestHandler = new RequestHandler();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("启动 netty, {}" + ctx.channel().hashCode());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读到数据进行处理
        //ByteBuf buf = (ByteBuf) msg;
        log.info("收到客户端的消息:{}" + msg);
        requestHandler.request(ctx.channel(), msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("收到客户端的消息后续操作:{}" + ctx.channel().hashCode());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
       log.info("catch exception,{}", cause);
    }
}
