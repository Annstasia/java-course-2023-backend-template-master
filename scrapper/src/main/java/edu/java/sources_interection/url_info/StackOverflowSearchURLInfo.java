package edu.java.sources_interection.url_info;

import java.time.OffsetDateTime;
import lombok.Getter;

public class StackOverflowSearchURLInfo implements URLInfo {
    @Getter
    private final String searchQuery;
    private final OffsetDateTime lastModified;

    public StackOverflowSearchURLInfo(String searchQuery, OffsetDateTime lastModified) {
        this.searchQuery = searchQuery;
        this.lastModified = lastModified;
    }

    @Override
    public OffsetDateTime getLastUpdate() {
        return lastModified;
    }
}
