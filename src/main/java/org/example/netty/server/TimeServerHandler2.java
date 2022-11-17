package org.example.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import java.util.Date;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/16 15:13
 */
public class TimeServerHandler2 extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String body = (String) msg;

        System.out.println("The time server receive order : " + body + "; the counter is : " + ++counter);

        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";

        currentTime = currentTime + System.getProperty("line.separator");

        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();
    }
}
