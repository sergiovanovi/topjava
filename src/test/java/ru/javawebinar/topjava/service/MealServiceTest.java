package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        Meal meal = service.get(FIRST_ID, 100000);
        MATCHER.assertEquals(MEAL_1, meal);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundGet() throws Exception {
        service.get(FIRST_ID, 100001);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(FIRST_ID, 100000);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        service.delete(FIRST_ID, 100001);
    }

    @Test
    public void testGetBetweenDates() throws Exception {
        Collection<Meal> meals = service.getBetweenDates(LocalDate.of(2016, 12, 27), LocalDate.of(2016, 12, 27), 100000);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_3, MEAL_2, MEAL_1), meals);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        Collection<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2016, 12, 28, 12, 59, 0), LocalDateTime.of(2016, 12, 28, 13, 1, 0), 100001);
        MATCHER.assertCollectionEquals(Collections.singletonList(MEAL_5), meals);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Meal> meals = service.getAll(100000);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_3, MEAL_2, MEAL_1), meals);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL_1);
        service.update(updated, 100000);
        MATCHER.assertEquals(updated, service.get(updated.getId(), 100000));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundUpdate() throws Exception {
        Meal updated = new Meal(MEAL_1);
        service.update(updated, 100001);
    }

    @Test
    public void testSave() throws Exception {
        Meal saved = new Meal(LocalDateTime.of(2016, 12, 28, 10, 0), "Завтрак", 500);
        service.save(saved, 100000);
        MATCHER.assertCollectionEquals(Arrays.asList(saved, MEAL_3, MEAL_2, MEAL_1), service.getAll(100000));
    }

}