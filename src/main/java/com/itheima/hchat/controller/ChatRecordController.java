package com.itheima.hchat.controller;

import com.itheima.hchat.exception.CustomException;
import com.itheima.hchat.pojo.TbChatRecord;
import com.itheima.hchat.service.ChatRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 聊天记录
 * @author qinshiji
 * @data 2019/7/25 9:47
 */
@RestController
@RequestMapping("/chatrecord")
public class ChatRecordController {

    @Autowired
    private ChatRecordService chatRecordService;
    /**
     * 查询聊天记录列表
     * @param userid
     * @return
     */
    @RequestMapping("/findUnreadByUserid")
    public List<TbChatRecord> findUnreadByUserid(String userid){
        if (StringUtils.isBlank(userid)) {
            throw new CustomException(false, "用户id不可为空");
        }
        return chatRecordService.findUnreadByUserid(userid);
    }

    /**
     * 查询对应好友聊天记录 并把聊天记录置为已读
     * @param userid
     * @param friendid
     * @return
     */
    @RequestMapping("/findByUserIdAndFriendId")
    public List<TbChatRecord> findByUserIdAndFriendId(String userid,String friendid){
        if (StringUtils.isBlank(userid)||StringUtils.isBlank(friendid)) {
            throw new CustomException(false, "非法入参");
        }
       return chatRecordService.findByUserIdAndFriendId(userid,friendid);

    }
}
