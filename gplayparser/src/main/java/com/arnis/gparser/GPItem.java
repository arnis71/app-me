package com.arnis.gparser;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndroidDev on 09.02.2017.
 */

public class GPItem {
    static final String UNKNOWN = "unknown";

    private String packageName;
    private String title;
    private String iconUrl;
    private String rating;
    private List<String> categories;

    GPItem(String packageName) {
        this.packageName = packageName;
        categories = new ArrayList<>();
        if (packageName.equals(UNKNOWN))
            categories.add(UNKNOWN);
    }

    public String getPackageName() {
        return packageName;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addCategory(String category){
        categories.add(category);
    }
}
