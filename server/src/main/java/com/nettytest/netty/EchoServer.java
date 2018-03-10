package com.nettytest.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: chengtong
 * @Date: 18/2/24 14:23
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() +
                            " ");
        }
        /* 设置端口值（如果端口参数的格式不正确，则抛出一个NumberFormatException）*/
        int port = Integer.parseInt("8080");
        /*调用服务器的start()方法*/
        new EchoServer(port).start();
    }
    public void start() throws Exception {
        final MyChannelHandler serverHandler = new MyChannelHandler();
        /* ➊ 创建Event-LoopGroup*/
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            /*❷ 创建Server-Bootstrap*/
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    /* ❸ 指定所使用的NIO传输Channel*/
                    .channel(NioServerSocketChannel.class)
                    /*❹ 使用指定的端口设置套接字地址*/
                    .localAddress(new InetSocketAddress(port))
                    /*❺添加一个EchoServer-Handler到子Channel的ChannelPipeline*/
                    .childHandler(new ChannelInitializer(){
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            /*EchoServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例*/
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            /*❻ 异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成*/
            ChannelFuture f = bootstrap.bind().sync();
            /*❼ 获取Channel的CloseFuture，并且阻塞当前线程直到它完成*/
            f.channel().closeFuture().sync();
        } finally {
            /*❽ 关闭EventLoopGroup，释放所有的资源*/
            group.shutdownGracefully().sync();
        }
    }
}
