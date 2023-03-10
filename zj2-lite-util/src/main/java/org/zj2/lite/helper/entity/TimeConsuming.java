package org.zj2.lite.helper.entity;


import java.io.Serializable;

public class TimeConsuming implements Serializable {
    private static final long serialVersionUID = 1669958093043210665L;
    final String name;
    final long startTime;
    long endTime;

    public TimeConsuming(String name) {
        this.name = name;
        this.startTime = System.currentTimeMillis();
        this.endTime = 0;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    protected void finish() {
        if(endTime == 0) { this.endTime = System.currentTimeMillis(); }
    }
}