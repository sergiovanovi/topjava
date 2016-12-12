package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

public interface MealDAO {

    public void add(Meal meal);

    public void remove(Meal meal);

    public void set(Meal meal);

    public int getId(Meal meal);

    public List<MealWithExceed> list();
}
