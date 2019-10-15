package com.itheima.hchat.controller;

import com.itheima.hchat.exception.CustomException;
import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.UserService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author qinshiji
 * @data 2019/7/19 11:12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/login")
    public Result login(@RequestBody TbUser tbUser) {
        if (StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPassword())) {
            return new Result(false, "入参错误");
        }
        User user = userService.login(tbUser);

        if (user == null) {
            return new Result(false, "用户不存在");
        }
        return new Result(true, "登录成功", user);

    }

    @RequestMapping("/register")
    public Result register(@RequestBody TbUser tbUser) throws IOException {
        userService.register(tbUser);
        return new Result(true, "注册成功");
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile file, String userId) throws IOException {
        User user = userService.upload(file, userId);
        return new Result(true, "上传成功", user);
    }

    @RequestMapping("/updateNickname")
    public Result updateNickname(@RequestBody TbUser tbUser) {
        String nickname = tbUser.getNickname();
        String userId = tbUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(false, "用户id不可为空");
        }
        if (StringUtils.isBlank(nickname)) {
            throw new CustomException(false, "用户名称不可为空");
        }

        userService.updateNickname(userId, nickname);
        return new Result(true, "修改成功!");
    }

    @RequestMapping("/findById")
    public User findById(String userid) {
        if (StringUtils.isBlank(userid)) {
            throw new CustomException(false, "用户id不可为空");
        }
        return userService.findById(userid);
    }

    @RequestMapping("/findByUsername")
    public Result findByUsername(String userid, String friendUsername) {
        if (StringUtils.isBlank(userid)) {
            throw new CustomException(false, "用户id不可为空");
        }
        if (StringUtils.isBlank(friendUsername)) {
            throw new CustomException(false, "搜索用户名不可为空");
        }
        User user = userService.findByUsername(userid, friendUsername);
        if (user == null) {
            return new Result(false, "搜索用户不存在");
        }
        return new Result(true, "成功", user);

    }


}
