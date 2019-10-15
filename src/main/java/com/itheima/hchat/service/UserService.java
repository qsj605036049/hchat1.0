package com.itheima.hchat.service;

import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.vo.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author qinshiji
 * @data 2019/7/19 11:16
 */
public interface UserService {
    /**
     * 用户登录
     * @param tbUser
     * @return
     */
    User login(TbUser tbUser);

    /**
     * 注册用户
     * @param tbUser
     */
    void register(TbUser tbUser) throws IOException;

    /**
     * 上传头像
     * @param file
     * @param userId
     * @return
     */
    User upload(MultipartFile file, String userId) throws IOException;

    /**
     * 修改用户名称
     * @param userId 用户id
     * @param nickname 用户名称
     */
    void updateNickname(String userId, String nickname);

    /**
     * 根据id查询用户信息
     * @param userid 用户id
     * @return 用户信息
     */
    User findById(String userid);

    /**
     * 搜索好友信息
     * @param userid 用户id
     * @param friendUsername 好友用户名
     * @return
     */
    User findByUsername(String userid, String friendUsername);
}
