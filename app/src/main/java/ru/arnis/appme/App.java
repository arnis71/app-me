package ru.arnis.appme;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AndroidDev on 09.02.2017.
 */

public class App {
    private static Map<String,App> allApps = new HashMap<>();
//    private static long minLTU= 99999999999999L;
//    private static long maxLTU=0;

    private static DateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void newApp(String pack,String name){
        new App(pack, name);
    }
    public static App getApp(String packageName) {
        return allApps.get(packageName);
    }
//    public static void applyMinMax(){
//        for (App app:allApps.values())
//            app.result = ((double)app.lastTimeUsed-minLTU)/((double) maxLTU-minLTU);
//    }
    public static Collection<App> getApps(){
        return allApps.values();
    }

    private String pack;
    private String name;
    private long lastTimeUsed;
    private boolean system;
    private double result;

    private App(String pack, String name) {
        this.pack = pack;
        this.name = name;
        allApps.put(pack,this);
    }

    public String getName() {
        return name;
    }

    public double getResult() {
        return result;
    }

    public String getPack() {
        return pack;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public void setLastTimeUsed(long lastTimeUsed) {
//        if (lastTimeUsed<minLTU && lastTimeUsed!=0)
//            minLTU = lastTimeUsed;
//        if (lastTimeUsed>maxLTU)
//            maxLTU = lastTimeUsed;
        this.lastTimeUsed = lastTimeUsed;
    }

    public boolean isActive(){
        return lastTimeUsed != 0;
    }

    public String getLastTimeUsed() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastTimeUsed);
        return mDateFormat.format(calendar.getTime());
    }
}
