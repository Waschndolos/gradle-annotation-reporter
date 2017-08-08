package de.waschndolos;

import de.waschndolos.tasks.GenerateAnnotationReportTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnnotationReporterPluginTest {

    private AnnotationReporterPlugin plugin = new AnnotationReporterPlugin();

    @Test
    public void testTasksAreApplied() throws Exception {
        plugin.sources = new HashSet<>();
        plugin.annotationClass = "de.waschndolos.annotation.TestAnnotation";

        Project project = ProjectBuilder.builder().withName("demoProject").build();
        plugin.apply(project);

        Task generateHtmlAnnotationReport = project.getTasks().getByName("generateHtmlAnnotationReport");
        assertEquals("report", generateHtmlAnnotationReport.getGroup());
        assertTrue(generateHtmlAnnotationReport instanceof GenerateAnnotationReportTask);

    }
}