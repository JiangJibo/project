package com.bob.common.queue;

/**
 * 循环有界队列
 *
 * @author JiangJibo
 * @create 2020-06-15 20:04
 */
public class CycledArrayListQueue<E> {

    private int size;
    private int capacity;
    private int head;
    private int tail;
    private Object[] queue;

    public CycledArrayListQueue(int capacity) {
        this.capacity = capacity;
        head = tail = size = 0;
    }

    /**
     * 入列
     *
     * @param e
     * @return
     */
    public boolean enqueue(E e) {
        if (size == capacity) {
            throw new IllegalStateException("队列已满");
        }
        if (queue == null) {
            queue = new Object[capacity];
            queue[0] = e;
            head++;
        } else {
            queue[head] = e;
            if (head == queue.length) {
                head = 0;
            } else {
                head++;
            }
        }
        size++;
        return true;
    }

    /**
     * 出列
     *
     * @return
     */
    public E dequeue() {
        E e;
        if (size == 0) {
            throw new IllegalStateException("元素为空");
        }
        if (tail == capacity - 1) {
            e = (E)queue[capacity - 1];
            queue[capacity - 1] = null;
            tail = 0;
            size--;
            return e;
        }
        e = (E)queue[tail];
        queue[tail] = null;
        tail++;
        size--;
        return e;
    }

    public static void main(String[] args) {
        CycledArrayListQueue<Integer> queue = new CycledArrayListQueue(4);
        queue.enqueue(0);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
    }
}
