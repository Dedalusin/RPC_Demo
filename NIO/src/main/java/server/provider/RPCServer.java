package server.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class RPCServer {

//    int coreSize = Runtime.getRuntime().availableProcessors();
//    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreSize, coreSize + 1
//            , 1, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    public void start(final Object service, int port) throws IOException {
        ServerBootstrap b = new ServerBootstrap();
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            b.group(boosGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline()
                            //添加解码器、编码器
                            .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                            .addLast(new ObjectEncoder())
                            //添加处理器
                            .addLast(new RPCRequestHandler(service));
                }
            });
            //绑定服务器
            ChannelFuture channelFuture = b.bind(port).sync();
            System.out.println("服务器启动成功， 监听端口："+port);
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
