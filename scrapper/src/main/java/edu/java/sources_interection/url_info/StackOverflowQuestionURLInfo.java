package edu.java.sources_interection.url_info;

import java.time.OffsetDateTime;
import lombok.Getter;

public class StackOverflowQuestionURLInfo implements URLInfo {
    @Getter private final long questionID;
    private final OffsetDateTime lastModified;

    public StackOverflowQuestionURLInfo(long questionID, OffsetDateTime lastModified) {
        this.questionID = questionID;
        this.lastModified = lastModified;
    }

    @Override public OffsetDateTime getLastUpdate() {
        return lastModified;
    }
}
