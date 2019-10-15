package com.itheima.hchat.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳机制
 * @author qinshiji
 * @data 2019/7/25 11:02
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    /**
     * 客户端一段时间没有动作触发次事件
     * @param ctx channel处理上下文
     * @param evt 监听触发事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        触发事件  包括:读空闲 写空闲  读写空闲
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state()== IdleState.READER_IDLE){
                System.out.println("触发读空闲");
            }else if (event.state()==IdleState.WRITER_IDLE){
                System.out.println("触发写空闲");
            }else if (event.state()==IdleState.ALL_IDLE){
                System.out.println("触发读写空闲,关闭连接");
                ctx.channel().close();
            }
        }
    }
}
