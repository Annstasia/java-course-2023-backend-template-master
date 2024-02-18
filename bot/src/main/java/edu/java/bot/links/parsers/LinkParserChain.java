package edu.java.bot.links.parsers;

import edu.java.bot.links.classes.URLInfo;

public class LinkParserChain implements URLParser {
    private final static LinkParserNode CHAIN = new LinkParserNode(new StackOverflowURLParser());

    static {
        CHAIN.setNext(new GitHubURLParser());
    }

    @Override
    public URLInfo parseLink(String link) {
        return CHAIN.parseLink(link);
    }
}
