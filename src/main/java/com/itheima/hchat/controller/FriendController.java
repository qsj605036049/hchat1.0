package com.itheima.hchat.controller;

import com.itheima.hchat.exception.CustomException;
import com.itheima.hchat.pojo.TbFriendReq;
import com.itheima.hchat.pojo.vo.ReqUser;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.FriendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qinshiji
 * @data 2019/7/23 14:59
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @RequestMapping("/sendRequest")
    public Result sendRequest(@RequestBody TbFriendReq tbFriendReq){
        String fromUserid = tbFriendReq.getFromUserid();
        String toUserid = tbFriendReq.getToUserid();
        if (StringUtils.isBlank(fromUserid)||StringUtils.isBlank(toUserid)){
            throw new CustomException(false, "非法入参");
        }
        friendService.sendRequest(fromUserid,toUserid);
        return new Result(true, "添加成功");
    }

    @RequestMapping("/findFriendReqByUserid")
    public List<ReqUser> findFriendReqByUserid(String userid){
        if (StringUtils.isBlank(userid)){
            throw new CustomException(false, "非法入参");
        }
        return friendService.findFriendReqByUserid(userid);
    }

    /**
     * 忽略好友请求
     * @param reqid 请求id
     * @return
     */
    @RequestMapping("/ignoreFriendReq")
    public Result ignoreFriendReq(String reqid){
        if (StringUtils.isBlank(reqid)){
            throw new CustomException(false, "非法入参");
        }
        friendService.ignoreFriendReq(reqid);
        return new Result(true, "操作成功");

    }

    @RequestMapping("/acceptFriendReq")
    public Result acceptFriendReq(String reqid){
        if (StringUtils.isBlank(reqid)){
            throw new CustomException(false, "非法入参");
        }
        friendService.acceptFriendReq(reqid);
        return new Result(true, "操作成功");
    }

    @RequestMapping("/findFriendByUserid")
    public List<User> findFriendByUserid(String userid){
        if (StringUtils.isBlank(userid)){
            throw new CustomException(false, "非法入参");
        }
        return friendService.findFriendByUserid(userid);
    }

}
