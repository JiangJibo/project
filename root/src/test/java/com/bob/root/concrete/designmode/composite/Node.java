/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.composite;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * @since 2017年6月22日 下午2:40:59
 * @version $Id$
 * @author JiangJibo
 *
 */
public class Node {

	private String name;

	private int deep;

	private Set<Node> children;

	public Node(String name) {
		this.name = name;
	}

	/**
	 * 添加子节点
	 * 
	 * @param node
	 */
	public Node addNode(Node node) {
		if (this.children == null) {
			this.children = new HashSet<Node>();
		}
		node.deep = this.deep + 1;
		node.updateChildrenDeep();
		this.children.add(node);
		return this;
	}

	/**
	 * 更新children的节点深度
	 */
	public void updateChildrenDeep() {
		if (isCompisite()) {
			for (Node node : children) {
				node.deep = this.deep + 1;
				node.updateChildrenDeep();
			}
		}
	}

	/**
	 * 删除子节点
	 * 
	 * @param node
	 */
	public void removeNode(Node node) {
		Assert.notEmpty(children, this.name + "节点的children属性为null");
		this.children.remove(node);
	}

	/**
	 * 是否是组合节点
	 * 
	 * @return
	 */
	public boolean isCompisite() {
		return this.children != null && !this.children.isEmpty();
	}

	/**
	 * 遍历所有节点
	 */
	public void toString(StringBuilder sb) {
		sb.append("{");
		sb.append("\"name\":\"" + this.getName() + "\",");
		sb.append("\"deep\":" + this.deep + (isCompisite() ? "," : ""));
		if (isCompisite()) {
			sb.append("\"children\":[");
			for (Node node : children) {
				node.toString(sb);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]");
		}
		sb.append("}");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the deep
	 */
	public int getDeep() {
		return deep;
	}

}
