package com.huiseong.blockphone.item;

public class AlarmListItem {
    private String timeRange2;

    private String day2;
    //public Switch alarmOnoff; //이것을 굳이 클래스에 넣을 필요가 없다. //
    private  int swiOnoff;
    private  int alarmID;

    //getter & setter
    public String getTimeRange2() {
        return timeRange2;
    }

    public void setTimeRange2(String timeRange2) {
        this.timeRange2 = timeRange2;
    }

    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    //생성자
    public AlarmListItem(String timeRan, String dayPara, int swiOnoff, int alarmID) {
    this.timeRange2 = timeRan;
    this.day2 = dayPara;
    this.swiOnoff = swiOnoff;
    this.alarmID=alarmID;
    }

    public int getSwiOnoff() {
        return swiOnoff;
    }

    public void setSwiOnoff(int swiOnoff) {
        this.swiOnoff = swiOnoff;
    }

    public int getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(int alarmID) {
        this.alarmID = alarmID;
    }
}