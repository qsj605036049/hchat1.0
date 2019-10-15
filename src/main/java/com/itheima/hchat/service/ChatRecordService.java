package com.itheima.hchat.service;

import com.itheima.hchat.pojo.TbChatRecord;

import java.util.List;

/**
 * 聊天记录服务
 * @author qinshiji
 * @data 2019/7/24 15:52
 */
public interface ChatRecordService {
    /**
     * 添加聊天记录
     * @param chatRecord 消息
     * @return
     */
    public String insert(TbChatRecord chatRecord);

    /**
     * 查询聊天记录列表
     * @param userid
     * @return
     */
    List<TbChatRecord> findUnreadByUserid(String userid);

    /**
     * 查询好友聊天记录并记录为已读
     * @param userid
     * @param friendid
     * @return
     */
    List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid);

    /**
     * 将聊天记录置为已读
     * @param chatRecordId
     */
    void updateChatRecordToRead(String chatRecordId);
}
