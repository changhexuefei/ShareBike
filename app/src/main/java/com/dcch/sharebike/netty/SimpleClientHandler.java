package com.dcch.sharebike.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


  
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {  
  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        System.out.println("SimpleClientHandler.channelRead");    
        ByteBuf result = (ByteBuf) msg;    
        byte[] result1 = new byte[result.readableBytes()];    
        result.readBytes(result1);    
        System.out.println("Server said:" + new String(result1));    
        result.release();    
    }  
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        // �������쳣�͹ر�����  
        cause.printStackTrace();  
        ctx.close();  
    }  
  
      
    // ���ӳɹ�����server������Ϣ    
    @Override    
    public void channelActive(ChannelHandlerContext ctx) throws Exception {    
        String msg = "hello Server!";    
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());    
        encoded.writeBytes(msg.getBytes());    
        ctx.write(encoded);    
        ctx.flush();    
    }    
}  