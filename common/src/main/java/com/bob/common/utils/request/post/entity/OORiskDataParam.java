package com.bob.common.utils.request.post.entity;


import com.bob.common.utils.request.post.BodyFiled;
import lombok.Data;

/**
 * 信息采集
 *
 * @author wb-jjb318191
 * @create 2019-07-17 9:47
 */
@Data
public class OORiskDataParam extends CommonRiskData {

    /**
     * 群消息内容
     */
    private String msg;

    /**
     * 是否是群消息 1：是, 0：否
     */
    @BodyFiled("istroop")
    private Integer isTroop;

    /**
     * 群号
     */
    @BodyFiled("group_num")
    private String groupNum;

    /**
     * 种子账号
     */
    private String seedAccount;

    /**
     * 图片地址,如果是图片消息
     */
    @BodyFiled("pic_url")
    private String picUrl;


}
