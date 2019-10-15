package com.itheima.hchat.pojo.vo;

import com.itheima.hchat.pojo.TbChatRecord;

import java.io.Serializable;

/**
 * @author qinshiji
 * @data 2019/7/24 11:23
 */
public class Message implements Serializable {
    /**
     * 消息类型
     */
    private Integer type;
    /**
     * 消息体
     */
    private TbChatRecord chatRecord;
    /**
     * 扩展字段
     */
    private String ext;

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", chatRecord=" + chatRecord +
                ", ext='" + ext + '\'' +
                '}';
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public TbChatRecord getChatRecord() {
        return chatRecord;
    }

    public void setChatRecord(TbChatRecord chatRecord) {
        this.chatRecord = chatRecord;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
