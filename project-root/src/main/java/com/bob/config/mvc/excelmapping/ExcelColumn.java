/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.excelmapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * @since 2016年5月19日 下午4:54:29
 * @version $Id: ExcelColumn.java 17256 2016-06-26 09:35:05Z WuJianqiang $
 * @author JiangJibo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface ExcelColumn {

	/**
	 * 配置列的名称,对应A,B,C,D....
	 */
	Column value() default Column.DEFAULT_NONE;

	/**
	 * 是否可作为唯一性的标识<br>
	 * 默认 notNull() = true
	 * 
	 * @return
	 */
	boolean key() default false;

	/**
	 * 非空校验 <br>
	 * null or '' or ' ' ? true: false
	 * 
	 * @return
	 */
	boolean notNull() default false;

	/**
	 * 是否为最后一列
	 * 
	 * @return
	 */
	boolean last() default false;

	/**
	 * 列信息
	 */
	public enum Column {
		DEFAULT_NONE("NONE", -1), // 默认无

		A("A", 0), B("B", 1), C("C", 2), D("D", 3), E("E", 4), F("F", 5), G("G", 6),

		H("H", 7), I("I", 8), J("J", 9), K("K", 10), L("L", 11), M("M", 12), N("N", 13),

		O("O", 14), P("P", 15), Q("Q", 16), R("R", 17), S("S", 18), T("T", 19), U("M", 20),

		V("V", 21), W("W", 22), X("X", 23), Y("Y", 24), Z("Z", 25), AA("AA", 26), AB("AB", 27),

		AC("AC", 28), AD("AD", 29), AE("AE", 30), AF("AF", 31), AG("AG", 32), AH("AH", 33), AI("AI", 34),

		AJ("AJ", 35), AK("AK", 36), AL("AL", 37), AM("AM", 38), AN("AN", 39), AO("AO", 40);

		public String name;

		public Integer value;

		private Column(String name, Integer value) {
			this.name = name;
			this.value = value;
		}
	}
}
