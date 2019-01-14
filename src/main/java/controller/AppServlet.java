package controller;

import db.DBFactory;
import db.ReservationsDB;
import db.RoomsDB;
import domain.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@WebServlet("/app")
public class AppServlet extends BaseServlet {
    protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("action") == null) {
            getApp(request, response);
        }
        else {
            try {
                getRooms(request, response);
            } catch (ParseException e) {
                throw new RuntimeException("Invlid date");
            }
        }
    }

    protected void getApp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("rooms", generateRoomsJS(roomsDb.getRooms()));
        request.getRequestDispatcher("app/index.jsp").forward(request, response);
    }

    protected void getRooms(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Date date = dateFormat.parse(request.getParameter("date"));
        Date start = Util.getMorning(date);
        Date end = Util.getEvening(date);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Cache-Control", "max-age:600");
        response.getWriter().write(generateRoomsJSON(roomsDb.getRooms(), start, end));
    }



}
