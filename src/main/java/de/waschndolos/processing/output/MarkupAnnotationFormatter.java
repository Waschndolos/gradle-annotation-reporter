package de.waschndolos.processing.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.waschndolos.model.AnnotationInfo;
import de.waschndolos.processing.exception.ReportCreationException;

public class MarkupAnnotationFormatter implements AnnotationFormatter {


    private static final String CLASS_NAME = "ClassName";

    @Override
    public String getOutputFormat() {
        return OutputFormats.MARKDOWN.getFormatString();
    }

    @Override
    public void createReport(List<AnnotationInfo> annotationInfoList, String destinationPath) throws ReportCreationException {
        File report = getConfiguredOutputFile(destinationPath);

        StringBuffer markdownString = new StringBuffer();

        createTableHeader(annotationInfoList, markdownString);

        createTableContent(annotationInfoList, markdownString);

        try (BufferedWriter writer = Files.newBufferedWriter(report.toPath())) {

            writer.write(markdownString.toString());
        } catch (IOException e) {
            throw new ReportCreationException("Unable to write to file " + report, e);
        }

    }

    private void createTableHeader(List<AnnotationInfo> annotationInfoList, StringBuffer markdownString) {

        markdownString.append(CLASS_NAME).append("|");

        Set<String> headings = annotationInfoList.get(0).getAnnotationFields().keySet();
        Set<Integer> headerSizes = new HashSet<>();
        headerSizes.add(CLASS_NAME.length());
        for (String heading : headings) {
            markdownString.append(heading).append("|");
            headerSizes.add(heading.length());
        }

        removeLastPipe(markdownString);

        markdownString.append("\n");

        for (Integer colSize : headerSizes) {
            for (int i = 0; i < colSize; i++) {
                markdownString.append("-");
            }
            markdownString.append("|");
        }

        removeLastPipe(markdownString);
    }

    private void createTableContent(List<AnnotationInfo> annotationInfoList, StringBuffer markdownString) {
        for (AnnotationInfo annotationInfo : annotationInfoList) {
            markdownString.append("\n");

            String className = annotationInfo.getClassName();
            markdownString.append(className).append("|");

            Map<String, String> annotationFields = annotationInfo.getAnnotationFields();
            for(String value : annotationFields.values()) {
                markdownString.append(value).append("|");
            }
            removeLastPipe(markdownString);
        }
    }

    private File getConfiguredOutputFile(String destinationPath) throws ReportCreationException {
        File report = new File(StringUtils.appendIfMissing(destinationPath, "/") + "report.md");
        if (!report.exists()) {
            try {
                report.createNewFile();
            } catch (IOException e) {
                throw new ReportCreationException("Unable to create File " + destinationPath, e);
            }
        }
        return report;
    }

    private void removeLastPipe(StringBuffer markdownString) {
        markdownString.deleteCharAt(markdownString.length() - 1);
    }


}
