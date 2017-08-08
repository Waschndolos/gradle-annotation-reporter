package de.waschndolos;

import de.waschndolos.tasks.GenerateAnnotationReportTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.Input;

import java.io.File;
import java.util.Set;

public class AnnotationReporterPlugin implements Plugin<Project> {

    @Input
    Set<File> sources;

    @Input
    String annotationClass;

    @Override
    public void apply(Project project) {

        project.getTasks().create("generateHtmlAnnotationReport", GenerateAnnotationReportTask.class);

    }
}
