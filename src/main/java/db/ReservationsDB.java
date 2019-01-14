package db;

import domain.Reservation;
import domain.Room;
import domain.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class ReservationsDB {
    abstract List<Reservation> getAllReservations(Room room);

    public List<Reservation> getReservations(Room room, Date start, Date end) {
        List<Reservation> allReservations = getAllReservations(room);
        List<Reservation> reservations = new ArrayList<>();

        for(Reservation res : allReservations) {
            if(!res.getEnd().before(start) && !res.getStart().after(end)) {
                reservations.add(res);
            }
        }
        //reservations.removeIf(reservation -> reservation.getEnd().before(start) || reservation.getStart().after(end));
        return reservations;
    }

    public List<Reservation> getReservationsToday(Room room) {
        Date today = new Date();
        return getReservations(room, Util.getMorning(today), Util.getEvening(today));
    }

    public void close() {
    }
}
