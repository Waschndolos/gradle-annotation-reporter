package de.waschndolos.processing.exception;

import java.io.IOException;

public class ReportCreationException extends Exception {

    public ReportCreationException(String msg) {
        super(msg);
    }

    public ReportCreationException(String msg, Exception e) {
        super(msg, e);
    }
}
