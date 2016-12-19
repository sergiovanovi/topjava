package ru.javawebinar.topjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.util.MealsUtil;

/**
 * GKislin
 * 06.03.2015.
 */
public class AuthorizedUser {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorizedUser.class);

    private static int id = 1;

    public static int getId(){
        return id;
    }

    public static void setId(int userId){
        id = userId;
        LOG.info("Logged as " + id);
    }

    public static int getCaloriesPerDay() {
        return MealsUtil.DEFAULT_CALORIES_PER_DAY;
    }
}
