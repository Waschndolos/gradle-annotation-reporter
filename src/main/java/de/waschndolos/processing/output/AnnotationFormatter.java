package de.waschndolos.processing.output;

import de.waschndolos.model.AnnotationInfo;
import de.waschndolos.processing.exception.ReportCreationException;

import java.io.File;
import java.io.File;
import java.util.List;

/**
 * Implementations shall be able to create a report in the given format
 */
public interface AnnotationFormatter {

    /**
     * Creates a report
     * @param annotationInfos list of {@link AnnotationInfo}s
     * @param destinationPath the target folder to put the report to
     * @throws ReportCreationException if anything fails
     */
    void createReport(List<AnnotationInfo> annotationInfos, String destinationPath) throws ReportCreationException;

    /**
     * @return The output format of the report
     */
    String getOutputFormat();
}
