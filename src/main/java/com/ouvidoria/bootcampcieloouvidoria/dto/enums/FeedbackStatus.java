package com.ouvidoria.bootcampcieloouvidoria.dto.enums;

public enum FeedbackStatus {
    RECEIVED("RECEIVED"),
    IN_PROCESS("IN_PROCESS"),
    FINALIZED("FINALIZED");

    public final String label;

    private FeedbackStatus(String label) {
        this.label = label;
    }
}
