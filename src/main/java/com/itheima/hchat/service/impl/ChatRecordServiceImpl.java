package com.itheima.hchat.service.impl;

import com.itheima.hchat.mapper.TbChatRecordMapper;
import com.itheima.hchat.pojo.TbChatRecord;
import com.itheima.hchat.pojo.TbChatRecordExample;
import com.itheima.hchat.service.ChatRecordService;
import com.itheima.hchat.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author qinshiji
 * @data 2019/7/24 15:53
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChatRecordServiceImpl implements ChatRecordService {
    @Autowired
    private TbChatRecordMapper tbChatRecordMapper;
    @Autowired
    private IdWorker idWorker;

    @Override
    public String insert(TbChatRecord chatRecord) {
        chatRecord.setCreatetime(new Date());
        chatRecord.setHasRead(0);
        chatRecord.setHasDelete(0);
        chatRecord.setId(idWorker.nextId());
        tbChatRecordMapper.insert(chatRecord);
        return chatRecord.getId();
    }

    @Override
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        TbChatRecordExample tbChatRecordExample = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = tbChatRecordExample.createCriteria();
        criteria.andFriendidEqualTo(userid);
        criteria.andHasReadEqualTo(0);
        return tbChatRecordMapper.selectByExample(tbChatRecordExample);
    }

    @Override
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        TbChatRecordExample tbChatRecordExample = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = tbChatRecordExample.createCriteria();
        criteria.andUseridEqualTo(friendid);
        criteria.andFriendidEqualTo(userid);
        criteria.andHasDeleteEqualTo(0);

        TbChatRecordExample.Criteria criteria1 = tbChatRecordExample.createCriteria();
        criteria1.andUseridEqualTo(userid);
        criteria1.andFriendidEqualTo(friendid);
        criteria1.andHasDeleteEqualTo(0);
        tbChatRecordExample.or(criteria);
        tbChatRecordExample.or(criteria1);
        List<TbChatRecord> tbChatRecords = tbChatRecordMapper.selectByExample(tbChatRecordExample);
//将未读消息置位已读
        TbChatRecordExample tbChatRecordExample1 = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria2 = tbChatRecordExample1.createCriteria();
        criteria2.andFriendidEqualTo(userid);
        criteria2.andUseridEqualTo(friendid);
        criteria2.andHasReadEqualTo(0);
        List<TbChatRecord> tbChatRecords1 = tbChatRecordMapper.selectByExample(tbChatRecordExample1);
        for (TbChatRecord tbChatRecord : tbChatRecords1) {
            tbChatRecord.setHasRead(1);
            tbChatRecordMapper.updateByPrimaryKey(tbChatRecord);
        }

        return tbChatRecords;
    }

    @Override
    public void updateChatRecordToRead(String chatRecordId) {
        TbChatRecord chatRecord = tbChatRecordMapper.selectByPrimaryKey(chatRecordId);
        chatRecord.setHasRead(1);
        tbChatRecordMapper.updateByPrimaryKey(chatRecord);
    }

}
