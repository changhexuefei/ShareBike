package com.dcch.sharebike.netty;

import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.utils.LogUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import timber.log.Timber;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class NettyClient {
    private static NettyClient nettyClient = new NettyClient();

    private EventLoopGroup group;

    private NettyListener listener;

    private Channel channel;

    private boolean isConnect = false;

    private int reconnectNum = Integer.MAX_VALUE;

    private long reconnectIntervalTime = 5000;

    public static NettyClient getInstance() {
        return nettyClient;
    }

    public synchronized NettyClient connect() {
        if (!isConnect) {
            group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap().group(group)
//                    .option(ChannelOption.SO_TIMEOUT,10)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer(listener));

            try {
                bootstrap.connect(MyContent.HOST, MyContent.TCP_PORT).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            isConnect = true;
                            channel = channelFuture.channel();
                        } else {
                            isConnect = false;
                        }
                    }
                }).sync();

            } catch (Exception e) {
                Timber.e(e, e.getMessage());
                LogUtils.d("netty", "yichang" + e.getMessage());
                LogUtils.d("netty", "请检查网络连接");
                listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_ERROR);
                reconnect();
            }
        }
        return this;
    }

    public void disconnect() {
        group.shutdownGracefully();
    }

    public void reconnect() {
        if (reconnectNum > 0 && !isConnect) {
            reconnectNum--;
            try {
                Thread.sleep(reconnectIntervalTime);
            } catch (InterruptedException e) {
                LogUtils.d("netty", "InterruptedException" + e.getMessage());
            }
            Timber.e("重新连接");
            disconnect();
            connect();
        } else {
            disconnect();
        }
    }

    public boolean sendMsgToServer(byte[] data, ChannelFutureListener listener) {
        boolean flag = channel != null && isConnect;
        if (flag) {
            ByteBuf buf = Unpooled.copiedBuffer(data);
            String s = new String(buf.array());
            LogUtils.d("netty", "flag" + flag + "\n" + "发送数据：" + buf + "\n" + s.toString());
            channel.writeAndFlush(buf).addListener(listener);
        }
        return flag;
    }

    public void setReconnectNum(int reconnectNum) {
        this.reconnectNum = reconnectNum;
    }

    public void setReconnectIntervalTime(long reconnectIntervalTime) {
        this.reconnectIntervalTime = reconnectIntervalTime;
    }

    public boolean getConnectStatus() {
        return isConnect;
    }

    public void setConnectStatus(boolean status) {
        this.isConnect = status;
    }

    public void setListener(NettyListener listener) {
        this.listener = listener;
    }
}
