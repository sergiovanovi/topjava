package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealDAOImpl implements MealDAO {
    private List<Meal> list = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));

    @Override
    public void add(Meal meal) {
        list.add(meal);
    }

    @Override
    public void remove(Meal meal) {
        int id = getId(meal);
        if (id != -1) list.remove(id);
    }

    @Override
    public void set(Meal meal) {
        int id = getId(meal);
        if (id != -1) list.set(id, meal);
    }

    @Override
    public int getId(Meal meal) {
        return list.indexOf(meal);
    }

    @Override
    public List<MealWithExceed> list() {
        return MealsUtil.getFilteredWithExceeded(list, LocalTime.of(0, 0), LocalTime.of(23, 0), 2000);
    }
}
