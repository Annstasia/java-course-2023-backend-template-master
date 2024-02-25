package edu.java.sources_interection.response;

import java.time.OffsetDateTime;
import lombok.Getter;

public class TimeResponse {
    @Getter
    private final OffsetDateTime lastModified;

    public TimeResponse(OffsetDateTime lastModified) {
        this.lastModified = lastModified;
    }
}
