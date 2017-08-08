package de.waschndolos.processing;

import de.waschndolos.model.AnnotationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class AnnotationStringParser {


    public static AnnotationInfo parse(String annotationString, String className) {
        AnnotationInfo info = new AnnotationInfo(className);

        String arguments = StringUtils.substringBetween(annotationString, "(", ")");

        Map<String, String> annotationFields = new HashMap<>();
        String[] slittedArguments = arguments.split(",");
        for (String argumentPair : slittedArguments) {
            annotationFields.put(StringUtils.substringBefore(argumentPair, "=").trim(), StringUtils.substringBetween(StringUtils.substringAfter(argumentPair, "=").trim(), "\""));
        }
        info.setAnnotationFields(annotationFields);

        return info;
    }
}
