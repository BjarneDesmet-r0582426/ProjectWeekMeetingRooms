package controller;

import db.DBFactory;
import db.ReservationsDB;
import db.RoomsDB;
import domain.Reservation;
import domain.Room;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

abstract public class BaseServlet extends HttpServlet {
    protected RoomsDB roomsDb = DBFactory.getRoomsDB();
    protected ReservationsDB reservationsDB = DBFactory.getReservationDB();

    protected String generateRoomsJS(List<Room> rooms) {
        return "var ROOMS = " + generateRoomsJSON(rooms) + ";";
    }

    protected String generateRoomsJSON(List<Room> rooms, Function<Room, List<Reservation>> fn) {
        List<String> items = new ArrayList<>();

        for(Room room : rooms) {
            String res = "{";
            res += "\"name\": \"" + room.getName().replace("\"", "\\\"") + "\",";
            res += "\"number\": \"" + room.getNumber() + "\",";
            res += "\"email\": \"" + room.getEmail() + "\",";
            res += "\"capacity\": " + room.getCapacity() + ",";
            res += "\"plugs\": " + room.getPlugCount() + ",";
            res += "\"reservations\": " + generateReservationsJSON(fn.apply(room));
            res += "}";
            items.add(res);
        }

        return "[" + String.join(",", items) + "]";
    }

    protected String generateRoomsJSON(List<Room> rooms) {
        return generateRoomsJSON(rooms, (room) -> reservationsDB.getReservationsToday(room));
    }

    protected String generateRoomsJSON(List<Room> rooms, Date start, Date end) {
        return generateRoomsJSON(rooms, (room) -> reservationsDB.getReservations(room, start, end));
    }

    protected String generateReservationsJSON(List<Reservation> reservations) {
        List<String> items = new ArrayList<>();

        for (Reservation reservation : reservations) {
            String registrar = reservation.getRegistrar();
            String res = "{";
            res += "\"registrar\": \"" + (registrar == null ? "" : registrar.replace("\"", "\\\"")) + "\",";
            res += "\"start\": " + reservation.getStart().getTime() + ",";
            res += "\"end\": " + reservation.getEnd().getTime();
            res += "}";
            items.add(res);
        }

        return "[" + String.join(",", items) + "]";
    }
}
