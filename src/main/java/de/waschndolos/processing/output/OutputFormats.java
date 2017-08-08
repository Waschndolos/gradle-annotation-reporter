package de.waschndolos.processing.output;

public enum OutputFormats {

    MARKDOWN("markdown");

    private String formatString;

    OutputFormats(String formatString) {
        this.formatString = formatString;
    }

    public String getFormatString() {
        return formatString;
    }
}
