package edu.java.sources_interection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackOverflowDTO(List<ItemDTO> items) {
    public record ItemDTO(@JsonProperty("last_activity_date") long lastActivityDate) {
    }
}
