package com.dcch.sharebike.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private NettyListener listener;

    private int WRITE_WAIT_SECONDS = 10;

    private int READ_WAIT_SECONDS = 13;

    public NettyClientInitializer(NettyListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        SslContext sslCtx = SslContextBuilder.forClient()
//                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
//
        ChannelPipeline pipeline = ch.pipeline();
//        pipeline.addLast(sslCtx.newHandler(ch.alloc()));    // 开启SSL
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));    // 开启日志，可以设置日志等级
//        pipeline.addLast(new IdleStateHandler(30, 60, 100));
        pipeline.addLast(new NettyClientHandler(listener));
    }

}
