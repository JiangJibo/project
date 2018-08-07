package com.bob.root.concrete.lambda;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import com.bob.root.config.entity.RootUser;
import org.junit.Before;
import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2018-08-07 14:03
 */
public class CollectionStreamTest {

    private List<RootUser> users;
    private Stream<RootUser> stream;

    @Before
    public void init() {
        RootUser user1 = new RootUser(1, "lanboal", "123456");
        RootUser user2 = new RootUser(2, "bob", "123456");
        RootUser user3 = new RootUser(3, "lili", "123456");
        RootUser user4 = new RootUser(4, "lucy", "123456");
        users = Arrays.asList(user1, user2, user3, user4);
        stream = users.stream();
    }

    /**
     * 依据指定规则获取最大的对象
     */
    @Test
    public void testMax() {
        RootUser max = stream.max(Comparator.comparingInt(use -> use.getId())).get();
        System.out.println(max.getName());
    }

    /**
     * 依据指定的规则湖区最小的对象
     */
    @Test
    public void testMin() {
        RootUser min = stream.min(Comparator.comparingInt(use -> use.getId())).get();
        System.out.println(min.getName());
    }

    /**
     * 测试{@link Stream#filter(Predicate)},依据指定条件过滤原始
     */
    @Test
    public void testFilter() {
        stream.filter(user -> user.getId() > 1 && user.getId() < 4)
            .forEach(RootUser::getId);
    }

    /**
     * 测试{@link Stream#findFirst()},找到第一个匹配的元素
     */
    @Test
    public void testFindFirst() {
        RootUser firstMatch = stream.filter(user -> user.getId() > 1 && user.getId() < 4).findFirst().get();
        System.out.println(firstMatch.getId());
    }

    /**
     * 测试{@link Stream#allMatch(Predicate)},集合内是否所有元素都匹配
     */
    @Test
    public void testAllMatch() {
        boolean allMatch = stream.allMatch(user -> user.getId() > 2);
        System.out.println(String.format("是否所有元素的id都大于2,结果：[%s]", allMatch));
    }

    /**
     * 测试{@link Stream#anyMatch(Predicate)},集合内是否有元素匹配
     */
    @Test
    public void testAnyMatch() {
        boolean anyMatch = stream.anyMatch(user -> user.getId() > 2);
        System.out.println(String.format("是否有元素的id大于2,结果：[%s]", anyMatch));
    }

    /**
     * 将元素集合映射为指定类型的集合
     * {@link Stream#mapToInt(ToIntFunction)}
     * {@link Stream#mapToDouble(ToDoubleFunction)}
     * {@link Stream#mapToLong(ToLongFunction)}
     */
    @Test
    public void testMapToInt() {
        stream.mapToInt(user -> user.getId()).forEach(id -> System.out.println(id));
    }

    /**
     * 测试并行的Stream
     * {@link Collection#parallelStream()}
     */
    @Test
    public void testParallelStream() {
        Stream stream = users.parallelStream();
    }

}
