package com.bob.project.utils.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import com.bob.project.utils.validate.ann.Email;
import com.bob.project.utils.validate.ann.Max;
import com.bob.project.utils.validate.ann.MaxLength;
import com.bob.project.utils.validate.ann.Min;
import com.bob.project.utils.validate.ann.NotEmpty;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 数据校验器
 *
 * @author wb-jjb318191
 * @create 2018-01-31 10:35
 */
public enum Validator {

    /**
     * 非空校验器
     */
    NOT_NULL(NotNull.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            checkAnnIfApplicable(ann, NotNull.class);
        }
    },

    /**
     * 内部非空校验器
     */
    NOT_EMPTY(NotEmpty.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            checkAnnIfApplicable(ann, NotEmpty.class);
        }
    },

    /**
     * 邮箱校验器
     */
    EMAIL(Email.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            checkAnnIfApplicable(ann, Email.class);
            if (value == null) {
                return;
            }
            Assert.isInstanceOf(String.class, value, stringErrorInfo(field.getName()));
            Assert.isTrue(EMAIL_PATTERN.matcher((String)value).matches(), "[" + field.getName() + "]不符合邮箱格式");
        }
    },

    /**
     * 字符串最大长度校验器
     */
    MAX_LENGTH(MaxLength.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            MaxLength maxLength = checkAnnIfApplicable(ann, MaxLength.class);
            if (value == null) {
                return;
            }
            Assert.isInstanceOf(String.class, value, stringErrorInfo(field.getName()));
            Assert.isTrue(((String)value).length() <= maxLength.value(), "[" + field.getName() + "]长度超过了" + maxLength.value());
        }
    },

    /**
     * 最小值校验器
     */
    MIN(Min.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            checkAnnIfApplicable(ann, Min.class);
        }
    },

    /**
     * 最大值校验器
     */
    MAX(Max.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            checkAnnIfApplicable(ann, Max.class);
        }
    };

    private Class<? extends Annotation> annotation;

    private static final String EMAIL_RULE = "/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_RULE);

    Validator(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    /**
     * 验证注解类型是否契合
     *
     * @param ann
     * @param clazz
     */
    private static <T> T checkAnnIfApplicable(Annotation ann, Class<T> clazz) {
        Assert.state(ann.annotationType() == clazz, "待校验注解的类型不匹配");
        return (T)ann;
    }

    private static String stringErrorInfo(String prefix) {
        return "[" + prefix + "]属性必须为字符串类型";
    }

    private static String nullErrorInfo(String prefix) {
        return "[" + prefix + "]属性不能为空";
    }

    /**
     * 数据校验
     *
     * @param ann
     * @param value
     * @return
     */
    public abstract void validate(Field field, Object value, Annotation ann);
}