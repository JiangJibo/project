package com.bob.common.utils.request.post.entity;

import com.bob.common.utils.request.post.BodyFiled;
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
    private Long msgUid;

    /**
     * 发送时间戳
     */
    private Long time;

    /**
     * 消息类型
     */
    @BodyFiled("msgtype")
    private Long msgType;


}
