package com.arnis.gparser;

/**
 * Created by AndroidDev on 09.02.2017.
 */

public abstract class GPRequest {
    public static final String TOP_FREE_APPS = "https://play.google.com/store/apps/collection/topselling_free";
    public static final String TOP_PAID_APPS = "https://play.google.com/store/apps/collection/topselling_paid";
    public static final String TOP_GROSSING_APPS = "https://play.google.com/store/apps/collection/topgrossing";
    public static final String TOP_FREE_GAMES = "https://play.google.com/store/apps/category/GAME/collection/topselling_free";
    public static final String TOP_PAID_GAMES = "https://play.google.com/store/apps/category/GAME/collection/topselling_paid";

    static final String CATEGORIES = "https://play.google.com/store/apps";


    public static GPRequestItems make(int startIndex, int endIndex, String category){
        return new GPRequestItems(startIndex, endIndex, category);
    }
    public static GPRequestItem make(String packageName){
        return new GPRequestItem(packageName);
    }

    static class GPRequestItems extends GPRequest{
        private int startIndex;
        private int endIndex;
        private String category;

        private GPRequestItems(int startIndex, int endIndex, String category) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.category = category;
        }
        String getStartIndex() {
            return String.valueOf(startIndex);
        }

        String getEndIndex() {
            return String.valueOf(endIndex);
        }

        String getCategoryUrl() {
            return category;
        }
    }

    static class GPRequestItem extends GPRequest{
        private static final String BASE = "https://play.google.com/store/apps/details?id=";
        private String packageName;

        private GPRequestItem(String packageName) {
            this.packageName = packageName;
        }

        String getPackageName() {
            return packageName;
        }

        String getItemUrl(){
            return BASE+packageName;
        }
    }
}
