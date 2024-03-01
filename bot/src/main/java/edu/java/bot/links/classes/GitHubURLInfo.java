package edu.java.bot.links.classes;

public class GitHubURLInfo extends URLInfo {
    String link;

    public GitHubURLInfo(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }

    @Override
    public String getResourceType() {
        return "GitHub";
    }
}
