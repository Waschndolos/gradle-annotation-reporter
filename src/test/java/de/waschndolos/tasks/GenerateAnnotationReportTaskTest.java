package de.waschndolos.tasks;

import de.waschndolos.AnnotationReporterPlugin;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class GenerateAnnotationReportTaskTest {


    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Project project = ProjectBuilder.builder().build();

    @Before
    public void setUp() throws Exception {
        project.getPluginManager().apply(AnnotationReporterPlugin.class);
    }

    @Test
    public void testReportCreation() throws Exception {
        HashSet<File> sourceSet = new HashSet<>();
        sourceSet.add(new File(getClass().getClassLoader().getResource("TestClass.java").toURI()));

        File outputFile = temporaryFolder.newFile("report.md");

        GenerateAnnotationReportTask generateHtmlAnnotationReport = (GenerateAnnotationReportTask) project.getTasks().getByName("generateHtmlAnnotationReport");
        generateHtmlAnnotationReport.sources = sourceSet;
        generateHtmlAnnotationReport.annotationClass = "MyAnnotation";
        generateHtmlAnnotationReport.outputFormat = "markup";
        generateHtmlAnnotationReport.destination = temporaryFolder.getRoot().getPath();

        generateHtmlAnnotationReport.executeTask();


        URL expectedResultURL = getClass().getClassLoader().getResource("report.md");
        File file = new File(expectedResultURL.toURI());

        assertEquals("Files should match", FileUtils.readFileToString(file, "UTF-8"), FileUtils.readFileToString(outputFile, "UTF-8"));


    }
}