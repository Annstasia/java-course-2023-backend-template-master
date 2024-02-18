package edu.java.bot.links.parsers;

import edu.java.bot.links.classes.URLInfo;

public interface URLParser {
    URLInfo parseLink(String link);
}
