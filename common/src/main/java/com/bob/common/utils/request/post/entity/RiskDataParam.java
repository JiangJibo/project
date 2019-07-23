package com.bob.common.utils.request.post.entity;

import lombok.Data;

/**
 * 情报上传参数
 *
 * @author wb-jjb318191
 * @create 2019-07-22 10:30
 */
@Data
public class RiskDataParam<T extends CommonRiskData> {

    /**
     * 种子类型
     *
     * @see SeedType#name()
     */
    private String seedType;

    /**
     * 情报数据
     */
    private T data;

    /**
     * 指定{@link T} 的实际类型
     *
     * @return
     */
    public Class<? extends CommonRiskData> resolveDataType() {
        SeedType type = SeedType.valueOf(seedType);
        switch (type) {
            case OO:
                return OORiskDataParam.class;
            default:
                throw new IllegalArgumentException(String.format("非允许的种子类型[%s]", seedType));
        }
    }
}
