package com.bob.root.concrete.lambda;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.bob.root.config.entity.RootUser;
import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2018-08-07 13:25
 */
public class OptionalTest {

    /**
     * 将数据映射为指定类型的对象
     */
    @Test
    public void testMap() {
        RootUser user = Optional.ofNullable("aaa")
            .map(str -> str.startsWith("a") ? null : new RootUser(str, "123"))
            .orElse(new RootUser("default", "123"));
        System.out.println(user.getName());
    }

    /**
     * 测试{@link Optional#flatMap(Function)}
     * 水平映射,将一个 Optional映射为另一个 Optional,需要我们自己将原始对象封装成Optional
     * 一般情况下使用{@link Optional#map(Function)},它帮我们做好了封装成Optional对象的事情
     */
    @Test
    public void testFlatMap() {
        RootUser user = Optional.ofNullable("aaa")
            .flatMap(str -> str.startsWith("a") ? Optional.empty() : Optional.of(new RootUser(str, "123")))
            .orElseGet(RootUser::new);
        System.out.println(user.getName());
    }

    /**
     * 测试{@link Optional#filter(Predicate)}
     */
    @Test
    public void testFilter() {
        RootUser user = Optional.ofNullable("aaa")
            .map(str -> str.startsWith("b") ? null : new RootUser(str, "123"))
            .filter(use -> use.getName().startsWith("a"))
            .orElse(new RootUser("default", "123"));
        System.out.println(user.getName());
    }

}
