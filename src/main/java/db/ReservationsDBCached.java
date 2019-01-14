package db;

import domain.Reservation;
import domain.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReservationsDBCached extends ReservationsDB {

    private final ReservationsDB reservationsDB;
    private final Map<Room, List<Reservation>> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
    private final int CACHE_REFRESH_TIME = 5 * 60;

    public ReservationsDBCached(ReservationsDB reservationsDB) {
        this.reservationsDB = reservationsDB;
        scheduler.scheduleAtFixedRate(new CacheReservations(), CACHE_REFRESH_TIME, CACHE_REFRESH_TIME, TimeUnit.SECONDS);
    }

    @Override
    public List<Reservation> getAllReservations(Room room) {
        if (cache.containsKey(room)) {
            return cache.get(room);
        }
        List<Reservation> reservations = reservationsDB.getAllReservations(room);
        cache.put(room, reservations);
        return reservations;
    }

    @Override
    public void close() {
        reservationsDB.close();
        scheduler.shutdown();
    }

    class CacheReservations implements Runnable {
        @Override
        public void run() {
            try {
                for (Room room : cache.keySet()) {
                    cache.put(room, reservationsDB.getAllReservations(room));
                }
            } catch (Exception ignored) {}
        }
    }
}
