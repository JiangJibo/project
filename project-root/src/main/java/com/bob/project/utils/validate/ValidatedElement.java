package com.bob.project.utils.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 待校验元素
 *
 * @author wb-jjb318191
 * @create 2018-01-31 13:36
 */
class ValidatedElement {

    private Field field;
    private List<Annotation> annotations;

    ValidatedElement(Field field) {
        this.field = field;
    }

    ValidatedElement(Field field, List<Annotation> annotations) {
        this.field = field;
        this.annotations = annotations;
    }

    void addAnnotation(Annotation ann) {
        if (annotations == null) {
            annotations = new ArrayList<>();
        }
        annotations.add(ann);
    }

    boolean isQulified() {
        return annotations != null;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    List<Annotation> getAnnotations() {
        return annotations;
    }

}
