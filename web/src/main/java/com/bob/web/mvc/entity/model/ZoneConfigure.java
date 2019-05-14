package com.bob.web.mvc.entity.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.Data;

@Data
public class ZoneConfigure {

    /**
     * 分区编码
     * @see
     */
    private String zoneCode;

    /**
     * 邮编段
     */
    private List<ZipCodeSectionDTO> zipCodeSections;

    /**
     * 分区包含的邮编列表
     */
    private Set<String> zipCodes;

    /**
     * ISO的国家码
     */
    private String countryCode;

    /**
     * 解决方案code
     */
    private String solutionCode;

    /**
     * 仓库code
     */
    private String warehouseCode;

    /**
     * 是否偏远分区
     */
    private Boolean isRemote;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ZoneConfigure)) { return false; }
        ZoneConfigure that = (ZoneConfigure)o;
        return Objects.equals(getZoneCode(), that.getZoneCode()) &&
            Objects.equals(getZipCodeSections(), that.getZipCodeSections()) &&
            Objects.equals(getZipCodes(), that.getZipCodes()) &&
            Objects.equals(getCountryCode(), that.getCountryCode()) &&
            Objects.equals(getSolutionCode(), that.getSolutionCode()) &&
            Objects.equals(getWarehouseCode(), that.getWarehouseCode()) &&
            Objects.equals(getIsRemote(), that.getIsRemote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getZoneCode(), getZipCodeSections(), getZipCodes(), getCountryCode(), getSolutionCode(),
            getWarehouseCode(), getIsRemote());
    }
}
