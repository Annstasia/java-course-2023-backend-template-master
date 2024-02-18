package edu.java.bot.links.classes;

public class StackOverflowURLInfo extends URLInfo {
    String link;

    public StackOverflowURLInfo(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }

    @Override
    public String getResourceType() {
        return "StackOverflow";
    }
}
