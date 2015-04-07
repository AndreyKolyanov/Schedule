package ru.kolyanov542255.schedule_classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Адольф on 29.12.2014.
 */
public class Lesson {
    private UUID id;
    private String name;
    private Date beginTime;
    private Date endTime;
    private String teacher;
    private boolean isShowTime;
    private String homeWork;
    private String room;
    private String time;

    public static final String LESSON_ID = "id";
    public static final String LESSON_NAME = "name";
    public static final String LESSON_BEGIN_TIME = "time";
    public static final String LESSON_TEACHER = "teacher";
    public static final String LESSON_ROOM = "room";
    public static final String IS_SHOW_TIME = "showTime";

    Lesson(String name){
        this.isShowTime = true;
        this.id = UUID.randomUUID();
        this.name = name;
        this.beginTime = new Date();
        long myTime = beginTime.getTime();
        myTime += (90 * 60 * 1000);
        this.endTime = new Date(myTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        if (isShowTime) {
            this.time = dateFormat.format(this.beginTime) + "-" + '\n' + "-"
                    + dateFormat.format(this.endTime);
            this.teacher = "";
        }else {
            this.time = "";
        }
    }

    public Lesson(JSONObject json) throws JSONException{
        this.id = UUID.fromString(json.getString(LESSON_ID));
        this.name = json.getString(LESSON_NAME);
        this.beginTime = new Date(json.getLong(LESSON_BEGIN_TIME));
        this.teacher = json.getString(LESSON_TEACHER);
        this.isShowTime = true;
        try{
            this.room = json.getString(LESSON_ROOM);
        } catch (JSONException e){
            this.room = "";
        }

        long myTime = beginTime.getTime();
        myTime += (95 * 60 * 1000);
        this.endTime = new Date(myTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        if (isShowTime) {
            this.time = dateFormat.format(this.beginTime) + "-" + '\n' + "-"
                    + dateFormat.format(this.endTime);
        }else {
            this.time = "";
        }
    }

    public void setShowTime(boolean isShowTime) {
        this.isShowTime = isShowTime;
    }

    public boolean isShowTime() {
        return isShowTime;
    }

    public UUID getId() {
        return id;
    }

    public String getTime() {
        if (isShowTime) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            this.time = dateFormat.format(this.beginTime) + "-" + '\n' + "-"
                    + dateFormat.format(this.endTime);
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public JSONObject toJSON() throws JSONException{

        JSONObject json = new JSONObject();

        json.put(LESSON_ID, id.toString());
        json.put(LESSON_NAME, name);
        json.put(LESSON_BEGIN_TIME, beginTime.getTime());
        json.put(LESSON_TEACHER, teacher);
        json.put(LESSON_ROOM, room);
        json.put(IS_SHOW_TIME, isShowTime);

        return json;

    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
        long myTime = beginTime.getTime();
        myTime += (95 * 60 * 1000);
        this.endTime = new Date(myTime);
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getHomeWork() {
        return homeWork;
    }

    public void setHomeWork(String homeWork) {
        this.homeWork = homeWork;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
