package de.waschndolos.processing.output;

import de.waschndolos.model.AnnotationInfo;
import de.waschndolos.processing.exception.ReportCreationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MarkupAnnotationFormatter implements AnnotationFormatter {


    @Override
    public void createReport(List<AnnotationInfo> annotationInfos, String destinationPath) throws ReportCreationException {
        File report = new File(destinationPath + "/report.md");
        if (!report.exists()) {
            try {
                report.createNewFile();
            } catch (IOException e) {
                throw new ReportCreationException("Unable to create File " + destinationPath, e);
            }
        }

        StringBuffer markdownString = new StringBuffer();
        markdownString.append("ClassName").append("|");
        Set<String> headings = annotationInfos.get(0).getAnnotationFields().keySet();
        Set<Integer> headerSizes = new HashSet<>();
        headerSizes.add("ClassName".length());
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

        // remove last |
        removeLastPipe(markdownString);

        for (AnnotationInfo annotationInfo : annotationInfos) {
            markdownString.append("\n");

            String className = annotationInfo.getClassName();
            markdownString.append(className).append("|");

            Map<String, String> annotationFields = annotationInfo.getAnnotationFields();
            for(String value : annotationFields.values()) {
                markdownString.append(value).append("|");
            }
            removeLastPipe(markdownString);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(report.toPath())) {

            writer.write(markdownString.toString());
        } catch (IOException e) {
            throw new ReportCreationException("Unable to write to file " + report, e);
        }

    }

    private void removeLastPipe(StringBuffer markdownString) {
        markdownString.deleteCharAt(markdownString.length() - 1);
    }

    @Override
    public String getOutputFormat() {
        return "markup";
    }
}
