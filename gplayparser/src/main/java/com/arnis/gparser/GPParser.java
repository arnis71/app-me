package com.arnis.gparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GPParser implements GPResponseListener {
    private GPNetwork network;
    private GPMatcher matcher;
    private List<GPItem> gpItems;
    private List<String> gpCategories;
    private TaskListener taskListener;
    private int totalItems;
    private int totalRequests;
    private int requestCount;

//    public static void main(String[] args) {
//        GPParser gpParser = new GPParser();
//        gpParser.init();
//
////        gpParser.loadApps(GPRequest.make(0,1,GPRequest.TOP_FREE_APPS));
//        System.out.println("list loaded");
//    }

    public void init(){
        network = new GPNetwork();
        network.setGpResponseListener(this);
        matcher = new GPMatcher();
        gpItems = new ArrayList<>();
        gpCategories = new ArrayList<>();
        totalItems = totalRequests = 0;
        loadCategories();
    }

    public GPParser loadApps(GPRequest... gpRequests) {
        requestCount=0;
        totalRequests = gpRequests.length;
        for (GPRequest request: gpRequests)
            network.getItems((GPRequest.GPRequestItems) request);
        return this;
    }

    public GPItem syncLoadAppInfo(GPRequest.GPRequestItem request){
        try {
            String response = network.syncGetItemInfo(request);
            return matcher.fetchItemInfo(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new GPItem(GPItem.UNKNOWN);
    }

    private void loadAppsInfo(){
        totalItems = gpItems.size();
        for (int i = 0;i<gpItems.size();i++){
            network.getItemInfo(GPRequest.make(gpItems.get(i).getPackageName()),i);
        }
    }

    private void loadCategories(){
        network.getCategories();
    }

    public List<GPItem> getGpItems() {
        return gpItems;
    }

    public List<String> getGpCategories() {
        return gpCategories;
    }

    public void setTaskListener(TaskListener taskListener) {
        this.taskListener = taskListener;
    }

    @Override
    public void onAppsListLoaded(String response) {
        requestCount++;

        if (response!=null)
            gpItems.addAll(matcher.fetchItemsFromList(response));

        if (requestCount == totalRequests)
            loadAppsInfo();
    }

    @Override
    public void onAppInfoLoaded(String response, int index) {
        if (response!=null)
            matcher.fetchItemInfo(response,gpItems.get(index));

        if (taskListener!= null && index == totalItems-1 && requestCount == totalRequests)
            taskListener.taskFinished();
    }

    @Override
    public void onCategoriesLoaded(String response) {
        if (response!=null)
            gpCategories = matcher.fetchCategories(response);
    }
}
