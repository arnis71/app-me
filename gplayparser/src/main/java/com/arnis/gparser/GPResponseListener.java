package com.arnis.gparser;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by AndroidDev on 09.02.2017.
 */

interface GPResponseListener {
    void onAppsListLoaded(String response);
    void onAppInfoLoaded(String response, int index);
    void onCategoriesLoaded(String response);
}
