package com.bob.mvc.model;

/**
 * 数据库表：ieasy_content_area
 * 
 * @author wb-jjb318191
 * @create 2018-01-18
 */
public class IeasyContentArea {
    /**
     * id
     */
    private Long id;

    /**
     * 主键ID
     */
    private Long contentId;

    /**
     * 服务内容名称
     */
    private String contentName;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 区域名称
     */
    private String areaName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName == null ? null : contentName.trim();
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }
}