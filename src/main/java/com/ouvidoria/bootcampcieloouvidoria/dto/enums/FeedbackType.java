package com.ouvidoria.bootcampcieloouvidoria.dto.enums;

public enum FeedbackType {
    SUGGESTION("Suggestion"),
    PRAISE("Praise"),
    CRITICISM("Criticism");

    public final String label;

    private FeedbackType(String label) {
        this.label = label;
    }
}
