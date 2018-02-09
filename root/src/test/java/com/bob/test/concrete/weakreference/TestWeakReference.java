/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.weakreference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import org.junit.Before;
import org.junit.Test;

/**
 * @since 2017年4月6日 下午8:12:45
 * @version $Id$
 * @author JiangJibo
 *
 */
public class TestWeakReference {

	private Entity entity;

	@Before
	public void init() {
		entity = new Entity();
	}

	@Test
	public void testWeahRef0() {
		WeakHashMap<String, String> w = new WeakHashMap<String, String>();
		// 三个key-value中的key 都是匿名对象，没有强引用指向该实际对象
		String yw = new String("语文");
		w.put(yw, new String("优秀"));
		yw = null;
		String str = "及格";
		w.put(new String("数学"), str);
		w.put(new String("英语"), new String("中等"));
		// 增加一个字符串的强引用
		w.put("java", new String("特别优秀"));
		System.out.println(w);
		System.gc(); // 告诉垃圾收集器打算进行垃圾收集，而垃圾收集器进不进行收集是不确定的
		System.runFinalization(); // 强制调用已经失去引用的对象的finalize方法
		// 再次输出w
		System.out.println("第二次输出:" + w);
		System.out.println("str:" + str);
	}

	@Test
	public void testWeakRef1() {
		System.out.println(testWeakHashMap());
	}

	private String testWeakHashMap() {
		String a = new String("a");
		WeakReference<String> b = new WeakReference<String>(a);
		WeakHashMap<String, Integer> weakMap = new WeakHashMap<String, Integer>();
		weakMap.put(b.get(), 1);
		a = null;
		System.out.println("GC前b.get()：" + b.get());
		System.out.println("GC前weakMap：" + weakMap);
		System.gc();
		System.out.println("GC后" + b.get());
		System.out.println("GC后" + weakMap);
		String c = "";
		try {
			c = b.get().replace("a", "b");
			System.out.println("C:" + c);
			return c;
		} catch (Exception e) {
			c = "c";
			System.out.println("Exception");
			return c;
		} finally {
			c += "d";
		}
	}

	@Test
	public void testThreadLocal0() {
		WeakHashMap<ThreadLocal<Entity>, Entity> whm = new WeakHashMap<ThreadLocal<Entity>, Entity>();
		ThreadLocal<Entity> tls = new ThreadLocal<Entity>() {

			/* (non-Javadoc)
			 * @see java.lang.Object#finalize()
			 */
			@Override
			protected void finalize() throws Throwable {
				super.finalize();
				System.out.println("tls对象被回收");
			}

		};
		Entity ent = new Entity() {

			/* (non-Javadoc)
			 * @see java.lang.Object#finalize()
			 */
			@Override
			protected void finalize() throws Throwable {
				super.finalize();
				System.out.println("Ent对象被回收");
			}

		};
		whm.put(tls, ent);
		tls = null;
		ent = null;
		System.gc();
		System.out.println(whm.toString());
	}

	@Test
	public void testNoneRef() {
		Entity entity = new Entity();
		entity = null;
		System.gc();
	}

	@Test
	public void testWeakRef2() {
		Entity entity = new Entity();
		WeakReference<Entity> wrf = new WeakReference<Entity>(entity);
		System.out.println(wrf.get());
		entity = null;
		System.gc();
		System.out.println(wrf.get());
	}

	@Test
	public void testWeakHashMap1() {
		Entity entity = new Entity();
		WeakHashMap<Entity, String> whm = new WeakHashMap<Entity, String>();
		whm.put(entity, new String("value"));
		System.out.println(whm.toString());
		entity = null;
		System.gc();
		System.out.println(whm.toString());
	}

	@Test
	public void mockRequestContextHolder() {

	}

	@Test
	public void testThreadLocal1() {

		ThreadLocal<Entity> tls = new ThreadLocal<Entity>() {

			@Override
			public Entity get() {
				return new Entity();
			}

			/* (non-Javadoc)
			 * @see java.lang.Object#finalize()
			 */
			@Override
			protected void finalize() throws Throwable {
				super.finalize();
				System.out.println("tls对象被回收");
			}
		};
		WeakHashMap<ThreadLocal<Entity>, Entity> whm = new WeakHashMap<ThreadLocal<Entity>, Entity>();
		whm.put(tls, tls.get());
		tls = null;
		System.gc();
		System.runFinalization();
		System.out.println("第一次GC");
		System.out.println(whm.toString());
	}

	@Test
	public void testWeakRef() throws InterruptedException {

		WeakReference<Entity> weakEnt = new WeakReference<Entity>(entity, new ReferenceQueue<Entity>());
		int i = 0;
		while (true) {
			if (weakEnt.isEnqueued()) {
				System.out.println("当WeakReference所引用的对象被回收后,可以将WeakReference存入ReferenceQueue中");
			}
			if (weakEnt.get() != null) {
				i++;
				System.out.println("经过" + i + "次循环后," + weakEnt + "对象还存活");
				Thread.sleep(1000);
			} else {
				System.out.println("经过" + i + "次循环后," + weakEnt + "对象已经被回收");
				break;
			}
			if (i == 10) {
				weakEnt.clear(); // 手动取消对象的引用
				weakEnt.enqueue(); // 将此对象存入ReferenceQueue中
			}
		}
	}

}
