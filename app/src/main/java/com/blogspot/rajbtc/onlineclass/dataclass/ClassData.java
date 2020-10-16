package com.blogspot.rajbtc.onlineclass.dataclass;

import com.google.firebase.database.DatabaseReference;

public class ClassData {
    private DatabaseReference docID;
    private  String className,day,teacher,startTime,lastUpdate,duration,link;

    public ClassData(DatabaseReference docID,String className,String day, String teacher, String startTime, String lastUpdate, String duration, String link) {
        this.docID=docID;
        this.className = className;
        this.day=day;
        this.teacher = teacher;
        this.startTime = startTime;
        this.lastUpdate = lastUpdate;
        this.duration = duration;
        this.link = link;
    }

    public DatabaseReference getDocID() {
        return docID;
    }

    public String getClassName() {
        return className;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDate() {
        return lastUpdate;
    }

    public String getDuration() {
        return duration;
    }

    public String getLink() {
        return link;
    }

}
