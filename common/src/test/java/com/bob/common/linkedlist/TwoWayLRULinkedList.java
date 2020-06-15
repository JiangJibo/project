package com.bob.common.linkedlist;

/**
 * 双向LRU链表
 *
 * @author wb-jjb318191
 * @create 2020-06-15 15:13
 */
public class TwoWayLRULinkedList<T> {

    private Node head;
    private Node tail;

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
            tail = node;
            head.next = node;
            node.prev = head;
            return;
        }
        tail.next = node;
        node.prev = tail;
        this.tail = node;
    }

    public void remove(T t) {
        Node node = head;
        do {
            if (node.data.equals(t)) {
                if (node == head) {
                    head = head.next;
                    node = head;
                } else if (node == tail) {
                    this.tail = tail.prev;
                    tail.next = null;
                    return;
                } else {
                    Node prev = node.prev;
                    Node next = node.next;
                    prev.next = next;
                    next.prev = prev;
                    node = next;
                }
            } else {
                node = node.next;
            }
        } while (node != null);
    }

    public void get(T t) {
        Node node = head;
        do {
            if (node.data.equals(t)) {
                if (node == head) {
                    node = node.next;
                } else if (node == tail) {
                    this.tail = tail.prev;
                    tail.next = null;
                    Node head = this.head;
                    this.head = node;
                    node.next = head;
                    node.prev = null;
                    return;
                } else {
                    Node prev = node.prev;
                    Node next = node.next;
                    prev.next = next;
                    next.prev = prev;

                    Node head = this.head;
                    this.head = node;
                    node.next = head;
                    node.prev = null;

                    node = next;
                }
            } else {
                node = node.next;
            }
        } while (node != null);
    }

    /**
     * 链表反转
     */
    public TwoWayLRULinkedList invert() {
        TwoWayLRULinkedList<T> l = new TwoWayLRULinkedList();
        Node<T> node = this.tail;
        do {
            l.add(node.data);
            node = node.prev;
        } while (node != null);
        return l;
    }

    /**
     * 找寻中间节点
     *
     * @return
     */
    public Node findTheMiddle() {
        Node h2 = head, h1, t2 = tail, t1;
        do {
            h1 = h2.next;
            t1 = t2.prev;
            if (h1 == t1 || h1 == t2) {
                return h1;
            }
            h2 = h2.next;
            t2 = t2.prev;
        } while (true);
    }

    public static void main(String[] args) {
        TwoWayLRULinkedList<Integer> linkedList = new TwoWayLRULinkedList<>();
        linkedList.add(3);
        linkedList.add(4);
        linkedList.add(2);
        linkedList.add(6);
        linkedList.add(5);
        linkedList.add(10);
        linkedList.add(11);

        //linkedList.remove(2);
        Node node = linkedList.head;
        do {
            System.out.println(node.data);
            node = node.next;
        } while (node != null);

        TwoWayLRULinkedList<Integer> inverted = linkedList.invert();

        System.out.println("######################反转链表######################");
        node = inverted.head;
        do {
            System.out.println(node.data);
            node = node.next;
        } while (node != null);

        Node middle = inverted.findTheMiddle();
        System.out.println("中间节点:" + middle.data);

    }

}
