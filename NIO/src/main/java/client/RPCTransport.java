package client;

import common.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RPCTransport {
    private String host;
    private int port;
    public Object call(RpcRequest rpcRequest) {
        final RPCResultHandler rpcResultHandler = new RPCResultHandler();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline()
                            //添加解码器、编码器
                            .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                            .addLast(new ObjectEncoder())
                            .addLast(rpcResultHandler);
                }
            }).option(ChannelOption.TCP_NODELAY, true);

            ChannelFuture channelFuture = b.connect(host, port).sync();
            channelFuture.channel().writeAndFlush(rpcRequest).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
        return rpcResultHandler.getResult();
    }
}
