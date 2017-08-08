package de.waschndolos.model;

import java.util.Map;

public class AnnotationInfo {

    private String className;

    private Map<String, String> annotationFields;

    public AnnotationInfo(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public Map<String, String> getAnnotationFields() {
        return annotationFields;
    }

    @Override
    public String toString() {
        return "AnnotationInfo{" +
                "className='" + className + '\'' +
                ", annotationFields=" + annotationFields +
                '}';
    }

    public void setAnnotationFields(Map<String, String> annotationFields) {
        this.annotationFields = annotationFields;
    }
}
