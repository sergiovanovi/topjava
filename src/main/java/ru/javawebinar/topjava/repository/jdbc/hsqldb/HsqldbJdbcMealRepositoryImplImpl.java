package ru.javawebinar.topjava.repository.jdbc.hsqldb;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.jdbc.JdbcMealRepositoryImpl;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Collection;

@Repository
@Profile("hsqldb")
public class HsqldbJdbcMealRepositoryImplImpl extends JdbcMealRepositoryImpl {

    public HsqldbJdbcMealRepositoryImplImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", Timestamp.valueOf(meal.getDateTime()))
                .addValue("user_id", userId);

        return preSave(map, meal);
    }

    @Override
    public Collection<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=?  AND date_time BETWEEN  ? AND ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
    }
}
