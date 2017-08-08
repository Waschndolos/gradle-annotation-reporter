package de.waschndolos.model;

import java.util.List;

public class Result {

    private List<AnnotationInfo> annotationInfos;

    public List<AnnotationInfo> getAnnotationInfos() {
        return annotationInfos;
    }

    @Override
    public String toString() {
        return "Result{" +
                "annotationInfos=" + annotationInfos +
                '}';
    }
}
