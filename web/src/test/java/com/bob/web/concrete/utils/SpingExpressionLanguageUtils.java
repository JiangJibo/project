package com.bob.web.concrete.utils;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Spel表达式解析工具
 *
 * @author wb-jjb318191
 * @create 2020-03-10 16:09
 */
@Slf4j
public class SpingExpressionLanguageUtils {

    /**
     * 计算表达式
     *
     * @param expression  表达式, 比如 1&&(2||3)
     * @param expSegments 表达式片段, 比如 1:age>10; 2:name=='abc'
     * @param variables   变量集合
     * @return
     */
    public boolean evaluate(String expression, Map<Integer, String> expSegments, Map<String, Object> variables) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            // 如果当前字符是一个数字
            if (Character.isDigit(c)) {
                int order = Integer.valueOf(String.valueOf(c));
                if (!expSegments.containsKey(order)) {
                    throw new IllegalArgumentException(String.format("表达式片段集合:%s 中不包含片段:[%d]", JSON.toJSONString(expSegments), order));
                }
                // 加上数字对应的表达式片段
                sb.append(expSegments.get(order));
            }else{
                sb.append(c);
            }
        }
        String resolvedExpression = sb.toString();
        System.out.println(resolvedExpression);
        return false;
    }

    public static void main(String[] args) {
        Map<String, Object> values = ImmutableMap.of("name", "bob", "age", 32);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(values);
        String exp1 = "#age > 30";
        String exp2 = "#name == 'bob'";
        Expression expression = parser.parseExpression(exp1 + " && " + exp2);
        Boolean success = expression.getValue(context, Boolean.class);
        System.out.println("result:" + success);

    }

}
