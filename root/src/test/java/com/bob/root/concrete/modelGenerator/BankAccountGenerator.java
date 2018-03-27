package com.bob.root.concrete.modelGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * 银行账户
 *
 * @author wb-jjb318191
 * @create 2017-09-12 14:20
 */
public class BankAccountGenerator {

    private static List<String> banks;
    private static final List<String> RANKS = Arrays.asList("silver","gold","platinum","diamond");

    static {
        banks = loadBanks("中国工商银行 招商银行 中国农业银行 中国建设银行 \n"
            + "中国银行 中国民生银行 中国光大银行 中信银行 \n"
            + "交通银行 兴业银行 上海浦东发展银行 中国人民银行 \n"
            + "华夏银行 深圳发展银行 广东发展银行 国家开发银行 \n"
            + "厦门国际银行 中国进出口银行 中国农业发展银行 北京银行 \n"
            + "上海银行 中国邮政储蓄银行");
    }

    private static List<String> loadBanks(String values) {
        String[] bankArray = values.split(" ");
        List<String> bankList = new ArrayList<>(bankArray.length);
        for (String string : bankArray) {
            if (StringUtils.hasText(string)) {
                bankList.add(string.trim());
            }
        }
        return bankList;
    }

    public static String getBank() {
        return banks.get(BankUserGenerator.nextInt(0, banks.size() - 1));
    }

    /**
     * 生成随机时间
     *
     * @return
     */
    public static Date randomTime() {
        Calendar calendar = Calendar.getInstance();
        //注意月份要减去1
        calendar.set(1990, 11, 31);
        calendar.getTime().getTime();
        //根据需求，这里要将时分秒设置为0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long min = calendar.getTime().getTime();
        calendar.set(2017, 9, 12);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.getTime().getTime();
        long max = calendar.getTime().getTime();
        //得到大于等于min小于max的double值
        double randomDate = Math.random() * (max - min) + min;
        //将double值舍入为整数，转化成long类型
        calendar.setTimeInMillis(Math.round(randomDate));
        return calendar.getTime();
    }

    /*public static BankAccount generateAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBank(getBank());
        bankAccount.setCreateTime(randomTime());
        bankAccount.setUpdateTime(new Date());
        bankAccount.setScore(BankUserGenerator.nextInt(10000,100000));
        bankAccount.setRank(RANKS.get(BankUserGenerator.nextInt(0,3)));
        bankAccount.setMoney(BankUserGenerator.nextDecimal(3000.12,1000000.00));
        bankAccount.setActive(BankUserGenerator.nextInt(0,1) == 1);
        return bankAccount;
    }*/


}
