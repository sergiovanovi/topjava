package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class MealRestController {
    private static final Logger LOG = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public Meal save(Meal meal){
        LOG.info("save " + meal);
        return service.save(meal);
    }

    public void delete(int id, int userId){
        LOG.info("delete " + id);
        service.delete(id, userId);
    }

    public Meal get(int id, int userId){
        LOG.info("get " + id);
        return service.get(id, userId);
    }

    public List<Meal> getAll(int userId){
        LOG.info("getAll");
        return service.getAll(userId);
    }
}
