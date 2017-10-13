package com.bob.mvc.model;

import java.math.BigDecimal;
import java.util.Date;

import com.bob.config.mvc.excelmapping.ExcelColumn;
import com.bob.config.mvc.excelmapping.ExcelColumn.Column;
import com.bob.config.mvc.excelmapping.ExcelMapping;
import com.bob.config.mvc.excelmapping.PropertyInitializer;

/**
 * 数据库表：bank_account
 * 
 * @author wb-jjb318191
 * @create 2017-09-26
 */
@ExcelMapping(titleRow = 0,dataRow = 1)
public class BankAccount implements PropertyInitializer<BankAccount>{
    /**
     * 账户主键
     */
    @ExcelColumn(value = Column.A)
    private Integer id;

    /**
     * 所属用户ID
     */
    @ExcelColumn(value = Column.B,key = true)
    private Integer userId;

    /**
     * 账户所属银行
     */
    @ExcelColumn(value = Column.C)
    private String bank;

    /**
     * 账户创建时间
     */
    @ExcelColumn(value = Column.D)
    private Date createTime;

    /**
     * 账户最近更新时间
     */
    @ExcelColumn(value = Column.E)
    private Date updateTime;

    /**
     * 账户积分
     */
    @ExcelColumn(value = Column.F)
    private Integer score;

    /**
     * 账户等级
     */
    @ExcelColumn(value = Column.G)
    private String rank;

    /**
     * 账户余额
     */
    @ExcelColumn(value = Column.H)
    private BigDecimal money;

    /**
     * 账户激活状态，1：激活；0：未激活
     */
    @ExcelColumn(value = Column.I,last = true)
    private Boolean active;

    @Override
    public BankAccount initProperties() {
        return new BankAccount();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank == null ? null : bank.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank == null ? null : rank.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


}