package edu.java.bot.db;

import edu.java.bot.dialogs.Dialog;
import edu.java.bot.links.classes.URLInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FictiveStorageManager implements StorageManager {
    private final Map<Long, List<URLInfo>> fictiveDB = new HashMap<>();
    private final Map<Long, Dialog> fictiveDialogDB = new HashMap<>();

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

    @Override
    public void addDialog(long id, Dialog dialog) {
        fictiveDialogDB.put(id, dialog);
    }

    @Override
    public void removeDialog(long id) {
        fictiveDialogDB.remove(id);
    }

    @Override
    public Dialog getDialog(Long id) {
        return fictiveDialogDB.get(id);
    }

}
