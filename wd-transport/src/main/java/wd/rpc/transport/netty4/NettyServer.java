package wd.rpc.transport.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyServer {

    private Bootstrap bootstrap;

    private Channel channel;

    private EventLoopGroup boss;

    private EventLoopGroup worker;

    public NettyServer(){
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup(10);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast("decoder",new StringDecoder(CharsetUtil.UTF_8))
                                    .addLast("encoder", new StringEncoder(CharsetUtil.UTF_8))
                                    .addLast(new NettyServerHandler())
                                    .addLast(new NettyServerOutHandler());
                        }
                    })
                    .bind(InetAddress.getLocalHost().getHostAddress(),2884);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public void closeServer(){
        try {
            channel.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (boss != null){
                boss.shutdownGracefully();
            }
            if (worker != null){
                worker.shutdownGracefully();
            }
        }
    }
}
