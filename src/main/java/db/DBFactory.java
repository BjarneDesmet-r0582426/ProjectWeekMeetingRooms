package db;

import domain.Reservation;

public class DBFactory {
    private static final ReservationsDB reservationsDB = new ReservationsDBCached(new ReservationsDBExchange());
    //private static final ReservationsDB reservationsDB = new ReservationsDBStatic();
    private static final RoomsDB roomsDB = new RoomsDBSql();

    public static ReservationsDB getReservationDB() {
        return reservationsDB;
    }

    public static RoomsDB getRoomsDB() {
        return roomsDB;
    }
}
