package com.bob.web.concrete.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import com.bob.web.config.BaseControllerTest;
import com.bob.web.mvc.entity.model.ZipCodeSectionDTO;
import com.bob.web.mvc.entity.model.ZoneConfigure;
import com.bob.web.mvc.entity.model.ZoneEnum;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author wb-jjb318191
 * @create 2019-05-13 15:32
 */
public class GspZipZoneMapperTest extends BaseControllerTest {

    @Autowired
    private GspZipZoneMapper gspZipZoneMapper;

    @Test
    public void testBatchInsert() throws IOException {
        Resource resource = new ClassPathResource("/zipzone/ZipZoneInfo.txt");
        Assert.notNull(resource, "");
        List<String> configs = FileUtils.readLines(resource.getFile());
        Map<String, GspZipZone> GspZipZoneS = new HashMap<>(configs.size() * 15);
        Map<String, String> stringMap = new HashMap<>();

        // 每1000条数据写入一次
        List<GspZipZone> insertList = new ArrayList<>();
        for (int i = 0; i < configs.size(); i++) {
            insertList.addAll(convertAndSplitToDO(JSONObject.parseObject(configs.get(i), ZoneConfigure.class)));
            if (insertList.size() > 1000) {
                doBatchInsert(insertList, i + 1);
                insertList.clear();
            }
        }
    }

    private int doBatchInsert(List<GspZipZone> insertList, int fileRowNum) {
        List<List<GspZipZone>> partitions = ListUtils.partition(insertList, 1000);
        for (int i = 0; i < partitions.size(); i++) {
            List<GspZipZone> inserts = partitions.get(i);
            int rowNum = gspZipZoneMapper.batchInsert(inserts);
            if (rowNum != inserts.size()) {
                throw new IllegalStateException(String.format("第%d行插入出现异常", fileRowNum));
            }
        }
        return insertList.size();
    }

    /**
     * 转换和拆分
     *
     * @param zone
     * @return
     */
    private List<GspZipZone> convertAndSplitToDO(ZoneConfigure zone) {

        List<GspZipZone> zipZoneDOS = new ArrayList<>();
        List<ZipCodeSectionDTO> zipCodeSections = zone.getZipCodeSections();
        zipCodeSections.forEach(sectionDTO -> {
            String startZipCode = sectionDTO.getStartZipCode();
            int start = Integer.valueOf(startZipCode);
            String endZipCode = sectionDTO.getEndZipCode();
            int end;
            if (StringUtils.isEmpty(endZipCode)) {
                end = start;
            } else {
                end = Integer.valueOf(endZipCode);
            }
            for (int i = start; i < end; i++) {
                GspZipZone GspZipZone = new GspZipZone();
                //GspZipZone.setId();
                GspZipZone.setGmtCreate(new Date());
                GspZipZone.setGmtModified(new Date());
                GspZipZone.setModifier("WB-JJB318191");
                GspZipZone.setRuleCode(zone.getSolutionCode());
                GspZipZone.setSource(zone.getWarehouseCode());
                GspZipZone.setCountryCode(zone.getCountryCode());
                GspZipZone.setZipCode(i + "");
                GspZipZone.setZoneCode(zone.getZoneCode());
                GspZipZone.setZoneType(ZoneEnum.isRemote(zone.getZoneCode()) ? 2 + "" : 1 + "");
                GspZipZone.setIsDeleted(0);
                GspZipZone.setVersion(0);
                zipZoneDOS.add(GspZipZone);
            }
        });
        return zipZoneDOS;
    }

}
