package com.study.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {

    private final String ip;
    private final int port;

    public EchoClient(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
        System.out.println(args[0] + args[1]);
        if(args.length != 2){
            System.err.println("Usage "+ EchoClient.class.getSimpleName() + " <ip><port>");
            return;
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(ip, port).start();
    }

    private void start() throws Exception{
        EchoClientHandler handler = new EchoClientHandler();

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(ip, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }


    }
}
