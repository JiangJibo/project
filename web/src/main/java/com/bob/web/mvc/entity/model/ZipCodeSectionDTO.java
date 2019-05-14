package com.bob.web.mvc.entity.model;

import lombok.Data;

/**
 * @author wb-jjb318191
 * @create 2019-05-10 14:26
 */
@Data
public class ZipCodeSectionDTO {
    private final static String SEPARATOR = "-";

    /**
     * 起始邮编编码
     */
    private String startZipCode;

    /**
     * 终止邮编编码
     */
    private String endZipCode;

    public String getDisplayDesc(){
        String result = "";
        if(startZipCode != null && !startZipCode.isEmpty()){
            result = result +  startZipCode;
        }
        if(endZipCode != null && !endZipCode.isEmpty()){
            result = result +  SEPARATOR + endZipCode;
        }
        return result;
    }
}

