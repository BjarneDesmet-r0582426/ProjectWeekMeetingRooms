package ui;

import domain.Reservation;
import domain.Room;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class TxtPerRoomOutput {
    private final String filePath;

    public TxtPerRoomOutput(String filePath) {
        this.filePath = filePath;
    }

    public void write(Room room, List<Reservation> reservations) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(filePath + room.getName() + ".txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new UiException(e.getMessage());
        }

        writer.println("Lokaal " + room.getName());
        writer.println("--------------------");
        writer.println("Start | Einde | Naam");
        writer.println("--------------------");

        for (Reservation reservation : reservations) {
            writer.print(reservation.getStartString());
            writer.print(" | ");
            writer.print(reservation.getEndString());
            writer.print(" | ");
            writer.println(reservation.getRegistrar());
        }

        writer.close();
    }

    public void close() {
    }
}
