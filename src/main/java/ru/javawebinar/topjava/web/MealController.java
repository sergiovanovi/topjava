package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
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
    public String filter(HttpServletRequest request, Model model) {
        String startDate = request.getParameter("startDate");
        String startTime = request.getParameter("startTime");
        LocalDateTime startDateTime = LocalDateTime.of(startDate.equals("") ? LocalDate.of(2000, 1, 1) : LocalDate.parse(startDate)
                , startTime.equals("") ? LocalTime.MIN : LocalTime.parse(startTime));

        String endDate = request.getParameter("endDate");
        String endTime = request.getParameter("endTime");
        LocalDateTime endDateTime = LocalDateTime.of(endDate.equals("") ? LocalDate.of(3000, 1, 1) : LocalDate.parse(endDate)
                , endTime.equals("") ? LocalTime.MAX : LocalTime.parse(endTime));

        Collection<Meal> result = service.getBetweenDateTimes(startDateTime, endDateTime, AuthorizedUser.id());

        model.addAttribute("meals", MealsUtil.getWithExceeded(result, AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    public String saveAndEdit(HttpServletRequest request) {
        String idString = request.getParameter("id");
        if (idString.equals("")) {
            Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            service.save(meal, AuthorizedUser.id());
        } else {
            Meal meal = new Meal(Integer.parseInt(idString),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            service.update(meal, AuthorizedUser.id());
        }
        return "redirect:/meals";
    }

    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request) {
        service.delete(Integer.parseInt(request.getParameter("id")), AuthorizedUser.id());
        return "redirect:/meals";
    }

    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, Model model) {
        Meal meal = service.get(Integer.parseInt(request.getParameter("id")), AuthorizedUser.id());
        model.addAttribute("meal", meal);
        return "meal";
    }

    @RequestMapping(value = "/add")
    public String add(Model model) {
        model.addAttribute("meal", new Meal());
        return "meal";
    }
}
