package com.bob.common.utils.request.post.entity;

import lombok.Data;

/**
 * 信息采集
 *
 * @author wb-jjb318191
 * @create 2019-07-17 9:47
 */
@Data
public class OORiskDataParam extends CommonRiskData {

    @FieldMapping("msgUid")
    public Long getMessageUuid() {
        return super.messageUuid;
    }

    @FieldMapping("msg")
    public String getMessage() {
        return super.message;
    }

    @FieldMapping("istroop")
    public Integer getIsTroop() {
        return super.isTroop;
    }

    @FieldMapping("group_num")
    public String getGroupNum() {
        return super.groupNum;
    }

    @Override
    @FieldMapping("msgtype")
    public Long getMessageType() {
        return super.messageType;
    }

    @FieldMapping("oo_num")
    public String getMessageAccount() {
        return super.messageAccount;
    }

    @FieldMapping("seed_num")
    public String getSeedAccount() {
        return super.seedAccount;
    }

    @FieldMapping("pic_url")
    public String getPicUrl() {
        return super.picUrl;
    }

    @FieldMapping("time")
    public Long getReportTime() {
        return super.reportTime;
    }
}
