/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.cglibproxy;

import java.lang.reflect.Method;

import com.bob.root.config.entity.RootUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;

/**
 * CGLIB动态代理测试
 * 
 * @since 2017年8月1日 下午8:27:21
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CglibProxyTest extends CglibProxyContextConfig<ProxiedModel> {

	private ProxiedModel proxyBean; // CGLIB代理生成的对象
	private Method adviceMethod; // Advice切面方法

	/* (non-Javadoc)
	 * @see com.bob.test.concrete.cglibproxy.CglibProxyBaseConfig#init()
	 */
	@Override
	protected void init() {
		super.originalBean = new ProxiedModel();
		super.adviceDeclaredScope = AspectPointcutConfig.class;
	}

	@Test
	public void testOnBefore() throws Exception {
		adviceMethod = findAspectMethod("argsOnBefore", JoinPoint.class);
		proxyBean = createProxy(adviceMethod);
		proxyBean.setMsgWithUser(new RootUser());
		System.out.println(proxyBean.getMsg());
	}

	@Test
	public void testOnAfter() throws Exception {
		adviceMethod = findAspectMethod("exeOnAfter", JoinPoint.class);
		proxyBean = createProxy(adviceMethod);
		proxyBean.setMsg("lanboal");
	}

	@Test
	public void testOnAround() throws Exception {
		adviceMethod = findAspectMethod("atAnnOnAround", ProceedingJoinPoint.class);
		proxyBean = createProxy(adviceMethod);
		System.out.println(proxyBean.getClass().getName());
		proxyBean.invokeForAround(new RootUser());
	}

	@Test
	public void testAfterReturning() throws Exception {
		adviceMethod = findAspectMethod("atWithinOnAfterReturnning", JoinPoint.class, Object.class);
		proxyBean = createProxy(adviceMethod);
		// proxyBean.invokeForAtWithin(new RequestScopeExample());
		// proxyBean.setMsg("sss");
		proxyBean.getUser();
	}

	@Test
	public void testAfterThrowing() throws Exception {
		adviceMethod = findAspectMethod("thisOnAfterThrowing", JoinPoint.class, IllegalStateException.class);
		proxyBean = createProxy(adviceMethod);
		proxyBean.invokeThrowing();
		/*try {
			proxyBean.invokeForThis();
		} catch (Exception e) {
		}*/
	}

	@Test
	public void testEnum(){
		AspectjAdviceTye type =AspectjAdviceTye.BEFORE;
		System.out.println(type.name());
		System.out.println(type instanceof  Enum);
		System.out.println(type.getClass());
		System.out.println(type.getDeclaringClass());
		System.out.println(type.getClass().getSuperclass());
		System.out.println(type.ordinal());
	}

}
