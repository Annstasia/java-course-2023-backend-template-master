package edu.java.bot.links.links_comparators;

import edu.java.bot.links.classes.URLInfo;

import java.util.Comparator;

public class ComparatorByType implements Comparator<URLInfo> {
    @Override
    public int compare(URLInfo o1, URLInfo o2) {
        int typeCompare = o1.getClass().getName().compareTo(o2.getClass().getName());
        if (typeCompare != 0) {
            return typeCompare;
        }
        return o1.toString().compareTo(o2.toString());
    }
}
