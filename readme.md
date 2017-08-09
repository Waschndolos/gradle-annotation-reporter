# Gradle annotation reporter

## Purpose
This plugin will generate a report of all annotations present in the java application source code. 
This can be necessary when you e.g. have a custom annotation and you want to create a report of where this annotation is used.


## Configuration
    
    task createMarkdownReport(type: de.waschndolos.tasks.GenerateAnnotationReportTask) {
        projects = project.subprojects.asList()
        annotationClass = 'Refactor'
    }

    
## -- Under Construction --
