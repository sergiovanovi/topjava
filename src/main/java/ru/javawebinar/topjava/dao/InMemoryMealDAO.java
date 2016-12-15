package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

public interface InMemoryMealDAO {

    public void remove(int id);

    public void put(int id, Meal meal);

    public List<MealWithExceed> list();
}
