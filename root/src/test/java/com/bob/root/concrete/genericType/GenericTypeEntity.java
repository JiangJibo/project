package com.bob.root.concrete.genericType;

import java.util.List;
import java.util.Map;

/**
 * 泛型包装类
 *
 * @author wb-jjb318191
 * @create 2017-12-20 16:06
 */
public class GenericTypeEntity<String> {

    private List<String> list;
    private Map<String, Integer> map;
    private List<Map<String, Integer>> listMap;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public List<Map<String, Integer>> getListMap() {
        return listMap;
    }

    public void setListMap(List<Map<String, Integer>> listMap) {
        this.listMap = listMap;
    }
}
