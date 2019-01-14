package domain;

import db.DBFactory;
import db.ReservationsDB;
import db.RoomsDB;
import ui.PdfPerRoomOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        // Make sure the output directory exists
        File txtDir = new File("output");
        if (!txtDir.exists() && !txtDir.mkdirs()) {
            throw new RuntimeException("Failed to create output directories!");
        }

        ReservationsDB reservationsDB = DBFactory.getReservationDB();
        RoomsDB roomsDB = DBFactory.getRoomsDB();

        PdfPerRoomOutput pdf = new PdfPerRoomOutput("output/rooms.pdf");

        List<Room> rooms = roomsDB.getRooms();
        for (Room room : rooms) {
            List<Reservation> reservations = reservationsDB.getReservationsToday(room);
            pdf.write(room, reservations);
        }

        pdf.close();
    }
}
