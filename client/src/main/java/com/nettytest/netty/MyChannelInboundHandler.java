package com.nettytest.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: chengtong
 * @Date: 18/2/24 14:48
 */
@ChannelHandler.Sharable
public class MyChannelInboundHandler extends SimpleChannelInboundHandler{


    private LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("netty have fun", CharsetUtil.UTF_8));
        queue.add(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",    // ⇽---  当被通知Channel是活跃的时候，发送一条消息
                CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
