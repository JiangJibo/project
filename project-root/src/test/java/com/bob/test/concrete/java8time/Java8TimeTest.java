/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.java8time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.Test;

/**
 * @since 2017年3月30日 下午2:02:37
 * @version $Id$
 * @author JiangJibo
 *
 */
public class Java8TimeTest {

	@Test
	public void getLocalDate() { // 2017-03-30
		LocalDate today = LocalDate.now();
		System.out.println("Today is " + today.toString());
	}

	@Test
	public void getMonYear() {
		LocalDate today = LocalDate.now();
		int year = today.getYear();
		int month = today.getMonthValue();
		int day = today.getDayOfMonth();
		System.out.printf("Year : %d; Month : %d; day : %d \t %n", year, month, day);
	}

	@Test
	public void createDate() { // 1988-07-26
		LocalDate birthDay = LocalDate.of(1988, 7, 26);
		System.out.println(birthDay);
	}

	@Test
	public void testEquals() { // 判断日期相等
		LocalDate birthDay = LocalDate.of(2017, 3, 30);
		System.out.println(LocalDate.now().equals(birthDay));
	}

	@Test // 月重复日
	public void getMonthDay() {
		LocalDate birthDay = LocalDate.of(1988, 3, 30);
		MonthDay monthDay = MonthDay.of(birthDay.getMonthValue(), birthDay.getDayOfMonth());
		MonthDay currentMonthDay = MonthDay.from(LocalDate.now());
		if (currentMonthDay.equals(monthDay)) {
			System.out.println("生日快乐");
		} else {
			System.out.println("不是生日");
		}
	}

	@Test
	public void getLocalTime() {
		LocalTime time = LocalTime.now();
		System.out.println(time.toString());
	}

	@Test // 增加时分秒。返回新的实例
	public void testPlusHours() {
		LocalTime time = LocalTime.now();
		time.plusMinutes(5);
		LocalTime time0 = time.plusHours(2);
		time0.plus(3, ChronoUnit.MINUTES);
		System.out.println(time);
		System.out.println(time0);
	}

	@Test // 增加日期
	public void testPlusWeek() {
		LocalDate today = LocalDate.now();
		System.out.println(today.plus(1, ChronoUnit.DAYS));
		System.out.println(today.minus(1, ChronoUnit.YEARS));
	}

	@Test // 时钟对象
	public void testLock() throws InterruptedException {
		long t1 = Clock.systemDefaultZone().millis();
		Thread.sleep(1000);
		Clock clock = Clock.systemDefaultZone();
		long t2 = clock.millis();
		System.out.println(t2 - t1);
		System.out.println(LocalDate.now(clock));
	}

	@Test
	public void testDateBefore() {
		LocalDate d1 = LocalDate.of(2017, 3, 29);
		LocalDate d2 = LocalDate.of(2017, 3, 30);
		System.out.println(d1.isAfter(d2));
		System.out.println(d1.isBefore(d2));
		System.out.println(d1.equals(d2));
	}

	@Test // 判断两个日期之间相隔多少天,需要(月份*30+天数)
	public void testPeriod() {
		LocalDate d1 = LocalDate.of(2017, 5, 18);
		System.out.println(d1);
		Period p = Period.between(LocalDate.of(2017, 3, 30), d1);
		System.out.println(p.getMonths());
		System.out.println(p.get(ChronoUnit.DAYS));
	}

	@Test // Java8之前的date
	public void testInstant() {
		Instant ins = Instant.now();
		System.out.println(ins);
		System.out.println(Date.from(ins));
		System.out.println(new Date().toInstant());
	}

	@Test
	public void testDateFormat() {
		String timeString = "20170330";
		LocalDate date = LocalDate.parse(timeString, DateTimeFormatter.BASIC_ISO_DATE);
		System.out.println(date);
		System.out.println(date.format(DateTimeFormatter.BASIC_ISO_DATE));
	}

	@Test // 自定义格式化样式
	public void customizeFormatPattern() {
		String pattern = "yyyy,MM,dd,hh:mm:ss";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime dt = LocalDateTime.now();
		String result = formatter.format(dt);
		System.out.println(result);
	}
}
