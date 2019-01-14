package db;

import domain.Reservation;
import domain.Room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservationsDBStatic extends ReservationsDB {
    @Override
    public List<Reservation> getAllReservations(Room room) {
        List<Reservation> reservations = new ArrayList<>();

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(Calendar.MINUTE, 0);
        end.set(Calendar.MINUTE, 0);

        start.set(Calendar.HOUR_OF_DAY, 8);
        end.set(Calendar.HOUR_OF_DAY, 10);
        reservations.add(new Reservation(room, "Test person 1", start.getTime(), end.getTime()));

        start.set(Calendar.HOUR_OF_DAY, 12);
        end.set(Calendar.HOUR_OF_DAY, 16);
        reservations.add(new Reservation(room, "Test person 2", start.getTime(), end.getTime()));

        start.set(Calendar.HOUR_OF_DAY, 17);
        end.set(Calendar.HOUR_OF_DAY, 18);
        reservations.add(new Reservation(room, "Test person 3", start.getTime(), end.getTime()));

        start.set(Calendar.HOUR_OF_DAY, 19);
        end.set(Calendar.HOUR_OF_DAY, 20);
        reservations.add(new Reservation(room, "Test person 4", start.getTime(), end.getTime()));

        return reservations;
    }
}
