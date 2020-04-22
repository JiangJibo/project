package com.bob.common.guava;


import java.util.function.Predicate;

import com.bob.common.entity.base.Paging;
import com.google.common.base.Predicates;
import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2020-04-09 14:35
 */
public class PredicatesTest {

    @Test
    public void testPredicate() {

        Predicate<Paging> predicate = Predicates.<Paging>alwaysFalse().
            and(input -> input.getCurrentPage() > 1)
            .and(input -> input.getLimit() < 10)
            .and(input -> {
                if (input == null) {
                    return false;
                } else {
                    return input.getStartRow() == 10;
                }
            });

        Paging paging = new Paging();
        paging.setCurrentPage(2);
        paging.setLimit(5);
        boolean applied = predicate.test(paging);
        System.out.println(applied);
    }

}
