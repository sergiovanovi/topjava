package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealMemoryDAO;
import ru.javawebinar.topjava.dao.MealMemoryDAOImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import static org.slf4j.LoggerFactory.getLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {

    private static final Logger LOG = getLogger(MealServlet.class);
    private MealMemoryDAO mealMemoryDAO = new MealMemoryDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action != null) {
            if (action.equalsIgnoreCase("remove")) {
                mealMemoryDAO.remove(Integer.parseInt(request.getParameter("id")));
                LOG.debug("remove complete");
            }
            if (action.equalsIgnoreCase("update")) {
                request.setAttribute("newMeal", MealMemoryDAOImpl.hardMap.get(Integer.parseInt(request.getParameter("id"))));
            }
        }

        request.setAttribute("list", mealMemoryDAO.list());
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
        LOG.debug("redirect to meals");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        LocalDateTime date = LocalDateTime.parse(request.getParameter("date"));
        String desc = request.getParameter("desc");
        int ccal = Integer.parseInt(request.getParameter("ccal"));
        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            int newId = MealsUtil.getId();
            mealMemoryDAO.put(newId, new Meal(newId , date, desc, ccal));
            LOG.debug("add complete");
        } else {
            int newId = Integer.parseInt(id);
            mealMemoryDAO.put(newId, new Meal(newId, date, desc, ccal));
            LOG.debug("update complete");
        }

        response.sendRedirect("meals");
        LOG.debug("redirect to meals");
    }
}
