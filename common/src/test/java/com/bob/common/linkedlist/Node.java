package com.bob.common.linkedlist;

/**
 * @author wb-jjb318191
 * @create 2020-06-15 17:08
 */
public class Node<T> {

    public T data;
    public Node prev;
    public Node next;

    public Node(T data) {
        this.data = data;
    }
}
