package com.bob.common.string;

import com.bob.common.utils.excelmapping.ExcelColumn;
import com.bob.common.utils.excelmapping.ExcelColumn.Column;
import com.bob.common.utils.excelmapping.ExcelMapping;
import com.bob.common.utils.excelmapping.PropertyInitializer;

/**
 * @author wb-jjb318191
 * @create 2019-04-16 11:33
 */
@ExcelMapping(titleRow = 0, dataRow = 1)
public class SettlementRow implements PropertyInitializer<SettlementRow> {

    @ExcelColumn(Column.A)
    private Long cfOrderId;

    @ExcelColumn(Column.B)
    private String lgOrderCode;

    public Long getCfOrderId() {
        return cfOrderId;
    }

    public void setCfOrderId(Long cfOrderId) {
        this.cfOrderId = cfOrderId;
    }

    public String getLgOrderCode() {
        return lgOrderCode;
    }

    public void setLgOrderCode(String lgOrderCode) {
        this.lgOrderCode = lgOrderCode;
    }

    @Override
    public SettlementRow initProperties() {
        return new SettlementRow();
    }
}
