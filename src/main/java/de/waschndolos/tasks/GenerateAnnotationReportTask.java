package de.waschndolos.tasks;

import de.waschndolos.model.AnnotationInfo;
import de.waschndolos.processing.AnnotationStringParser;
import de.waschndolos.processing.exception.ReportCreationException;
import de.waschndolos.processing.output.ReportContext;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateAnnotationReportTask extends DefaultTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateAnnotationReportTask.class);

    @Input
    Set<File> sources;

    @Input
    String annotationClass;

    @Input
    String outputFormat;

    @Input
    String destination;

    @Override
    public String getGroup() {
        return "report";
    }

    @TaskAction
    public void executeTask() {

        LOGGER.debug("Searching in {} source directories.", sources.size());

        Set<Path> sources = new HashSet<>();
        for (File srcDir : this.sources) {
            try {
                sources.addAll(Files.walk(srcDir.toPath())
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".java"))
                        .collect(Collectors.toList()));
            } catch (IOException e) {
                LOGGER.error("Error while collecting source files. ", e);
            }
        }

        sources.forEach(path -> LOGGER.debug("Collected source file {}", path));

        List<AnnotationInfo> annotationInfo = new ArrayList<>();
        for (Path sourceFile : sources) {
            annotationInfo.addAll(createAnnotationInfo(sourceFile));
        }

        ReportContext reportContext = new ReportContext();

        try {
            reportContext.createReport(outputFormat, annotationInfo, destination);
        } catch (ReportCreationException e) {
            LOGGER.error("Unable to create Report.", e);
        }

    }

    private List<AnnotationInfo> createAnnotationInfo(Path sourceFile) {

        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        try(Stream<String> stream  = Files.lines(sourceFile)) {

            List<String> candidates = stream
                    .filter(line -> line.contains("@" + annotationClass))
                    .map(String::trim)
                    .collect(Collectors.toList());

            candidates.forEach(s ->  annotationInfos.add(createAnnotationInfoFromString(s, sourceFile)));

        } catch (IOException e) {
            LOGGER.error("Unable to read file {}", sourceFile, e);
        }
        return annotationInfos;
    }


    private AnnotationInfo createAnnotationInfoFromString(String annotationString, Path sourceFile) {
        return AnnotationStringParser.parse(annotationString, sourceFile.toFile().getName());
    }
}
