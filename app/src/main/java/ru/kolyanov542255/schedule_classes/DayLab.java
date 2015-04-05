package ru.kolyanov542255.schedule_classes;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Адольф on 18.01.2015.
 */
public class DayLab {

    public static final String TAG = "DayLab";
    public static final String FILENAME = "schedule.json";

    private ArrayList<DayOfWeek> days;
    private ArrayList<DayOfWeek> oddWeek;
    private ArrayList<DayOfWeek> evenWeek;
    private ScheduleJSONSerializer mSerializer;
    private static DayLab dayLab;
    private Context context;

    private DayLab(Context appContext){

        Resources res = appContext.getResources();
        final String[] days_names = res.getStringArray(R.array.DaysOfWeek);
        this.context = appContext;
        this.mSerializer = new ScheduleJSONSerializer(context, FILENAME);

        try {
            days = mSerializer.loadSchedule();
        } catch (Exception e) {
            days = new ArrayList<DayOfWeek>();
            Log.e(TAG, "Error loading lessons: ", e);
         }
        oddWeek = new ArrayList<DayOfWeek>();
        evenWeek = new ArrayList<DayOfWeek>();


        for (DayOfWeek d: days){
            if (d.isOdd()){
                oddWeek.add(d);
            }else{
                evenWeek.add(d);
            }
        }
        /*for (int i = 0, j = 0; i < 12; i++, j++){
            DayOfWeek d = new DayOfWeek(days_names[j].toString());
            if (i <= 5) {
                d.setOdd(false);
                evenWeek.add(d);
            }

            if (i == 5) {
                j = -1;
            }

            if (i > 5){
                d.setOdd(true);
                oddWeek.add(d);
            }

            days.add(d);
        }*/
    }

    public static DayLab get(Context c){
        if (dayLab == null){
            dayLab = new DayLab(c.getApplicationContext());
        }
        return dayLab;
    }

    public ArrayList<DayOfWeek> getDays() {
        return days;
    }

    public ArrayList<DayOfWeek> getOddWeek() {
        return oddWeek;
    }

    public ArrayList<DayOfWeek> getEvenWeek() {
        return evenWeek;
    }

    public ArrayList<ArrayList<DayOfWeek>> getWeeks(){
        ArrayList<ArrayList<DayOfWeek>> weeks = new ArrayList<ArrayList<DayOfWeek>>();
        weeks.add(oddWeek);
        weeks.add(evenWeek);

        return weeks;
    }





    public DayOfWeek getDay(UUID id){
        for (DayOfWeek day: days){
            if (id.equals(day.getId())){
                return day;
            }
        }
        Log.d(TAG, "return null");
        return null;
    }

    public Boolean saveSchedule(){
        try{
            mSerializer.saveSchedule(days);
            Log.d(TAG, "schedule saved");
            return true;
        } catch (Exception e){
            Log.e(TAG, "schedule not saved", e);
            return false;
        }
    }
}
