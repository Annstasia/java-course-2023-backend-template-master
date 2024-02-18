package edu.java.bot.links.parsers;

import edu.java.bot.links.classes.StackOverflowURLInfo;
import edu.java.bot.links.classes.URLInfo;

public class StackOverflowURLParser implements URLParser {
    @Override
    public URLInfo parseLink(String link) {
        if (link.startsWith("https://stackoverflow.com/") || link.equals("https://stackoverflow.com")) {
            return new StackOverflowURLInfo(link);
        }
        return null;
    }
}
