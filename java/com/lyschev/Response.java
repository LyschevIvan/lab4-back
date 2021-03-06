package com.lyschev;

import java.util.List;

public class Response {
    public static String statusOk = "success";
    public static String statusFail = "failed";

    public String message;
    public String status;
    public String key;
    public List<PointEntity> points;
    public PointEntity last_point;
}
