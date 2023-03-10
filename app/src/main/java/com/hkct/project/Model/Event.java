package com.hkct.project.Model;

import java.util.Date;

public class Event extends EventId{

    private static String image;
    private static String caption;
    private static Date time;
    private String user;

    public static String getImage_event() {
        return image;
    }

    public String getUser_event() {
        return user;
    }

    public static String getCaption_event() {
        return caption;
    }

    public static Date getTime_event() {
        return time;
    }

}
