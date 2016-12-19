package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private ClassPathXmlApplicationContext applicationContext;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = applicationContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        applicationContext.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (request.getParameter("dateTime") != null) {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")),
                    AuthorizedUser.getId());

            LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            mealRestController.save(meal);
            response.sendRedirect("meals");
        } else {
            String fromTime = request.getParameter("fromTime");
            String toTime = request.getParameter("toTime");
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");

            LocalTime startTime = LocalTime.parse(fromTime.isEmpty() ? LocalTime.MIN.toString() : fromTime);
            LocalTime endTime = LocalTime.parse(toTime.isEmpty() ? LocalTime.MAX.toString() : toTime);
            LocalDate startDate = LocalDate.parse(fromDate.isEmpty() ? LocalDate.MIN.toString() : fromDate);
            LocalDate endDate = LocalDate.parse(toDate.isEmpty() ? LocalDate.MAX.toString() : toDate);

            request.setAttribute("startTime", request.getParameter("fromTime"));
            request.setAttribute("endTime", request.getParameter("toTime"));
            request.setAttribute("startDate", request.getParameter("fromDate"));
            request.setAttribute("endDate", request.getParameter("toDate"));

            request.setAttribute("meals",
                    MealsUtil.getFilteredWithExceeded(mealRestController.getAll(AuthorizedUser.getId()).stream()
                            .filter(m -> (m.getDate().isAfter(startDate) || m.getDate().isEqual(startDate))
                                    && (m.getDate().isBefore(endDate) || m.getDate().isEqual(endDate)))
                            .collect(Collectors.toList()), startTime, endTime, AuthorizedUser.getCaloriesPerDay()));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");


        if (action == null) {
            LOG.info("getAll");
            request.setAttribute("meals",
                    MealsUtil.getWithExceeded(mealRestController.getAll(AuthorizedUser.getId()), AuthorizedUser.getCaloriesPerDay()));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);

        } else if ("delete".equals(action)) {
            int id = getId(request);
            LOG.info("Delete {}", id);
            mealRestController.delete(id, AuthorizedUser.getId());
            response.sendRedirect("meals");

        } else if ("create".equals(action) || "update".equals(action)) {
            final Meal meal = action.equals("create") ?
                    new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, AuthorizedUser.getId()) :
                    mealRestController.get(getId(request), AuthorizedUser.getId());
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("meal.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
