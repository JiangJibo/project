package com.bob.project.web.config.controlleradvice;

import com.bob.project.web.config.formatter.StudentFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 此类能够在StudentController的每一个HandlerMethod方法执行前执行里面的@InitBinder标注的方法,可以用来对WebDataBinder进行拓展
 * 
 * @since 2017年3月4日 下午2:09:26
 * @version $Id$
 * @author JiangJibo
 *
 */
//@ControllerAdvice(basePackages = "com.bob.student.controller", assignableTypes = RedisCacheController.class)
public class StudentControllerAdvice {

	final static Logger LOGGER = LoggerFactory.getLogger(StudentControllerAdvice.class);

	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		Object obj = binder.getTarget();
		if (null == obj) {
			return;
		}
		LOGGER.debug(
				"拓展:[" + binder.getClass().toString() + "],以添加针对:[" + obj.getClass().toString() + "]内field的Converter/PropertyEditor属性编辑器,而不能添加针对当前对象类型的属性编辑器");
		binder.addCustomFormatter(new StudentFormatter());
	}

	// @RequestMapping,不能有
	@ModelAttribute
	public void initModelAttribute(@ModelAttribute String name) {
		System.out.println("************************" + name);
	}

}
