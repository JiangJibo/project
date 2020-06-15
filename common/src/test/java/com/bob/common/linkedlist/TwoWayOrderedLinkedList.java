package com.bob.common.linkedlist;

import java.util.Comparator;

/**
 * 有序的双向链表
 *
 * @author wb-jjb318191
 * @create 2020-06-15 17:07
 */
public class TwoWayOrderedLinkedList<T> {

    private Node<T> head;
    private Node<T> tail;
    private Comparator<T> comparator;

    public TwoWayOrderedLinkedList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * 添加元素
     *
     * @param t
     */
    public void add(T t) {
        Node node = new Node(t);
        if (head == null) {
            head = node;
            return;
        }
        if (tail == null) {
            int order = comparator.compare(head.data, t);
            if (order > 0) {
                Node h = head;
                head = node;
                node.next = h;
                h.prev = node;
            } else {
                tail = node;
                head.next = node;
                node.prev = head;
            }
            return;
        }
        do {

        } while (true);
    }
}
