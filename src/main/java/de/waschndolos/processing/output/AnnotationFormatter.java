package de.waschndolos.processing.output;

import java.util.List;

import de.waschndolos.model.AnnotationInfo;
import de.waschndolos.processing.exception.ReportCreationException;

/**
 * Implementations shall be able to create a report in the given format
 */
public interface AnnotationFormatter {

    /**
     * Creates a report
     * @param annotationInfos list of {@link AnnotationInfo}s
     * @param destinationPath the target folder to put the report to
     * @param annotationClassName
     * @throws ReportCreationException if anything fails
     */
    void createReport(List<AnnotationInfo> annotationInfos, String destinationPath, String annotationClassName) throws ReportCreationException;

    /**
     * @return The output format of the report
     */
    String getOutputFormat();
}
