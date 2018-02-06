package com.bob.project.test.concrete.modelGenerator;

import com.bob.project.test.config.BaseControllerTest;

/**
 * Mysql表内数据生成器
 *
 * @author wb-jjb318191
 * @create 2017-09-13 14:27
 */
public class MysqlModelGenerator extends BaseControllerTest {


   /* @Autowired
    private BankUserMapper bankUserMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Test
    public void testInsertUser() {
        for (int i = 0; i < 20000; i++) {
            try {
                bankUserMapper.insert(BankUserGenerator.generateBankUser());
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Test
    public void testInsertAccount() {
        for (int i = 10325; i < 36609; i++) {
            BankAccount bankAccount = BankAccountGenerator.generateAccount();
            bankAccount.setUserId(i);
            bankAccountMapper.insert(bankAccount);
        }
    }*/

}
