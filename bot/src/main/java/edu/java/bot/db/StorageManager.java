package edu.java.bot.db;

import edu.java.bot.links.classes.URLInfo;
import java.util.List;

public interface StorageManager {
    boolean addLink(long id, URLInfo urlInfo);

    boolean removeLink(long id, URLInfo urlInfo);

    List<URLInfo> getAllLinksById(long id);
}
