package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

@Controller
public class MealController {

    @Autowired
    private MealService service;

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public String users(Model model) {
        model.addAttribute("meals", MealsUtil.getWithExceeded(service.getAll(AuthorizedUser.id()), AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.POST)
    public String filter(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                         @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        LocalDateTime startDateTime = LocalDateTime.of(startDate.equals("") ? LocalDate.of(2000, 1, 1) : LocalDate.parse(startDate)
                , startTime.equals("") ? LocalTime.MIN : LocalTime.parse(startTime));
        LocalDateTime endDateTime = LocalDateTime.of(endDate.equals("") ? LocalDate.of(3000, 1, 1) : LocalDate.parse(endDate)
                , endTime.equals("") ? LocalTime.MAX : LocalTime.parse(endTime));

        Collection<Meal> result = service.getBetweenDateTimes(startDateTime, endDateTime, AuthorizedUser.id());

        model.addAttribute("meals", MealsUtil.getWithExceeded(result, AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    public String saveAndEdit(@RequestParam("id") String id, @RequestParam("dateTime") String dateTime,
                              @RequestParam("description") String description, @RequestParam("calories") String cal) {
        if (id.equalsIgnoreCase("")) {
            Meal meal = new Meal(LocalDateTime.parse(dateTime), description, Integer.valueOf(cal));
            service.save(meal, AuthorizedUser.id());
        } else {
            Meal meal = new Meal(Integer.valueOf(id), LocalDateTime.parse(dateTime), description, Integer.valueOf(cal));
            service.update(meal, AuthorizedUser.id());
        }
        return "redirect:/meals";
    }

    @RequestMapping(value = "/delete{id}")
    public String delete(@PathVariable("id") int id) {
        service.delete(id, AuthorizedUser.id());
        return "redirect:/meals";
    }

    @RequestMapping(value = "/edit{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Meal meal = service.get(id, AuthorizedUser.id());
        model.addAttribute("meal", meal);
        return "meal";
    }

    @RequestMapping(value = "/add")
    public String add(Model model) {
        model.addAttribute("meal", new Meal());
        return "meal";
    }
}
