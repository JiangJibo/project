package com.bob.project.utils.model;

import java.util.Date;

import com.bob.project.utils.excelmapping.ExcelColumn;
import com.bob.project.utils.excelmapping.ExcelColumn.Column;
import com.bob.project.utils.excelmapping.ExcelMapping;

/**
 * Excel映射的Model扩展类
 *
 * @author dell-7359
 * @create 2017-10-19 19:55
 */
@ExcelMapping(titleRow = 0, dataRow = 1)
public class ExcelModelExtends extends ExcelModel {

    private static final long serialVersionUID = 445175433808433505L;

    @ExcelColumn(value = Column.G)
    private String description;

    private Date parseTime;

    @Override
    @ExcelColumn(value = Column.A, key = true)
    public Integer getId() {
        return super.getId();
    }

    @Override
    @ExcelColumn(value = Column.B)
    public String getUserName() {
        return super.getUserName();
    }

    @Override
    @ExcelColumn(value = Column.C)
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    @ExcelColumn(value = Column.D)
    public Integer getAge() {
        return super.getAge();
    }

    @Override
    @ExcelColumn(value = Column.E)
    public String getTelephone() {
        return super.getTelephone();
    }

    @Override
    @ExcelColumn(value = Column.F)
    public String getAdress() {
        return super.getAdress();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getParseTime() {
        return parseTime;
    }

    public void setParseTime(Date parseTime) {
        this.parseTime = parseTime;
    }

    /**
     * 初始化Model对象
     *
     * @return
     */
    @Override
    public ExcelModelExtends initProperties() {
        ExcelModelExtends modelExtends = new ExcelModelExtends();
        modelExtends.setParseTime(new Date());
        return modelExtends;
    }
}
