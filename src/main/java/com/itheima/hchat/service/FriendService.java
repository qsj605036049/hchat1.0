package com.itheima.hchat.service;

import com.itheima.hchat.pojo.vo.ReqUser;
import com.itheima.hchat.pojo.vo.User;

import java.util.List;

/**
 * @author qinshiji
 * @data 2019/7/23 15:03
 */
public interface FriendService {
    /**
     * 发送好友申请
     * @param fromUserid 用户id
     * @param toUserid 好友id
     */
    void sendRequest(String fromUserid, String toUserid);

    /**
     * 查询好友申请列表
     * @param userid
     * @return
     */
    List<ReqUser> findFriendReqByUserid(String userid);

    /**
     * 忽略好友请求
     * @param reqid 请求id
     */
    void ignoreFriendReq(String reqid);

    /**
     * 接受好友请求
     * @param reqid 请求id
     */
    void acceptFriendReq(String reqid);

    /**
     * 查询联系人列表
     * @param userid 用户id
     * @return
     */
    List<User> findFriendByUserid(String userid);
}
