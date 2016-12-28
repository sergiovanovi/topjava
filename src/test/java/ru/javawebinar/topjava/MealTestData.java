package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

/**
 * GKislin
 * 13.03.2015.
 */
public class MealTestData {
    public static final int FIRST_ID = START_SEQ + 2;

    public static final Meal MEAL_1 = new Meal(FIRST_ID, LocalDateTime.of(2016, 12, 27, 9, 0), "Завтрак", 500);
    public static final Meal MEAL_2 = new Meal(FIRST_ID + 1, LocalDateTime.of(2016, 12, 27, 12, 0), "Обед", 1000);
    public static final Meal MEAL_3 = new Meal(FIRST_ID + 2, LocalDateTime.of(2016, 12, 27, 18, 0), "Ужин", 500);
    public static final Meal MEAL_4 = new Meal(FIRST_ID + 3, LocalDateTime.of(2016, 12, 28, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_5 = new Meal(FIRST_ID + 4, LocalDateTime.of(2016, 12, 28, 13, 0), "Обед", 1000);
    public static final Meal MEAL_6 = new Meal(FIRST_ID + 5, LocalDateTime.of(2016, 12, 28, 19, 0), "Ужин", 510);

    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher<>((expected, actual) -> expected == actual ||
            (Objects.equals(expected.getId(), actual.getId())
                    && Objects.equals(expected.getDateTime(), actual.getDateTime())
                    && Objects.equals(expected.getDescription(), actual.getDescription())
                    && Objects.equals(expected.getCalories(), actual.getCalories())
            ));

}
