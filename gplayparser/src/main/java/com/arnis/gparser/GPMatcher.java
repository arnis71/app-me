package com.arnis.gparser;

import org.omg.CORBA.UNKNOWN;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AndroidDev on 09.02.2017.
 */

class GPMatcher {
    private static final String ITEMS_PATTERN = "</div> <a class=\"card-click-target\" href=\"/store/apps/details\\?id=(.+?)\"";

    private static final String ITEM_TITLE_PATTERN = "<div class=\"id-app-title\" tabindex=\"0\">(.+?)</div>";
    private static final String ITEM_CATEGORY_PATTERN = "<a class=\"document-subtitle category\" href=\"/store/apps/category/(.+?)\"> <span itemprop=\"genre\">(.+?)</span> </a>";

    private static final String CATEGORIES_PATTERN = "jsan=\"7.child-submenu-link,8.href,0.title\">(.+?)</a>";
    private static final String PACKAGE_PATTERN = "data-docid=\"(.+?)\"";

    GPMatcher() {
    }

    List<GPItem> fetchItemsFromList(String source){
        List<GPItem> gpItems = new ArrayList<>();
        Pattern r = Pattern.compile(ITEMS_PATTERN);

        Matcher m = r.matcher(source);
        while (m.find()) {
            gpItems.add(new GPItem(m.group(1)));
        }

        return gpItems;
    }

    void fetchItemInfo(String source, GPItem gpItem){
        Pattern pattern = Pattern.compile(ITEM_TITLE_PATTERN);

        Matcher m = pattern.matcher(source);
        if (m.find()) {
            gpItem.setTitle(m.group(1));
        }

        pattern = Pattern.compile(ITEM_CATEGORY_PATTERN);

        m = pattern.matcher(source);
        while (m.find()){
            gpItem.addCategory(m.group(2));
        }
    }

    GPItem fetchItemInfo(String source){
        GPItem gpItem = new GPItem(GPItem.UNKNOWN);

        Pattern pattern = Pattern.compile(PACKAGE_PATTERN);
        Matcher m = pattern.matcher(source);

        if (m.find()) {
            gpItem = new GPItem(m.group(1));

            pattern = Pattern.compile(ITEM_TITLE_PATTERN);
            m = pattern.matcher(source);
            if (m.find()) {
                gpItem.setTitle(m.group(1));
            }

            pattern = Pattern.compile(ITEM_CATEGORY_PATTERN);
            m = pattern.matcher(source);
            while (m.find()) {
                gpItem.addCategory(m.group(2));
            }
        }

        return gpItem;
    }

    List<String> fetchCategories(String source){
        List<String> categories = new ArrayList<>();
        Pattern r = Pattern.compile(CATEGORIES_PATTERN);

        Matcher m = r.matcher(source);
        while (m.find()) {
            categories.add(m.group(1));
        }

        return categories;
    }
}
