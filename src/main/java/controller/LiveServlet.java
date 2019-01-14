package controller;

import db.DBFactory;
import db.ReservationsDB;
import db.RoomsDB;
import domain.Reservation;
import domain.Room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/live")
public class LiveServlet extends BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Reservation> reservations = new ArrayList<>();
        List<Room> rooms = roomsDb.getRooms();

        for (Room room : rooms) {
            reservations.addAll(reservationsDB.getReservationsToday(room));
        }

        //request.setAttribute("reservations", generateReservationsJS(reservations));
        request.setAttribute("rooms", generateRoomsJS(rooms));
        String url = request.getParameter("secret") == null ? "live/index.jsp" : "secret/index.jsp";
        request.getRequestDispatcher(url).forward(request, response);
    }

}
