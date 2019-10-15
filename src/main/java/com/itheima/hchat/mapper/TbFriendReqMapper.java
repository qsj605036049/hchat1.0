package com.itheima.hchat.mapper;

import com.itheima.hchat.pojo.TbFriendReq;
import com.itheima.hchat.pojo.TbFriendReqExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Mapper
public interface TbFriendReqMapper {
    int countByExample(TbFriendReqExample example);

    int deleteByExample(TbFriendReqExample example);

    int deleteByPrimaryKey(String id);

    int insert(TbFriendReq record);

    int insertSelective(TbFriendReq record);

    List<TbFriendReq> selectByExample(TbFriendReqExample example);

    TbFriendReq selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TbFriendReq record, @Param("example") TbFriendReqExample example);

    int updateByExample(@Param("record") TbFriendReq record, @Param("example") TbFriendReqExample example);

    int updateByPrimaryKeySelective(TbFriendReq record);

    int updateByPrimaryKey(TbFriendReq record);
}