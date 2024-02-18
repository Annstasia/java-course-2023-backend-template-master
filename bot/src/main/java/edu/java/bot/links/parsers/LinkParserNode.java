package edu.java.bot.links.parsers;

import edu.java.bot.links.classes.URLInfo;

public class LinkParserNode implements URLParser {
    private LinkParserNode next;
    private URLParser realParser;

    public LinkParserNode(URLParser parser) {
        realParser = parser;
    }

    public LinkParserNode setNext(URLParser parser) {
        return setNext(new LinkParserNode(parser));
    }

    public LinkParserNode setNext(LinkParserNode next) {
        this.next = next;
        return this.next;
    }

    @Override
    public URLInfo parseLink(String link) {
        URLInfo parsedLink = realParser.parseLink(link);
        if (parsedLink != null) {
            return parsedLink;
        } else if (next != null) {
            return next.parseLink(link);
        }
        return null;
    }
}
