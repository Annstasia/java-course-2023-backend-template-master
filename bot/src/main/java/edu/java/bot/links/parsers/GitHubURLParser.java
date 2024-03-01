package edu.java.bot.links.parsers;

import edu.java.bot.links.classes.GitHubURLInfo;
import edu.java.bot.links.classes.URLInfo;

public class GitHubURLParser implements URLParser {
    @Override
    public URLInfo parseLink(String link) {
        if (link.startsWith("https://github.com/") || link.equals("https://github.com")) {
            return new GitHubURLInfo(link);
        }
        return null;
    }
}
