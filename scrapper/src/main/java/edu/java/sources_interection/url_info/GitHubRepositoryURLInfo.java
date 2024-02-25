package edu.java.sources_interection.url_info;

import java.time.OffsetDateTime;
import lombok.Getter;

public class GitHubRepositoryURLInfo implements URLInfo {
    @Getter private final String name;
    @Getter private final String repository;
    private final OffsetDateTime lastModified;

    public GitHubRepositoryURLInfo(String name, String repository, OffsetDateTime lastModified) {
        this.name = name;
        this.repository = repository;
        this.lastModified = lastModified;
    }

    @Override
    public OffsetDateTime getLastUpdate() {
        return lastModified;
    }
}
