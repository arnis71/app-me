package com.arnis.gparser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by AndroidDev on 09.02.2017.
 */

class GPNetwork {
    private OkHttpClient client;
    private GPResponseListener gpResponseListener;

    GPNetwork() {
        client = new OkHttpClient();
    }

    void setGpResponseListener(GPResponseListener gpResponseListener) {
        this.gpResponseListener = gpResponseListener;
    }

    void getItems(final GPRequest.GPRequestItems gpRequest){
        RequestBody formBody = new FormBody.Builder()
                    .add("start", gpRequest.getStartIndex())
                    .add("num", gpRequest.getEndIndex())
                    .build();
        final Request request = new Request.Builder()
                    .url(gpRequest.getCategoryUrl())
                    .post(formBody)
                    .build();

        client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        gpResponseListener.onAppsListLoaded(null);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        gpResponseListener.onAppsListLoaded(response.body().string());
                    }
                });
    }

    void getItemInfo(final GPRequest.GPRequestItem gpRequest, final int itemIndex){
        final Request request = new Request.Builder()
                .url(gpRequest.getItemUrl())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                gpResponseListener.onAppInfoLoaded(null,0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                gpResponseListener.onAppInfoLoaded(response.body().string(),itemIndex);
            }
        });
    }

    String syncGetItemInfo(final GPRequest.GPRequestItem gpRequest) throws IOException {
        final Request request = new Request.Builder()
                .url(gpRequest.getItemUrl())
                .build();

        return client.newCall(request).execute().body().string();
    }

    void getCategories(){
        final Request request = new Request.Builder()
                .url(GPRequest.CATEGORIES)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                gpResponseListener.onCategoriesLoaded(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                gpResponseListener.onCategoriesLoaded(response.body().string());
            }
        });
    }
}
