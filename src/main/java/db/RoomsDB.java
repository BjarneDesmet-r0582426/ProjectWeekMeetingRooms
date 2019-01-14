package db;

import domain.Room;

import java.util.List;

public interface RoomsDB {
    List<Room> getRooms();

    Room getRoom(String roomNumber);
}
