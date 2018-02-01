package com.bob.project.utils.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import com.bob.project.utils.validate.ann.Email;
import com.bob.project.utils.validate.ann.Max;
import com.bob.project.utils.validate.ann.MaxLength;
import com.bob.project.utils.validate.ann.Min;
import com.bob.project.utils.validate.ann.NotEmpty;
import org.springframework.util.Assert;

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
            Assert.notNull(value, generateErrorPrefix(field) + "属性不能为空");
        }
    },

    /**
     * 内部非空校验器
     */
    NOT_EMPTY(NotEmpty.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            convertIfApplicable(ann, NotEmpty.class);
            if (value == null) {
                return;
            }
            if (value.getClass().isArray()) {
                Assert.notEmpty((Object[])value, generateErrorPrefix(field) + "属性不能是空数组");
            } else if (Collection.class.isAssignableFrom(value.getClass())) {
                Assert.notEmpty((Collection<?>)value, getErrorInfo(field));
            } else if (Map.class.isAssignableFrom(value.getClass())) {
                Assert.notEmpty((Map<?, ?>)value, getErrorInfo(field));
            } else {
                throw new IllegalStateException(generateErrorPrefix(field) + "属性不适用于@NotEmpty注解");
            }
        }

        private String getErrorInfo(Field field) {
            return generateErrorPrefix(field) + "不能是空集合";
        }
    },

    /**
     * 邮箱校验器
     */
    EMAIL(Email.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            convertIfApplicable(ann, Email.class);
            if (value == null) {
                return;
            }
            assertClassCompatible(field, value, String.class);
            Assert.isTrue(EMAIL_PATTERN.matcher((String)value).matches(), generateErrorPrefix(field) + "属性不符合邮箱格式");
        }
    },

    /**
     * 字符串最大长度校验器
     */
    MAX_LENGTH(MaxLength.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            MaxLength maxLength = convertIfApplicable(ann, MaxLength.class);
            if (value == null) {
                return;
            }
            assertClassCompatible(field, value, String.class);
            Assert.isTrue(((String)value).length() <= maxLength.value(), generateErrorPrefix(field) + "长度超过了" + maxLength.value());
        }
    },

    /**
     * 最小值校验器
     */
    MIN(Min.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            Min min = convertIfApplicable(ann, Min.class);
            if (value == null) {
                return;
            }
            assertClassCompatible(field, value, Number.class);
            Assert.isTrue(((Number)value).longValue() >= min.value(), generateErrorPrefix(field) + "属性值必须>=" + min.value());
        }
    },

    /**
     * 最大值校验器
     */
    MAX(Max.class) {
        @Override
        public void validate(Field field, Object value, Annotation ann) {
            Max max = convertIfApplicable(ann, Max.class);
            if (value == null) {
                return;
            }
            assertClassCompatible(field, value, Number.class);
            Assert.isTrue(((Number)value).longValue() <= max.value(), generateErrorPrefix(field) + "属性值必须<=" + max.value());
        }
    };

    private Class<? extends Annotation> annotation;

    private static final String EMAIL_RULE = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
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
    private static <T> T convertIfApplicable(Annotation ann, Class<T> clazz) {
        Assert.state(ann.annotationType() == clazz, "待校验注解的类型不匹配");
        return (T)ann;
    }

    private static void assertClassCompatible(Field field, Object value, Class<?> expectClass) {
        Assert.isTrue(expectClass.isAssignableFrom(value.getClass()), generateErrorPrefix(field) + "属性类型与数据校验注解不匹配");
    }

    private static String generateErrorPrefix(Field field) {
        return "[" + field.getDeclaringClass().getName() + "." + field.getName() + "]";
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