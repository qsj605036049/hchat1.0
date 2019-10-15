package com.itheima.hchat.service.impl;

import com.itheima.hchat.exception.CustomException;
import com.itheima.hchat.mapper.TbFriendMapper;
import com.itheima.hchat.mapper.TbFriendReqMapper;
import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.*;
import com.itheima.hchat.pojo.vo.ReqUser;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.FriendService;
import com.itheima.hchat.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author qinshiji
 * @data 2019/7/23 15:03
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private TbFriendMapper tbFriendMapper;
    @Autowired
    private TbFriendReqMapper tbFriendReqMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbUserMapper tbUserMapper;
    @Override
    public void sendRequest(String fromUserid, String toUserid) {
        if (fromUserid.equals(toUserid)){
            throw new CustomException(false, "不能添加自己为好友");
        }
        TbFriendExample example = new TbFriendExample();
        TbFriendExample.Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(fromUserid);
        criteria.andFriendsIdEqualTo(toUserid);
        List<TbFriend> tbFriends = tbFriendMapper.selectByExample(example);
        if (tbFriends!=null&&tbFriends.size()>0){
            throw new CustomException(false, "对方已为您的好友");
        }
        TbFriendReqExample reqExample = new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria1 = reqExample.createCriteria();
        criteria1.andFromUseridEqualTo(fromUserid);
        criteria1.andToUseridEqualTo(toUserid);
        criteria1.andStatusEqualTo(1);
        List<TbFriendReq> tbFriendReqs = tbFriendReqMapper.selectByExample(reqExample);
        if (tbFriendReqs!=null&&tbFriendReqs.size()>0){
            throw new CustomException(false, "您已申请添加对方好友");
        }
        TbFriendReq friendReq = new TbFriendReq();
        friendReq.setId(idWorker.nextId());
        friendReq.setCreatetime(new Date());
        friendReq.setFromUserid(fromUserid);
        friendReq.setToUserid(toUserid);
        friendReq.setMessage("");
        friendReq.setStatus(0);
        tbFriendReqMapper.insert(friendReq);
    }

    @Override
    public List<ReqUser> findFriendReqByUserid(String userid) {
        TbFriendReqExample tbFriendReqExample = new TbFriendReqExample() ;
        TbFriendReqExample.Criteria criteria = tbFriendReqExample.createCriteria();
        criteria.andToUseridEqualTo(userid);
        criteria.andStatusEqualTo(0);
        List<TbFriendReq> tbFriendReqs = tbFriendReqMapper.selectByExample(tbFriendReqExample);
        List<ReqUser> userList = new ArrayList<>();
        for (TbFriendReq friendReq : tbFriendReqs) {
            String fromUserid = friendReq.getFromUserid();
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(fromUserid);
            ReqUser reqUser = new ReqUser();
            BeanUtils.copyProperties(tbUser, reqUser);
            reqUser.setId(friendReq.getId());
            userList.add(reqUser);
        }
        return userList;
    }

    @Override
    public void ignoreFriendReq(String reqid) {
        updateFriendReqById(reqid);

    }

    private TbFriendReq updateFriendReqById(String reqid) {
        TbFriendReq friendReq = tbFriendReqMapper.selectByPrimaryKey(reqid);
        if (friendReq == null) {
            throw new CustomException(false, "该申请不存在");
        }
        friendReq.setStatus(1);
        tbFriendReqMapper.updateByPrimaryKey(friendReq);
        return friendReq;
    }

    @Override
    public void acceptFriendReq(String reqid) {
        TbFriendReq tbFriendReq = updateFriendReqById(reqid);
        TbFriend tbFriend = new TbFriend();
        tbFriend.setComments("");
        tbFriend.setUserid(tbFriendReq.getFromUserid());
        tbFriend.setFriendsId(tbFriendReq.getToUserid());
        tbFriend.setCreatetime(new Date());
        tbFriend.setId(idWorker.nextId());
        TbFriend tbFriend1 = new TbFriend();
        tbFriend1.setComments("");
        tbFriend1.setUserid(tbFriendReq.getToUserid());
        tbFriend1.setFriendsId(tbFriendReq.getFromUserid());
        tbFriend1.setCreatetime(new Date());
        tbFriend1.setId(idWorker.nextId());
        tbFriendMapper.insert(tbFriend);
        tbFriendMapper.insert(tbFriend1);
    }

    @Override
    public List<User> findFriendByUserid(String userid) {
        TbFriendExample tbFriendExample = new TbFriendExample();
        TbFriendExample.Criteria criteria = tbFriendExample.createCriteria();
        criteria.andUseridEqualTo(userid);
        List<User> userList = new ArrayList<>();
        List<TbFriend> tbFriends = tbFriendMapper.selectByExample(tbFriendExample);
        for (TbFriend tbFriend : tbFriends) {
            String friendsId = tbFriend.getFriendsId();
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(friendsId);
            User user = new User();
            BeanUtils.copyProperties(tbUser, user);
            userList.add(user);
        }
        return userList;
    }
}
