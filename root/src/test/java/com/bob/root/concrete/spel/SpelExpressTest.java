package com.bob.root.concrete.spel;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author JiangJibo
 * @create 2020-03-09 17:38
 */
public class SpelExpressTest {

    @Test
    public void testSpel() {
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
