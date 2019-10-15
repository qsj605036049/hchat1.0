package com.itheima.hchat.netty;


import com.alibaba.fastjson.JSON;
import com.itheima.hchat.pojo.TbChatRecord;
import com.itheima.hchat.pojo.vo.Message;
import com.itheima.hchat.service.ChatRecordService;
import com.itheima.hchat.service.impl.ChatRecordServiceImpl;
import com.itheima.hchat.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import jdk.nashorn.internal.ir.IfNode;


/**
 * 处理消息的handler
 * TextWebSocketFrame: 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 * @author qinshiji
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {



    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    /**
     * 接收到客户端发来消息触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        ChatRecordService chatRecordService = SpringUtil.getBean(ChatRecordService.class);
        String text = msg.text();
        Message message = JSON.parseObject(text, Message.class);
        Integer type = message.getType();
        switch (type){
            case 0:
//                1.1 websocket第一次连接时候,初始化channel,绑定userId与channel
                String userid = message.getChatRecord().getUserid();
                Channel channel = ctx.channel();
                UserChannelMap.put(userid, channel);
                break;
            case 1:
//                2.1 记录聊天消息到数据库并标记<未签收>
                TbChatRecord chatRecord = message.getChatRecord();
                chatRecord.setHasRead(0);
                String chatRecordId = chatRecordService.insert(chatRecord);
                chatRecord.setId(chatRecordId);

//                2.2 判断用户是否在线
                String friendid = chatRecord.getFriendid();
                Channel friendChannel = UserChannelMap.get(friendid);
//                2.3 在线发送消息到对应通道
                if (friendChannel!=null){
                    if (clients.contains(friendChannel)){
                        chatRecord.setHasRead(1);
                        friendChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                    }
                }

                break;
            case 2:
//               签收消息
                chatRecordService.updateChatRecordToRead(message.getChatRecord().getId());
                break;
            case 3:
//                接收到心跳信息
                break;
            default:
        }

    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channel，并且放入到ChannelGroup中去进行管理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 将channel添加到客户端
        clients.add(ctx.channel());
    }

    /**
     * 断开连接时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        clients.remove(ctx.channel());
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
    }

    /**
     * 抛出异常时触发 移除通道
     * @param ctx 通道处理内容
     * @param cause 异常
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        channel.close();
        clients.remove(channel);
        UserChannelMap.removeByChannelId(channel.id().asLongText());
    }
}
