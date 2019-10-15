package com.itheima.hchat.netty;

import io.netty.channel.Channel;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

/**
 * userId与channel用到关联集合
 * @author qinshiji
 * @data 2019/7/24 11:12
 */
@Component
public class UserChannelMap {
    private static Map<String, Channel> userChannelMap = new HashMap<>();

    public static void put(String userId, Channel channel){
        userChannelMap.put(userId, channel);
    }

    public static Channel get(String userId){
        return userChannelMap.get(userId);
    }

    /**
     * 删除关联
     * @param channelId 通道id
     */
    public static void removeByChannelId(String channelId){
        if (StringUtils.isBlank(channelId)){
            return;
        }
        for (String userid : userChannelMap.keySet()) {
            Channel channel = userChannelMap.get(userid);
            if (channelId.equals(channel.id().asLongText())){
                userChannelMap.remove(userid);
                break;
            }
        }
    }
}
