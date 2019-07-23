package com.bob.common.utils.request.post.entity;

import lombok.Data;

/**
 * 通用情报数据
 *
 * @author wb-jjb318191
 * @create 2019-07-22 10:25
 */
@Data
public abstract class CommonRiskData {

    /**
     * msg唯一ID
     */
    protected Long messageUuid;

    /**
     * 消息内容
     */
    protected String message;

    /**
     * 是否是群消息 1：是, 0：否
     */
    protected Integer isTroop;

    /**
     * 群号
     */
    protected String groupNum;

    /**
     * 消息类型
     */
    protected Long messageType;

    /**
     * 发送信息账号
     */
    protected String messageAccount;

    /**
     * 种子账号
     */
    protected String seedAccount;

    /**
     * 图片地址,如果是图片消息
     */
    protected String picUrl;

    /**
     * 上报时间戳
     */
    protected Long reportTime;

}
