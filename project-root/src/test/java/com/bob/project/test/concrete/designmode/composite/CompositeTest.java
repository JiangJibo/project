/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.designmode.composite;

import org.junit.Test;

import com.google.gson.Gson;

/**
 * 组合模式测试
 * 
 * @since 2017年6月22日 下午3:08:46
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CompositeTest {

	@Test
	public void compositeModeTest() {
		Node a = new Node("A");
		a.addNode(new Node("B").addNode(new Node("C").addNode(new Node("D")).addNode(new Node("E"))));
		a.addNode(new Node("D"));
		StringBuilder sb = new StringBuilder();
		a.toString(sb);
		System.out.println(sb.toString());

		Gson gson = new Gson();
		Node n = gson.fromJson(sb.toString(), Node.class);
		System.out.println(gson.toJson(n));
	}

}
