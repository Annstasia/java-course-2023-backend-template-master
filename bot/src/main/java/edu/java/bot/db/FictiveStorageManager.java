package edu.java.bot.db;

import edu.java.bot.links.classes.URLInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FictiveStorageManager implements StorageManager {
    private final Map<Long, List<URLInfo>> fictiveDB = new HashMap<>();

    @Override
    public boolean addLink(long id, URLInfo urlInfo) {
        return fictiveDB
            .computeIfAbsent(
                id, absentID -> new ArrayList<>())
            .add(urlInfo);
    }

    @Override
    public boolean removeLink(long id, URLInfo urlInfo) {
        return fictiveDB.getOrDefault(id, new ArrayList<>()).remove(urlInfo);
    }

    @Override
    public List<URLInfo> getAllLinksById(long id) {
        return fictiveDB.getOrDefault(id, new ArrayList<>());
    }
}
