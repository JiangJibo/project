package com.bob.web.mvc.entity.model;

import com.bob.common.entity.base.BaseModel;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据库表：geo_info
 * 
 * @author wb-jjb318191
 * @create 2018-05-24
 */
public class GeoInfo extends BaseModel {
    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 名称
     */
    private String name;

    /**
     * 业务属性ID，在业务系统中的ID
     */
    private String businessId;

    /**
     * 业务类型，业务系统中的类型
     */
    private String businessType;

    /**
     * 所属公司
     */
    private Long companyId;

    /**
     * 所属园区ID
     */
    private Long campusId;

    /**
     * 所属楼号ID
     */
    private Long buildingId;

    /**
     * 所属楼层ID
     */
    private Long floorId;

    /**
     * 启用停用
     */
    private Long status;

    /**
     * 高德楼层ID
     */
    private Integer geoFloorId;

    /**
     * 中心点经度
     */
    private BigDecimal centralx;

    /**
     * 中心点纬度
     */
    private BigDecimal centraly;

    /**
     * 数据来源
     */
    private String source;

    /**
     * null
     */
    private Object geometry;

    /**
     * null
     */
    private String poiUuid;

    /**
     * 是否删除，默认0表示未删除
     */
    private Integer isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId == null ? null : businessId.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCampusId() {
        return campusId;
    }

    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Integer getGeoFloorId() {
        return geoFloorId;
    }

    public void setGeoFloorId(Integer geoFloorId) {
        this.geoFloorId = geoFloorId;
    }

    public BigDecimal getCentralx() {
        return centralx;
    }

    public void setCentralx(BigDecimal centralx) {
        this.centralx = centralx;
    }

    public BigDecimal getCentraly() {
        return centraly;
    }

    public void setCentraly(BigDecimal centraly) {
        this.centraly = centraly;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Object getGeometry() {
        return geometry;
    }

    public void setGeometry(Object geometry) {
        this.geometry = geometry;
    }

    public String getPoiUuid() {
        return poiUuid;
    }

    public void setPoiUuid(String poiUuid) {
        this.poiUuid = poiUuid == null ? null : poiUuid.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
