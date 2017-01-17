package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.MealTestData.MEAL2;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class ServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceTest.class);
    private static StringBuilder results = new StringBuilder();

    @Autowired
    UserService userService;

    @Autowired
    MealService mealService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            LOG.info(result + " ms\n");
        }
    };

    @Before
    public void setUp() throws Exception {
        userService.evictCache();
    }

    @AfterClass
    public static void printResult() {
        LOG.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------\n" +
                results +
                "---------------------------------\n");
    }

    @Category(User.class)
    @Test
    public void userTestSave() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", 1555, false, Collections.singleton(Role.ROLE_USER));
        User created = userService.save(newUser);
        newUser.setId(created.getId());
        USER_MODEL_MATCHER.assertCollectionEquals(Arrays.asList(ADMIN, newUser, USER), userService.getAll());
    }

    @Category(User.class)
    @Test(expected = DataAccessException.class)
    public void userTestDuplicateMailSave() throws Exception {
        userService.save(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER));
    }

    @Category(User.class)
    @Test
    public void userTestDelete() throws Exception {
        userService.delete(USER_ID);
        USER_MODEL_MATCHER.assertCollectionEquals(Collections.singletonList(ADMIN), userService.getAll());
    }

    @Category(User.class)
    @Test(expected = NotFoundException.class)
    public void userTestNotFoundDelete() throws Exception {
        userService.delete(1);
    }

    @Category(User.class)
    @Test
    public void userTestGet() throws Exception {
        User user = userService.get(USER_ID);
        USER_MODEL_MATCHER.assertEquals(USER, user);
    }

    @Category(User.class)
    @Test(expected = NotFoundException.class)
    public void userTestGetNotFound() throws Exception {
        userService.get(1);
    }

    @Category(User.class)
    @Test
    public void userTestGetByEmail() throws Exception {
        User user = userService.getByEmail("user@yandex.ru");
        USER_MODEL_MATCHER.assertEquals(USER, user);
    }

    @Category(User.class)
    @Test
    public void userTestGetAll() throws Exception {
        Collection<User> all = userService.getAll();
        USER_MODEL_MATCHER.assertCollectionEquals(Arrays.asList(ADMIN, USER), all);
    }

    @Category(User.class)
    @Test
    public void userTestUpdate() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        userService.update(updated);
        USER_MODEL_MATCHER.assertEquals(updated, userService.get(USER_ID));
    }

    @Category(Meal.class)
    @Test
    public void mealTestDelete() throws Exception {
        mealService.delete(MEAL1_ID, USER_ID);
        MEAL_MODEL_MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), mealService.getAll(USER_ID));
    }

    @Category(Meal.class)
    @Test
    public void mealTestDeleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        mealService.delete(MEAL1_ID, 1);
    }

    @Category(Meal.class)
    @Test
    public void mealTestSave() throws Exception {
        Meal created = getCreated();
        mealService.save(created, USER_ID);
        MEAL_MODEL_MATCHER.assertCollectionEquals(Arrays.asList(created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), mealService.getAll(USER_ID));
    }

    @Category(Meal.class)
    @Test
    public void mealTestGet() throws Exception {
        Meal actual = mealService.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MODEL_MATCHER.assertEquals(ADMIN_MEAL1, actual);
    }

    @Category(Meal.class)
    @Test
    public void mealTestGetNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        mealService.get(MEAL1_ID, ADMIN_ID);
    }

    @Category(Meal.class)
    @Test
    public void mealTestUpdate() throws Exception {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        MEAL_MODEL_MATCHER.assertEquals(updated, mealService.get(MEAL1_ID, USER_ID));
    }

    @Category(Meal.class)
    @Test
    public void mealTestUpdateNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
        mealService.update(MEAL1, ADMIN_ID);
    }

    @Category(Meal.class)
    @Test
    public void mealTestGetAll() throws Exception {
        MEAL_MODEL_MATCHER.assertCollectionEquals(MEALS, mealService.getAll(USER_ID));
    }

    @Category(Meal.class)
    @Test
    public void mealTestGetBetween() throws Exception {
        MEAL_MODEL_MATCHER.assertCollectionEquals(Arrays.asList(MEAL3, MEAL2, MEAL1),
                mealService.getBetweenDates(LocalDate.of(2015, Month.MAY, 30), LocalDate.of(2015, Month.MAY, 30), USER_ID));
    }
}
