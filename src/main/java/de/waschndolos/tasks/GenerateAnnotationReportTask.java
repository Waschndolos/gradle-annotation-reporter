package de.waschndolos.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.waschndolos.model.AnnotationInfo;
import de.waschndolos.processing.AnnotationStringParser;
import de.waschndolos.processing.exception.ReportCreationException;
import de.waschndolos.processing.output.ReportContext;

public class GenerateAnnotationReportTask extends DefaultTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateAnnotationReportTask.class);

    @Input
    Set<Project> projects;

    @Input
    String annotationClass;

    @Input
    String outputFormat = "markdown";

    @Input
    String destination = getProject().getBuildDir().getPath();

    @Override
    public String getGroup() {
        return "report";
    }

    @TaskAction
    public void executeTask() {

        Set<File> sources = collectSources();

        LOGGER.debug("Searching in {} source directories.", sources.size());

        Set<Path> sourcePathList = new HashSet<>();
        for (File srcDir : sources) {
            try {
                sourcePathList.addAll(Files.walk(srcDir.toPath())
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".java"))
                        .collect(Collectors.toList()));
            } catch (IOException e) {
                LOGGER.error("Error while collecting source files. ", e);
            }
        }

        sourcePathList.forEach(path -> LOGGER.debug("Collected source file {}", path));

        List<AnnotationInfo> annotationInfo = new ArrayList<>();
        for (Path sourceFile : sourcePathList) {
            annotationInfo.addAll(createAnnotationInfo(sourceFile));
        }

        ReportContext reportContext = new ReportContext();

        try {
            reportContext.createReport(outputFormat, annotationInfo, destination);
        } catch (ReportCreationException e) {
            LOGGER.error("Unable to create Report.", e);
        }

    }

    private Set<File> collectSources() {
        Set<File> sourceFiles = new HashSet<>();

        for (Project p : projects) {
            if (p.getPlugins().hasPlugin("java")) {
                JavaPluginConvention javaConvention = p.getConvention().getPlugin(JavaPluginConvention.class);
                LOGGER.info("Collecting from project {}", p.getName());
                javaConvention.getSourceSets().stream().forEach(sourceSet -> sourceFiles.addAll(sourceSet.getAllJava().getFiles()));
            }
        }
        return sourceFiles;
    }

    private List<AnnotationInfo> createAnnotationInfo(Path sourceFile) {

        LOGGER.info("Processing now source file: {}", sourceFile);

        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        try(Stream<String> stream  = Files.lines(sourceFile,  Charset.forName("Cp1252"))) {

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
