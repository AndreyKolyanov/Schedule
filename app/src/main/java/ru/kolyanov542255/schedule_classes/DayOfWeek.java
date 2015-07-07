package ru.kolyanov542255.schedule_classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Адольф on 18.01.2015.
 */
public class DayOfWeek {

    private ArrayList<Lesson> lessons;
    private Lesson onDel;
    private String name;
    private boolean isOdd;
    private UUID id;
    private int duration;

    DayOfWeek(String name, int duration){
        this.name = name;
        this.id = UUID.randomUUID();
        this.duration = duration;

        lessons = new ArrayList<Lesson>();
    }

    DayOfWeek(JSONObject json, int duration)throws JSONException{
        lessons = new ArrayList<Lesson>();

        this.name = json.getString("DAY_NAME");
        this.id = UUID.fromString(json.getString("DAY_ID"));
        this.isOdd = json.getBoolean("DAY_IS_ODD");
        JSONArray lessons_json = json.getJSONArray("LESSONS");

        for (int i = 0; i < lessons_json.length(); i++){
            lessons.add(new Lesson(lessons_json.getJSONObject(i),duration));
        }

    }

    public void addLesson(Lesson lesson){

        lessons.add(lesson);
    }

    public String toString(){
        return this.name;
    }

    public boolean isOdd() {
        return isOdd;
    }

    public JSONObject toJSON() throws JSONException{

        Boolean isOdd = isOdd();
        JSONArray array = new JSONArray();
        JSONObject day = new JSONObject();
        day.put("DAY_NAME", name);
        day.put("DAY_IS_ODD", isOdd);
        day.put("DAY_ID", id);

        for (Lesson l : lessons){
            array.put(l.toJSON());
        }
        day.put("LESSONS", array);

        return day;
    }

    public void setOdd(boolean isOdd) {
        this.isOdd = isOdd;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public Lesson getLesson(UUID lessonId){
        for (Lesson l : lessons){
            if (l.getId().equals(lessonId)){
                return l;
            }
        }
        return null;
    }

    public void removeLesson(Lesson less){
        onDel = less;
    }

    public void delLesson(){
        lessons.remove(onDel);
    }

    public void delLesson(Lesson lesson){
        lessons.remove(lesson);
    }
}
