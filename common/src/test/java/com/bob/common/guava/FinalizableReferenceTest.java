package com.bob.common.guava;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import com.bob.common.entity.result.BaseResult;
import com.google.common.base.FinalizablePhantomReference;
import com.google.common.base.FinalizableReferenceQueue;
import com.google.common.base.FinalizableWeakReference;
import org.junit.Test;

/**
 * @author JiangJibo
 * @create 2020-04-08 19:48
 */
public class FinalizableReferenceTest {

    FinalizableReferenceQueue referenceQueue = new FinalizableReferenceQueue();

    /**
     * 测试Guava 的 幽灵引用队列
     */
    @Test
    public void testPhantomReference() {
        FinalizablePhantomReference<BaseResult> phantomReference = buildPhantomReference();
        System.gc();
        System.out.println("xxxxxxxxxxxx");
    }

    private FinalizablePhantomReference<BaseResult> buildPhantomReference() {
        FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
        BaseResult baseResult = new BaseResult();
        FinalizablePhantomReference<BaseResult> phantomReference = new FinalizablePhantomReference<BaseResult>(baseResult, queue) {
            @Override
            public void finalizeReferent() {
                System.out.println("baseResult is been clean");
            }
        };
        return phantomReference;
    }

    // #####################################################################################################################

    /**
     * 测试Guava 的 弱引用队列
     */
    @Test
    public void testWeakReferenceQueue() {
        FinalizableWeakReference<ByteBuffer> weakReference = addWeakReference();
        weakReference.get();
    }

    private FinalizableWeakReference<ByteBuffer> addWeakReference() {
        // System.gc() 时内存可回收的垃圾比较少的话不会对弱引用 enqueue
        FinalizableWeakReference<ByteBuffer> weakReference = new FinalizableWeakReference<ByteBuffer>(ByteBuffer.allocateDirect( 1024 * 100), referenceQueue) {
            @Override
            public void finalizeReferent() {
                System.out.println("weak reference object has been clean");
            }
        };
        System.gc();
        return weakReference;
    }

    // #####################################################################################################################

    public void testDirectByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 100);
    }

    // #####################################################################################################################

    private static ReferenceQueue<VeryBig> rq = new ReferenceQueue<VeryBig>();

    public static void checkQueue() {
        Reference<? extends VeryBig> ref = null;
        while ((ref = rq.poll()) != null) {
            if (ref != null) {
                System.out.println("In queue: " + ((VeryBigWeakReference)(ref)).id);
            }
        }
    }

    @Test
    public void main() throws InterruptedException {
        int size = 10;
        LinkedList<WeakReference<VeryBig>> weakList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            weakList.add(new VeryBigWeakReference(new VeryBig("Weak " + i), rq));
            System.out.println("Just created weak: " + weakList.getLast());

        }

        System.gc();
        checkQueue();
        Thread.sleep(1000);
    }
}

class VeryBig {

    public String id;
    byte[] b = new byte[5 * 1024 * 1024];

    /**
     * GC是内存占用空间太小，即使显式的 {@link System#gc()} 也不会触发实际的GC
     */
    //byte[] b = new byte[2 * 1024 * 1024];
    public VeryBig(String id) {
        this.id = id;
    }

    protected void finalize() {
        System.out.println("Finalizing VeryBig " + id);
    }
}

class VeryBigWeakReference extends WeakReference<VeryBig> {

    public String id;

    public VeryBigWeakReference(VeryBig big, ReferenceQueue<VeryBig> rq) {
        super(big, rq);
        this.id = big.id;
    }

    protected void finalize() {
        System.out.println("Finalizing VeryBigWeakReference " + id);
    }

}
