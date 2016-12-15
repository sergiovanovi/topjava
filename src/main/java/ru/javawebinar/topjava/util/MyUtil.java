package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class MyUtil {
    private static AtomicInteger id = new AtomicInteger(0);

    public static int getId(){
        return id.getAndIncrement();
    }
}
