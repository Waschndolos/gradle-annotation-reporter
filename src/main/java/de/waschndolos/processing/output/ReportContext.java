package de.waschndolos.processing.output;

import de.waschndolos.model.AnnotationInfo;
import de.waschndolos.processing.exception.ReportCreationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportContext {

    private List<AnnotationFormatter> annotationFormatter;

    public void createReport(String outputFormat, List<AnnotationInfo> annotationInfos, String destinationPath, String annotationClassName) throws ReportCreationException {

        AnnotationFormatter formatter = getResponsibleAnnotationFormatter(outputFormat);

        formatter.createReport(annotationInfos, destinationPath, annotationClassName);
    }

    private AnnotationFormatter getResponsibleAnnotationFormatter(String outputFormat) throws ReportCreationException {

        if (annotationFormatter == null) {
            initializeKnownAnnotationFormatter();
        }

        Optional<AnnotationFormatter> selectedAnnotationFormatter = annotationFormatter.stream().filter(formatter -> formatter.getOutputFormat().equals(outputFormat)).findFirst();
        if (selectedAnnotationFormatter != null && selectedAnnotationFormatter.isPresent()) {
            return selectedAnnotationFormatter.get();
        }
        throw new ReportCreationException("Unable to find formatter for " + outputFormat + ".");
    }

    private void initializeKnownAnnotationFormatter() {
        annotationFormatter = new ArrayList<>();
        annotationFormatter.add(new MarkupAnnotationFormatter());
    }
}
