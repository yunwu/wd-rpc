package wd.rpc.transport.netty4;

import com.alibaba.fastjson2.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {

    private Bootstrap bootstrap;

    private Channel channel;

    private String ip;

    private int port;

    public NettyClient(String url){
        bootstrap = new Bootstrap();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()//.addLast("logging",new LoggingHandler(LogLevel.INFO))//for debug
                                .addLast("decoder",new StringDecoder(CharsetUtil.UTF_8))
                                .addLast("encoder", new StringEncoder(CharsetUtil.UTF_8))
                                .addLast(new NettyClientHandler())
                                .addLast(new NettyClientOutBoundHandler());
                    }
                });
        String[] urlArr = url.split(":");
        ip = urlArr[0];
        port = Integer.parseInt(urlArr[1]);

    }
    public void sendMsg(Object msg){
        log.info("send msg,{}", msg);
        channel.writeAndFlush(JSON.toJSONString(msg));
    }

    public void connect(){
        ChannelFuture channelFuture = bootstrap.connect(ip, port);
        this.channel = channelFuture.channel();
    }

    public void closeChannel(){
        channel.close();
    }
}
