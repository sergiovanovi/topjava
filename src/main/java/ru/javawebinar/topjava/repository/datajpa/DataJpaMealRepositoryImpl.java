package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * GKislin
 * 27.03.2015.
 */
@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {

    @Autowired
    private CrudMealRepository mealRepository;

    @Autowired
    private CrudUserRepository userRepository;


    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(userRepository.getOne(userId));
        return mealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal tmp = mealRepository.findOne(id);

        if (tmp != null && tmp.getUser().getId() == userId) {
            return tmp;
        } else {
            return null;
        }
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return mealRepository.findAll(new Sort(Sort.Direction.DESC, "dateTime"))
                .stream()
                .filter(m -> m.getUser().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return getAll(userId)
                .stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDateTime(), startDate, endDate))
                .collect(Collectors.toList());
    }
}
