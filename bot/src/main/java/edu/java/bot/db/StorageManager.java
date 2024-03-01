package edu.java.bot.db;

import edu.java.bot.dialogs.Dialog;
import edu.java.bot.links.classes.URLInfo;
import java.util.List;

public interface StorageManager {
    boolean addLink(long id, URLInfo urlInfo);

    boolean removeLink(long id, URLInfo urlInfo);

    List<URLInfo> getAllLinksById(long id);

    void addDialog(long id, Dialog dialog);

    void removeDialog(long id);

    Dialog getDialog(Long id);
}
