package com.bob.web.concrete.kfc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.bob.common.utils.excelmapping.Excel;
import com.bob.common.utils.excelmapping.ExcelColumn;
import com.bob.common.utils.excelmapping.ExcelColumn.Column;
import com.bob.common.utils.excelmapping.ExcelMapping;
import com.bob.common.utils.excelmapping.ExcelMappingProcessor;
import com.bob.common.utils.excelmapping.PropertyInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author wb-jjb318191
 * @create 2019-11-18 11:14
 */
@Data
public class KfcAppModel {

    @Data
    @ExcelMapping(titleRow = 0, dataRow = 1)
    public static class AppModel implements PropertyInitializer<AppModel> {

        @ExcelColumn(Column.A)
        private String appName;

        @ExcelColumn(Column.B)
        private String kfcClientVersion;

        @ExcelColumn(Column.C)
        private String appOwner;

        @Override
        public AppModel initProperties() {
            return new AppModel();
        }

    }

    @Data
    @ExcelMapping(titleRow = 0, dataRow = 1)
    public static class AppOwnerEmail implements PropertyInitializer<AppOwnerEmail> {

        @ExcelColumn(Column.A)
        private String email;

        @Override
        public AppOwnerEmail initProperties() {
            return new AppOwnerEmail();
        }
    }

    @Data
    public static class KfcJsonModel {
        private String appName;
        private Object appId;
        private String classX;
        private List<EmployInfoListBean> employInfoList;
    }

    @Data
    public static class EmployInfoListBean {
        private String nickNameCn;
        private String employId;
        private String classX;
        private String primaryEmail;

    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\wb-jjb318191\\Desktop\\待升级应用.json");
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String kw1 = "com.taobao.kfc:kfc-client:";
            String kw2 = "com.taobao.kfc :kfc-client:";
            if (line.contains(kw1) || line.contains(kw2)) {
                lines.remove(i);
                line = line.contains(kw1) ? line.replace(kw1, "") : line.replace(kw2, "");
                line = line.replace(".", "_");
                lines.add(i, line);
            }
            sb.append(line);
        }
        JsonObject jsonObject = new Gson().fromJson(sb.toString(), JsonObject.class);
        Map<String, List<KfcJsonModel>> processResult = new HashMap<>();
        for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            List<KfcJsonModel> values = new Gson().fromJson(entry.getValue().toString(),
                new TypeToken<List<KfcJsonModel>>() {}.getType());
            processResult.put(entry.getKey(), values);
        }
        //processKfcAppOwner(processResult);
        processKfcEmail(processResult);
    }

    private static void processKfcAppOwner(Map<String, List<KfcJsonModel>> params) throws IOException {
        Map<String, AppModel> appModels = new HashMap<>();
        Set<String> hasNullOwnerApps = new HashSet<>();
        for (Entry<String, List<KfcJsonModel>> entry : params.entrySet()) {
            String version = entry.getKey().replace("_", ".");
            List<KfcJsonModel> values = entry.getValue();
            for (KfcJsonModel jsonModel : values) {
                String key = version + jsonModel.getAppName();
                List<EmployInfoListBean> infos = jsonModel.getEmployInfoList();
                // 已存在
                if (appModels.containsKey(key)) {
                    AppModel appModel = appModels.get(key);
                    Set<String> uniqueOwners = new HashSet<>();
                    CollectionUtils.mergeArrayIntoCollection(appModel.getAppOwner().split(","), uniqueOwners);
                    if (CollectionUtils.isEmpty(infos)) {
                        continue;
                    }
                    infos.stream().forEach(emp -> {
                        String nick = emp.getNickNameCn();
                        if (StringUtils.isEmpty(nick)) {
                            hasNullOwnerApps.add(jsonModel.getAppName());
                        } else {
                            uniqueOwners.add(emp.getNickNameCn());
                        }
                    });
                    appModel.setAppOwner(StringUtils.collectionToCommaDelimitedString(uniqueOwners));
                } else {
                    AppModel appModel = new AppModel();
                    appModel.setAppName(jsonModel.getAppName());
                    appModel.setKfcClientVersion(version);
                    if (CollectionUtils.isEmpty(infos)) {
                        continue;
                    }
                    List<String> owners = new ArrayList<>();
                    infos.stream().forEach(emp -> {
                        String nick = emp.getNickNameCn();
                        if (StringUtils.isEmpty(nick)) {
                            hasNullOwnerApps.add(jsonModel.getAppName());
                        } else {
                            owners.add(emp.getNickNameCn());
                        }
                    });
                    appModel.setAppOwner(StringUtils.collectionToCommaDelimitedString(owners));
                    appModels.put(key, appModel);
                }
            }
        }

        System.out.println(StringUtils.collectionToCommaDelimitedString(hasNullOwnerApps));
        File file = new File("C:/Users/wb-jjb318191/Desktop/KFC.xlsx");
        ExcelMappingProcessor.populateData(new Excel(true), new ArrayList<>(appModels.values()), 1).write(file);
    }

    private static void processKfcEmail(Map<String, List<KfcJsonModel>> params) throws IOException {
        Set<String> emails = new HashSet<>();
        for (Entry<String, List<KfcJsonModel>> entry : params.entrySet()) {
            for (KfcJsonModel model : entry.getValue()) {
                List<EmployInfoListBean> employInfoList = model.getEmployInfoList();
                if (CollectionUtils.isEmpty(employInfoList)) {
                    continue;
                }
                employInfoList.forEach(employInfoListBean -> {
                    String email = employInfoListBean.getPrimaryEmail();
                    if (!StringUtils.isEmpty(email)) {
                        emails.add(email);
                    }
                });
            }
        }

        String path = "C:/Users/wb-jjb318191/Desktop/待升级appops联系人.txt";
        List<String> hasSendEmails = FileUtils.readLines(new File(path), "UTF-8");
        emails.removeAll(hasSendEmails);

        List<AppOwnerEmail> ownerEmails = emails.stream().map(s -> {
            AppOwnerEmail email = new AppOwnerEmail();
            email.setEmail(s);
            return email;
        }).collect(Collectors.toList());
        File file = new File("C:/Users/wb-jjb318191/Desktop/KFC-Email.xlsx");
        ExcelMappingProcessor.populateData(new Excel(true), ownerEmails, 1).write(file);
    }

}
