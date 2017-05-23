//package com.dcch.sharebike.netty;
//
//import io.netty.buffer.ByteBuf;
//
///**
// * Created by Administrator on 2017/5/15 0015.
// */
//
//public interface NettyListener {
//
//    byte STATUS_CONNECT_SUCCESS = 1;
//
//    byte STATUS_CONNECT_CLOSED = 0;
//
//    byte STATUS_CONNECT_ERROR = 0;
//
//
//    /**
//     * 对消息的处理
//     */
//    void onMessageResponse(ByteBuf byteBuf);
//
//    /**
//     * 当服务状态发生变化时触发
//     */
//    void onServiceStatusConnectChanged(int statusCode);
//
//}
