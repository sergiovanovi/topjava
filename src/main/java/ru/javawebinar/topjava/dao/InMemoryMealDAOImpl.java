package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.MyUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryMealDAOImpl implements InMemoryMealDAO {
    private static final int MAX_CCAL_PER_DAY = 2000;
    public static ConcurrentHashMap<Integer, Meal> hardMap = new ConcurrentHashMap<>();
    static {
        int id = MyUtil.getId();
        hardMap.put(id, new Meal(id, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        id = MyUtil.getId();
        hardMap.put(id, new Meal(id, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        id = MyUtil.getId();
        hardMap.put(id, new Meal(id, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        id = MyUtil.getId();
        hardMap.put(id, new Meal(id, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        id = MyUtil.getId();
        hardMap.put(id, new Meal(id, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        id = MyUtil.getId();
        hardMap.put(id, new Meal(id, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public void remove(int id) {
        hardMap.remove(id);
    }

    @Override
    public void put(int id, Meal meal) {
        hardMap.put(id, meal);
    }

    @Override
    public List<MealWithExceed> list() {
        return MealsUtil.getFilteredWithExceeded(hardMap.values().stream().collect(Collectors.toList()), LocalTime.MIN, LocalTime.MAX, MAX_CCAL_PER_DAY);
    }
}
