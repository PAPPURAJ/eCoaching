package com.blogspot.rajbtc.onlineclass.dataclass;

public class ClassUpData {
    public String className,teacher,startTime,duration,classLink;

    public ClassUpData(){

    }

    public ClassUpData(String className, String teacher, String startTime, String duration, String classLink) {
        this.className = className;
        this.teacher = teacher;
        this.startTime = startTime;
        this.duration = duration;
        this.classLink = classLink;
    }

    public String getClassName() {
        return className;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getClassLink() {
        return classLink;
    }
}
