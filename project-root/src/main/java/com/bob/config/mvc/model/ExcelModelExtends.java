package com.bob.config.mvc.model;

import java.util.Date;

import com.bob.config.mvc.excelmapping.ExcelColumn;
import com.bob.config.mvc.excelmapping.ExcelColumn.Column;
import com.bob.config.mvc.excelmapping.ExcelMapping;
import com.bob.config.mvc.excelmapping.PropertyInitializer;

/**
 * Excel映射的Model扩展类
 *
 * @author dell-7359
 * @create 2017-10-19 19:55
 */
@ExcelMapping(titleRow = 0, dataRow = 3, sheetAt = 0)
public class ExcelModelExtends extends ExcelModel implements PropertyInitializer<ExcelModelExtends> {

    private static final long serialVersionUID = 445175433808433505L;

    @ExcelColumn(value = Column.G,last = true)
    private String description;

    private Date parseTime;

    @ExcelColumn(value = Column.A,key = true)
    public Integer getId() {
        return super.getId();
    }

    @ExcelColumn(value = Column.B)
    public String getUserName() {
        return super.getUserName();
    }

    @ExcelColumn(value = Column.C)
    public String getPassword() {
        return super.getPassword();
    }

    @ExcelColumn(value = Column.D)
    public Integer getAge() {
        return super.getAge();
    }

    @ExcelColumn(value = Column.E)
    public String getTelephone() {
        return super.getTelephone();
    }

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

    /**初始化Model对象
     * @return
     */
    @Override
    public ExcelModelExtends initProperties() {
        ExcelModelExtends modelExtends = new ExcelModelExtends();
        modelExtends.setParseTime(new Date());
        return modelExtends;
    }
}
