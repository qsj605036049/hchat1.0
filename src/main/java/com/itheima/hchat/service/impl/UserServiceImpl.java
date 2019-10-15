package com.itheima.hchat.service.impl;

import com.itheima.hchat.exception.CustomException;
import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.TbUserExample;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.UserService;
import com.itheima.hchat.utils.FastDFSClient;
import com.itheima.hchat.utils.IdWorker;
import com.itheima.hchat.utils.QRCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.ServiceMode;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author qinshiji
 * @data 2019/7/19 11:16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Value("${fdfs.httpurl}")
    private String httpurl;
    @Value("${hcat.tmpdir}")
    private String tmpdir;

    @Override
    public User login(TbUser tbUser) {
        String username = tbUser.getUsername();
        String password = tbUser.getPassword();
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if (tbUsers != null && tbUsers.size() == 1) {
            TbUser user = tbUsers.get(0);
            if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
                throw new CustomException(false, "密码错误");
            }
            User newUser = new User();
            BeanUtils.copyProperties(user, newUser);
            return newUser;

        }
        return null;
    }

    @Override
    public void register(TbUser tbUser) throws IOException {
        String username = tbUser.getUsername();
        String password = tbUser.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new CustomException(false, "非法入参");
        }
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if (tbUsers != null && tbUsers.size() > 0) {
            throw new CustomException(false, "用户名已存在");
        }

        tbUser.setId(idWorker.nextId());
        tbUser.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        tbUser.setCreatetime(new Date());
        tbUser.setNickname("");
        tbUser.setPicNormal("");
        tbUser.setPicSmall("");
//      生成二维码
        File file = new File(tmpdir);
        if (!file.exists()){
            file.mkdirs();
        }
        String tmpQrFilePath = tmpdir + username + ".png/";
        qrCodeUtils.createQRCode(tmpQrFilePath, "user_code:" + username);
        String qrUrl = fastDFSClient.uploadFile(new File(tmpQrFilePath));
        tbUser.setQrcode(qrUrl);

        tbUserMapper.insert(tbUser);
    }

    @Override
    public User upload(MultipartFile file, String userId) throws IOException {
//        高清图路径
        String url = fastDFSClient.uploadFile(file);
        String suffix = "_150*150.";
        String[] split = url.split("\\.");
//        缩略图路径
        String thumpImgUrl = split[0] + suffix + split[1];
        User user = updatePic(userId, url, thumpImgUrl);
        user.setPicNormal(httpurl + url);
        user.setPicSmall(httpurl + thumpImgUrl);
        return user;
    }

    @Override
    public void updateNickname(String userId, String nickname) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(userId);
        if (tbUser == null) {
            throw new CustomException(false, "该用户不存在");
        }
        tbUser.setNickname(nickname);
        tbUserMapper.updateByPrimaryKey(tbUser);
    }

    @Override
    public User findById(String userid) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
        if (tbUser == null) {
            throw new CustomException(false, "该用户不存在");
        }
        User user = new User();
        BeanUtils.copyProperties(tbUser, user);
        String picNormal = user.getPicNormal();
        String picSmall = user.getPicSmall();
        String qrcode = user.getQrcode();
        user.setPicSmall(httpurl+picSmall);
        user.setPicNormal(httpurl+picNormal);
        user.setQrcode(httpurl+qrcode);
        return user;

    }

    @Override
    public User findByUsername(String userid, String friendUsername) {
//        1.不能搜索自己
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
        if (tbUser==null){
            throw new CustomException(false, "该用户不存在");
        }
        if (tbUser.getUsername().equals(friendUsername)){
            throw new CustomException(false, "不能添加自己为好友");
        }
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(friendUsername);
        List<TbUser> friendUsers = tbUserMapper.selectByExample(example);
        if (friendUsers!=null&&friendUsers.size()>0){
            User user = new User();
            BeanUtils.copyProperties(friendUsers.get(0), user);
            return user;
        }
        return null;
    }

    /**
     * 更新头像信息
     *
     * @param userId
     * @param url
     * @param thumpImgUrl
     */
    private User updatePic(String userId, String url, String thumpImgUrl) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(userId);
        if (tbUser == null) {
            throw new CustomException(false, "用户不存在");
        }
        tbUser.setPicNormal(url);
        tbUser.setPicSmall(thumpImgUrl);
        tbUserMapper.updateByPrimaryKey(tbUser);
        User user = new User();
        BeanUtils.copyProperties(tbUser, user);
        return user;
    }
}
