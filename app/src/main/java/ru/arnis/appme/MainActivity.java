package ru.arnis.appme;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.IntegerRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.arnis.gparser.GPItem;
import com.arnis.gparser.GPParser;
import com.arnis.gparser.GPRequest;
import com.arnis.gparser.TaskListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    GPParser gpParser;
    private Map<String,AtomicInteger> categoriesStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.RGBA_8888);

        gpParser = new GPParser();
        gpParser.init();
//        gpParser.loadApps(GPRequest.make(0,100,GPRequest.TOP_FREE_APPS),GPRequest.make(0,100,GPRequest.TOP_PAID_APPS)).setTaskListener(new TaskListener() {
//            @Override
//            public void taskFinished() {
//                Log.d("happy", "taskFinished: ");
//            }
//        });


        getUserApps();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getAppUsages();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (gpParser.getGpCategories().size()!=60)
                        try {
                            Thread.sleep(1000);
                            Log.d("happy", "WAIT");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    loadCategories();
                    for (final App app:App.getApps())
                        if (app.isActive()){
                            GPItem item = gpParser.syncLoadAppInfo(GPRequest.make(app.getPack()));
                            incCategories(item.getCategories());
                            Log.d("happy", app.getName()+" from category "+ item.getCategories().get(0)+" last used: "+app.getLastTimeUsed()+", score: "+String.valueOf(app.getResult()));
                        }

                    setUpChart();
                }
            }).start();
        }
    }

    private void getUserApps() {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                App.newApp(packageInfo.packageName,pm.getApplicationLabel(packageInfo).toString());
        }
    }

    @RequiresApi(android.os.Build.VERSION_CODES.LOLLIPOP_MR1)
    private void getAppUsages(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,-1);
        UsageStatsManager usageStatsManager = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,calendar.getTimeInMillis(),System.currentTimeMillis());
        for (UsageStats stats:usageStats){
            App app = App.getApp(stats.getPackageName());
            if (app !=null)
                app.setLastTimeUsed(stats.getLastTimeUsed());
        }
    }

    private void loadCategories(){
        categoriesStats = new HashMap<>();
        for (String category:gpParser.getGpCategories())
            categoriesStats.put(category,new AtomicInteger(0));
    }

    private void incCategories(List<String> categories){
        for (String category:categories){
            AtomicInteger ai = categoriesStats.get(category);
            if (ai!=null)
                ai.incrementAndGet();
        }
    }

    private void setUpChart(){
        final PieChart pieChart = (PieChart) findViewById(R.id.pie_chart);
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry entry:categoriesStats.entrySet())
            if (((AtomicInteger)entry.getValue()).intValue()!=0){
                Log.d("happy", entry.getKey()+" "+entry.getValue());
                entries.add(new PieEntry(((AtomicInteger)entry.getValue()).floatValue(),entry.getKey().toString()));
            }

        PieDataSet dataSet = new PieDataSet(entries,"preferences");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        final PieData piedata = new PieData(dataSet);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pieChart.setData(piedata);
                pieChart.animateY(1000);
                pieChart.setHoleRadius(30f);
                pieChart.setTransparentCircleRadius(0f);
                pieChart.setHoleColor(Color.TRANSPARENT);
            }
        });
    }

}

